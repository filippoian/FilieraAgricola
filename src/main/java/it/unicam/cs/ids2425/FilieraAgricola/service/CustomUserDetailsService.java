package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set; // Importato Set
import java.util.stream.Collectors; // Importato Collectors

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nessun utente trovato con l'email: " + email));

        // Converte il Set<Role> in un Set<SimpleGrantedAuthority>
        Set<SimpleGrantedAuthority> authorities = utente.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());

        return new CustomUserDetails(
                utente.getId(),
                utente.getEmail(),
                utente.getPassword(),
                authorities // Passa il Set di autorità
        );
    }
}