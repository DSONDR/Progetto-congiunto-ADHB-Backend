package it.unife.sample.backend.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String cf;

    private String nome;

    private String cognome;

    private String username;

    private String email;

    private String tipoIscritto;

    private String messaggio;
}
