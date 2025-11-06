package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LottoCreateDTO;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.StepCreateDTO; // Import
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.StepResponseDTO; // Import
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.TraceabilityGraphDTO;
import it.unicam.cs.ids2425.FilieraAgricola.model.ProductBatch;
import it.unicam.cs.ids2425.FilieraAgricola.service.TracciabilitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Import

@RestController
@RequestMapping("/api/tracciabilita")
@RequiredArgsConstructor
public class TracciabilitaController {

    private final TracciabilitaService tracciabilitaService;

    /**
     * API per creare un nuovo lotto (nodo del grafo).
     */
    @PostMapping("/lotti")
    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE')")
    public ResponseEntity<ProductBatch> creaLotto(@RequestBody LottoCreateDTO request) {
        return ResponseEntity.ok(tracciabilitaService.creaLotto(request));
    }

    /**
     * API pubblica per visualizzare la storia completa (grafo + fasi) di un lotto.
     */
    @GetMapping("/lotti/{id}/storia")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TraceabilityGraphDTO> getStoriaLotto(@PathVariable Long id) {
        return ResponseEntity.ok(tracciabilitaService.getStoriaLotto(id));
    }


    /**
     * API per aggiungere una fase (step) a un lotto.
     */
    @PostMapping("/lotti/{id}/fasi")
    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    public ResponseEntity<StepResponseDTO> aggiungiFase(
            @PathVariable Long id,
            @RequestBody StepCreateDTO request) {
        return ResponseEntity.ok(tracciabilitaService.aggiungiFase(id, request));
    }

    /**
     * API pubblica per visualizzare solo le fasi di un lotto (il "diario di bordo").
     */
    @GetMapping("/lotti/{id}/fasi")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<StepResponseDTO>> getFasiLotto(@PathVariable Long id) {
        return ResponseEntity.ok(tracciabilitaService.getFasiLotto(id));
    }
}