package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.EventoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.EventoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Evento;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.EventoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    public EventoResponse creaEvento(EventoRequest request) {
        Utente organizzatore = utenteRepository.findById(request.getOrganizzatoreId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + request.getOrganizzatoreId()));

        Evento evento = new Evento();
        evento.setNome(request.getNome());
        evento.setDescrizione(request.getDescrizione());
        evento.setLuogo(request.getLuogo());
        evento.setData(request.getData());
        evento.setOrganizzatore(organizzatore);

        return new EventoResponse(eventoRepository.save(evento));
    }

    public List<EventoResponse> getAllEventi() {
        return eventoRepository.findAll()
                .stream()
                .map(EventoResponse::new)
                .collect(Collectors.toList());
    }

    public List<EventoResponse> getEventiDaApprovare() {
        return eventoRepository.findByApprovatoFalse()
                .stream()
                .map(EventoResponse::new)
                .collect(Collectors.toList());
    }

    public void approvaEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        evento.setApprovato(true);
        eventoRepository.save(evento);
    }

    public EventoResponse aggiornaEvento(Long id, EventoRequest request) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        evento.setNome(request.getNome());
        evento.setDescrizione(request.getDescrizione());
        evento.setData(request.getData());
        evento.setLuogo(request.getLuogo());

        return new EventoResponse(eventoRepository.save(evento));
    }

    public void eliminaEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento non trovato");
        }
        eventoRepository.deleteById(id);
    }

    public List<EventoResponse> getEventiApprovati() {
        return eventoRepository.findByApprovatoTrue()
                .stream()
                .map(EventoResponse::new)
                .collect(Collectors.toList());
    }
}