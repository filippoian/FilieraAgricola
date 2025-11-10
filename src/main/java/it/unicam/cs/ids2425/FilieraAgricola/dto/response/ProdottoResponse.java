package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import it.unicam.cs.ids2425.FilieraAgricola.model.StatoContenuto;
import lombok.Data;

@Data
public class ProdottoResponse {
    private Long id;
    private String nome;
    private String descrizione;
    private String categoria;
    private String certificazioni;
    private String metodiColtivazione;
    private Long utenteId;
    private String stato;

    public ProdottoResponse(Prodotto prodotto) {
        this.id = prodotto.getId();
        this.nome = prodotto.getNome();
        this.descrizione = prodotto.getDescrizione();
        this.categoria = prodotto.getCategoria();
        this.certificazioni = prodotto.getCertificazioni();
        this.metodiColtivazione = prodotto.getMetodiColtivazione();
        if (prodotto.getUtente() != null) {
            this.utenteId = prodotto.getUtente().getId();
        }
        if (prodotto.getSubmission() != null) {
            this.stato = prodotto.getSubmission().getStatus().name();
        } else {
            this.stato = StatoContenuto.BOZZA.name(); // Default se la submission è null
        }
    }
}