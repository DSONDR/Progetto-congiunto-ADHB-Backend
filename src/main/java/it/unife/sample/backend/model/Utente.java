package it.unife.sample.backend.model;

import jakarta.persistence.*;      // Risolve @Entity, @Table, @Id, @Column, @Inheritance
import java.time.LocalDate;        // Risolve LocalDate
import lombok.Data;                // Per Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.JOINED)
@Data                // Crea automaticamente Getter, Setter, equals, canEqual, hashCode e toString
@NoArgsConstructor   // Lombok Crea il costruttore vuoto (obbligatorio per JPA)
@AllArgsConstructor  // Lombok Crea il costruttore con tutti i campi
public class Utente {
    @Id
    @Column(length = 16, nullable = false, unique = true)
    private String codiceFiscale;

    private String nome;
    private String cognome;
    private String genere;
    private LocalDate dataNascita;
    private String cittaResidenza;
    
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    private Double stipendio; // Può essere null
    private String tipoVisita;
    private LocalDate emissioneVisita;
    private LocalDate scadenzaVisita;
    private String medicoRiferimento;

    // Getter e Setter fatti da Lombok.
}
