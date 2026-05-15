package it.unife.sample.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unife.sample.backend.model.ComposizioneSquadra;
import it.unife.sample.backend.model.ComposizioneSquadraId;

@Repository
public interface ComposizioneSquadraRepository extends JpaRepository<ComposizioneSquadra, ComposizioneSquadraId> {

    // Trova la composizione di una squadra, restituendo i record di
    // collegamento atleta-squadra
    List<ComposizioneSquadra> findBySquadraId(Long squadraId);

    // Trova tutte le associazioni atleta-squadra dato il cf di un atleta
    List<ComposizioneSquadra> findByAtletaCf(String atletaCf);
}
