package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    Optional<Carrello> findByUtenteId(Long utenteId);
}