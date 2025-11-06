package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentSubmissionRepository extends JpaRepository<ContentSubmission, Long> {

    /**
     * Trova la sottomissione associata a una specifica entità.
     */
    Optional<ContentSubmission> findBySubmittableEntityIdAndSubmittableEntityType(Long entityId, String entityType);

    /**
     * Trova tutte le sottomissioni in un determinato stato (per il dashboard del Curatore).
     */
    List<ContentSubmission> findByStatus(StatoContenuto status);
}