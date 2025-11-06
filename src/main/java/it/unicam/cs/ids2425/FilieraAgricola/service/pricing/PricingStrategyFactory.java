package it.unicam.cs.ids2425.FilieraAgricola.service.pricing;

import it.unicam.cs.ids2425.FilieraAgricola.model.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PricingStrategyFactory {

    private final PricingStrategy singleItemStrategy;
    private final PricingStrategy packageStrategy;

    @Autowired
    public PricingStrategyFactory(
            // Inietta la strategia specifica usando il @Qualifier
            @Qualifier("singleItemPricingStrategy") PricingStrategy singleItemStrategy,
            @Qualifier("packagePricingStrategy") PricingStrategy packageStrategy) {
        this.singleItemStrategy = singleItemStrategy;
        this.packageStrategy = packageStrategy;
    }

    /**
     * Seleziona la strategia di prezzo appropriata in base
     * al contenuto della OrderLine.
     *
     * @param line La linea d'ordine
     * @return La strategia corretta
     */
    public PricingStrategy getStrategy(OrderLine line) {
        // Se la linea ha un MarketplaceItem, usa la strategia per item singoli
        if (line.getItem() != null) {
            return singleItemStrategy;
        }
        // Se la linea ha un Pacchetto, usa la strategia per pacchetti
        if (line.getPacchetto() != null) {
            return packageStrategy;
        }
        // Se non ha nessuno dei due, è un errore
        throw new IllegalArgumentException("OrderLine non valida: nessun item o pacchetto associato.");
    }
}

