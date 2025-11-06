package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Lob
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "distributore_id", nullable = false)
    private Utente distributore;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo_totale;

    // Relazione Uno-a-Molti con la tabella di join
    @OneToMany(mappedBy = "pacchetto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MarketplaceItemPacchetto> items = new HashSet<>();
}
