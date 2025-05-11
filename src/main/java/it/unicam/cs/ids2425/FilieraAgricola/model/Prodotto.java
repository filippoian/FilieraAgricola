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
}
