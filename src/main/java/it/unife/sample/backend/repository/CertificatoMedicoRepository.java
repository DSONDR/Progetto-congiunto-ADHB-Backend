package it.unife.sample.backend.repository;

import it.unife.sample.backend.model.CertificatoMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CertificatoMedicoRepository extends JpaRepository<CertificatoMedico, Long> {

    // Trova certificato medico relativo all'utente dal suo Cf
    List<CertificatoMedico> findByUtenteCf(String cf);
}