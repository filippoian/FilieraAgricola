package it.unicam.cs.ids2425.FilieraAgricola.model;

/**
 * Stato IN_REVISIONE: In attesa di approvazione del Curatore.
 */
public class InRevisioneState implements ContentState {

    @Override
    public void sottometti(ContentSubmission context) {
        throw new IllegalStateException("Contenuto già sottomesso e in attesa di revisione.");
    }

    @Override
    public void approva(ContentSubmission context) {
        // Transizione: IN_REVISIONE -> APPROVATO
        context.setStatus(StatoContenuto.APPROVATO);
        context.setFeedbackCuratore("Approvato.");
        context.updateState();

        // Qui (nel CurationService) verrà aggiunta la logica
        // per aggiornare l'entità collegata (es. Prodotto).
    }

    @Override
    public void rifiuta(ContentSubmission context, String feedback) {
        // Transizione: IN_REVISIONE -> RIFIUTATO
        context.setStatus(StatoContenuto.RIFIUTATO);
        context.setFeedbackCuratore(feedback);
        context.updateState();
    }

    @Override
    public void rimandaInBozza(ContentSubmission context) {
        // Transizione: IN_REVISIONE -> BOZZA (Proprietario ritira / Curatore richiede modifiche)
        context.setStatus(StatoContenuto.BOZZA);
        context.setFeedbackCuratore("Rimandato in bozza per modifiche.");
        context.updateState();
    }
}