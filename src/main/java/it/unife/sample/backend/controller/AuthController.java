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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    //REGISTRAZIONE
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(service.register(dto));
    }

    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    //LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(service.logout());
    }
    
    //CANCELLA ACCOUNT
    @DeleteMapping("/{cf}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String cf) {
        service.deleteAccount(cf);
        return ResponseEntity.noContent().build();
    }
}
