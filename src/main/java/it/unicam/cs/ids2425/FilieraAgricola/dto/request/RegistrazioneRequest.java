package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

// import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente; // Rimosso
import lombok.Data;

@Data
public class RegistrazioneRequest {
    private String nome;
    private String email;
    private String password;
    // Il campo RuoloUtente è stato rimosso per seguire le specifiche.
    // Il ruolo base sarà assegnato automaticamente dal servizio.
    // private RuoloUtente ruolo; // Rimosso
}
c1\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\