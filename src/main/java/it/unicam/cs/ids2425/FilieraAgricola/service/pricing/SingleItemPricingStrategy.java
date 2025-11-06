package it.unicam.cs.ids2425.FilieraAgricola.service.pricing;

import it.unicam.cs.ids2425.FilieraAgricola.model.OrderLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Strategia di prezzo per un MarketplaceItem singolo.
 */
@Service("singleItemPricingStrategy") // Nome del bean per la Factory
public class SingleItemPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(OrderLine line) {
        if (line.getItem() == null) {
            throw new IllegalArgumentException("Strategy errata: OrderLine non è un item singolo.");
        }
        // La logica è già "congelata" nell'entità OrderLine
        // per garantire che il prezzo non cambi dopo l'acquisto.
        return line.getSubtotale();
    }
}

