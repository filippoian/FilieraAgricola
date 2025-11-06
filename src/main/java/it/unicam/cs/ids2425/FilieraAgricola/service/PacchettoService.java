package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.PacchettoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.PacchettoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacchettoService {

    private final PacchettoRepository pacchettoRepository;
    private final UtenteRepository utenteRepository;
    private final ProdottoRepository prodottoRepository;
    private final ProdottoPacchettoRepository prodottoPacchettoRepository;

    public PacchettoResponse creaPacchetto(PacchettoRequest request) {
        Utente distributore = utenteRepository.findById(request.getDistributoreId())
                .orElseThrow(() -> new RuntimeException("Distributore non trovato"));

        Pacchetto pacchetto = new Pacchetto();
        pacchetto.setNome(request.getNome());
        pacchetto.setDescrizione(request.getDescrizione());
        pacchetto.setDistributore(distributore);
        pacchetto = pacchettoRepository.save(pacchetto);

        for (Long prodottoId : request.getProdottiIds()) {
            Prodotto prodotto = prodottoRepository.findById(prodottoId)
                    .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + prodottoId));

            ProdottoPacchetto pp = new ProdottoPacchetto();
            pp.setPacchetto(pacchetto);
            pp.setProdotto(prodotto);
            pp.setQuantita(1);
            prodottoPacchettoRepository.save(pp);
        }

        return new PacchettoResponse(pacchetto);
    }

    public List<PacchettoResponse> getAllPacchetti() {
        return pacchettoRepository.findAll()
                .stream()
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
    }

    public List<PacchettoResponse> getPacchettiDaApprovare() {
        return pacchettoRepository.findByApprovatoFalse()
                .stream()
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
    }

    public void approvaPacchetto(Long id) {
        Pacchetto pacchetto = pacchettoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pacchetto non trovato"));
        pacchetto.setApprovato(true);
        pacchettoRepository.save(pacchetto);
    }

    public PacchettoResponse aggiornaPacchetto(Long id, PacchettoRequest request) {
        Pacchetto pacchetto = pacchettoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pacchetto non trovato"));

        pacchetto.setNome(request.getNome());
        pacchetto.setDescrizione(request.getDescrizione());

        return new PacchettoResponse(pacchettoRepository.save(pacchetto));
    }

    public void eliminaPacchetto(Long id) {
        if (!pacchettoRepository.existsById(id)) {
            throw new RuntimeException("Pacchetto non trovato");
        }
        pacchettoRepository.deleteById(id);
    }

    public List<PacchettoResponse> getByDistributore(Long distributoreId) {
        return pacchettoRepository.findByDistributoreId(distributoreId)
                .stream()
                .map(PacchettoResponse::new)
                .collect(Collectors.toList());
    }
}
