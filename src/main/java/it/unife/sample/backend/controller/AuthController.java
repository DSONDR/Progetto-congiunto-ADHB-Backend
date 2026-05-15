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
 * Controller per la gestione dell'Autenticazione (Login, Registrazione, Logout
 * e cancellazione)
 *
 * API Esposte:
 * - POST /api/auth/register -> Registrazione nuovo utente (Atleta, ecc.)
 * - POST /api/auth/login -> Login utente
 * - POST /api/auth/logout -> Logout utente
 * - DELETE /api/auth/delete/{cf} -> Cancellazione account utente
 */
@RestController
@RequestMapping("/api/auth")
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
}
