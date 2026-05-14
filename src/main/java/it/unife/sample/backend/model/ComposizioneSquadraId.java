package it.unife.sample.backend.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComposizioneSquadraId implements Serializable {

    private Long squadra;
    private String atleta;
}
