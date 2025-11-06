package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LoginRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RegistrazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.LoginResponse;
import it.unicam.cs.ids2425.FilieraAgricola.exception.EmailAlreadyExistException;
import it.unicam.cs.ids2425.FilieraAgricola.model.ERole;
import it.unicam.cs.ids2425.FilieraAgricola.model.Role;
import it.unicam.cs.ids2425.FilieraAgricola.model.UserProfile; // Import
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.RoleRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UserProfileRepository; // Import
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import it.unicam.cs.ids2425.FilieraAgricola.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private UtenteRepository utenteRepository;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // --- INIEZIONE AGGIUNTA ---
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public AuthService(UtenteRepository utenteRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager,
                       UserProfileRepository userProfileRepository) { // Aggiunto
        this.utenteRepository = utenteRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userProfileRepository = userProfileRepository; // Aggiunto
    }

    @Transactional // Aggiunto @Transactional per garantire l'atomicità
    public LoginResponse registraUtente(RegistrazioneRequest request) {

        if (utenteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email già registrata");
        }

        // 1. Crea l'entità UserAccount (Utente)
        Utente nuovoUtente = new Utente();
        nuovoUtente.setEmail(request.getEmail());
        nuovoUtente.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_UTENTEGENERICO)
                .orElseThrow(() -> new RuntimeException("Errore: Ruolo di default non trovato."));
        roles.add(userRole);
        nuovoUtente.setRoles(roles);

        // Salva l'utente per generare l'ID
        Utente salvato = utenteRepository.save(nuovoUtente);

        // 2. Crea l'entità UserProfile collegata
        UserProfile nuovoProfilo = new UserProfile(
                salvato,
                request.getNome(),
                request.getCognome(),
                request.getTelefono()
        );
        userProfileRepository.save(nuovoProfilo);

        // Aggiorna l'oggetto utente con il profilo appena creato (opzionale ma pulito)
        salvato.setUserProfile(nuovoProfilo);

        // 3. Genera il token
        List<String> ruoliPerToken = salvato.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        String token = jwtUtils.generateToken(salvato.getEmail(), ruoliPerToken);

        // Passiamo 'salvato' alla LoginResponse
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