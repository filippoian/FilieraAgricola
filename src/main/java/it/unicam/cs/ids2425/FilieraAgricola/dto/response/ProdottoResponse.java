package it.unicam.cs.ids2425.FilieraAgricola.dto.response;

import it.unicam.cs.ids2425.FilieraAgricola.model.Prodotto;
import lombok.Data;

@Data
public class ProdottoResponse {
    private Long id;
    private String nome;
    private String descrizione;
    private String categoria;
    private String certificazioni;
    private String metodiColtivazione;

    public ProdottoResponse(Prodotto prodotto) {
        this.id = prodotto.getId();
        this.nome = prodotto.getNome();
        this.descrizione = prodotto.getDescrizione();
        this.categoria = prodotto.getCategoria();
        this.certificazioni = prodotto.getCertificazioni();
        this.metodiColtivazione = prodotto.getMetodiColtivazione();
    }
}