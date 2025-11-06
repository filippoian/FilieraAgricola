package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class FilieraPoint {
    // ... (campi id, nomePunto, descrizione, indirizzo, lat, lon, tipo, utente)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomePunto;

    @Lob
    private String descrizione;

    private String indirizzo;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitudine;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitudine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFilieraPoint tipo;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    // --- CAMPO AGGIUNTO ---
    /**
     * Collega il FilieraPoint alla sua sottomissione per la Curation (Pattern State).
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "submission_id")
    private ContentSubmission submission;
}