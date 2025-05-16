package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.TracciabilitaRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.TracciabilitaResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.model.Tracciabilita;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ProdottoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.TracciabilitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TracciabilitaService {

    private final TracciabilitaRepository tracciabilitaRepository;
    private final ProdottoRepository prodottoRepository;

    public TracciabilitaResponse aggiungiFase(TracciabilitaRequest request) {
        Prodotto prodotto = prodottoRepository.findById(request.getProdottoId())
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + request.getProdottoId()));

        Tracciabilita t = new Tracciabilita();
        t.setProdotto(prodotto);
        t.setFase(request.getFase());
        t.setDescrizione(request.getDescrizione());
        t.setData(request.getData());

        return new TracciabilitaResponse(tracciabilitaRepository.save(t));
    }

    public List<TracciabilitaResponse> getFasiByProdotto(Long prodottoId) {
        return tracciabilitaRepository.findByProdottoId(prodottoId)
                .stream()
                .map(TracciabilitaResponse::new)
                .collect(Collectors.toList());
    }
}