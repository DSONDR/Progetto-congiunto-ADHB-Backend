package it.unife.sample.backend.repository; // Cartella repository

import it.unife.sample.backend.model.Squadra; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadraRepository extends JpaRepository<Utente, Long> {
	//FK di squadra è il cf allenatore
	List<Squadra> findByAllenatoreCf(String cf);
	
	//In compone ho cf atleta, e in model lo riporto su squadra, qui lista squadre di un atleta
	List<Squadra> findByAtletiCf(String cf);
}
