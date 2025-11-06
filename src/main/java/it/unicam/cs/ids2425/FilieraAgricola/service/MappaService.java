package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.response.GeoJSONFeature;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.GeoJSONFeatureCollection;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.GeoJSONGeometry;
import it.unicam.cs.ids2425.FilieraAgricola.model.FilieraPoint;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import it.unicam.cs.ids2425.FilieraAgricola.repository.FilieraPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MappaService {

    private final FilieraPointRepository filieraPointRepository;

    /**
     * Costruisce la FeatureCollection GeoJSON di tutti i FilieraPoint approvati.
     */
    @Transactional(readOnly = true)
    public GeoJSONFeatureCollection getPuntiMappa() {

        List<FilieraPoint> puntiApprovati = filieraPointRepository.findAll().stream()
                .filter(fp -> fp.getSubmission() != null &&
                        fp.getSubmission().getStatus() == StatoContenuto.APPROVATO &&
                        fp.getLatitudine() != null &&
                        fp.getLongitudine() != null)
                .collect(Collectors.toList());

        List<GeoJSONFeature> features = puntiApprovati.stream()
                .map(this::convertiAPuntoGeoJSON)
                .collect(Collectors.toList());

        return new GeoJSONFeatureCollection(features);
    }

    /**
     * Converte un FilieraPoint in una GeoJSONFeature.
     */
    private GeoJSONFeature convertiAPuntoGeoJSON(FilieraPoint punto) {
        // 1. Crea la Geometria
        GeoJSONGeometry geometry = new GeoJSONGeometry(punto.getLongitudine(), punto.getLatitudine());

        // 2. Crea le Proprietà (i metadati)
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", punto.getId());
        properties.put("nome", punto.getNomePunto());
        properties.put("tipo", punto.getTipo().name());
        properties.put("descrizione", punto.getDescrizione());
        properties.put("indirizzo", punto.getIndirizzo());

        // 3. Ritorna la Feature
        return new GeoJSONFeature(geometry, properties);
    }
}