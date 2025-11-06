package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ordini") // Specifichiamo il nome della tabella per chiarezza
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "acquirente_id", nullable = false)
    private Utente acquirente;

    @Column(nullable = false)
    private LocalDateTime data = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato = StatoOrdine.In_elaborazione;

    @Column(precision = 10, scale = 2)
    private BigDecimal totale;

    // Relazione Uno-a-Molti con le righe dell'ordine
    // CascadeType.ALL: Se salvo un Ordine, salva anche le sue linee.
    // orphanRemoval=true: Se rimuovo una linea dalla lista, viene eliminata dal DB.
    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> linee = new ArrayList<>();

    // Metodo helper per aggiungere linee in modo sicuro (mantiene la coerenza)
    public void addLinea(OrderLine linea) {
        linee.add(linea);
        linea.setOrdine(this);
    }
}
