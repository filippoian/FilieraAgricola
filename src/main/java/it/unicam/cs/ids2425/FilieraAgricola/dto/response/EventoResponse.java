package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Evento;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventoResponse {
    private Long id;
    private String nome;
    private String descrizione;
    private LocalDate data;
    private String luogo;

    public EventoResponse(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.descrizione = evento.getDescrizione();
        this.data = evento.getData();
        this.luogo = evento.getLuogo();
    }
}