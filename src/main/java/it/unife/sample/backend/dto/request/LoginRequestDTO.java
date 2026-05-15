package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO utilizzato per la richiesta di Login
 * Raccoglie le credenziali (email e password) inviate dal frontend
 * Utilizzato nel file AuthController (metodo login)
 * e processato da AuthService (metodo login)
 */
@Data
public class LoginRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
