package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ActorProfile_Trasformatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    @OneToOne
    @JoinColumn(name = "filiera_point_id", nullable = false, unique = true)
    private FilieraPoint filieraPoint;

    private String ragioneSociale;
    private String partitaIva;

    @Lob
    private String descrizioneLaboratorio;
}