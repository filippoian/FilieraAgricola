package it.unicam.cs.ids2425.FilieraAgricola.controller;

import it.unicam.cs.ids2425.FilieraAgricola.dto.request.LoginRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.request.RegistrazioneRequest;
import it.unicam.cs.ids2425.FilieraAgricola.dto.response.LoginResponse;
// import it.unicam.cs.ids2425.FilieraAgricola.model.Utente; // Rimosso
import it.unicam.cs.ids2425.FilieraAgricola.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    // Modificato il tipo di ritorno da Utente a LoginResponse
    public ResponseEntity<LoginResponse> registra(@RequestBody RegistrazioneRequest request) {
        try {
            return ResponseEntity.ok(authService.registraUtente(request));
        } catch (Exception e) {
            // Gestiremo le eccezioni specifiche (es. EmailAlreadyExist)
            // in un @ControllerAdvice più avanti.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
