package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.response.GeoJSONFeatureCollection;
import it.unicam.cs.ids2425.FilieraAgricola.service.MappaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mappa")
@RequiredArgsConstructor
public class MappaController {

    private final MappaService mappaService;

    /**
     * Endpoint pubblico che restituisce i punti della filiera in formato GeoJSON
     * per la visualizzazione sulla mappa (come da specifica).
     */
    @GetMapping("/punti")
    public ResponseEntity<GeoJSONFeatureCollection> getPuntiMappa() {
        return ResponseEntity.ok(mappaService.getPuntiMappa());
    }
}