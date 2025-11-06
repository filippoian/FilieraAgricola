package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.PrenotazioneEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrenotazioneEventoRepository extends JpaRepository<PrenotazioneEvento, Long> {
    List<PrenotazioneEvento> findByUtenteId(Long utenteId);
}