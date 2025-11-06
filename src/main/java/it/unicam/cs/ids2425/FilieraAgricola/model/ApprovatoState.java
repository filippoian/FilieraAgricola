package it.unicam.cs.ids2425.FilieraAgricola.model;

/**
 * Stato APPROVATO: Visibile e pubblicato.
 */
public class ApprovatoState implements ContentState {

    @Override
    public void sottometti(ContentSubmission context) {
        throw new IllegalStateException("Contenuto già approvato. Per modifiche, rimandare in bozza.");
    }

    @Override
    public void approva(ContentSubmission context) {
        // Già approvato, nessuna azione.
    }

    @Override
    public void rifiuta(ContentSubmission context, String feedback) {
        throw new IllegalStateException("Contenuto già approvato. Per rimuoverlo, rimandare in bozza.");
    }

    @Override
    public void rimandaInBozza(ContentSubmission context) {
        // Transizione: APPROVATO -> BOZZA (es. per modifiche post-pubblicazione)
        context.setStatus(StatoContenuto.BOZZA);
        context.updateState();
    }
}