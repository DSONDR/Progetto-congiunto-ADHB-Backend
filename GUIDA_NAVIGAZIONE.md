# Guida alla Navigazione del Progetto Backend

Questa guida spiega come seguire una funzionalità partendo dal Frontend fino ad arrivare al Database.
L'architettura utilizzata è a livelli (Layered Architecture) e segue rigidamente questo flusso:


**Database** $\rightarrow$ **Entity (Model)** $\rightarrow$ **Repository** $\rightarrow$ **Service** $\rightarrow$ **Controller** $\leftrightarrow$ **DTO**

Di seguito trovi sia la mappatura dei file per ogni modulo, sia la **descrizione passo-passo dei flussi di esecuzione** per capire come le funzioni comunicano tra loro.

---

## 1. Modulo Autenticazione / Gestione Utenti

### Mappatura File
- **Controller:** `AuthController.java` (Endpoint: `POST /register`, `POST /login`, `DELETE /delete/{cf}`)
- **DTO:** `RegisterRequestDTO`, `LoginRequestDTO`, `LoginResponseDTO`
- **Service:** `AuthService.java` (logica di validazione e smistamento), `UtenteService.java` (CRUD base)
- **Repository:** `UtenteRepository.java`, `AtletaRepository.java`, ecc.
- **Entity:** `Utente.java`, `Atleta.java`, `Allenatore.java`, `Istruttore.java`

### Flusso di Esecuzione: Registrazione di un Atleta
1. **Frontend** invia una chiamata HTTP `POST /register` con un JSON contenente i dati (es. nome, cognome, cf, tipoIscritto="ATLETA").
2. Il **Controller** (`AuthController.register`) riceve il JSON che viene automaticamente convertito nell'oggetto `RegisterRequestDTO`.
3. Il Controller passa il DTO al **Service** chiamando `authService.register(request)`.
4. Nel **Service**, viene eseguita la logica di business:
   - Controlla tramite `UtenteRepository.existsByEmail()` se l'utente esiste già.
   - Legge `request.getTipoIscritto()` e istanzia l'Entity corretta (nel nostro caso viene fatto `new Atleta()`).
   - Copia i dati dal DTO all'Entity.
   - Chiama il **Repository** (`atletaRepository.save(atleta)`) per salvare fisicamente la riga sul Database.
5. Il Service restituisce un `UserResponseDTO` al Controller, che a sua volta lo impacchetta in una `ResponseEntity.ok()` per segnalare successo al Frontend.

---

## 2. Modulo Abbonamenti

### Mappatura File
- **Controller:** 
  - `AbbonamentoController.java` (`GET /tipi` per i listini)
  - `SottoscrizioneController.java` (`POST /sottoscrivi`, `GET /utente/{cf}`, ecc.)
- **DTO:** `TipoAbbonamentoDTO` (dettagli listino), `SottoscrizioneRequest` (dati acquisto)
- **Service:** `AbbonamentoService.java` (listino), `SottoscrizioneService.java` (motore di acquisto)
- **Repository:** `SottoscrizioneRepository.java`, `PagamentoRepository.java`, `AbbonamentoRepository.java`
- **Entity:** `Abbonamento.java`, `Sottoscrizione.java`, `Pagamento.java`

### Flusso di Esecuzione: Acquisto di un Abbonamento
1. **Frontend** invia `POST /sottoscrivi` con il DTO `SottoscrizioneRequest` (es. `atletaCf`, `tipoAbbonamento="Trimestrale"`, `metodo="Carta"`).
2. Il **Controller** (`SottoscrizioneController`) prima recupera l'Atleta dal DB tramite `AtletaService.findById()`, poi chiama `sottoscrizioneService.sottoscrivi(atleta, request.getTipoAbbonamento(), request.getMetodo())`.
3. Nel **Service** (`SottoscrizioneService`):
   - Per prima cosa, chiede i dettagli tecnici dell'abbonamento chiamando `abbonamentoService.getDettagliTipo("Trimestrale")`.
   - `AbbonamentoService` cerca "Trimestrale" nel listino statico e restituisce un `TipoAbbonamentoDTO` (es. prezzo 135€, durata 3 mesi).
   - A questo punto si inizia a scrivere sul DB: si crea il **Pagamento** copiando l'importo dal DTO e salvandolo con `pagRepo.save()`.
   - Si crea l'**Abbonamento** legandolo all'Atleta e al Pagamento. Si calcola la `dataScadenza` sommando la durata estratta dal DTO alla data odierna (`LocalDate.now().plusMonths(tipo.getDurataMesi())`). Lo si salva con `abbRepo.save()`.
   - Infine, si crea la **Sottoscrizione** (entità ponte) che chiude il cerchio legando tutto, e la si salva con `sottRepo.save()`.

---

## 3. Modulo Iscrizioni e Uso Abbonamento

