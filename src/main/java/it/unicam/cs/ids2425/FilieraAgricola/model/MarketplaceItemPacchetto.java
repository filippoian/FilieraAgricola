package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "marketplace_item_pacchetto")
public class MarketplaceItemPacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marketplace_item_id", nullable = false)
    private MarketplaceItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacchetto_id", nullable = false)
    private Pacchetto pacchetto;

    @Column(nullable = false)
    private Integer quantita = 1;
}
