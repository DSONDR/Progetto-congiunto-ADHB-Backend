package it.unife.sample.backend.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SottoscrizioneId implements Serializable {
    private Long numeroAbb;
    private Long idPagamento;
    private String cf;
}