package it.unicam.cs.ids2425.FilieraAgricola.controller;

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

    @PreAuthorize("hasRole('ACQUIRENTE')")
    @PostMapping
    public ResponseEntity<OrdineResponse> creaOrdine(@RequestBody OrdineRequest request) {
        return ResponseEntity.ok(ordineService.creaOrdine(request));
    }

    @PreAuthorize("hasRole('ACQUIRENTE')")
    @GetMapping("/utente/{id}")
    public ResponseEntity<List<OrdineResponse>> getOrdiniByUtente(@PathVariable Long id) {
        return ResponseEntity.ok(ordineService.getOrdiniByUtente(id));
    }

    @PreAuthorize("hasRole('GESTORE') or hasRole('DISTRIBUTORE')")
    @PostMapping("/{id}/stato")
    public ResponseEntity<String> aggiornaStatoOrdine(
            @PathVariable Long id,
            @RequestParam String stato) {
        ordineService.aggiornaStato(id, stato);
        return ResponseEntity.ok("Stato aggiornato con successo");
    }

    @PostMapping("/dal-carrello/{utenteId}")
    @PreAuthorize("#utenteId == authentication.principal.id or hasRole('GESTORE')")
    public ResponseEntity<OrdineResponse> creaOrdineDalCarrello(@PathVariable Long utenteId) {
        return ResponseEntity.ok(ordineService.creaOrdineDalCarrello(utenteId));
    }
}
