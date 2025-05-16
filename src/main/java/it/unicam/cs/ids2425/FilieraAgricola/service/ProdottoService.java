package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ProdottoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;

    public ProdottoResponse creaProdotto(ProdottoRequest request) {
        Utente utente = utenteRepository.findById(request.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con id: " + request.getUtenteId()));

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setCategoria(request.getCategoria());
        prodotto.setCertificazioni(request.getCertificazioni());
        prodotto.setMetodiColtivazione(request.getMetodiColtivazione());
        prodotto.setUtente(utente);

        return new ProdottoResponse(prodottoRepository.save(prodotto));
    }

    public List<ProdottoResponse> getAllProdotti() {
        return prodottoRepository.findAll()
                .stream()
                .map(ProdottoResponse::new)
                .collect(Collectors.toList());
    }

    public ProdottoResponse getProdottoById(Long id) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));
        return new ProdottoResponse(prodotto);
    }
}
