package it.unife.sample.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atleta")
@PrimaryKeyJoinColumn(name = "cf")
@Data
@EqualsAndHashCode(callSuper = true)
public class Atleta extends Utente {

}
