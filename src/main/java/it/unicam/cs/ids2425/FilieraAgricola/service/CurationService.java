package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ContentSubmissionRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final ContentSubmissionRepository submissionRepository;
    private final UtenteRepository utenteRepository;

    /**
     * Recupera il dashboard del Curatore: tutti i contenuti in attesa di revisione.
     */
    @Transactional(readOnly = true)
    public List<ContentSubmission> getContenutiInRevisione() {
        // Questo endpoint è già protetto a livello di controller (solo CURATORE)
        return submissionRepository.findByStatus(StatoContenuto.IN_REVISIONE);
    }

    /**
     * Azione del proprietario per sottomettere un contenuto in BOZZA.
     */
    @Transactional
    public ContentSubmission sottomettiContenuto(Long submissionId) {
        ContentSubmission submission = findSubmissionById(submissionId);

        // TODO: Aggiungere controllo proprietà (solo il proprietario può sottomettere)

        // Delega l'azione all'oggetto di stato corrente (BozzaState)
        submission.sottometti();

        return submissionRepository.save(submission);
    }

    /**
     * Azione del Curatore per approvare un contenuto.
     */
    @Transactional
    public ContentSubmission approvaContenuto(Long submissionId) {
        ContentSubmission submission = findSubmissionById(submissionId);

        // Delega l'azione all'oggetto di stato corrente (InRevisioneState)
        submission.approva();

        // TODO: Aggiungere logica post-approvazione (es. aggiornare Prodotto.isApproved)
        // Sebbene lo stato sia centralizzato, potremmo voler de-normalizzare
        // un flag "isApproved" sull'entità Prodotto/Evento per query più veloci.
        // Per ora, lo stato è solo in ContentSubmission.

        return submissionRepository.save(submission);
    }

    /**
     * Azione del Curatore per rifiutare un contenuto.
     */
    @Transactional
    public ContentSubmission rifiutaContenuto(Long submissionId, String feedback) {
        ContentSubmission submission = findSubmissionById(submissionId);

        if (feedback == null || feedback.isBlank()) {
            throw new IllegalArgumentException("Il feedback è obbligatorio per il rifiuto.");
        }

        // Delega l'azione all'oggetto di stato corrente (InRevisioneState)
        submission.rifiuta(feedback);

        return submissionRepository.save(submission);
    }

    /**
     * Azione per rimandare un contenuto in BOZZA (es. Curatore chiede modifiche).
     */
    @Transactional
    public ContentSubmission rimandaInBozza(Long submissionId) {
        ContentSubmission submission = findSubmissionById(submissionId);

        // Delega l'azione all'oggetto di stato corrente
        submission.rimandaInBozza();

        return submissionRepository.save(submission);
    }


    private ContentSubmission findSubmissionById(Long submissionId) {
        ContentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Sottomissione non trovata con id: " + submissionId));

        // Assicura che l'oggetto di stato transiente sia inizializzato
        submission.updateState();
        return submission;
    }

    /**
     * Helper per trovare la submission associata a un'entità (es. Prodotto 15).
     * Usato per l'endpoint POST /api/prodotti/{id}/sottometti
     */
    @Transactional(readOnly = true)
    public ContentSubmission findSubmissionByEntity(Long entityId, String entityType) {
        return submissionRepository.findBySubmittableEntityIdAndSubmittableEntityType(entityId, entityType)
                .orElseThrow(() -> new RuntimeException("Nessuna sottomissione trovata per " + entityType + " con id: " + entityId));
    }
}