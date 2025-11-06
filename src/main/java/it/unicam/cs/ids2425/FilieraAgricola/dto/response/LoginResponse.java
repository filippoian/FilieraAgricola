// ... (import)
import it.unicam.cs.ids2425.FilieraAgricola.model.Utente;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginResponse {
    private String token;
    private Long id;
    private String nome; // Campo 'nome' (da UserProfile)
    private String cognome; // Campo 'cognome' (da UserProfile)
    private String email;
    private List<String> ruoli;

    public LoginResponse(String token, Utente utente) {
        this.token = token;
        this.id = utente.getId();
        this.email = utente.getEmail();

        // Estrae i dati anagrafici dal profilo collegato 
        if (utente.getUserProfile() != null) {
            this.nome = utente.getUserProfile().getNome();
            this.cognome = utente.getUserProfile().getCognome();
        }

        this.ruoli = utente.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
}