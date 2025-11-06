package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * DTO che rappresenta la geometria "Point" GeoJSON.
 */
@Data
public class GeoJSONGeometry {
    private final String type = "Point";
    // Coordinate in formato [longitudine, latitudine]
    private List<BigDecimal> coordinates;

    public GeoJSONGeometry(BigDecimal longitudine, BigDecimal latitudine) {
        // Formato GeoJSON standard: [longitudine, latitudine]
        this.coordinates = Arrays.asList(longitudine, latitudine);
    }
}