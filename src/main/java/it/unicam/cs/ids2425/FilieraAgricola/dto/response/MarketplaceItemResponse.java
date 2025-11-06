package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.MarketplaceItem;
import it.unicam.cs.ids2425.FilieraAgricola.model.UnitaDiMisura;
import lombok.Data;

/**
 * DTO per la risposta di un articolo sul marketplace.
 * Include dettagli sul prodotto e sul venditore.
 */
@Data
public class MarketplaceItemResponse {
    private Long itemId;
    private double prezzoUnitario;
    private UnitaDiMisura unitaDiMisura;
    private int stockDisponibile;
    private ProdottoInItemResponse prodotto;
    private VenditoreInItemResponse venditore;

    // Costruttore che mappa dall'entità
    public MarketplaceItemResponse(MarketplaceItem item) {
        this.itemId = item.getId();
        this.prezzoUnitario = item.getPrezzoUnitario();
        this.unitaDiMisura = item.getUnitaDiMisura();
        this.stockDisponibile = item.getStockDisponibile();
        this.prodotto = new ProdottoInItemResponse(item.getProdotto());
        this.venditore = new VenditoreInItemResponse(item.getVenditore());
    }

    // Sotto-DTO per i dettagli del prodotto
    @Data
    private static class ProdottoInItemResponse {
        private Long prodottoId;
        private String nomeProdotto;
        private String descrizione;

        ProdottoInItemResponse(it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto prodotto) {
            this.prodottoId = prodotto.getId();
            this.nomeProdotto = prodotto.getNome();
            this.descrizione = prodotto.getDescrizione();
        }
    }

    // Sotto-DTO per i dettagli del venditore
    @Data
    private static class VenditoreInItemResponse {
        private Long venditoreId;
        private String nomeVenditore;

        VenditoreInItemResponse(it.unicam.cs.ids2425.FilieraAgricola.model.Utente venditore) {
            this.venditoreId = venditore.getId();
            this.nomeVenditore = venditore.getNome();
        }
    }
}
