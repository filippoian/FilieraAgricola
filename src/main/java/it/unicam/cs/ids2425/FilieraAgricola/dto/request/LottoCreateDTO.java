package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO per la richiesta di creazione di un nuovo Lotto (ProductBatch).
 * , include gli ID dei lotti di input.
 */
@Data
public class LottoCreateDTO {
    private Long productId; // ID del Prodotto "modello" (es. Pomodoro)
    private double quantitaIniziale;
    private String codiceLottoUnivoco;
    private LocalDate dataProduzione;

    /**
     * Lista degli ID dei lotti usati come input.
     * Vuota se è un lotto "sorgente" (raccolto).
     * Piena se è un lotto "trasformato" (marmellata).
     */
    private List<Long> inputBatchIds;
}