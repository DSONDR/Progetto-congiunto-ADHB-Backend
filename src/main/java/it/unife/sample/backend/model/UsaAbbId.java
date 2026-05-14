package it.unife.sample.backend.model;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsaAbbId implements Serializable {
    private Long numeroAbb;
    private Long codiceAtt;
    private String cf;
    private LocalDate dataUso;
}