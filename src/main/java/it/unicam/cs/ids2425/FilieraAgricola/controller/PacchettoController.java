package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.PacchettoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.PacchettoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.PacchettoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacchetti")
@RequiredArgsConstructor
public class PacchettoController {

    private final PacchettoService pacchettoService;

    @PreAuthorize("hasRole('DISTRIBUTORE')")
    @PostMapping
    public ResponseEntity<PacchettoResponse> creaPacchetto(@RequestBody PacchettoRequest request) {
        return ResponseEntity.ok(pacchettoService.creaPacchetto(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<PacchettoResponse>> getAllPacchetti() {
        return ResponseEntity.ok(pacchettoService.getAllPacchetti());
    }

    @PreAuthorize("hasRole('GESTORE') or #id == authentication.principal.id")
    @GetMapping("/distributore/{id}")
    public List<PacchettoResponse> getByDistributore(@PathVariable Long id) {
        return pacchettoService.getByDistributore(id);
    }

    @PreAuthorize("hasRole('CURATORE')")
    @GetMapping("/da-approvare")
    public List<PacchettoResponse> pacchettiDaApprovare() {
        return pacchettoService.getPacchettiDaApprovare();
    }

    @PreAuthorize("hasRole('CURATORE')")
    @PostMapping("/{id}/approva")
    public void approvaPacchetto(@PathVariable Long id) {
        pacchettoService.approvaPacchetto(id);
    }

    @PreAuthorize("hasRole('DISTRIBUTORE')")
    @PutMapping("/{id}")
    public PacchettoResponse aggiornaPacchetto(@PathVariable Long id, @RequestBody PacchettoRequest request) {
        return pacchettoService.aggiornaPacchetto(id, request);
    }

    @PreAuthorize("hasRole('DISTRIBUTORE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminaPacchetto(@PathVariable Long id) {
        pacchettoService.eliminaPacchetto(id);
        return ResponseEntity.ok("Pacchetto eliminato");
    }
}
