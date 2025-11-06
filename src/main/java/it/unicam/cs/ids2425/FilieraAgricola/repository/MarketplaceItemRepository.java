package it.unicam.cs.ids2425.FilieraAgricola.repository;

import it.unicam.cs.ids2425.FilieraAgricola.model.MarketplaceItem;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketplaceItemRepository extends JpaRepository<MarketplaceItem, Long> {

    /**
     * Trova tutti gli articoli del marketplace filtrando in base allo
     * stato del prodotto associato (es. APPROVATO).
     * Questo sarà usato per popolare il catalogo pubblico.
     *
     * @param status Lo stato del prodotto (es. StatoContenuto.APPROVATO)
     * @return Lista di MarketplaceItem
     */
    List<MarketplaceItem> findByProdottoStatus(StatoContenuto status);
}
