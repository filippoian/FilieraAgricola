package it.unicam.cs.ids2425.FilieraAgricola.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entità centrale per il flusso di Curation (Pattern State).
 * Rappresenta la sottomissione di un contenuto (Prodotto, Evento, ecc.)
 * per l'approvazione.
 */
@Entity
@Data
@NoArgsConstructor
public class ContentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Identificazione Polimorfica del Contenuto ---
    // Come da specifica, usiamo ID e Tipo per collegare
    // a diverse entità (Prodotto, Evento, FilieraPoint).

    /**
     * L'ID dell'entità sottomessa (es. Prodotto ID: 15).
     */
    @Column(nullable = false)
    private Long submittableEntityId;

    /**
     * Il tipo di entità sottomessa (es. "PRODOTTO", "EVENTO").
     */
    @Column(nullable = false)
    private String submittableEntityType;

    // --- Gestione dello Stato ---

    /**
     * Lo stato attuale del ciclo di vita, persistito nel DB.
     * Questo è l'enum 'StatoContenuto' che ora è centralizzato.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoContenuto status;

    /**
     * Feedback opzionale lasciato dal Curatore (es. in caso di rifiuto).
     */
    @Lob
    private String feedbackCuratore;

    /**
     * Oggetto di stato transiente che implementa il Pattern State.
     * NON viene persistito nel DB.
     */
    @Transient
    private ContentState state;

    public ContentSubmission(Long entityId, String entityType) {
        this.submittableEntityId = entityId;
        this.submittableEntityType = entityType;
        this.status = StatoContenuto.BOZZA;
        // All'avvio, impostiamo lo stato concreto corretto
        this.updateState();
    }

    /**
     * Metodo per sincronizzare l'oggetto di stato transiente (@Transient)
     * con lo stato persistito (@Enumerated) nel database.
     * Questo è il cuore del Pattern State.
     */
    public void updateState() {
        switch (this.status) {
            case BOZZA:
                this.state = new BozzaState();
                break;
            case IN_REVISIONE:
                this.state = new InRevisioneState();
                break;
            case APPROVATO:
                this.state = new ApprovatoState();
                break;
            case RIFIUTATO:
                this.state = new RifiutatoState();
                break;
        }
    }

    /**
     * Metodo di callback JPA.
     * Assicura che dopo aver caricato l'entità dal DB,
     * l'oggetto 'state' transiente venga inizializzato correttamente.
     */
    @PostLoad
    private void onPostLoad() {
        this.updateState();
    }

    // --- Metodi delegati allo Stato ---
    // L'entità delega le azioni al suo oggetto di stato corrente.

    public void sottometti() {
        this.state.sottometti(this);
    }

    public void approva() {
        this.state.approva(this);
    }

    public void rifiuta(String feedback) {
        this.state.rifiuta(this, feedback);
    }

    public void rimandaInBozza() {
        this.state.rimandaInBozza(this);
    }
}