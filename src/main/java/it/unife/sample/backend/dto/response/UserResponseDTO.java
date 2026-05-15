package it.unife.sample.backend.dto.response;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO utilizzato per l'invio dei dati anagrafici pubblici di un Utente (a
 * seguito di registrazione)
 * Omette volontariamente dati sensibili come la password
 * Creato in AuthService (metodo mapUserResponse)
 * e restituito da AuthController (metodo register)
 */
@Data
public class UserResponseDTO {
    // Nota che nei dto di response non serve @notblank perché
    // i dati sono già presi da db e validati in precedenza
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
