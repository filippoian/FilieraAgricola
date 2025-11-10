package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AziendaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.AziendaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.AziendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aziende")
@RequiredArgsConstructor
public class AziendaController {

    private final AziendaService aziendaService;

    @PostMapping
    @PreAuthorize("hasRole('GESTORE')")
    public ResponseEntity<AziendaResponse> creaAzienda(@RequestBody AziendaRequest request) {
        return ResponseEntity.ok(aziendaService.creaAzienda(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AziendaResponse> getAzienda(@PathVariable Long id) {
        return ResponseEntity.ok(aziendaService.getAzienda(id));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<AziendaResponse>> getAllAziende() {
        return ResponseEntity.ok(aziendaService.getAll());
    }

    @GetMapping("/coordinate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<AziendaResponse>> getAziendeConCoordinate(@RequestParam(required = false) String ruolo) {
        return ResponseEntity.ok(aziendaService.getAziendeConCoordinate(ruolo));
    }

    @GetMapping("/da-approvare")
    @PreAuthorize("hasRole('CURATORE')")
    public ResponseEntity<List<AziendaResponse>> getAziendeDaApprovare() {
        return ResponseEntity.ok(aziendaService.getAziendeDaApprovare());
    }

    @PostMapping("/{id}/approva")
    @PreAuthorize("hasRole('CURATORE')")
    public ResponseEntity<Void> approvaAzienda(@PathVariable Long id) {
        aziendaService.approvaAzienda(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTORE')")
    public ResponseEntity<AziendaResponse> aggiornaAzienda(@PathVariable Long id, @RequestBody AziendaRequest request) {
        return ResponseEntity.ok(aziendaService.aggiornaAzienda(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTORE')")
    public ResponseEntity<Void> eliminaAzienda(@PathVariable Long id) {
        aziendaService.eliminaAzienda(id);
        return ResponseEntity.ok().build();
    }
}