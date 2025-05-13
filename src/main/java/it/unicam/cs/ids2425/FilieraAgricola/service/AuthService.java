package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LoginRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RegistrazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.LoginResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente registraUtente(RegistrazioneRequest request) {
        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setEmail(request.getEmail());
        String password = request.getPassword();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        nuovoUtente.setPassword(hashedPassword);
        nuovoUtente.setRuolo(request.getRuolo());

        System.out.println("Utente salvato");
        return utenteRepository.save(nuovoUtente);
    }

    public LoginResponse login(LoginRequest request) {
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email non registrata"));

        String password = request.getPassword();
        boolean match = BCrypt.checkpw(password, utente.getPassword());

        if (!match) {
            throw new RuntimeException("Password errata");
        }

        return new LoginResponse(utente);
    }
}
