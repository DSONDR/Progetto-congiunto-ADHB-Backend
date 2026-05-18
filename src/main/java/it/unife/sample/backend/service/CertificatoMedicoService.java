package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unife.sample.backend.model.CertificatoMedico;
import it.unife.sample.backend.repository.CertificatoMedicoRepository;

@Service
public class CertificatoMedicoService {

    @Autowired
    private CertificatoMedicoRepository repository;

    // CRUD BASE
    public List<CertificatoMedico> findAll() {
        return repository.findAll();
    }

    public Optional<CertificatoMedico> findById(Long id) {
        return repository.findById(id);
    }

    public CertificatoMedico save(CertificatoMedico certificato) {
        return repository.save(certificato);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Recupera tutti i certificati medici di un atleta dato il CF
    // Usato da IscrizioneService (validazione iscrizione) e SottoscrizioneService
    // (validazione acquisto abbonamento)
    public List<CertificatoMedico> findByUtenteCf(String cf) {
        return repository.findByUtenteCf(cf);
    }
}