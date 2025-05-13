package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AziendaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.AziendaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.AziendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aziende")
public class AziendaController {

    private final AziendaService aziendaService;

    public AziendaController(AziendaService aziendaService) {
        this.aziendaService = aziendaService;
    }

    @PostMapping
    public ResponseEntity<AziendaResponse> creaAzienda(@RequestBody AziendaRequest request) {
        return ResponseEntity.ok(aziendaService.creaAzienda(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AziendaResponse> getAzienda(@PathVariable Long id) {
        return ResponseEntity.ok(aziendaService.getAzienda(id));
    }

    @GetMapping
    public ResponseEntity<List<AziendaResponse>> getAll() {
        return ResponseEntity.ok(aziendaService.getAll());
    }
}