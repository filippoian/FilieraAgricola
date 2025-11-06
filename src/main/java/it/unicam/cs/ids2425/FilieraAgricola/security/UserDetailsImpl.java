package it.unicam.cs.ids2425.FilieraAgricola.security;

// import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente; // Rimosso
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
// import java.util.Collections; // Rimosso
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String password;

    // --- MODIFICATO ---
    // Da 'private GrantedAuthority authority;'
    // a 'private Collection<? extends GrantedAuthority> authorities;'
    private Collection<? extends GrantedAuthority> authorities;

    // --- MODIFICATO ---
    // Il costruttore ora accetta una Collection
    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Utente utente) {

        // --- MODIFICATO ---
        // Converte il Set<Role> (dall'entità Utente)
        // in una List<GrantedAuthority>
        List<GrantedAuthority> authorities = utente.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                utente.getId(),
                utente.getEmail(),
                utente.getPassword(),
                authorities // Passa la lista
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // --- MODIFICATO ---
        // Restituisce la collezione di autorità
        return authorities;
    }

    public Long getId() {
        return id;
    }
    // ... (il resto della classe (getPassword, getUsername, isEnabled...) rimane invariato) ...
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Usiamo l'email come username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

