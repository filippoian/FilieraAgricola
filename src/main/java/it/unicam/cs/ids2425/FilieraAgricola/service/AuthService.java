package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LoginRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RegistrazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.LoginResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.ERole; // Importa il nuovo Enum
import it.unicam.cs.ids2425.FilieraAgricola.model.Role; // Importa la nuova Entità
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.RoleRepository; // Importa il nuovo Repo
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unicam.cs.ids2425.FilieraAgricola.security.JwtUtils;
import it.unicam.cs.ids2425.FilieraAgricola.exception.EmailAlreadyExistException;
// import it.unicam.cs.ids2425.FilieraAgricola.exception.InvalidRoleException; // Rimosso
// import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente; // Rimosso
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private UtenteRepository utenteRepository;
    private RoleRepository roleRepository; // Aggiunto
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UtenteRepository utenteRepository,
                       RoleRepository roleRepository, // Aggiunto
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.utenteRepository = utenteRepository;
        this.roleRepository = roleRepository; // Aggiunto
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse registraUtente(RegistrazioneRequest request) {
        // Logica del ruolo rimossa: non si accetta più un ruolo dalla request
        // try { ... } catch (IllegalArgumentException e) { ... } Rimosso

        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email già registrata");
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(request.getNome());
        nuovoUtente.setEmail(request.getEmail());
        nuovoUtente.setPassword(passwordEncoder.encode(request.getPassword()));

        // --- Nuova Logica Ruoli ---
        // Assegna il ruolo di default (es. UTENTEGENERICO o ACQUIRENTE)
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_UTENTEGENERICO)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo di default non trovato nel database."));
        roles.add(userRole);
        nuovoUtente.setRoles(roles);
        // --- Fine Nuova Logica Ruoli ---

        Utente salvato = utenteRepository.save(nuovoUtente);

        // Prepara la lista di ruoli per il token
        List<String> ruoliPerToken = salvato.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(salvato.getEmail(), ruoliPerToken);

        return new LoginResponse(token, salvato);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Errore interno: Utente non trovato dopo autenticazione"));

        // Prepara la lista di ruoli per il token
        List<String> ruoliPerToken = utente.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(utente.getEmail(), ruoliPerToken);

        return new LoginResponse(token, utente);
    }
}
