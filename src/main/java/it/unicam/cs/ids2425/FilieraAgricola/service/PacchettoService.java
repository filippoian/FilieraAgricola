package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.PacchettoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.PacchettoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacchettoService {

    private final PacchettoRepository pacchettoRepository;
    private final UtenteRepository utenteRepository;

    // --- Repository Corretti ---
    private final MarketplaceItemRepository marketplaceItemRepository;
    private final MarketplaceItemPacchettoRepository marketplaceItemPacchettoRepository;

    // --- Repository Aggiunto per Curation ---
    private final ContentSubmissionRepository submissionRepository;

    @Transactional
    public PacchettoResponse creaPacchetto(PacchettoRequest request) {
        Utente distributore = utenteRepository.findById(request.getDistributoreId())
                .orElseThrow(() -> new RuntimeException("Distributore non trovato"));

        // Controllo Sicurezza: Solo il distributore stesso o un admin
        checkOwnershipOrAdmin(distributore.getId(), "creare pacchetti");

        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(request.getNome());
        pacchetto.setDescrizione(request.getDescrizione());
        pacchetto.setDistributore(distributore);
        pacchetto.setPrezzo_totale(request.getPrezzoTotale());

        // Salva il pacchetto per ottenere l'ID
        Pacchetto savedPacchetto = pacchettoRepository.save(pacchetto);

        // Crea la sottomissione associata (in stato BOZZA di default)
        ContentSubmission submission = new ContentSubmission(savedPacchetto.getId(), "PACCHETTO");
        ContentSubmission savedSubmission = submissionRepository.save(submission);

        // Collega la sottomissione al pacchetto
        savedPacchetto.setSubmission(savedSubmission);
        pacchettoRepository.save(savedPacchetto);


        // Collega i MarketplaceItems
        for (Long itemId : request.getMarketplaceItemIds()) {
            MarketplaceItem item = marketplaceItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("MarketplaceItem non trovato con id: " + itemId));

            // Controllo Curation: l'item deve essere approvato
            if (item.getProdotto().getSubmission() == null ||
                    item.getProdotto().getSubmission().getStatus() != StatoContenuto.APPROVATO) {
                throw new IllegalStateException("Impossibile aggiungere al pacchetto l'item non approvato: " + item.getId());
            }

            MarketplaceItemPacchetto link = new MarketplaceItemPacchetto();
            link.setPacchetto(savedPacchetto);
            link.setItem(item);
            link.setQuantita(1);

            marketplaceItemPacchettoRepository.save(link);
            savedPacchetto.getItems().add(link); // Assicura che la collection sia aggiornata
        }

        return new PacchettoResponse(savedPacchetto);
    }

    /**
     * Ritorna tutti i pacchetti visibili al pubblico (APPROVATI).
     */
    public List<PacchettoResponse> getAllPacchetti() {
        return pacchettoRepository.findAll()
                .stream()
                .filter(p -> p.getSubmission() != null &&
                        p.getSubmission().getStatus() == StatoContenuto.APPROVATO)
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PacchettoResponse aggiornaPacchetto(Long id, PacchettoRequest request) {
        Pacchetto pacchetto = pacchettoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pacchetto non trovato"));

        // Controllo permessi (solo proprietario/admin)
        checkOwnershipOrAdmin(pacchetto.getDistributore().getId(), "aggiornare");

        pacchetto.setNome(request.getNome());
        pacchetto.setDescrizione(request.getDescrizione());
        pacchetto.setPrezzo_totale(request.getPrezzoTotale());

        // Gestione aggiornamento items: rimuovi i vecchi, aggiungi i nuovi
        // (orphanRemoval=true su Pacchetto.items si occuperà di pulire il DB)
        pacchetto.getItems().clear();
        marketplaceItemPacchettoRepository.deleteAllByPacchettoId(id); // Pulizia esplicita

        for (Long itemId : request.getMarketplaceItemIds()) {
            MarketplaceItem item = marketplaceItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("MarketplaceItem non trovato con id: " + itemId));

            // Controllo Curation
            if (item.getProdotto().getSubmission() == null ||
                    item.getProdotto().getSubmission().getStatus() != StatoContenuto.APPROVATO) {
                throw new IllegalStateException("Impossibile aggiungere al pacchetto l'item non approvato: " + item.getId());
            }

            MarketplaceItemPacchetto link = new MarketplaceItemPacchetto();
            link.setPacchetto(pacchetto);
            link.setItem(item);
            link.setQuantita(1);

            pacchetto.getItems().add(link);
        }

        // Se aggiornato, lo stato Curation deve tornare a BOZZA
        ContentSubmission submission = pacchetto.getSubmission();
        if (submission != null && submission.getStatus() != StatoContenuto.BOZZA) {
            submission.setStatus(StatoContenuto.BOZZA);
            submission.setFeedbackCuratore("Modificato, richiede nuova approvazione.");
            submission.updateState();
            submissionRepository.save(submission);
        }

        return new PacchettoResponse(pacchettoRepository.save(pacchetto));
    }

    @Transactional
    public void eliminaPacchetto(Long id) {
        Pacchetto pacchetto = pacchettoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pacchetto non trovato"));

        // Controllo permessi
        checkOwnershipOrAdmin(pacchetto.getDistributore().getId(), "eliminare");

        pacchettoRepository.deleteById(id);
    }

    public List<PacchettoResponse> getByDistributore(Long distributoreId) {
        // Controllo permessi
        checkOwnershipOrAdmin(distributoreId, "visualizzare i pacchetti");

        return pacchettoRepository.findByDistributoreId(distributoreId)
                .stream()
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Metodo helper per controllare i permessi.
     * Verifica se l'utente autenticato è il proprietario (tramite ID)
     * o ha un ruolo di admin (CURATORE, GESTORE).
     */
    private void checkOwnershipOrAdmin(Long ownerId, String azione) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Nessun utente autenticato.");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CURATORE") || role.equals("ROLE_GESTORE"));

        if (isAdmin) {
            return; // Gli admin possono procedere
        }

        // Se non è admin, controlla la proprietà
        String userEmail = authentication.getName();
        Optional<Utente> utenteAttuale = utenteRepository.findByEmail(userEmail);

        if (utenteAttuale.isEmpty() || !utenteAttuale.get().getId().equals(ownerId)) {
            throw new AccessDeniedException("Non hai i permessi per " + azione + ".");
        }
    }
}