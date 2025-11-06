package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.ProductBatch;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Comparator; // Import
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TraceabilityGraphDTO {

    private Long batchId;
    private String codiceLotto;
    private String nomeProdotto;
    private LocalDate dataProduzione;

    /**
     * Le fasi (diario di bordo) eseguite su QUESTO lotto.
     */
    private Set<StepResponseDTO> steps = new HashSet<>();

    /**
     * I lotti "genitori" (ingredienti) usati per creare questo lotto.
     */
    private Set<TraceabilityGraphDTO> inputBatches = new HashSet<>();

    public TraceabilityGraphDTO(ProductBatch batch) {
        this.batchId = batch.getId();
        this.codiceLotto = batch.getCodiceLottoUnivoco();
        this.nomeProdotto = batch.getProdotto().getNome();
        this.dataProduzione = batch.getDataProduzione();
    }

    public static TraceabilityGraphDTO buildRecursive(ProductBatch batch) {
        TraceabilityGraphDTO node = new TraceabilityGraphDTO(batch);

        // 1. Popola le FASI (Steps) di questo lotto
        Set<StepResponseDTO> steps = batch.getSteps().stream()
                .map(StepResponseDTO::new)
                .sorted(Comparator.comparing(StepResponseDTO::getDataStep)) // Ordina per data
                .collect(Collectors.toSet());
        node.setSteps(steps);

        // 2. Popola ricorsivamente i lotti di INPUT (Grafo)
        Set<TraceabilityGraphDTO> inputs = batch.getLottiInput().stream()
                .map(link -> buildRecursive(link.getInputBatch()))
                .collect(Collectors.toSet());

        node.setInputBatches(inputs);
        return node;
    }
}