package it.unicam.cs.ids2425.FilieraAgricola.service;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LottoCreateDTO;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.StepCreateDTO; // Import
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.StepResponseDTO; // Import
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.TraceabilityGraphDTO;
import it.unicam.cs.ids2425.FilieraAgricola.model.*; // Import
import it.unicam.cs.ids2425.FilieraAgricola.repository.*; // Import
import it.unicam.cs.ids2425.FilieraAgricola.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder; // Import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Import
import java.util.Comparator; // Import
import java.util.List; // Import
import java.util.stream.Collectors; // Import

@Service
@RequiredArgsConstructor
public class TracciabilitaService {

    private final ProductBatchRepository batchRepository;
    private final ProdottoRepository prodottoRepository;
    private final BatchInputLinkRepository batchInputLinkRepository;

    // --- Repository Aggiunti ---
    private final TraceabilityStepRepository stepRepository;
    private final UtenteRepository utenteRepository;
    private final FilieraPointRepository filieraPointRepository;


    @Transactional
    public ProductBatch creaLotto(LottoCreateDTO request) {
        Prodotto prodotto = prodottoRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + request.getProductId()));

        ProductBatch newBatch = new ProductBatch();
        newBatch.setProdotto(prodotto);
        newBatch.setQuantitaIniziale(request.getQuantitaIniziale());
        newBatch.setCodiceLottoUnivoco(request.getCodiceLottoUnivoco());
        newBatch.setDataProduzione(request.getDataProduzione());

        ProductBatch savedBatch = batchRepository.save(newBatch);

        if (request.getInputBatchIds() != null && !request.getInputBatchIds().isEmpty()) {
            for (Long inputId : request.getInputBatchIds()) {
                ProductBatch inputBatch = batchRepository.findById(inputId)
                        .orElseThrow(() -> new RuntimeException("Lotto di input non trovato con id: " + inputId));

                BatchInputLink link = new BatchInputLink();
                link.setOutputBatch(savedBatch);
                link.setInputBatch(inputBatch);

                batchInputLinkRepository.save(link);
            }
        }

        return batchRepository.findById(savedBatch.getId()).get();
    }

    @Transactional(readOnly = true)
    public TraceabilityGraphDTO getStoriaLotto(Long batchId) {
        ProductBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Lotto non trovato con id: " + batchId));

        return TraceabilityGraphDTO.buildRecursive(batch);
    }

    /**
     * Aggiunge una nuova fase (TraceabilityStep) a un lotto esistente.
     */
    @Transactional
    public StepResponseDTO aggiungiFase(Long batchId, StepCreateDTO request) {
        // 1. Recupera l'utente autenticato
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utente utente = utenteRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // 2. Recupera le entità collegate
        ProductBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Lotto non trovato con id: " + batchId));

        FilieraPoint point = filieraPointRepository.findById(request.getFilieraPointId())
                .orElseThrow(() -> new RuntimeException("FilieraPoint non trovato con id: " + request.getFilieraPointId()));

        // TODO: Aggiungere controllo permessi (l'utente è il proprietario del FilieraPoint?)

        // 3. Crea e salva la nuova fase
        TraceabilityStep step = new TraceabilityStep();
        step.setBatch(batch);
        step.setUtente(utente);
        step.setFilieraPoint(point);
        step.setAzione(request.getAzione());
        step.setDescrizione(request.getDescrizione());

        // Usa la data fornita o quella attuale
        step.setDataStep(request.getDataStep() != null ? request.getDataStep() : LocalDateTime.now());

        TraceabilityStep savedStep = stepRepository.save(step);
        return new StepResponseDTO(savedStep);
    }

    /**
     * Recupera tutte le fasi (ordinate) per un singolo lotto.
     */
    @Transactional(readOnly = true)
    public List<StepResponseDTO> getFasiLotto(Long batchId) {
        ProductBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Lotto non trovato con id: " + batchId));

        return batch.getSteps().stream()
                .map(StepResponseDTO::new)
                .sorted(Comparator.comparing(StepResponseDTO::getDataStep))
                .collect(Collectors.toList());
    }
}