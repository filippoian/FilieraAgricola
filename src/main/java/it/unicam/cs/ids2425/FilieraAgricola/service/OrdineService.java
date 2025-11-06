package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.CarrelloCheckoutDTO; // DTO Nuovo (vedi sotto)
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.OrdineResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.*;
import it.unicam.cs.ids2425.FilieraAgricola.service.pricing.PricingStrategyFactory; // Import Strategy
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Import
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final UtenteRepository utenteRepository;

    // --- Repository e Factory Aggiunti ---
    private final MarketplaceItemRepository marketplaceItemRepository;
    private final PacchettoRepository pacchettoRepository;
    private final PricingStrategyFactory pricingStrategyFactory;

    /**
     * Crea un ordine basandosi su un DTO di checkout (il carrello).
     * Implementa il Pattern Strategy per il calcolo del prezzo.
     */
    @Transactional
    public OrdineResponse creaOrdineDaCheckout(Long utenteId, CarrelloCheckoutDTO request) {
        Utente acquirente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato con id: " + utenteId));

        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setAcquirente(acquirente);

        BigDecimal totaleOrdine = BigDecimal.ZERO;

        // Itera sugli ARTICOLI SINGOLI nel carrello
        for (CarrelloCheckoutDTO.Item item : request.getItems()) {
            MarketplaceItem marketplaceItem = marketplaceItemRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Articolo non trovato: " + item.getId()));

            // "Congela" il prezzo al momento dell'acquisto
            BigDecimal prezzoDiAcquisto = BigDecimal.valueOf(marketplaceItem.getPrezzoUnitario());

            OrderLine linea = new OrderLine(
                    nuovoOrdine,
                    marketplaceItem, // Articolo
                    null,            // Pacchetto (null)
                    item.getQuantita(),
                    prezzoDiAcquisto
            );

            // Delega il calcolo al Pattern Strategy
            BigDecimal subtotale = pricingStrategyFactory.getStrategy(linea).calculatePrice(linea);
            totaleOrdine = totaleOrdine.add(subtotale);

            nuovoOrdine.getLinee().add(linea);
        }

        // Itera sui PACCHETTI nel carrello
        for (CarrelloCheckoutDTO.Item item : request.getPacchetti()) {
            Pacchetto pacchetto = pacchettoRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Pacchetto non trovato: " + item.getId()));

            // "Congela" il prezzo al momento dell'acquisto
            BigDecimal prezzoDiAcquisto = pacchetto.getPrezzo_totale();

            OrderLine linea = new OrderLine(
                    nuovoOrdine,
                    null,          // Articolo (null)
                    pacchetto,     // Pacchetto
                    item.getQuantita(),
                    prezzoDiAcquisto
            );

            // Delega il calcolo al Pattern Strategy
            BigDecimal subtotale = pricingStrategyFactory.getStrategy(linea).calculatePrice(linea);
            totaleOrdine = totaleOrdine.add(subtotale);

            nuovoOrdine.getLinee().add(linea);
        }

        // Salva il totale calcolato
        nuovoOrdine.setTotale(totaleOrdine);

        // Salva l'ordine (e le OrderLine grazie a CascadeType.ALL)
        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);

        return new OrdineResponse(ordineSalvato);
    }


    public List<OrdineResponse> getOrdiniByUtente(Long id) {
        return ordineRepository.findByAcquirenteId(id)
                .stream()
                .map(OrdineResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void aggiornaStato(Long idOrdine, String nuovoStato) {
        Ordine ordine = ordineRepository.findById(idOrdine)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));
        try {
            StatoOrdine stato = StatoOrdine.valueOf(nuovoStato.toUpperCase());
            ordine.setStato(stato);
            ordineRepository.save(ordine);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Stato ordine non valido: " + nuovoStato);
        }
    }
}