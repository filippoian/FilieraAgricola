package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracciabilita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private String fase;

    @Lob
    private String descrizione;

    private LocalDate data;
}
