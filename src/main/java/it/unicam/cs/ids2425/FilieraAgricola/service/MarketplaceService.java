package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.MarketplaceItemRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.MarketplaceItemResponse;
import it.unicam.cs.ids2425.FilieraAgricola.model.*;
import it.unicam.cs.ids2425.FilieraAgricola.repository.MarketplaceItemRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.ProdottoRepository;
import it.unicam.cs.ids2425.FilieraAgricola.repository.UtenteRepository;
import it.unicam.cs.ids2425.FilieraAgricola.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceService {

    @Autowired
    private MarketplaceItemRepository marketplaceItemRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional
    public MarketplaceItemResponse creaItem(MarketplaceItemRequest request) {
        // 1. Recupera l'utente autenticato
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utente venditore = utenteRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utente venditore non trovato"));

        // 2. Trova il prodotto da vendere
        Prodotto prodotto = prodottoRepository.findById(request.getProdottoId())
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + request.getProdottoId()));

        // 3. Controlli di sicurezza e logica
        // 3a. Controlla che il prodotto sia stato APPROVATO tramite Curation
        if (prodotto.getSubmission() == null ||
                prodotto.getSubmission().getStatus() != StatoContenuto.APPROVATO) {

            throw new IllegalStateException("Impossibile vendere un prodotto che non è 'APPROVATO'. Stato attuale: " +
                    (prodotto.getSubmission() != null ? prodotto.getSubmission().getStatus() : "NON SOTTOMESSO"));
        }

        // 3b. Controlla che il venditore sia il proprietario del prodotto
        if (!prodotto.getUtente().getId().equals(venditore.getId())) {
            throw new IllegalStateException("Non hai i permessi per vendere un prodotto di un altro utente.");
        }

        // 4. Crea e salva il nuovo MarketplaceItem
        MarketplaceItem item = new MarketplaceItem();
        item.setProdotto(prodotto);
        item.setVenditore(venditore);
        item.setPrezzoUnitario(request.getPrezzoUnitario());
        item.setUnitaDiMisura(request.getUnitaDiMisura());
        item.setStockDisponibile(request.getStockDisponibile());

        MarketplaceItem savedItem = marketplaceItemRepository.save(item);

        return new MarketplaceItemResponse(savedItem);
    }

    /**
     * Recupera il catalogo pubblico di tutti gli articoli in vendita.
     * Mostra solo gli articoli il cui prodotto associato è 'APPROVATO'.
     */
    @Transactional(readOnly = true)
    public List<MarketplaceItemResponse> getCatalogo() {

        // N.B. Il vecchio metodo 'findByProdottoStatus' non è più valido.
        // Filtriamo in memoria per controllare lo stato della submission.
        List<MarketplaceItem> items = marketplaceItemRepository.findAll();

        return items.stream()
                .filter(item -> item.getProdotto().getSubmission() != null &&
                        item.getProdotto().getSubmission().getStatus() == StatoContenuto.APPROVATO)
                .map(MarketplaceItemResponse::new)
                .collect(Collectors.toList());

        /* * Per ottimizzare, potremmo aggiungere un metodo al MarketplaceItemRepository:
         * @Query("SELECT i FROM MarketplaceItem i WHERE i.prodotto.submission.status = :status")
         * List<MarketplaceItem> findByProdottoSubmissionStatus(@Param("status") StatoContenuto status);
         * e chiamare quello.
         */
    }
}