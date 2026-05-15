package it.unife.sample.backend.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unife.sample.backend.model.Squadra;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.repository.SquadraRepository;
import it.unife.sample.backend.repository.AtletaRepository;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class SquadraService {
    @Autowired
    private SquadraRepository sqRepo;
    @Autowired
    private AtletaRepository atlRepo;

    // CRUD base
    public List<Squadra> findAll() {
        return sqRepo.findAll();
    }

    public Optional<Squadra> findById(Long id) {
        return sqRepo.findById(id);
    }

    public Squadra save(Squadra squadra) {
        return sqRepo.save(squadra);
    }

    public void deleteById(Long id) {
        sqRepo.deleteById(id);
    }

    // Altre funzioni
    // TODO, da usare ancora tutti
    // FK di squadra è il cf allenatore
    public List<Squadra> findByAllenatoreCf(String cf) {
        return sqRepo.findByAllenatoreCf(cf);
    }

    // In compone ho cf atleta, e in model lo riporto su squadra, qui lista squadre
    // di un atleta
    public List<Squadra> findByAtletaCf(String cf) {
        return sqRepo.findByAtletaCf(cf);
    }

    // Aggiungo atleta a una squadra
    public void aggiungiAtletaASquadra(Long squadraId, String atletaCf) {
        Squadra squadra = sqRepo.findById(squadraId).orElseThrow(() -> new RuntimeException("Squadra non trovata"));
        Atleta atleta = atlRepo.findById(atletaCf).orElseThrow(() -> new RuntimeException("Atleta non trovato"));

        squadra.getAtleti().add(atleta);
        sqRepo.save(squadra);
    }

    // Listo atleti di una squadra
    public List<Atleta> getAtletiBySquadra(Long squadraId) {
        return sqRepo.findById(squadraId)
                .map(Squadra::getAtleti)
                .orElse(new ArrayList<>());
    }

    // Ottengo tutti gli atleti con visita medica scaduta
    public List<Atleta> getAtletiScadutiBySquadra(Long squadraId) {
        Squadra squadra = sqRepo.findById(squadraId)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        return squadra.getAtleti().stream()
                .filter(atleta -> atleta.getCertificatiMedici() == null || atleta.getCertificatiMedici().isEmpty() ||
                        atleta.getCertificatiMedici().stream()
                                .noneMatch(cert -> cert.getDataScadenza() != null
                                        && !cert.getDataScadenza().isBefore(LocalDate.now())))
                .collect(Collectors.toList());
    }
}
