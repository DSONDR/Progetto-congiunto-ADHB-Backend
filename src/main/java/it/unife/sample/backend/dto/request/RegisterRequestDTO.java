package it.unife.sample.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

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
