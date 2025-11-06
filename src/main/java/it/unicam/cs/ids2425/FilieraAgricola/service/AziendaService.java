package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AziendaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.AziendaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Azienda;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.AziendaRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AziendaService {

    private final AziendaRepository aziendaRepository;
    private final UtenteRepository utenteRepository;

    public AziendaService(AziendaRepository aziendaRepository, UtenteRepository utenteRepository) {
        this.aziendaRepository = aziendaRepository;
        this.utenteRepository = utenteRepository;
    }

    public AziendaResponse creaAzienda(AziendaRequest request) {
        Utente utente = utenteRepository.findById(request.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Azienda azienda = new Azienda();
        azienda.setNome(request.getNome());
        azienda.setIndirizzo(request.getIndirizzo());
        azienda.setLatitudine(BigDecimal.valueOf(request.getLatitudine()));
        azienda.setLongitudine(BigDecimal.valueOf(request.getLongitudine()));
        azienda.setUtente(utente);

        return new AziendaResponse(aziendaRepository.save(azienda));
    }

    public AziendaResponse getAzienda(Long id) {
        Azienda azienda = aziendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        return new AziendaResponse(azienda);
    }

    public List<AziendaResponse> getAll() {
        return aziendaRepository.findAll()
                .stream()
                .map(AziendaResponse::new)
                .collect(Collectors.toList());
    }

    public List<AziendaResponse> getAziendeConCoordinate(String ruolo) {
        return aziendaRepository.findAll().stream()
                .filter(a -> a.getLatitudine() != null && a.getLongitudine() != null)
                .filter(a -> a.getUtente() != null)
                .filter(a -> ruolo == null || ruolo.isEmpty() || a.getUtente().getRuolo().name().equalsIgnoreCase(ruolo))
                .map(AziendaResponse::new)
                .collect(Collectors.toList());
    }

    public List<AziendaResponse> getAziendeDaApprovare() {
        return aziendaRepository.findByApprovatoFalse()
                .stream()
                .map(AziendaResponse::new)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasAuthority('Curatore')")
    public void approvaAzienda(Long id) {
        Azienda azienda = aziendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata"));
        azienda.setApprovato(true);
        aziendaRepository.save(azienda);
    }

    public AziendaResponse aggiornaAzienda(Long id, AziendaRequest request) {
        Azienda azienda = aziendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Azienda non trovata"));

        azienda.setNome(request.getNome());
        azienda.setIndirizzo(request.getIndirizzo());
        azienda.setLatitudine(BigDecimal.valueOf(request.getLatitudine()));
        azienda.setLongitudine(BigDecimal.valueOf(request.getLongitudine()));

        return new AziendaResponse(aziendaRepository.save(azienda));
    }

    public void eliminaAzienda(Long id) {
        if (!aziendaRepository.existsById(id)) {
            throw new RuntimeException("Azienda non trovata");
        }
        aziendaRepository.deleteById(id);
    }

    public List<AziendaResponse> getAziendeApprovate() {
        return aziendaRepository.findByApprovatoTrue()
                .stream()
                .map(AziendaResponse::new)
                .collect(Collectors.toList());
    }
}