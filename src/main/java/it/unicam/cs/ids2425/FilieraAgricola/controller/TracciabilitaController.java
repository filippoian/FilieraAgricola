package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.TracciabilitaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.TracciabilitaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.TracciabilitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracciabilita")
@RequiredArgsConstructor
public class TracciabilitaController {

    private final TracciabilitaService tracciabilitaService;

    @PostMapping
    public ResponseEntity<TracciabilitaResponse> aggiungiFase(@RequestBody TracciabilitaRequest request) {
        return ResponseEntity.ok(tracciabilitaService.aggiungiFase(request));
    }

    @GetMapping("/prodotto/{id}")
    public ResponseEntity<List<TracciabilitaResponse>> getFasiByProdotto(@PathVariable Long id) {
        return ResponseEntity.ok(tracciabilitaService.getFasiByProdotto(id));
    }
}
