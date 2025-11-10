package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Azienda;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AziendaResponse {
    private Long id;
    private String nome;
    private String indirizzo;
    private BigDecimal latitudine;
    private BigDecimal longitudine;
    private String ruolo; // Mostra il primo ruolo significativo

    public AziendaResponse(Azienda azienda) {
        this.id = azienda.getId();
        this.nome = azienda.getNome();
        this.indirizzo = azienda.getIndirizzo();
        this.latitudine = azienda.getLatitudine();
        this.longitudine = azienda.getLongitudine();

        // CORREZIONE: Gestisce Set<Role> invece di un singolo ruolo
        if (azienda.getUtente() != null && azienda.getUtente().getRoles() != null && !azienda.getUtente().getRoles().isEmpty()) {
            // Prende il primo ruolo come rappresentativo
            this.ruolo = azienda.getUtente().getRoles().stream()
                    .findFirst()
                    .map(role -> role.getName().name())
                    .orElse("N/A");
        }
    }
}