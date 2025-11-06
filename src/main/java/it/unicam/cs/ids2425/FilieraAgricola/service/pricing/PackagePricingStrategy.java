package it.unicam.cs.ids2425.FilieraAgricola.service.pricing;

import it.unicam.cs.ids2425.FilieraAgricola.model.OrderLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Strategia di prezzo per un Pacchetto.
 */
@Service("packagePricingStrategy") // Nome del bean per la Factory
public class PackagePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(OrderLine line) {
        if (line.getPacchetto() == null) {
            throw new IllegalArgumentException("Strategy errata: OrderLine non è un pacchetto.");
        }
        // Anche qui, la logica è già "congelata" nell'entità OrderLine.
        // Il prezzo è forfettario per il pacchetto.
        return line.getSubtotale();
    }
}

