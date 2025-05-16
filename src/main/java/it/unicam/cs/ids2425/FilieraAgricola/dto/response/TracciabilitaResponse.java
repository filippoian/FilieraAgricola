package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Tracciabilita;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TracciabilitaResponse {
    private Long id;
    private String fase;
    private String descrizione;
    private LocalDate data;

    public TracciabilitaResponse(Tracciabilita entity) {
        this.id = entity.getId();
        this.fase = entity.getFase();
        this.descrizione = entity.getDescrizione();
        this.data = entity.getData();
    }
}
