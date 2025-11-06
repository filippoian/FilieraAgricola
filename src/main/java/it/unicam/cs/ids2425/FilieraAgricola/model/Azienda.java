package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Azienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    private String indirizzo;

    private BigDecimal latitudine;
    private BigDecimal longitudine;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @Column(nullable = false)
    private boolean approvato = false;
}
