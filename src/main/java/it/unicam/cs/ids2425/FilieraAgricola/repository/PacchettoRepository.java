package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Pacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {
    List<Pacchetto> findByDistributoreId(Long distributoreId);
}
