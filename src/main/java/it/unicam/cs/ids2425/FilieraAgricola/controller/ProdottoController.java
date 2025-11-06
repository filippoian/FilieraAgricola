package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission; // Import
import it.unicam.cs.ids2425.FilieraAgricola.service.CurationService; // Import
import it.unicam.cs.ids2425.FilieraAgricola.service.ProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;
    private final CurationService curationService; // Servizio aggiunto

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    @PostMapping
    public ResponseEntity<ProdottoResponse> creaProdotto(@RequestBody ProdottoRequest request) {
        return ResponseEntity.ok(prodottoService.creaProdotto(request));
    }

    /**
     * API per il proprietario, per sottomettere il prodotto alla Curation.
     */
    @PostMapping("/{id}/sottometti")
    @PreAuthorize("isAuthenticated()") // La logica di proprietà è gestita nel servizio
    public ResponseEntity<ContentSubmission> sottomettiProdotto(@PathVariable Long id) {
        // 1. Trova la submission collegata all'ID del Prodotto
        ContentSubmission submission = curationService.findSubmissionByEntity(id, "PRODOTTO");

        // 2. Esegue l'azione di sottomissione (cambia stato da BOZZA a IN_REVISIONE)
        return ResponseEntity.ok(curationService.sottomettiContenuto(submission.getId()));
    }

    /**
     * Ritorna tutti i prodotti approvati (visibili al pubblico).
     * Il servizio ora filtra automaticamente.
     */
    @GetMapping
    public ResponseEntity<List<ProdottoResponse>> getAllProdotti() {
        return ResponseEntity.ok(prodottoService.getAllProdotti());
    }

    /**
     * Ritorna un prodotto. Se non approvato, è visibile solo al proprietario o admin.
     * La logica di visibilità è gestita nel servizio.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdottoById(@PathVariable Long id) {
        return ResponseEntity.ok(prodottoService.getProdottoById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/utente/{utenteId}")
    public List<ProdottoResponse> getProdottiByUtente(@PathVariable Long utenteId) {
        return prodottoService.getProdottiByUtente(utenteId);
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    @PutMapping("/{id}")
    public ProdottoResponse aggiornaProdotto(
            @PathVariable Long id,
            @RequestBody ProdottoRequest request) {
        return prodottoService.aggiornaProdotto(id, request);
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminaProdotto(@PathVariable Long id) {
        prodottoService.eliminaProdotto(id);
        return ResponseEntity.ok("Prodotto eliminato");
    }

    /**
     * Alias per GET /api/prodotti
     */
    @GetMapping("/approvati")
    public List<ProdottoResponse> prodottiApprovati() {
        return prodottoService.getAllProdotti();
    }
}