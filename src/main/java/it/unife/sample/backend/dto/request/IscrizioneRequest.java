package it.unife.sample.backend.dto.request;

public record IscrizioneRequest(String utenteCf, Long attivitaId, Double importo, String metodo) {
}
