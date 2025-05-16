package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Tracciabilita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TracciabilitaRepository extends JpaRepository<Tracciabilita, Long> {
    List<Tracciabilita> findByProdottoId(Long prodottoId);
}
