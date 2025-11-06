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
        // 3a. Controlla che il prodotto sia stato APPROVATO
        if (prodotto.getStatus() != StatoContenuto.APPROVATO) {
            throw new IllegalStateException("Impossibile vendere un prodotto che non è 'APPROVATO'. Stato attuale: " + prodotto.getStatus());
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
        // Usa il metodo del repository per filtrare per stato del prodotto
        List<MarketplaceItem> items = marketplaceItemRepository.findByProdottoStatus(StatoContenuto.APPROVATO);

        // Converte la lista di entità in una lista di DTO
        return items.stream()
                .map(MarketplaceItemResponse::new)
                .collect(Collectors.toList());
    }
}

