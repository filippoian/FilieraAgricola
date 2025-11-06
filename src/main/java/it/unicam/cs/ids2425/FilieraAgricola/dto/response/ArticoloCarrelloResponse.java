package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticoloCarrelloResponse {
    private Long prodottoId;
    private String nomeProdotto;
    private int quantita;
}