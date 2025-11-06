package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;
import java.util.List;

/**
 * DTO che rappresenta il payload per la richiesta POST /api/ordini/checkout,
 * come da specifica.
 */
@Data
public class CarrelloCheckoutDTO {

    private List<Item> items;     // Lista di articoli singoli
    private List<Item> pacchetti; // Lista di pacchetti

    /**
     * Sotto-DTO per rappresentare una linea nel carrello.
     */
    @Data
    public static class Item {
        private Long id; // ID del MarketplaceItem o del Pacchetto
        private int quantita;
    }
}