package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.CarrelloCheckoutDTO;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.OrdineResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.*;
import it.unicam.cs.ids2425.FilieraAgricola.service.pricing.PricingStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final UtenteRepository utenteRepository;
    private final MarketplaceItemRepository marketplaceItemRepository;
    private final PacchettoRepository pacchettoRepository;
    private final PricingStrategyFactory pricingStrategyFactory;

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

            BigDecimal prezzoDiAcquisto = BigDecimal.valueOf(marketplaceItem.getPrezzoUnitario());

            // Aggiunto 'null' come primo argomento per il campo 'id'
            OrderLine linea = new OrderLine(
                    null, // <--- ID
                    nuovoOrdine,
                    marketplaceItem,
                    null,
                    item.getQuantita(),
                    prezzoDiAcquisto
            );

            BigDecimal subtotale = pricingStrategyFactory.getStrategy(linea).calculatePrice(linea);
            totaleOrdine = totaleOrdine.add(subtotale);

            nuovoOrdine.getLinee().add(linea);
        }

        // Itera sui PACCHETTI nel carrello
        for (CarrelloCheckoutDTO.Item item : request.getPacchetti()) {
            Pacchetto pacchetto = pacchettoRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Pacchetto non trovato: " + item.getId()));

            BigDecimal prezzoDiAcquisto = pacchetto.getPrezzo_totale();

            // Aggiunto 'null' come primo argomento per il campo 'id'
            OrderLine linea = new OrderLine(
                    null, // <--- ID
                    nuovoOrdine,
                    null,
                    pacchetto,
                    item.getQuantita(),
                    prezzoDiAcquisto
            );

            BigDecimal subtotale = pricingStrategyFactory.getStrategy(linea).calculatePrice(linea);
            totaleOrdine = totaleOrdine.add(subtotale);

            nuovoOrdine.getLinee().add(linea);
        }

        nuovoOrdine.setTotale(totaleOrdine);
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