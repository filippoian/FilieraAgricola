package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByEmail(String email);
}
