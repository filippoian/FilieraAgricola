package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ApproveRequestDTO;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RejectRequestDTO;
import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission;
import it.unicam.cs.ids2425.FilieraAgricola.service.CurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curatore")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CURATORE')") // Protegge l'intero controller
public class CurationController {

    private final CurationService curationService;

    /**
     * API per il dashboard del Curatore.
     * Recupera tutti i contenuti in attesa di approvazione (IN_REVISIONE).
     */
    @GetMapping("/revisione")
    public ResponseEntity<List<ContentSubmission>> getContenutiInRevisione() {
        return ResponseEntity.ok(curationService.getContenutiInRevisione());
    }

    /**
     * API per approvare un contenuto.
     */
    @PostMapping("/approva/{submissionId}")
    public ResponseEntity<ContentSubmission> approvaContenuto(
            @PathVariable Long submissionId,
            @RequestBody(required = false) ApproveRequestDTO request) {

        // Il DTO è opzionale, ma il servizio gestirà la logica
        return ResponseEntity.ok(curationService.approvaContenuto(submissionId));
    }

    /**
     * API per rifiutare un contenuto.
     */
    @PostMapping("/rifiuta/{submissionId}")
    public ResponseEntity<ContentSubmission> rifiutaContenuto(
            @PathVariable Long submissionId,
            @RequestBody RejectRequestDTO request) {

        return ResponseEntity.ok(curationService.rifiutaContenuto(submissionId, request.getFeedback()));
    }

    /**
     * API per rimandare un contenuto in BOZZA (es. per richiedere modifiche).
     */
    @PostMapping("/rimanda-in-bozza/{submissionId}")
    public ResponseEntity<ContentSubmission> rimandaInBozza(@PathVariable Long submissionId) {
        return ResponseEntity.ok(curationService.rimandaInBozza(submissionId));
    }
}