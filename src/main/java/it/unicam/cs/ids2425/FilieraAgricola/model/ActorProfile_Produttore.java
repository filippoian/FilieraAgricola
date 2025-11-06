package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Profilo specializzato per l'attore Produttore.
 * Collega l'anagrafica (UserProfile) e il punto fisico (FilieraPoint).
 */
@Entity
@Data
@NoArgsConstructor
public class ActorProfile_Produttore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione 1-a-1 con l'anagrafica
    @OneToOne
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    // Relazione 1-a-1 con il punto filiera (la sua azienda)
    @OneToOne
    @JoinColumn(name = "filiera_point_id", nullable = false, unique = true)
    private FilieraPoint filieraPoint;

    private String ragioneSociale;
    private String partitaIva;

    @Lob
    private String descrizioneAzienda;
}