### Mappatura File
- **Controller:** `IscrizioneController.java` (`POST /iscrivi` per pagare subito, `POST /usa-abbonamento` per scalare l'ingresso)
- **DTO:** `IscrizioneRequest`, `UsaAbbonamentoRequest`
- **Service:** `IscrizioneService.java` (cuore dei controlli capienza e validità)
- **Repository:** `IscrizioneRepository.java` (singole), `UsaAbbRepository.java` (da abbonamento), `AttivitaRepository.java`
- **Entity:** `Iscrizione.java`, `UsaAbb.java`, `Attivita.java`

### Flusso di Esecuzione: Ingresso a un'Attività "scalando" l'Abbonamento
1. **Frontend** invia `POST /usa-abbonamento` con il DTO `UsaAbbonamentoRequest` (`atletaCf`, `attivitaId`, `abbonamentoId`).
2. Il **Controller** verifica l'esistenza dei tre oggetti (Atleta, Attività, Abbonamento) richiamando i loro Service. Se esistono, passa la palla a `iscrizioneService.iscriviConAbbonamento(...)`.
3. Nel **Service** (`IscrizioneService`):
   - **Fase di Validazione:** L'abbonamento è ATTIVO? Il certificato medico (letto tramite `CertificatoMedicoService`) è ancora valido? 
   - **Controllo Capienza:** Si conta il totale dei posti occupati interrogando entrambi i repository (`IscrizioneRepo.count(...)` + `UsaAbbRepo.count(...)`). Se c'è posto, si procede.
   - **Logica dello Scalo:** Il Service legge la stringa `tipoAbb` dal DB (es. "10 Ingressi") e chiama `abbonamentoService.getDettagliTipo("10 Ingressi")` per ricevere il DTO e scoprirne le regole.
   - Se la regola dal DTO dice che è di tipo `"INGRESSI"`, incrementa il contatore `ingressiEffettuati` nel DB dell'abbonamento.
   - Se i vecchi ingressi + 1 raggiungono il limite `tipo.getMaxIngressi()`, il Service cambia proattivamente lo stato dell'abbonamento a "SCADUTO".
   - **Salvataggio:** Viene creata l'entità `UsaAbb` contenente un UUID generato per il QR Code e la data di oggi, che viene salvata in `usaAbbRepo.save()`.

---

## 4. Modulo Creazione e Gestione Attività

### Mappatura File
### Mappatura File
- **Controller:** `AttivitaGestioneController.java` (solo istruttori), `AttivitaVisualizzazioneController.java` (pubblico)
- **DTO:** `AttivitaRequestDTO`, `AttivitaResponseDTO`
- **Service:** `AttivitaService.java`
- **Repository:** `AttivitaRepository.java`, `ImpiantoRepository.java`
- **Entity:** `Attivita.java`, `DateAtt.java`, `Impianto.java`

### Flusso di Esecuzione: Creazione di una nuova Attività
1. **Frontend** invia una richiesta di creazione con un JSON mappato in `AttivitaRequestDTO` (nome, date multiple, istruttore, id impianto).
2. Il **Controller** chiama `attivitaService.create(dto)`.
3. Nel **Service** (`AttivitaService`):
   - Recupera e verifica l'esistenza dell'Istruttore e dell'Impianto dal Database.
   - **Prevenzione Sovrapposizioni:** Se sono presenti date, interroga `AttivitaRepository.existsByImpiantoAndDateOverlap(...)` per assicurarsi che nessun'altra attività impegni l'Impianto in quelle date esatte.
   - Se c'è sovrapposizione lancia un errore, altrimenti procede mappando il DTO nell'Entity `Attivita`.
   - Vengono anche convertite le date semplici nella tabella collegata `DateAtt`.
   - Si salva sul Database con `repo.save(attivita)` e si risponde con il DTO al frontend.

---

## 5. Funzionalità Extra (In attesa di essere collegate a macro/flussi)

Questa è una mappa delle funzioni che attualmente non fanno parte di un flusso end-to-end ma sono "pronte all'uso" nei rispettivi Service. Ottime per sviluppi futuri (dashboard, report, pannelli admin).

### Gestione Squadre e Allenatori
- `AllenatoreService.findByGrado(String)` e `findByGradoBetween(...)`: Filtri per cercare allenatori in base alla loro qualifica.
- `SquadraService.aggiungiAtletaASquadra(...)`: Assegna un Atleta a una Squadra.
- `SquadraService.getAtletiBySquadra(...)` e `getAtletiScadutiBySquadra(...)`: Report utilissimo per l'allenatore per vedere chi della sua squadra non è in regola col certificato medico.
- `SquadraService.findByAllenatoreCf(...)` e `findByAtletaCf(...)`: Lookup per i profili utente.
- `PartecipazioneSqService`: Contiene logiche per la gestione della partecipazione delle squadre alle attività (es. tornei).

### Gestione Sponsorizzazioni
- `SponsorService.aggiungiSponsorASquadra(...)` e `aggiungiSponsorAImpianto(...)`: Metodi per collegare un'azienda sponsor.
- `SponsorService.getSquadreBySponsor(...)` e `getImpiantiBySponsor(...)`: Per generare report di visibilità per le aziende.
- `SponsorizzazioneService.findBy...`: Metodi di ricerca vari per P.Iva, Squadra e Impianto.

### Gestione Impianti
- `ImpiantoService.findByTipoImpianto(...)` e `findByStato(...)`: Per creare una vista "Manutenzione" o "Disponibilità" impianti.
- `ImpiantoService.aggiungiAttivitaAImpianto(...)` / `getAttivitaByImpianto(...)`.

### Gestione Pagamenti e Contabilità
- `PagamentoService.ricercaAvanzata(date, importi)`: Perfetto per bilanci mensili.
- `PagamentoService.getPagamentiPerAttivita(id)`: Per calcolare quanto ha reso uno specifico corso/evento.

### Assistenza (Help Desk)
- `AssistenzaService.findByStato(...)`, `findByTipoAss(...)`, `findBySoddisfazione(...)`: Per un pannello admin dei ticket aperti/chiusi.
- `AssistenzaService.associaUtente(ticketId, cf)` e `getUtenteByTicket(ticketId)`: Per legare le segnalazioni agli utenti.

### Certificati Medici
- `CertificatoMedicoService.findByUtenteCf(...)`: Attualmente usato "silenziosamente" dalle Iscrizioni, ma utilissimo se si vuole creare una pagina frontend "I miei Documenti" per permettere all'atleta di vedere la data di scadenza del proprio certificato.
