package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.Istruttore; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IstruttoreRepository extends JpaRepository<Istruttore, String> {
}
