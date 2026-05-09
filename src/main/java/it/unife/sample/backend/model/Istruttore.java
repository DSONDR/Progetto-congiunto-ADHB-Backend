package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "istruttori")
@PrimaryKeyJoinColumn(name = "cf")
@Data
@EqualsAndHashCode(callSuper = true) // Importante per l'ereditarietà con Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Istruttore extends Utente {
    @NotNull(message = "Specializzazione obbligatoria")
    private String specializzazione;

}
