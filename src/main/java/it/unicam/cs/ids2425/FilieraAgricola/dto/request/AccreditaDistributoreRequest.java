package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccreditaDistributoreRequest {
    // Dati per ActorProfile
    private String ragioneSociale;
    private String partitaIva;
    private String infoLogistica;

    // Dati per FilieraPoint
    private String nomePunto;
    private String descrizionePunto;
    private String indirizzoPunto;
    private BigDecimal latitudine;
    private BigDecimal longitudine;
}