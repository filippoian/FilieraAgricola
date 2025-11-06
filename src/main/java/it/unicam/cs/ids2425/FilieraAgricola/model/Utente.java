package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- CAMPO RIMOSSO ---
    // 'nome' è stato spostato in UserProfile
    // @Column(nullable = false, length = 100)
    // private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email; // Questo è l'username

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "utente_roles",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // --- CAMPO AGGIUNTO ---
    // Relazione 1-a-1 con il profilo anagrafico
    // 'mappedBy' indica che l'entità UserProfile gestisce la colonna di join.
    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;
}