package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Rappresenta una fase specifica del ciclo produttivo applicata a un Lotto.
 * Es. (Lotto: 1, Azione: RACCOLTA, Data: ...)
 * (Lotto: 1, Azione: STOCCAGGIO, Data: ...)
 */
@Entity
@Data
@NoArgsConstructor
public class TraceabilityStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Il lotto a cui si riferisce questa fase.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "batch_id")
    private ProductBatch batch;

    /**
     * L'utente che ha eseguito/registrato la fase.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private Utente utente;

    /**
     * Il luogo (azienda, laboratorio) dove è avvenuta la fase.
     */
    @ManyToOne
    @JoinColumn(name = "filiera_point_id")
    private FilieraPoint filieraPoint;

    @Column(nullable = false)
    private String azione; // Enum: PRODUZIONE, RACCOLTA, TRASFORMAZIONE, STOCCAGGIO...

    @Lob
    private String descrizione;

    @Column(nullable = false)
    private LocalDateTime dataStep;
}