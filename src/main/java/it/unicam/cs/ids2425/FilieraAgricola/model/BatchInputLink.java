package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entità "Arco" del grafo (DAG).
 * Collega un lotto di input a un lotto di output.
 * Esempio: (Output: Lotto Salsa, Input: Lotto Pomodoro)
 * (Output: Lotto Salsa, Input: Lotto Olio)
 */
@Entity
@Data
@NoArgsConstructor
public class BatchInputLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Il lotto di output (trasformato) (es. Lotto Salsa).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "output_batch_id")
    private ProductBatch outputBatch;

    /**
     * Il lotto di input (ingrediente) (es. Lotto Pomodoro).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "input_batch_id")
    private ProductBatch inputBatch;

    // Potremmo aggiungere 'quantitaUtilizzata' se necessario
}