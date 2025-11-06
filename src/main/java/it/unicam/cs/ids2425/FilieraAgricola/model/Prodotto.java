package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Lob
    private String descrizione;

    private String categoria;

    @Lob
    private String certificazioni;

    @Lob
    private String metodiColtivazione;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    /**
     * Stato del prodotto nel ciclo di Curation (BOZZA, IN_REVISIONE, APPROVATO, RIFIUTATO).
     * Di default è in BOZZA.
     */
    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private StatoContenuto status = StatoContenuto.BOZZA;

    /**
     * Collega il Prodotto alla sua sottomissione per la Curation (Pattern State).
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "submission_id")
    private ContentSubmission submission;
}