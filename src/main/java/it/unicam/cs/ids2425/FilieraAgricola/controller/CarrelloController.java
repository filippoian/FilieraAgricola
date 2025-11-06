package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AggiungiAlCarrelloRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.CarrelloResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.CarrelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrello")
@RequiredArgsConstructor
public class CarrelloController {

    private final CarrelloService carrelloService;

    @GetMapping("/{utenteId}")
    @PreAuthorize("@customSecurityService.hasUserId(authentication, #utenteId) or hasRole('GESTORE')")
    public ResponseEntity<CarrelloResponse> getCarrello(@PathVariable Long utenteId) {
        return ResponseEntity.ok(carrelloService.getCarrello(utenteId));
    }

    @PostMapping("/{utenteId}/aggiungi")
    @PreAuthorize("@customSecurityService.hasUserId(authentication, #utenteId)")
    public ResponseEntity<CarrelloResponse> aggiungiProdotto(@PathVariable Long utenteId, @RequestBody AggiungiAlCarrelloRequest request) {
        return ResponseEntity.ok(carrelloService.aggiungiProdotto(utenteId, request));
    }

    @DeleteMapping("/{utenteId}/rimuovi/{prodottoId}")
    @PreAuthorize("@customSecurityService.hasUserId(authentication, #utenteId)")
    public ResponseEntity<CarrelloResponse> rimuoviProdotto(@PathVariable Long utenteId, @PathVariable Long prodottoId) {
        return ResponseEntity.ok(carrelloService.rimuoviProdotto(utenteId, prodottoId));
    }
}