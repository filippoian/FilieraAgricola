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

    @Transactional
    public EventoResponse creaEvento(EventoRequest request) {
        Utente organizzatore = utenteRepository.findById(request.getOrganizzatoreId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + request.getOrganizzatoreId()));

        // Controllo Sicurezza: Solo l'utente stesso può creare un evento a suo nome
        // (o un Gestore/Curatore)
        checkOwnershipOrAdmin(organizzatore.getId(), "creare eventi");

        Evento evento = new Evento();
        evento.setNome(request.getNome());
        evento.setDescrizione(request.getDescrizione());
        evento.setLuogo(request.getLuogo());
        evento.setData(request.getData());
        evento.setOrganizzatore(organizzatore);

        Evento savedEvento = eventoRepository.save(evento);

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

        // Controllo Sicurezza: Solo proprietario o admin
        checkOwnershipOrAdmin(evento.getOrganizzatore().getId(), "aggiornare");

        evento.setNome(request.getNome());
        evento.setDescrizione(request.getDescrizione());
        evento.setData(request.getData());
        evento.setLuogo(request.getLuogo());

        // Se l'evento viene modificato, la sua sottomissione torna in BOZZA
        // per una nuova approvazione
        ContentSubmission submission = evento.getSubmission();
        if (submission != null && submission.getStatus() != StatoContenuto.BOZZA) {
            submission.setStatus(StatoContenuto.BOZZA);
            submission.setFeedbackCuratore("Modificato, richiede nuova approvazione.");
            submission.updateState(); // Aggiorna l'oggetto di stato transiente
            submissionRepository.save(submission);
        }

        return new EventoResponse(eventoRepository.save(evento));
    }

    @Transactional
    public void eliminaEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        // Controllo Sicurezza: Solo proprietario o admin
        checkOwnershipOrAdmin(evento.getOrganizzatore().getId(), "eliminare");

        // L'entità Evento ha 'cascade = CascadeType.ALL, orphanRemoval = true'
        // sulla submission, quindi eliminando l'evento si elimina
        // automaticamente anche la submission associata.
        eventoRepository.deleteById(id);
    }

    /**
     * Metodo pubblico per visualizzare solo eventi approvati.
     * (Alias di getAllEventi, che ora fa già questo filtro)
     */
    public List<EventoResponse> getEventiApprovati() {
        return this.getAllEventi();
    }

    /**
     * Metodo helper per controllare i permessi.
     * Verifica se l'utente autenticato è il proprietario (tramite ID)
     * o ha un ruolo di admin (CURATORE, GESTORE).
     */
    private void checkOwnershipOrAdmin(Long ownerId, String azione) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Nessun utente autenticato.");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CURATORE") || role.equals("ROLE_GESTORE"));

        if (isAdmin) {
            return; // Gli admin possono procedere
        }

        // Se non è admin, controlla la proprietà
        String userEmail = authentication.getName();
        Optional<Utente> utenteAttuale = utenteRepository.findByEmail(userEmail);

        if (utenteAttuale.isEmpty() || !utenteAttuale.get().getId().equals(ownerId)) {
            throw new AccessDeniedException("Non hai i permessi per " + azione + " questo evento.");
        }
    }
}