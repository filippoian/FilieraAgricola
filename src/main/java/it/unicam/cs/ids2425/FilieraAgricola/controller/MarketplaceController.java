package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.MarketplaceItemRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.MarketplaceItemResponse;
import it.unicam.cs.ids2425.FilieraAgricola.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    /**
     * Endpoint per mettere in vendita un nuovo articolo.
     * Accessibile solo ai ruoli professionali di venditori.
     */
    @PostMapping("/items")
    @PreAuthorize("hasAnyAuthority('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE')")
    public ResponseEntity<MarketplaceItemResponse> creaMarketplaceItem(@RequestBody MarketplaceItemRequest request) {
        MarketplaceItemResponse response = marketplaceService.creaItem(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint pubblico per visualizzare tutti gli articoli in vendita (catalogo).
     * Mostra solo gli articoli 'APPROVATI'.
     */
    @GetMapping("/catalogo")
    public ResponseEntity<List<MarketplaceItemResponse>> getCatalogoPubblico() {
        return ResponseEntity.ok(marketplaceService.getCatalogo());
    }
}

