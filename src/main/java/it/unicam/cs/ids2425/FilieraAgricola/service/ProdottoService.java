package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission;
import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto; // Importato
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ContentSubmissionRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ProdottoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException; // Importato
import org.springframework.security.core.Authentication; // Importato
import org.springframework.security.core.GrantedAuthority; // Importato
import org.springframework.security.core.context.SecurityContextHolder; // Importato
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; // Importato
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

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setCategoria(request.getCategoria());
        prodotto.setCertificazioni(request.getCertificazioni());
        prodotto.setMetodiColtivazione(request.getMetodiColtivazione());
        prodotto.setUtente(utente);

        Prodotto savedProdotto = prodottoRepository.save(prodotto);

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
                // --- TODO IMPLEMENTATO ---
                // Filtra per mostrare solo prodotti la cui submission è APPROVATA
                .filter(p -> p.getSubmission() != null &&
                        p.getSubmission().getStatus() == StatoContenuto.APPROVATO)
                // --- FINE IMPLEMENTAZIONE ---
                .map(ProdottoResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Ritorna un singolo prodotto, applicando controlli di visibilità.
     * Se il prodotto non è APPROVATO, è visibile solo al proprietario o a un Curatore/Gestore.
     */
    public ProdottoResponse getProdottoById(Long id) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));

        // --- TODO IMPLEMENTATO ---
        // Controllo visibilità
        ContentSubmission submission = prodotto.getSubmission();
        if (submission == null || submission.getStatus() != StatoContenuto.APPROVATO) {

            // Se non è approvato, controlla i permessi dell'utente
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!hasViewPermission(authentication, prodotto)) {
                throw new AccessDeniedException("Non hai i permessi per visualizzare questo prodotto in stato " +
                        (submission != null ? submission.getStatus() : "NON SOTTOMESSO"));
            }
        }
        // --- FINE IMPLEMENTAZIONE ---

        return new ProdottoResponse(prodotto);
    }

    /**
     * Metodo helper per controllare i permessi di visualizzazione su contenuti non approvati.
     */
    private boolean hasViewPermission(Authentication authentication, Prodotto prodotto) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // Utente non autenticato
        }

        // 1. Controlla se l'utente è un Curatore o Gestore
        boolean isCuratoreOrGestore = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CURATORE") || role.equals("ROLE_GESTORE"));

        if (isCuratoreOrGestore) {
            return true;
        }

        // 2. Controlla se l'utente è il proprietario del prodotto
        // Recuperiamo l'utente dal DB tramite email (username)
        String userEmail = authentication.getName();
        Optional<Utente> utenteAttuale = utenteRepository.findByEmail(userEmail);

        if (utenteAttuale.isPresent() && prodotto.getUtente() != null) {
            return utenteAttuale.get().getId().equals(prodotto.getUtente().getId());
        }

        return false;
    }

}