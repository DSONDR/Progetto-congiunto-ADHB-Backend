package it.unife.sample.backend.dto.response;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO utilizzato per restituire i dati di un Utente in seguito a un Login
 * Omette volontariamente la password per motivi di sicurezza e aggiunge un
 * messaggio di stato
 * Creato in AuthService (metodo login)
 * e restituito da AuthController (metodo login)
 */
@Data
public class LoginResponseDTO {

    private String cf;

    private String nome;

    private String cognome;

    private String username;

    private String email;

    private LocalDate dataNascita;

    private String tipoIscritto;

    private LocalDate scadenzaCertificato;

    private Integer puntiGamification;

    private String messaggio;
}
