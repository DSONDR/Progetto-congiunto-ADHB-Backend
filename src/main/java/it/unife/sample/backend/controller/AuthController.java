package it.unife.sample.backend.controller;

import it.unife.sample.backend.dto.request.LoginRequestDTO;
import it.unife.sample.backend.dto.request.RegisterRequestDTO;
import it.unife.sample.backend.dto.response.LoginResponseDTO;
import it.unife.sample.backend.dto.response.UserResponseDTO;
import it.unife.sample.backend.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per la gestione dell'Autenticazione (Login, Registrazione, Logout e cancellazione)
 * Mappato lato frontend in: auth.service.ts
 * 
 * API Esposte:
 * - POST /api/auth/register -> Registrazione di un nuovo utente nel sistema [Gestione UtentiComponent / RegistrazioneComponent]
 * - POST /api/auth/login -> Autenticazione dell'utente tramite email o username e password. [Cancella ProfiloComponent / Gestione UtentiComponent / LoginComponent / RegistrazioneComponent]
 * - POST /api/auth/logout -> Logout utente [Nessun component specifico]
 * - DELETE /api/auth/delete/{cf} -> Cancellazione definitiva dell'account utente dal sistema. [Cancella ProfiloComponent]
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService service;


    // Funzionalità: Registrazione di un nuovo utente nel sistema (gestisce anche i
    // sottotipi tramite tipoIscritto).
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.ok(service.register(request));
    }

    // Funzionalità: Autenticazione dell'utente tramite email o username e password.
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(service.login(request));
    }

    // Funzionalità: Logout utente
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(service.logout());
    }

    // Funzionalità: Cancellazione definitiva dell'account utente dal sistema.
    @DeleteMapping("/delete/{cf}")
    public ResponseEntity<String> deleteAccount(@PathVariable String cf) {
        service.deleteAccount(cf);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({RuntimeException.class, IllegalArgumentException.class})
    public ResponseEntity<java.util.Map<String, String>> handleExceptions(Exception ex) {
        java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
