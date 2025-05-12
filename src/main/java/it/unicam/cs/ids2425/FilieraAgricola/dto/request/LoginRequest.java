package it.unicam.cs.ids2425.FilieraAgricola.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
