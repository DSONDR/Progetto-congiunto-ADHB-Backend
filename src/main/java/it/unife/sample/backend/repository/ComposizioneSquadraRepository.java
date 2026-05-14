package it.unife.sample.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unife.sample.backend.model.ComposizioneSquadra;
import it.unife.sample.backend.model.ComposizioneSquadraId;

@Repository
public interface ComposizioneSquadraRepository extends JpaRepository<ComposizioneSquadra, ComposizioneSquadraId> {

    List<ComposizioneSquadra> findBySquadraId(Long squadraId);

    List<ComposizioneSquadra> findByAtletaCf(String atletaCf);
}
