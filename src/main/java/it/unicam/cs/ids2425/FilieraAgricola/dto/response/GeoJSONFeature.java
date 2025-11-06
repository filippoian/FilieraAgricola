package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import lombok.Data;
import java.util.Map;

/**
 * DTO che rappresenta una singola "Feature" GeoJSON (es. un punto sulla mappa).
 */
@Data
public class GeoJSONFeature {
    private final String type = "Feature";
    private GeoJSONGeometry geometry;
    private Map<String, Object> properties; // Metadati (nome, tipo, ID)

    public GeoJSONFeature(GeoJSONGeometry geometry, Map<String, Object> properties) {
        this.geometry = geometry;
        this.properties = properties;
    }
}