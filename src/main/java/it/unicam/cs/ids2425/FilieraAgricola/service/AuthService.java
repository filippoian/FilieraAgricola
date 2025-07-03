package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LoginRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RegistrazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.LoginResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unicam.cs.ids2425.FilieraAgricola.config.JwtUtils;
import it.unicam.cs.ids2425.FilieraAgricola.exception.EmailAlreadyExistException;
import it.unicam.cs.ids2425.FilieraAgricola.exception.InvalidRoleException;
import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse registraUtente(RegistrazioneRequest request) {
        RuoloUtente ruolo;
        try {
            ruolo = RuoloUtente.valueOf(request.getRuolo().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Ruolo non valido: " + request.getRuolo());
        }

        System.out.println("Ruolo ricevuto: " + request.getRuolo());
        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email già registrata");
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setEmail(request.getEmail());
        nuovoUtente.setPassword(passwordEncoder.encode(request.getPassword()));
        nuovoUtente.setRuolo(ruolo);

        Utente salvato = utenteRepository.save(nuovoUtente);
        String token = jwtUtils.generateToken(salvato.getEmail(), salvato.getRuolo().name());

        return new LoginResponse(token, salvato.getId(), salvato.getNome(), salvato.getEmail(), salvato.getRuolo());
    }

    public LoginResponse login(LoginRequest request) {
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email non registrata"));

        if (!passwordEncoder.matches(request.getPassword(), utente.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        String token = jwtUtils.generateToken(utente.getEmail(), utente.getRuolo().name());

        return new LoginResponse(token, utente.getId(), utente.getNome(), utente.getEmail(), utente.getRuolo());
    }
}
