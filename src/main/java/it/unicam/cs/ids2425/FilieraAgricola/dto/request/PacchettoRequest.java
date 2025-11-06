package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;
import java.math.BigDecimal; // Import
import java.util.List;

@Data
public class PacchettoRequest {
    private String nome;
    private String descrizione;
    private Long distributoreId;

    /**
     * Il prezzo forfettario del pacchetto.
     */
    private BigDecimal prezzoTotale; // Aggiunto

    /**
     * Lista degli ID degli ARTICOLI (MarketplaceItem), non dei Prodotti.
     */
    private List<Long> marketplaceItemIds; // Modificato (da prodottiIds)
}