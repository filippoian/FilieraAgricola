package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.ProdottoRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.ProdottoResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto; // Importa il nuovo Enum
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

        // TODO: Aggiungere controllo sicurezza (es. solo PRODUTTORE o TRASFORMATORE)
        // if (!utente.getRoles().contains(ROLE_PRODUTTORE)...)

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setCategoria(request.getCategoria());
        prodotto.setCertificazioni(request.getCertificazioni());
        prodotto.setMetodiColtivazione(request.getMetodiColtivazione());
        prodotto.setUtente(utente);

        // Impostiamo lo stato di default
        prodotto.setStatus(StatoContenuto.BOZZA);

        return new ProdottoResponse(prodottoRepository.save(prodotto));
    }

    public List<ProdottoResponse> getAllProdotti() {
        return prodottoRepository.findAll()
                .stream()
                // N.B. In futuro, questo dovrà filtrare solo i prodotti APPROVATI
                // per gli utenti normali, ma per ora li mostra tutti.
                .map(ProdottoResponse::new)
                .collect(Collectors.toList());
    }

    public ProdottoResponse getProdottoById(Long id) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + id));

        // TODO: Aggiungere controllo visibilità
        // (es. se non è APPROVATO, solo il proprietario o il curatore possono vederlo)

        return new ProdottoResponse(prodotto);
    }
}
