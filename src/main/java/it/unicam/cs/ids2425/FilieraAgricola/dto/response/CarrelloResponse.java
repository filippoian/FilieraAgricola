package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CarrelloResponse {
    private Long carrelloId;
    private Long utenteId;
    private List<ArticoloCarrelloResponse> articoli;
}