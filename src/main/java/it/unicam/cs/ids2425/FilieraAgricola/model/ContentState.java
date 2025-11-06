package it.unicam.cs.ids2425.FilieraAgricola.model;

/**
 * Interfaccia per il Pattern State (Flusso di Curation).
 * Definisce le azioni possibili su un ContentSubmission.
 */
public interface ContentState {

    /**
     * Azione per inviare il contenuto al Curatore per la revisione.
     */
    void sottometti(ContentSubmission context);

    /**
     * Azione per approvare il contenuto (solo Curatore).
     */
    void approva(ContentSubmission context);

    /**
     * Azione per rifiutare il contenuto (solo Curatore).
     */
    void rifiuta(ContentSubmission context, String feedback);

    /**
     * Azione per ritirare il contenuto o modificarlo (solo Proprietario o Curatore).
     */
    void rimandaInBozza(ContentSubmission context);
}