package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "istruttori")
@Data
@EqualsAndHashCode(callSuper = true) // Importante per l'ereditarietà con Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Istruttore extends Utente {
    
    private String specializzazione;

}
