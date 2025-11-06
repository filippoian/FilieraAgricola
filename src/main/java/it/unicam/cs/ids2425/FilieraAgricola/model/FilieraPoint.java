package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Entità centrale per la mappatura OSM e la tracciabilità, come da specifica.
 * Sostituisce la logica precedentemente implementata in "Azienda".
 */
@Entity
@Data
@NoArgsConstructor
public class FilieraPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomePunto;

    @Lob
    private String descrizione;

    private String indirizzo;

    // Latitudine e Longitudine (precedentemente in Azienda)
    @Column(precision = 10, scale = 7) // Precisione per coordinate geografiche
    private BigDecimal latitudine;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitudine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFilieraPoint tipo;

    // Riferimento all'utente proprietario (Gestore/Produttore/etc)
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
}