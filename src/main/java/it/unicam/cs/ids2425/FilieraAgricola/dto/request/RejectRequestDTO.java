package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

/**
 * DTO per la richiesta di rifiuto (richiede feedback).
 */
@Data
public class RejectRequestDTO {
    private String feedback; // Es. "Foto non conformi."
}