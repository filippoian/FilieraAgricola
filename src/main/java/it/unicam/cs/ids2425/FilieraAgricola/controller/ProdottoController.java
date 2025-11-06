package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
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

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    @PostMapping
    public ResponseEntity<ProdottoResponse> creaProdotto(@RequestBody ProdottoRequest request) {
        return ResponseEntity.ok(prodottoService.creaProdotto(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<ProdottoResponse>> getAllProdotti() {
        return ResponseEntity.ok(prodottoService.getAllProdotti());
    }

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

    @PreAuthorize("hasRole('CURATORE')")
    @GetMapping("/da-approvare")
    public List<ProdottoResponse> prodottiDaApprovare() {
        return prodottoService.getProdottiDaApprovare();
    }

    @PreAuthorize("hasRole('CURATORE')")
    @PostMapping("/{id}/approva")
    public void approvaProdotto(@PathVariable Long id) {
        prodottoService.approvaProdotto(id);
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

    @GetMapping("/approvati")
    public List<ProdottoResponse> prodottiApprovati() {
        return prodottoService.getProdottiApprovati();
    }
}