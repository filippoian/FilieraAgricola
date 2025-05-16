package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.ProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    @PostMapping
    public ResponseEntity<ProdottoResponse> creaProdotto(@RequestBody ProdottoRequest request) {
        return ResponseEntity.ok(prodottoService.creaProdotto(request));
    }

    @GetMapping
    public ResponseEntity<List<ProdottoResponse>> getAllProdotti() {
        return ResponseEntity.ok(prodottoService.getAllProdotti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdottoById(@PathVariable Long id) {
        return ResponseEntity.ok(prodottoService.getProdottoById(id));
    }
}