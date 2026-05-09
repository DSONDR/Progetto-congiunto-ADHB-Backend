package it.unife.sample.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "allenatore")
@PrimaryKeyJoinColumn(name = "cf")
@Data
@EqualsAndHashCode(callSuper = true) // Importante per l'ereditarietà con Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Allenatore extends Utente {
    @NotNull(message = "Grado obbligatorio")
    private Integer grado;

}
