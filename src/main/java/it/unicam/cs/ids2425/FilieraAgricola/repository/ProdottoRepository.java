package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
}
