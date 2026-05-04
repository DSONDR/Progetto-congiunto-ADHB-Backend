package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atleti")
@Data
@EqualsAndHashCode(callSuper = true)
public class Atleta extends Utente {

}
