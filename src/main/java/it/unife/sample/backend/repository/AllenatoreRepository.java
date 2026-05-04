package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Allenatore; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllenatoreRepository extends JpaRepository<Allenatore, String> {

	//Trova tutti gli allenatori con un certo grado
	List<Allenatore> findByGrado(Integer grado);
	//Filtro allenatori tra due gradi
	List<Allenatore> findByGradoBetween(Integer min, Integer max);
}

