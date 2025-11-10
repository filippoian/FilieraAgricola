package it.unicam.cs.ids2425.FilieraAgricola.model;

/**
 * Stato RIFIUTATO: Rifiutato dal Curatore.
 */
public class RifiutatoState implements ContentState {

    @Override
    public void sottometti(ContentSubmission context) {
        // L'utente può ri-sottomettere dopo aver modificato
        context.setStatus(StatoContenuto.IN_REVISIONE);
        context.updateState();
    }

    @Override
    public void approva(ContentSubmission context) {
        throw new IllegalStateException("Impossibile approvare un contenuto rifiutato. L'utente deve prima risottometterlo.");
    }

    @Override
    public void rifiuta(ContentSubmission context, String feedback) {
        // Già rifiutato. Il curatore può aggiornare il feedback.
        context.setFeedbackCuratore(feedback);
    }

    @Override
    public void rimandaInBozza(ContentSubmission context) {
        // Transizione: RIFIUTATO -> BOZZA
        context.setStatus(StatoContenuto.BOZZA);
        context.updateState();
    }
}