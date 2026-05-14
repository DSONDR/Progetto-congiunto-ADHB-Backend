package it.unife.sample.backend.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserResponseDTO {

    private String cf;

    private String nome;

    private String cognome;

    private String genere;

    private LocalDate dataNascita;

    private String cittaResidenza;

    private String username;

    private String email;

    private String tipoIscritto;
}
