package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.MarketplaceItemPacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketplaceItemPacchettoRepository extends JpaRepository<MarketplaceItemPacchetto, Long> {

    /**
     * Elimina tutti i collegamenti associati a un ID pacchetto.
     * Necessario per l'aggiornamento dei pacchetti in PacchettoService.
     */
    void deleteAllByPacchettoId(Long pacchettoId);
}