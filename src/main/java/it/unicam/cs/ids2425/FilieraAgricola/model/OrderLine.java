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
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione Molti-a-Uno con l'ordine
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    // Una riga può essere o un item...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marketplace_item_id", nullable = true)
    private MarketplaceItem item;

    // ...o un pacchetto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacchetto_id", nullable = true)
    private Pacchetto pacchetto;

    @Column(nullable = false)
    private Integer quantita;

    /**
     * Prezzo "congelato" al momento dell'acquisto.
     * Questo è il prezzo *unitario* (o del pacchetto) al momento del checkout.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoDiAcquisto;

    /**
     * Metodo helper per calcolare il subtotale della linea.
     */
    public BigDecimal getSubtotale() {
        return prezzoDiAcquisto.multiply(new BigDecimal(quantita));
    }
}
