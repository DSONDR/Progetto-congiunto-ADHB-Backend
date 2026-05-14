package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.DateAtt;
import it.unife.sample.backend.model.DateAttId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DateAttRepository extends JpaRepository<DateAtt, DateAttId> {
    List<DateAtt> findByAttivitaCodiceAtt(Long codiceAtt);
}