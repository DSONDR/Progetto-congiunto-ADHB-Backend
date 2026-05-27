package it.unife.sample.backend.service;

import it.unife.sample.backend.dto.request.LoginRequestDTO;
import it.unife.sample.backend.dto.request.RegisterRequestDTO;
import it.unife.sample.backend.dto.response.LoginResponseDTO;
import it.unife.sample.backend.dto.response.UserResponseDTO;
import it.unife.sample.backend.model.Atleta;
import it.unife.sample.backend.model.Utente;
import it.unife.sample.backend.model.CertificatoMedico;
import it.unife.sample.backend.repository.UtenteRepository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UtenteRepository utenteRepository;

    // Funzionalità di REGISTRAZIONE [Solo degli atleti
    // Registra un nuovo atleta nel database: 
    // Crea prima l'istanza di Atleta, la popola coi dati del DTO, e infine la salva nel repository.
    // Usata in: AuthController.register
    // Frontend: Registrazione / Utenti
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO dto) {

        // Controlli di unicità pre creazione con i dati inseriti in frontend
        if (utenteRepository.existsById(dto.getCf())) {
            throw new RuntimeException("Codice fiscale già registrato");
        }
        if (utenteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }
        if (utenteRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username già esistente");
        }

        // Creo atleta se unicità andata a buon fine

        Atleta atleta = new Atleta();

        atleta.setCf(dto.getCf());
        atleta.setNome(dto.getNome());
        atleta.setCognome(dto.getCognome());
        atleta.setGenere(dto.getGenere());
        atleta.setDataNascita(dto.getDataNascita());
        atleta.setCittaResidenza(dto.getCittaResidenza());
        atleta.setUsername(dto.getUsername());
        atleta.setEmail(dto.getEmail());
        // Ruolo applicativo
        atleta.setTipoIscritto("ATLETA");
        // Temporaneo
        atleta.setPassword(dto.getPassword());
        // Campi opzionali
        atleta.setCertificatiMedici(null);
        atleta.setPuntiGamification(0);
        // Salvo nel DB
        atleta = utenteRepository.save(atleta);
        // E infine rispondo con i dati formattati nel DTO
        return mapUserResponse(atleta);
    }

    // Funzionalità di LOGIN [stavolta per tutti i tipi]
    // Esegue il login controllando le credenziali (email e password).
    // Restituisce i dati dell'utente formattati nel DTO di risposta per non esporre la password al FE.
    // Usata in: AuthController.login
    // Frontend: Login / Utenti
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Utente utente = utenteRepository.findByUsernameOrEmail(dto.getEmail(), dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Controllo password se giusta login
        if (!utente.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Password errata");
        }
        // Se ok creo DTO di risposta
        LoginResponseDTO response = new LoginResponseDTO();

        response.setCf(utente.getCf());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        response.setEmail(utente.getEmail());
        response.setUsername(utente.getUsername());
        response.setDataNascita(utente.getDataNascita());
        response.setTipoIscritto(utente.getTipoIscritto());
        response.setPuntiGamification(utente.getPuntiGamification());
        response.setMessaggio("Login effettuato");

        if (utente.getCertificatiMedici() != null && !utente.getCertificatiMedici().isEmpty()) {
            LocalDate scadenza = utente.getCertificatiMedici().stream()
                    .map(CertificatoMedico::getDataScadenza)
                    .max(LocalDate::compareTo)
                    .orElse(null);
            response.setScadenzaCertificato(scadenza);
        }

        return response;
    }

    // Funzionalità di LOGOUT, vuoto perchè solo logico, su frontend
    // Usata in: AuthController.logout
    // Frontend: Logout / Utenti
    public String logout() {
        return "Logout effettuato";
    }

    // Funzionalità di CANCELLAZIONE ACCOUNT:
    // Elimina fisicamente l'account e tutti i dati correlati dell'utente dal database 
    // in modo transazionale.
    // Usata in: AuthController.deleteAccount
    // Frontend: Login / Utenti
    @Transactional
    public void deleteAccount(String cf) {

        Utente utente = utenteRepository.findById(cf)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        utenteRepository.delete(utente);
    }

    // Mapper interno, chiamato alla creazione dell'atleta
    private UserResponseDTO mapUserResponse(Utente utente) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setCf(utente.getCf());
        dto.setNome(utente.getNome());
        dto.setCognome(utente.getCognome());
        dto.setGenere(utente.getGenere());
        dto.setDataNascita(utente.getDataNascita());
        dto.setCittaResidenza(utente.getCittaResidenza());
        dto.setUsername(utente.getUsername());
        dto.setEmail(utente.getEmail());
        dto.setTipoIscritto(utente.getTipoIscritto());
        dto.setPuntiGamification(utente.getPuntiGamification());

        if (utente.getCertificatiMedici() != null && !utente.getCertificatiMedici().isEmpty()) {
            LocalDate scadenza = utente.getCertificatiMedici().stream()
                    .map(CertificatoMedico::getDataScadenza)
                    .max(LocalDate::compareTo)
                    .orElse(null);
            dto.setScadenzaCertificato(scadenza);
        }

        return dto;
    }
}
