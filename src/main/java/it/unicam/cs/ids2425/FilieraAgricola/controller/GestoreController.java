package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaProduttoreRequest; // Import
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.MessageResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.GestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gestore")
public class GestoreController {

    @Autowired
    GestoreService gestoreService;

    /**
     * Endpoint per accreditare un utente con un ruolo "base" (es. CURATORE, GESTORE)
     * che non richiede un profilo attore complesso.
     */
    @PostMapping("/utenti/{userId}/accredita-ruolo")
    @PreAuthorize("hasAuthority('ROLE_GESTORE')") // Assicurati che i ruoli Spring Security inizino con ROLE_
    public ResponseEntity<?> accreditaRuoloBase(@PathVariable Long userId, @RequestBody AccreditaRequest request) {
        try {
            gestoreService.accreditaRuoloBase(userId, request.getRuolo());
            return ResponseEntity.ok(new MessageResponse("Ruolo " + request.getRuolo() + " aggiunto con successo all'utente " + userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Errore: " + e.getMessage()));
        }
    }

    /**
     * Endpoint per l'accredito completo di un PRODUTTORE,
     * che crea il profilo e il filiera point.
     */
    @PostMapping("/utenti/{userId}/accredita-produttore")
    @PreAuthorize("hasAuthority('ROLE_GESTORE')")
    public ResponseEntity<?> accreditaProduttore(@PathVariable Long userId, @RequestBody AccreditaProduttoreRequest request) {
        try {
            gestoreService.accreditaProduttore(userId, request);
            return ResponseEntity.ok(new MessageResponse("Utente " + userId + " accreditato come PRODUTTORE con successo."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Errore: " + e.getMessage()));
        }
    }
}