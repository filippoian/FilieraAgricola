package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LoginRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RegistrazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.LoginResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteRepository utenteRepository;

    public AuthService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente registraUtente(RegistrazioneRequest request) {
        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setEmail(request.getEmail());
        nuovoUtente.setPassword(request.getPassword());
        nuovoUtente.setRuolo(request.getRuolo());

        return utenteRepository.save(nuovoUtente);
    }

    public LoginResponse login(LoginRequest request) {
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email non registrata"));

        if (!utente.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        return new LoginResponse(utente);
    }
}
