package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import lombok.Data;
import java.util.List;

/**
 * DTO che rappresenta l'oggetto radice GeoJSON (FeatureCollection).
 */
@Data
public class GeoJSONFeatureCollection {
    private final String type = "FeatureCollection";
    private List<GeoJSONFeature> features;

    public GeoJSONFeatureCollection(List<GeoJSONFeature> features) {
        this.features = features;
    }
}