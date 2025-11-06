package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.OrdineRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.OrdineResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.CarrelloRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.OrdineRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final UtenteRepository utenteRepository;
    private final CarrelloRepository carrelloRepository;

    public OrdineResponse creaOrdine(OrdineRequest request) {

        Utente acquirente = utenteRepository.findById(request.getAcquirenteId()).orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        return new OrdineResponse(ordineRepository.save(ordine));

    }

    @Transactional
    public OrdineResponse creaOrdineDalCarrello(Long utenteId) {
        // Recupera l'utente e il suo carrello
        Utente acquirente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato con id: " + utenteId));

        Carrello carrello = carrelloRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente con id: " + utenteId));

        if (carrello.getArticoli().isEmpty()) {
            throw new IllegalStateException("Impossibile creare un ordine da un carrello vuoto.");
        }

        // Crea un nuovo ordine
        Ordine nuovoOrdine = new Ordine(acquirente);

        // Copia gli articoli dal carrello al nuovo ordine
        List<ArticoloOrdine> articoliOrdine = new ArrayList<>();
        for (ArticoloCarrello articoloCarrello : carrello.getArticoli()) {
            ArticoloOrdine articoloOrdine = new ArticoloOrdine(
                    nuovoOrdine,
                    articoloCarrello.getProdotto(),
                    articoloCarrello.getQuantita()
            );
            articoliOrdine.add(articoloOrdine);
        }
        nuovoOrdine.setArticoli(articoliOrdine);

        // Salva il nuovo ordine (e i suoi articoli, grazie al cascade)
        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);

        // Svuota il carrello
        carrello.getArticoli().clear();
        carrelloRepository.save(carrello);

        // La OrdineResponse andrebbe arricchita per includere gli articoli,
        // ma per ora manteniamo la compatibilità.
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