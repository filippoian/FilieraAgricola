package it.unicam.cs.ids2425.FilieraAgricola.model;

/**
 * Stato BOZZA: Il proprietario sta modificando.
 */
public class BozzaState implements ContentState {

    @Override
    public void sottometti(ContentSubmission context) {
        // Transizione: BOZZA -> IN_REVISIONE
        context.setStatus(StatoContenuto.IN_REVISIONE);
        context.updateState();
    }

    @Override
    public void approva(ContentSubmission context) {
        throw new IllegalStateException("Impossibile approvare un contenuto in stato BOZZA.");
    }

    @Override
    public void rifiuta(ContentSubmission context, String feedback) {
        throw new IllegalStateException("Impossibile rifiutare un contenuto in stato BOZZA.");
    }

    @Override
    public void rimandaInBozza(ContentSubmission context) {
        // Già in bozza, nessuna azione.
    }
}