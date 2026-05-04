package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Atleta; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtletaRepository extends JpaRepository<Atleta, String> {
}

