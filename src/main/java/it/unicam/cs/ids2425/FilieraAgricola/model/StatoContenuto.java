package it.unicam.cs.ids2425.FilieraAgricola.model;

/**
 * Enum che definisce il ciclo di vita di un contenuto (Prodotto, Certificazione, ecc.)
 * come richiesto dal Pattern State per la Curation.
 */
public enum StatoContenuto {
    BOZZA,        // Il produttore sta modificando, non visibile
    IN_REVISIONE, // Sottomesso al Curatore per approvazione
    APPROVATO,    // Approvato, visibile e vendibile sul marketplace
    RIFIUTATO     // Rifiutato dal Curatore
}