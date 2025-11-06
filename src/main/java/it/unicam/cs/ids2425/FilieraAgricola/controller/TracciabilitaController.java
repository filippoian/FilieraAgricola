package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.TracciabilitaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.TracciabilitaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.TracciabilitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracciabilita")
@RequiredArgsConstructor
public class TracciabilitaController {

    private final TracciabilitaService service;


    @PreAuthorize("hasAnyARole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    @PostMapping
    public TracciabilitaResponse aggiungiFase(@RequestBody TracciabilitaRequest request) {
        return service.aggiungiFase(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/prodotto/{prodottoId}")
    public List<TracciabilitaResponse> getFasiPerProdotto(@PathVariable Long prodottoId) {
        return service.getFasiPerProdotto(prodottoId);
    }
}