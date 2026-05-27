package it.unife.sample.backend.repository; // Cartella repository

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Devi importare il Model perché si trova in un'altra cartella (package)
import it.unife.sample.backend.model.ComposizioneSquadra;
import it.unife.sample.backend.model.ComposizioneSquadraId;

@Repository
public interface ComposizioneSquadraRepository extends JpaRepository<ComposizioneSquadra, ComposizioneSquadraId> {

    // Recupera i record filtrando per SquadraId
    // Usato da SponsorizzazioneService.findBySquadraId()
    List<ComposizioneSquadra> findBySquadraId(Long squadraId);
    
    // Recupera i record filtrando per AtletaCf
    // Usato da PagamentoService.getStoricoTransazioni() e da AbbonamentoService.findByAtletaCf()
    // e da SottoscrizioneService.getStoricoUtente()
    List<ComposizioneSquadra> findByAtletaCf(String atletaCf);
    
}
