package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Ordine;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdineResponse {
    private Long id;
    private LocalDateTime data;
    private String stato;
    private Long acquirenteId;

    public OrdineResponse(Ordine ordine) {
        this.id = ordine.getId();
        this.data = ordine.getData();
        this.stato = ordine.getStato().name();
        this.acquirenteId = ordine.getAcquirente() != null ? ordine.getAcquirente().getId() : null;
    }
}