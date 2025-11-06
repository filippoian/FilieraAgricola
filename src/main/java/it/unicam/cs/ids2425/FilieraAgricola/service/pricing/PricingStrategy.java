package it.unicam.cs.ids2425.FilieraAgricola.service.pricing;

import it.unicam.cs.ids2425.FilieraAgricola.model.OrderLine;

import java.math.BigDecimal;

/**
 * Interfaccia per il Pattern Strategy.
 * Definisce il contratto per calcolare il prezzo di una linea d'ordine.
 */
public interface PricingStrategy {
    /**
     * Calcola il subtotale per una specifica linea d'ordine.
     * @param line La linea d'ordine da calcolare.
     * @return Il subtotale (prezzo * quantità).
     */
    BigDecimal calculatePrice(OrderLine line);
}

