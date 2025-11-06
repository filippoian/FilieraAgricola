package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO per la richiesta di aggiunta di una nuova fase (TraceabilityStep) a un lotto.
 */
@Data
public class StepCreateDTO {

    // L'ID del lotto (ProductBatch) sarà nell'URL.
    // L'ID utente (chi registra) sarà preso dal SecurityContext.

    /**
     * L'azione eseguita (es. RACCOLTA, TRASFORMAZIONE, STOCCAGGIO).
     */
    private String azione;

    private String descrizione;

    /**
     * L'ID del luogo (FilieraPoint) dove è avvenuta la fase.
     */
    private Long filieraPointId;

    /**
     * Data e ora della fase (opzionale, se non fornita usa LocalDateTime.now()).
     */
    private LocalDateTime dataStep;
}