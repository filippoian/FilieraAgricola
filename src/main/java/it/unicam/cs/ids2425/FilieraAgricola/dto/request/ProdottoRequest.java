package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

@Data
public class ProdottoRequest {
    private String nome;
    private String descrizione;
    private String categoria;
    private String certificazioni;
    private String metodiColtivazione;
    private Long utenteId; // chi ha inserito il prodotto
}