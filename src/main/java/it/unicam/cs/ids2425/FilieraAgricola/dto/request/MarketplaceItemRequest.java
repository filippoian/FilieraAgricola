package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import it.unicam.cs.ids2425.FilieraAgricola.model.UnitaDiMisura;
import lombok.Data;

/**
 * DTO per la richiesta di creazione di un nuovo articolo sul marketplace.
 */
@Data
public class MarketplaceItemRequest {
    private Long prodottoId;
    private double prezzoUnitario;
    private UnitaDiMisura unitaDiMisura;
    private int stockDisponibile;
}
