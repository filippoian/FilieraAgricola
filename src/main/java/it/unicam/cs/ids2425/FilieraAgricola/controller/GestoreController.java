package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AccreditaRequest;
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
     * Endpoint per accreditare un utente con un nuovo ruolo professionale.
     * Accessibile solo da utenti con autorità GESTORE.
     */
    @PostMapping("/utenti/{userId}/accredita")
    @PreAuthorize("hasAuthority('GESTORE')")
    public ResponseEntity<?> accreditaUtente(@PathVariable Long userId, @RequestBody AccreditaRequest request) {

        try {
            gestoreService.accreditaUtente(userId, request.getRuolo());
            return ResponseEntity.ok(new MessageResponse("Ruolo " + request.getRuolo() + " aggiunto con successo all'utente " + userId));
        } catch (RuntimeException e) {
            // Se l'utente o il ruolo non esistono
            return ResponseEntity.badRequest().body(new MessageResponse("Errore: " + e.getMessage()));
        }
    }
}
