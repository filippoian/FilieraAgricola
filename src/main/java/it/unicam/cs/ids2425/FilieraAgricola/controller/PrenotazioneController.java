package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.PrenotazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.PrenotazioneResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.PrenotazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    @PreAuthorize("hasRole('ACQUIRENTE')")
    @PostMapping
    public ResponseEntity<PrenotazioneResponse> prenota(@RequestBody PrenotazioneRequest request) {
        return ResponseEntity.ok(prenotazioneService.prenotaEvento(request));
    }

    @PreAuthorize("hasRole('ACQUIRENTE')")
    @GetMapping("/utente/{id}")
    public ResponseEntity<List<PrenotazioneResponse>> getByUtente(@PathVariable Long id) {
        return ResponseEntity.ok(prenotazioneService.getPrenotazioniByUtente(id));
    }
}