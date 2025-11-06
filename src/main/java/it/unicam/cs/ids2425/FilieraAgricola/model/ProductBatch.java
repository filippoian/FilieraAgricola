package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entità che rappresenta un Lotto di Produzione (es. "Raccolto del 15/10").
 * È il nodo centrale del grafo di tracciabilità (DAG).
 */
@Entity
@Data
@NoArgsConstructor
public class ProductBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Il prodotto "modello" a cui questo lotto appartiene (es. "Pomodoro San Marzano").
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Prodotto prodotto;

    @Column(nullable = false)
    private LocalDate dataProduzione;

    private double quantitaIniziale; // Es. 100 (KG, PZ, L)

    @Column(nullable = false, unique = true)
    private String codiceLottoUnivoco; // Es. "PSM-151024-A"

    /**
     * Lista dei lotti "figli" (output) che hanno usato questo lotto come input.
     * Serve per navigare il grafo in avanti (raro).
     */
    @OneToMany(mappedBy = "inputBatch")
    private Set<BatchInputLink> lottiOutput = new HashSet<>();

    /**
     * Lista dei lotti "genitori" (input) usati per creare questo lotto.
     * Serve per navigare il grafo all'indietro (fondamentale).
     */
    @OneToMany(mappedBy = "outputBatch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BatchInputLink> lottiInput = new HashSet<>();

    /**
     * Lista delle fasi di lavorazione (es. RACCOLTA, TRASFORMAZIONE)
     * subite da questo specifico lotto.
     */
    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TraceabilityStep> steps = new HashSet<>();
}