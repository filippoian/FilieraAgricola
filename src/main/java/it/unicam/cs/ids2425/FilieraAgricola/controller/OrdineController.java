package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.CarrelloCheckoutDTO; // Import
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.OrdineRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.OrdineResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.OrdineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordini")
@RequiredArgsConstructor
public class OrdineController {

    private final OrdineService ordineService;

    // --- ENDPOINT AGGIORNATO (come da specifica) ---
    /**
     * Crea un nuovo ordine basato sul payload del carrello (Checkout).
     */
    @PostMapping("/checkout/{utenteId}")
    @PreAuthorize("hasRole('ACQUIRENTE') and #utenteId == authentication.principal.id")
    public ResponseEntity<OrdineResponse> creaOrdine(
            @PathVariable Long utenteId,
            @RequestBody CarrelloCheckoutDTO request) {

        return ResponseEntity.ok(ordineService.creaOrdineDaCheckout(utenteId, request));
    }

    @GetMapping("/utente/{id}")
    @PreAuthorize("hasRole('ACQUIRENTE') and #id == authentication.principal.id")
    public ResponseEntity<List<OrdineResponse>> getOrdiniByUtente(@PathVariable Long id) {
        return ResponseEntity.ok(ordineService.getOrdiniByUtente(id));
    }

    @PostMapping("/{id}/stato")
    @PreAuthorize("hasRole('GESTORE') or hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    public ResponseEntity<String> aggiornaStatoOrdine(
            @PathVariable Long id,
            @RequestParam String stato) {
        ordineService.aggiornaStato(id, stato);
        return ResponseEntity.ok("Stato aggiornato con successo");
    }
}