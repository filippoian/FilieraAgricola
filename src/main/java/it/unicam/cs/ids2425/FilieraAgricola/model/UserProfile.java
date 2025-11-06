package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contiene i dati anagrafici comuni a tutti gli utenti registrati,
 * collegato 1-a-1 con l'entità UserAccount (Utente).
 */
@Entity
@Data
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione 1-a-1 inversa con Utente (UserAccount)
    @OneToOne
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private Utente utente;

    @Column(length = 100)
    private String nome;

    @Column(length = 100)
    private String cognome;

    private String telefono;

    public UserProfile(Utente utente, String nome, String cognome, String telefono) {
        this.utente = utente;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
    }
}