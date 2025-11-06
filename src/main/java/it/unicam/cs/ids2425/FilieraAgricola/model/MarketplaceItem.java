package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketplaceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "seller_user_id", nullable = false)
    private Utente venditore;

    @Column(nullable = false)
    private double prezzoUnitario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitaDiMisura unitaDiMisura;

    @Column(nullable = false)
    private int stockDisponibile;
}
