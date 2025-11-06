package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utenti") // Meglio specificare il nome della tabella
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    // --- CAMPO RIMOSSO ---
    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private RuoloUtente ruolo;

    // --- CAMPO AGGIUNTO ---
    @ManyToMany(fetch = FetchType.EAGER) // EAGER è utile per la sicurezza
    @JoinTable(name = "utente_roles",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
