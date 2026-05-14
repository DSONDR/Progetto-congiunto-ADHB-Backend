package it.unife.sample.backend.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateAttId implements Serializable {
    private LocalDateTime date;
    private Long attivita;
}