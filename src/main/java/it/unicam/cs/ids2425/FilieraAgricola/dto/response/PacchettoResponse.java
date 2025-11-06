package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Pacchetto;
import lombok.Data;

@Data
public class PacchettoResponse {
    private Long id;
    private String nome;
    private String descrizione;

    public PacchettoResponse(Pacchetto pacchetto) {
        this.id = pacchetto.getId();
        this.nome = pacchetto.getNome();
        this.descrizione = pacchetto.getDescrizione();
    }
}
