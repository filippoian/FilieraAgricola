package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AziendaRepository extends JpaRepository<Azienda, Long> {
    List<Azienda> findByApprovatoFalse();
    List<Azienda> findByApprovatoTrue();
}
