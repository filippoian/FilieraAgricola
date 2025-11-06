package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import it.unicam.cs.ids2425.FilieraAgricola.model.ERole;
import lombok.Data;

@Data
public class AccreditaRequest {
    // Il ruolo (es. PRODUTTORE, GESTORE, ...) che si vuole assegnare
    private ERole ruolo;
}
