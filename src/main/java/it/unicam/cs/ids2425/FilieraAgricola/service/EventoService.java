package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.EventoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.EventoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.ContentSubmission;
import it.unicam.cs.ids2425.FilieraAgricola.model.Evento;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ContentSubmissionRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.EventoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;
    private final ContentSubmissionRepository submissionRepository;

    // Servizio Curation iniettato per gestire l'approvazione
    private final CurationService curationService;

    @Transactional
    public EventoResponse creaEvento(EventoRequest request) {
        Utente organizzatore = utenteRepository.findById(request.getOrganizzatoreId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + request.getOrganizzatoreId()));

        checkOwnershipOrAdmin(organizzatore.getId(), "creare eventi");

        Evento evento = new Evento();
        evento.setNome(request.getNome());
        evento.setDescrizione(request.getDescrizione());
        evento.setLuogo(request.getLuogo());
        evento.setData(request.getData());
        evento.setOrganizzatore(organizzatore);

        Evento savedEvento = eventoRepository.save(evento);

        // Crea la sottomissione in stato BOZZA
        ContentSubmission submission = new ContentSubmission(savedEvento.getId(), "EVENTO");
        ContentSubmission savedSubmission = submissionRepository.save(submission);

        savedEvento.setSubmission(savedSubmission);
        eventoRepository.save(savedEvento);

        return new EventoResponse(savedEvento);
    }

    /**
     * Ritorna tutti gli eventi visibili al pubblico (APPROVATI).
     */
    public List<EventoResponse> getAllEventi() {
        return eventoRepository.findAll()
                .stream()
                .filter(e -> e.getSubmission() != null &&
                        e.getSubmission().getStatus() == StatoContenuto.APPROVATO)
                .map(EventoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventoResponse aggiornaEvento(Long id, EventoRequest request) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        checkOwnershipOrAdmin(evento.getOrganizzatore().getId(), "aggiornare");

        evento.setNome(request.getNome());
        evento.setDescrizione(request.getDescrizione());
        evento.setData(request.getData());
        evento.setLuogo(request.getLuogo());

        ContentSubmission submission = evento.getSubmission();
        if (submission != null && submission.getStatus() != StatoContenuto.BOZZA) {
            submission.setStatus(StatoContenuto.BOZZA);
            submission.setFeedbackCuratore("Modificato, richiede nuova approvazione.");
            submission.updateState();
            submissionRepository.save(submission);
        }

        return new EventoResponse(eventoRepository.save(evento));
    }

    @Transactional
    public void eliminaEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        checkOwnershipOrAdmin(evento.getOrganizzatore().getId(), "eliminare");

        eventoRepository.deleteById(id);
    }

    public List<EventoResponse> getEventiApprovati() {
        return this.getAllEventi();
    }


    @Transactional(readOnly = true)
    public List<EventoResponse> getEventiDaApprovare() {
        // Trova le submission IN_REVISIONE di tipo EVENTO
        List<Long> idsEventi = submissionRepository.findByStatus(StatoContenuto.IN_REVISIONE)
                .stream()
                .filter(s -> s.getSubmittableEntityType().equals("EVENTO"))
                .map(ContentSubmission::getSubmittableEntityId)
                .collect(Collectors.toList());

        // Recupera gli eventi corrispondenti
        return eventoRepository.findAllById(idsEventi)
                .stream()
                .map(EventoResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approvaEvento(Long id) {
        // Trova l'evento
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        // Trova la submission collegata e approva tramite CurationService
        ContentSubmission submission = evento.getSubmission();
        if (submission == null) {
            throw new RuntimeException("Evento non sottomesso per l'approvazione");
        }
        curationService.approvaContenuto(submission.getId());
    }

    private void checkOwnershipOrAdmin(Long ownerId, String azione) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Nessun utente autenticato.");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CURATORE") || role.equals("ROLE_GESTORE"));

        if (isAdmin) {
            return;
        }

        String userEmail = authentication.getName();
        Optional<Utente> utenteAttuale = utenteRepository.findByEmail(userEmail);

        if (utenteAttuale.isEmpty() || !utenteAttuale.get().getId().equals(ownerId)) {
            throw new AccessDeniedException("Non hai i permessi per " + azione + " questo evento.");
        }
    }
}