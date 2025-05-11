package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoPacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "pacchetto_id", nullable = false)
    private Pacchetto pacchetto;

    private Integer quantita = 1;
}
