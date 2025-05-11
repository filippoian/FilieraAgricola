package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
