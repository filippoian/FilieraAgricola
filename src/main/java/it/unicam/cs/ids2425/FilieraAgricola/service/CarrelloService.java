package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.AggiungiAlCarrelloRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ArticoloCarrelloResponse;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.CarrelloResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.ArticoloCarrello;
import it.unicam.cs.ids2425.FilieraAgricola.model.Carrello;
import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.repository.CarrelloRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarrelloService {

    private final CarrelloRepository carrelloRepository;
    private final ProdottoRepository prodottoRepository;

    @Transactional
    public CarrelloResponse aggiungiProdotto(Long utenteId, AggiungiAlCarrelloRequest request) {
        Carrello carrello = carrelloRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente con id: " + utenteId));

        Prodotto prodotto = prodottoRepository.findById(request.getProdottoId())
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + request.getProdottoId()));

        Optional<ArticoloCarrello> articoloEsistente = carrello.getArticoli().stream()
                .filter(art -> art.getProdotto().getId().equals(prodotto.getId()))
                .findFirst();

        if (articoloEsistente.isPresent()) {
            ArticoloCarrello articolo = articoloEsistente.get();
            articolo.setQuantita(articolo.getQuantita() + request.getQuantita());
        } else {
            ArticoloCarrello nuovoArticolo = new ArticoloCarrello(carrello, prodotto, request.getQuantita());
            carrello.getArticoli().add(nuovoArticolo);
        }

        carrelloRepository.save(carrello);
        return toResponse(carrello);
    }

    @Transactional
    public CarrelloResponse rimuoviProdotto(Long utenteId, Long prodottoId) {
        Carrello carrello = carrelloRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente con id: " + utenteId));

        carrello.getArticoli().removeIf(articolo -> articolo.getProdotto().getId().equals(prodottoId));

        carrelloRepository.save(carrello);
        return toResponse(carrello);
    }

    public CarrelloResponse getCarrello(Long utenteId) {
        Carrello carrello = carrelloRepository.findByUtenteId(utenteId)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente con id: " + utenteId));
        return toResponse(carrello);
    }

    private CarrelloResponse toResponse(Carrello carrello) {
        List<ArticoloCarrelloResponse> articoliResponse = carrello.getArticoli().stream()
                .map(art -> new ArticoloCarrelloResponse(
                        art.getProdotto().getId(),
                        art.getProdotto().getNome(),
                        art.getQuantita()))
                .collect(Collectors.toList());
        return new CarrelloResponse(carrello.getId(), carrello.getUtente().getId(), articoliResponse);
    }
}