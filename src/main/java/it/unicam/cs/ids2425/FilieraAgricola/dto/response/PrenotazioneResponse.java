package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.PrenotazioneEvento;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrenotazioneResponse {
    private Long id;
    private LocalDateTime dataPrenotazione;
    private Long eventoId;
    private Long utenteId;

    public PrenotazioneResponse(PrenotazioneEvento p) {
        this.id = p.getId();
        this.dataPrenotazione = p.getDataPrenotazione();
        this.eventoId = p.getEvento().getId();
        this.utenteId = p.getUtente().getId();
    }
}