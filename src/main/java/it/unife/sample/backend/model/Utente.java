package it.unife.sample.backend.model;

import jakarta.persistence.*;      // Risolve @Entity, @Table, @Id, @Column, @Inheritance
import java.time.LocalDate;        // Risolve LocalDate
import lombok.Data;                // Per Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "UTENTE")
@Inheritance(strategy = InheritanceType.JOINED)
@Data                // Crea automaticamente Getter, Setter, equals, canEqual, hashCode e toString
@NoArgsConstructor   // Lombok Crea il costruttore vuoto (obbligatorio per JPA)
@AllArgsConstructor  // Lombok Crea il costruttore con tutti i campi
public class Utente {
    @Id
    @Column(name = "Cf", length = 16, nullable = false, unique = true)
    @NotBlank(message = "Codice fiscale obbligatorio")
    @Size(min = 16, max = 16, message = "Codice fiscale deve essere di 16 caratteri")
    private String cf;

    @Column(name = "Nome")
    @NotBlank(message = "Nome obbligatorio")
    private String nome;
    
    @Column(name = "Cognome")
    @NotBlank(message = "Cognome obbligatorio")
    private String cognome;
    
    @Column(name = "Genere")
    @NotBlank(message = "Genere obbligatorio")
    private String genere;
    
    @Column(name = "Data_Nascita")
    @NotNull(message = "Data di nascita obbligatoria")
    @Past(message = "Data di nascita deve essere nel passato")
    private LocalDate dataNascita;
    
    @Column(name = "Citta_Residenza")
    @NotBlank(message = "Città di residenza obbligatoria")
    private String cittaResidenza;
    
    @Column(name = "Password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, message = "Password deve essere almeno 8 caratteri")
    private String password;
    
    @Column(name = "Username", unique = true)
    @NotBlank(message = "Username obbligatorio")
    private String username;
    
    @Column(name = "Email", unique = true)
    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;
    
    @Column(name = "Tipo_Iscritto")
    private String tipoIscritto;
    
    @Column(name = "Stipendio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Stipendio deve essere positivo")
    private Double stipendio; // Può essere null
    private LocalDate emissioneVisita;
    private LocalDate scadenzaVisita;
    private String medicoRiferimento;

    // Getter e Setter fatti da Lombok.
}
