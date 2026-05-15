package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO utilizzato per la registrazione di un nuovo Atleta
 * Raccoglie i dati anagrafici e le credenziali inserite nel form di
 * registrazione dal frontend
 * Utilizzato nel file AuthController (metodo register)
 * e processato da AuthService (metodo register)
 */
@Data
public class RegisterRequestDTO {

    @NotBlank
    @Size(min = 16, max = 16)
    private String cf;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String genere;

    @NotNull
    @Past
    private LocalDate dataNascita;

    @NotBlank
    private String cittaResidenza;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
