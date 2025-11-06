package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AziendaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.AziendaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.AziendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
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

    @GetMapping("/coordinate")
    public List<AziendaResponse> getAziendeConCoordinate(
            @RequestParam(required = false) String ruolo) {
        return aziendaService.getAziendeConCoordinate(ruolo);
    }

    @GetMapping("/da-approvare")
    @PreAuthorize("hasRole('CURATORE')")
    public List<AziendaResponse> aziendeDaApprovare() {
        return aziendaService.getAziendeDaApprovare();
    }

    @PostMapping("/{id}/approva")
    @PreAuthorize("hasRole('CURATORE')")
    public void approvaAzienda(@PathVariable Long id) {
        aziendaService.approvaAzienda(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUTTORE', 'DISTRIBUTORE')")
    public AziendaResponse aggiornaAzienda(@PathVariable Long id, @RequestBody AziendaRequest request) {
        return aziendaService.aggiornaAzienda(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PRODUTTORE', 'DISTRIBUTORE', 'GESTORE')")
    public ResponseEntity<String> eliminaAzienda(@PathVariable Long id) {
        aziendaService.eliminaAzienda(id);
        return ResponseEntity.ok("Azienda eliminata");
    }

    @GetMapping("/approvate")
    public List<AziendaResponse> aziendeApprovate() {
        return aziendaService.getAziendeApprovate();
    }
}