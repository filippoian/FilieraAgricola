package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.PrenotazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.PrenotazioneResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Evento;
import it.unicam.cs.ids2425.FilieraAgricola.model.PrenotazioneEvento;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.EventoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.PrenotazioneEventoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PrenotazioneEventoRepository prenotazioneEventoRepository;
    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    public PrenotazioneResponse prenotaEvento(PrenotazioneRequest request) {
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        Utente utente = utenteRepository.findById(request.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        PrenotazioneEvento p = new PrenotazioneEvento();
        p.setEvento(evento);
        p.setUtente(utente);

        return new PrenotazioneResponse(prenotazioneEventoRepository.save(p));
    }

    public List<PrenotazioneResponse> getPrenotazioniByUtente(Long utenteId) {
        return prenotazioneEventoRepository.findByUtenteId(utenteId)
                .stream()
                .map(PrenotazioneResponse::new)
                .collect(Collectors.toList());
    }
}
