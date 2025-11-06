package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.TraceabilityStep;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO per visualizzare una singola fase di tracciabilità.
 */
@Data
public class StepResponseDTO {

    private Long id;
    private String azione;
    private String descrizione;
    private LocalDateTime dataStep;

    // Dati denormalizzati per l'utente
    private String nomeUtente;
    private String nomeFilieraPoint;

    public StepResponseDTO(TraceabilityStep step) {
        this.id = step.getId();
        this.azione = step.getAzione();
        this.descrizione = step.getDescrizione();
        this.dataStep = step.getDataStep();

        if (step.getUtente() != null && step.getUtente().getUserProfile() != null) {
            this.nomeUtente = step.getUtente().getUserProfile().getNome() + " " +
                    step.getUtente().getUserProfile().getCognome();
        } else if (step.getUtente() != null) {
            this.nomeUtente = step.getUtente().getEmail(); // Fallback
        }

        if (step.getFilieraPoint() != null) {
            this.nomeFilieraPoint = step.getFilieraPoint().getNomePunto();
        }
    }
}