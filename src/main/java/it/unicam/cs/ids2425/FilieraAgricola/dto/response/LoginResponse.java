package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente;
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String nome;
    private String email;
    private RuoloUtente ruolo;

    public LoginResponse(Utente utente) {
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.email = utente.getEmail();
        this.ruolo = utente.getRuolo();
    }
}
