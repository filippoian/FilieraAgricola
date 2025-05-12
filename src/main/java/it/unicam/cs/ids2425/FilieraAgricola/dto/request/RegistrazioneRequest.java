package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente;
import lombok.Data;

@Data
public class RegistrazioneRequest {
    private String nome;
    private String email;
    private String password;
    private RuoloUtente ruolo;
}
