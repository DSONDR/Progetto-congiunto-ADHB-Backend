package it.unife.sample.backend.model;

import jakarta.persistence.*;      // Risolve @Entity, @Table, @Id, @Column, @Inheritance
import java.time.LocalDate;        // Risolve LocalDate
import lombok.Data;                // Per Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.JOINED)
@Data                // Crea automaticamente Getter, Setter, equals, canEqual, hashCode e toString
@NoArgsConstructor   // Lombok Crea il costruttore vuoto (obbligatorio per JPA)
@AllArgsConstructor  // Lombok Crea il costruttore con tutti i campi
public class Utente {
    @Id
    @Column(length = 16, nullable = false, unique = true)
    @NotBlank(message = "Codice fiscale obbligatorio")
    @Size(min = 16, max = 16, message = "Codice fiscale deve essere di 16 caratteri")
    private String cf;

    @NotBlank(message = "Nome obbligatorio")
    private String nome;
    @NotBlank(message = "Cognome obbligatorio")
    private String cognome;
    @NotBlank(message = "Genere obbligatorio")
    private String genere;
    @NotNull(message = "Data di nascita obbligatoria")
    @Past(message = "Data di nascita deve essere nel passato")
    private LocalDate dataNascita;
    @NotBlank(message = "Città di residenza obbligatoria")
    private String cittaResidenza;
    
    @Column(unique = true)
    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;
    
    @Column(unique = true)
    @NotBlank(message = "Username obbligatorio")
    private String username;
    
    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, message = "Password deve essere almeno 8 caratteri")
    private String password;
    @DecimalMin(value = "0.0", inclusive = false, message = "Stipendio deve essere positivo")
    private Double stipendio; // Può essere null
    private String tipoVisita;
    private LocalDate emissioneVisita;
    private LocalDate scadenzaVisita;
    private String medicoRiferimento;

    // Getter e Setter fatti da Lombok.
}
