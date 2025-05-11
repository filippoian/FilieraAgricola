package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "acquirente_id")
    private Utente acquirente;

    private LocalDateTime data = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato = StatoOrdine.In_elaborazione;
}