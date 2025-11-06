package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

@Data
public class RegistrazioneRequest {
    // Campi anagrafici aggiunti per UserProfile
    private String nome;
    private String cognome;
    private String telefono;

    // Campi di autenticazione (UserAccount)
    private String email;
    private String password;
}