package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

// import it.unicam.cs.ids2425.FilieraAgricola.model.RuoloUtente; // Rimosso
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginResponse {
    private String token;
    private Long id;
    private String nome;
    private String email;
    // Modificato da RuoloUtente a List<String>
    private List<String> ruoli;

    // Costruttore aggiornato
    public LoginResponse(String token, Utente utente) {
        this.token = token;
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.email = utente.getEmail();
        // Converte il Set<Role> in una List<String>
        this.ruoli = utente.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
}
