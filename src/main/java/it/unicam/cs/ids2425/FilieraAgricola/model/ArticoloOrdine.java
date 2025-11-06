package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class ArticoloOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    @ManyToOne
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    // Aggiungiamo il prezzo al momento dell'ordine per storicizzarlo
    // Assumendo che il prodotto abbia un metodo getPrezzo() di tipo BigDecimal
    // Se non ce l'ha, puoi ometterlo o aggiungerlo all'entità Prodotto.
    // @Column(nullable = false)
    // private BigDecimal prezzoUnitario;

    public ArticoloOrdine(Ordine ordine, Prodotto prodotto, int quantita) {
        this.ordine = ordine;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }
}