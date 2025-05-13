package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

@Data
public class AziendaRequest {
    private String nome;
    private String indirizzo;
    private Double latitudine;
    private Double longitudine;
    private Long utenteId;
}
