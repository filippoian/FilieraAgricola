package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission;
import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ContentSubmissionRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ProdottoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;
    private final ContentSubmissionRepository submissionRepository;

    @Transactional
    public ProdottoResponse creaProdotto(ProdottoRequest request) {
        Utente utente = utenteRepository.findById(request.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + request.getUtenteId()));

        // Controllo sicurezza: solo l'utente stesso o un admin possono creare un prodotto a suo nome.
        checkOwnershipOrAdmin(request.getUtenteId(), "creare prodotti per questo utente");

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setCategoria(request.getCategoria());
        prodotto.setCertificazioni(request.getCertificazioni());
        prodotto.setMetodiColtivazione(request.getMetodiColtivazione());
        prodotto.setUtente(utente);

        Prodotto savedProdotto = prodottoRepository.save(prodotto);

        // Crea la sottomissione in stato BOZZA
        ContentSubmission submission = new ContentSubmission(savedProdotto.getId(), "PRODOTTO");
        ContentSubmission savedSubmission = submissionRepository.save(submission);

        savedProdotto.setSubmission(savedSubmission);
        prodottoRepository.save(savedProdotto);

        return new ProdottoResponse(savedProdotto);
    }

    /**
     * Ritorna una lista di tutti i prodotti visibili al pubblico (APPROVATI).
     */
    public List<ProdottoResponse> getAllProdotti() {
        return prodottoRepository.findAll()
                .stream()
                .filter(p -> p.getSubmission() != null &&
                        p.getSubmission().getStatus() == StatoContenuto.APPROVATO)
                .map(ProdottoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Ritorna un singolo prodotto. Se non APPROVATO, è visibile solo al proprietario o a un Curatore/Gestore.
     */
    public ProdottoResponse getProdottoById(Long id) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));

        ContentSubmission submission = prodotto.getSubmission();
        if (submission == null || submission.getStatus() != StatoContenuto.APPROVATO) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Usiamo checkOwnershipOrAdmin basato sull'ID del proprietario del prodotto
            if(prodotto.getUtente() == null) {
                throw new AccessDeniedException("Prodotto senza proprietario, accesso negato.");
            }
            checkOwnershipOrAdmin(prodotto.getUtente().getId(), "visualizzare questo prodotto non approvato");
        }
        return new ProdottoResponse(prodotto);
    }

    @Transactional(readOnly = true)
    public List<ProdottoResponse> getProdottiByUtente(Long utenteId) {
        // Controllo sicurezza: solo il proprietario o un admin possono vedere la lista completa dei prodotti
        checkOwnershipOrAdmin(utenteId, "visualizzare questa lista di prodotti");

        return prodottoRepository.findByUtenteId(utenteId)
                .stream()
                .map(ProdottoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProdottoResponse aggiornaProdotto(Long id, ProdottoRequest request) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));

        // Controllo permessi (solo proprietario o admin)
        if(prodotto.getUtente() == null) {
            throw new AccessDeniedException("Prodotto senza proprietario, impossibile aggiornare.");
        }
        checkOwnershipOrAdmin(prodotto.getUtente().getId(), "aggiornare questo prodotto");

        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setCategoria(request.getCategoria());
        prodotto.setCertificazioni(request.getCertificazioni());
        prodotto.setMetodiColtivazione(request.getMetodiColtivazione());

        // Se modificato, torna in BOZZA
        ContentSubmission submission = prodotto.getSubmission();
        if (submission != null && submission.getStatus() != StatoContenuto.BOZZA) {
            submission.setStatus(StatoContenuto.BOZZA);
            submission.setFeedbackCuratore("Modificato, richiede nuova approvazione.");
            submission.updateState();
            submissionRepository.save(submission);
        }

        return new ProdottoResponse(prodottoRepository.save(prodotto));
    }

    @Transactional
    public void eliminaProdotto(Long id) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));

        // Controllo permessi (solo proprietario o admin)
        if(prodotto.getUtente() == null) {
            throw new AccessDeniedException("Prodotto senza proprietario, impossibile eliminare.");
        }
        checkOwnershipOrAdmin(prodotto.getUtente().getId(), "eliminare questo prodotto");

        prodottoRepository.deleteById(id);
    }

    /**
     * Metodo helper per controllare i permessi.
     * Verifica se l'utente autenticato è il proprietario (tramite ID)
     * o ha un ruolo di admin (CURATORE, GESTORE).
     * Lancia AccessDeniedException se non autorizzato.
     */
    private void checkOwnershipOrAdmin(Long ownerId, String azione) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Nessun utente autenticato.");
        }

        // 1. Controlla se l'utente è un Curatore o Gestore
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CURATORE") || role.equals("ROLE_GESTORE"));

        if (isAdmin) {
            return; // Gli admin possono procedere
        }

        // 2. Controlla se l'utente è il proprietario
        String userEmail = authentication.getName();
        Optional<Utente> utenteAttuale = utenteRepository.findByEmail(userEmail);

        if (utenteAttuale.isEmpty() || !utenteAttuale.get().getId().equals(ownerId)) {
            throw new AccessDeniedException("Non hai i permessi per " + azione + ".");
        }
    }
}