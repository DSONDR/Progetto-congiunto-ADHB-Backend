package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "ISTRUTTORE")
@PrimaryKeyJoinColumn(name = "Cf")
@Data
@EqualsAndHashCode(callSuper = true) // Importante per l'ereditarietà con Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Istruttore extends Utente {
    @Column(name = "Specializzazione")
    @NotNull(message = "Specializzazione obbligatoria")
    private String specializzazione;

}
