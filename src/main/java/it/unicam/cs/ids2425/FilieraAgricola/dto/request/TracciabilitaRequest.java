package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TracciabilitaRequest {
    private Long prodottoId;
    private String fase;
    private String descrizione;
    private LocalDate data;
}
