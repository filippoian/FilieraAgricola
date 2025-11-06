package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

/**
 * DTO per la richiesta di approvazione (opzionale, per feedback).
 */
@Data
public class ApproveRequestDTO {
    private String feedback; // Es. "Approvato con complimenti."
}