package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PacchettoRequest {
    private String nome;
    private String descrizione;
    private Long distributoreId;
    private List<Long> prodottiIds;
}
