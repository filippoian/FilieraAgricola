package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO per la richiesta di "Accredito" come Produttore.
 * Contiene i dati per ActorProfile_Produttore e per il FilieraPoint associato.
 */
@Data
public class AccreditaProduttoreRequest {

    // Dati per ActorProfile_Produttore
    private String ragioneSociale;
    private String partitaIva;
    private String descrizioneAzienda;

    // Dati per il FilieraPoint (l'azienda agricola)
    private String nomePunto;
    private String descrizionePunto;
    private String indirizzoPunto;
    private BigDecimal latitudine;
    private BigDecimal longitudine;
}