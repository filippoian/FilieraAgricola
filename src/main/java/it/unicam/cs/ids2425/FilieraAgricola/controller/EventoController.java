package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.EventoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.EventoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PreAuthorize("hasRole('ANIMATORE')")
    @PostMapping
    public ResponseEntity<EventoResponse> creaEvento(@RequestBody EventoRequest request) {
        return ResponseEntity.ok(eventoService.creaEvento(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<EventoResponse>> getAllEventi() {
        return ResponseEntity.ok(eventoService.getAllEventi());
    }

    @PreAuthorize("hasRole('CURATORE')")
    @GetMapping("/da-approvare")
    public List<EventoResponse> eventiDaApprovare() {
        return eventoService.getEventiDaApprovare();
    }

    @PreAuthorize("hasRole('CURATORE')")
    @PostMapping("/{id}/approva")
    public void approvaEvento(@PathVariable Long id) {
        eventoService.approvaEvento(id);
    }

    @PreAuthorize("hasRole('ANIMATORE')")
    @PutMapping("/{id}")
    public EventoResponse aggiornaEvento(@PathVariable Long id, @RequestBody EventoRequest request) {
        return eventoService.aggiornaEvento(id, request);
    }

    @PreAuthorize("hasRole('ANIMATORE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminaEvento(@PathVariable Long id) {
        eventoService.eliminaEvento(id);
        return ResponseEntity.ok("Evento eliminato");
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/approvati")
    public List<EventoResponse> eventiApprovati() {
        return eventoService.getEventiApprovati();
    }
}