CREATE DATABASE  IF NOT EXISTS `webapp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `webapp`;
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: webapp
-- ------------------------------------------------------
-- Server version	8.0.45-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ABBONAMENTO`
--

DROP TABLE IF EXISTS `ABBONAMENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ABBONAMENTO` (
  `Numero_Abb` int NOT NULL AUTO_INCREMENT,
  `Data_Inizio` date DEFAULT NULL,
  `Data_Scadenza` date DEFAULT NULL,
  `Tipo_Abb` varchar(50) DEFAULT NULL,
  `Ingressi_Effettuati` int DEFAULT NULL,
  `Stato_Abb` varchar(50) DEFAULT NULL,
  `Cf_Atleta` char(16) DEFAULT NULL,
  `Id_Pagamento` int DEFAULT NULL,
  PRIMARY KEY (`Numero_Abb`),
  KEY `Cf_Atleta` (`Cf_Atleta`),
  KEY `Id_Pagamento` (`Id_Pagamento`),
  CONSTRAINT `ABBONAMENTO_ibfk_1` FOREIGN KEY (`Cf_Atleta`) REFERENCES `ATLETA` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ABBONAMENTO_ibfk_2` FOREIGN KEY (`Id_Pagamento`) REFERENCES `PAGAMENTO` (`Id_Pagamento`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ABBONAMENTO`
--

LOCK TABLES `ABBONAMENTO` WRITE;
/*!40000 ALTER TABLE `ABBONAMENTO` DISABLE KEYS */;
INSERT INTO `ABBONAMENTO` VALUES (1,'2026-01-10','2026-12-31','ANNUALE',10,'ATTIVO','BGLLDA95C25G273T',1),(2,'2025-05-15','2026-05-15','ANNUALE',45,'SCADUTO','GLLCHR98T52L736S',2),(3,'2026-02-20','2026-08-20','SEMESTRALE',5,'ATTIVO','MRRMRC00D18D969R',3),(4,'2026-03-01','2026-09-01','SEMESTRALE_PREMIUM',12,'ATTIVO','CNIGLI94S51G337K',4);
/*!40000 ALTER TABLE `ABBONAMENTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ALLENATORE`
--

DROP TABLE IF EXISTS `ALLENATORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ALLENATORE` (
  `Cf` char(16) NOT NULL,
  `Grado` int DEFAULT NULL,
  PRIMARY KEY (`Cf`),
  CONSTRAINT `ALLENATORE_ibfk_1` FOREIGN KEY (`Cf`) REFERENCES `UTENTE` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ALLENATORE`
--

LOCK TABLES `ALLENATORE` WRITE;
/*!40000 ALTER TABLE `ALLENATORE` DISABLE KEYS */;
INSERT INTO `ALLENATORE` VALUES ('PRTGCM75B15A944V',3),('STRGNN78M08D612U',2);
/*!40000 ALTER TABLE `ALLENATORE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSISTENZA`
--

DROP TABLE IF EXISTS `ASSISTENZA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ASSISTENZA` (
  `Id_Ticket` int NOT NULL AUTO_INCREMENT,
  `Oggetto` varchar(255) DEFAULT NULL,
  `Tipo_Ass` varchar(50) DEFAULT NULL,
  `Stato` varchar(50) DEFAULT NULL,
  `Contenuto` text,
  `Risposta` text,
  `Soddisfazione` int DEFAULT NULL,
  `Cf_Utente` char(16) DEFAULT NULL,
  `Cf_Assistente` char(16) DEFAULT NULL,
  PRIMARY KEY (`Id_Ticket`),
  KEY `Cf_Utente` (`Cf_Utente`),
  KEY `Cf_Assistente` (`Cf_Assistente`),
  CONSTRAINT `ASSISTENZA_ibfk_1` FOREIGN KEY (`Cf_Utente`) REFERENCES `UTENTE` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ASSISTENZA_ibfk_2` FOREIGN KEY (`Cf_Assistente`) REFERENCES `UTENTE` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSISTENZA`
--

LOCK TABLES `ASSISTENZA` WRITE;
/*!40000 ALTER TABLE `ASSISTENZA` DISABLE KEYS */;
INSERT INTO `ASSISTENZA` VALUES (1,'Problema con il badge','TECNICO','APERTO','Il mio badge non funziona ai tornelli.',NULL,NULL,'BGLLDA95C25G273T',NULL),(2,'Richiesta cambio corso','AMMINISTRATIVO','IN_LAVORAZIONE','Vorrei passare dal turno di mattina a quello di sera.','Abbiamo preso in carico la richiesta.',NULL,'GLLCHR98T52L736S','RSSMRA80A01H501Z'),(3,'Certificato non riconosciuto','MEDICO','RISOLTO','Ho caricato il certificato ma risulta assente.','Abbiamo approvato manualmente il certificato, ora è visibile.',NULL,'MRRMRC00D18D969R','RSSMRA80A01H501Z'),(4,'Rimborso quota','AMMINISTRATIVO','CHIUSO','Vorrei il rimborso per il corso annullato.','Rimborso emesso tramite bonifico in data odierna.',5,'VLASRA02P45A662Q','RSSMRA80A01H501Z');
/*!40000 ALTER TABLE `ASSISTENZA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ATLETA`
--

DROP TABLE IF EXISTS `ATLETA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATLETA` (
  `Cf` char(16) NOT NULL,
  PRIMARY KEY (`Cf`),
  CONSTRAINT `ATLETA_ibfk_1` FOREIGN KEY (`Cf`) REFERENCES `UTENTE` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ATLETA`
--

LOCK TABLES `ATLETA` WRITE;
/*!40000 ALTER TABLE `ATLETA` DISABLE KEYS */;
INSERT INTO `ATLETA` VALUES ('BGESMN03B28B157L'),('BGLLDA95C25G273T'),('CNIGLI94S51G337K'),('GLLCHR98T52L736S'),('GRGLCU96H20C351P'),('LLLNDR97E08L424M'),('MRRMRC00D18D969R'),('RSOMRT01R55G224N'),('VLASRA02P45A662Q'),('ZZRLNE99A70L781O');
/*!40000 ALTER TABLE `ATLETA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ATTIVITA`
--

DROP TABLE IF EXISTS `ATTIVITA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTIVITA` (
  `Codice_Att` int NOT NULL AUTO_INCREMENT,
  `Nome_Att` varchar(100) DEFAULT NULL,
  `Tipo_Evento` varchar(50) DEFAULT NULL,
  `Destinatario` varchar(50) DEFAULT NULL,
  `Descrizione` varchar(2000) DEFAULT NULL,
  `Quota_Base` decimal(10,2) DEFAULT NULL,
  `Max_Partecipanti` int DEFAULT NULL,
  `Cf_Istruttore` char(16) DEFAULT NULL,
  `Id_Impianto` int DEFAULT NULL,
  PRIMARY KEY (`Codice_Att`),
  KEY `Cf_Istruttore` (`Cf_Istruttore`),
  KEY `Id_Impianto` (`Id_Impianto`),
  CONSTRAINT `ATTIVITA_ibfk_1` FOREIGN KEY (`Cf_Istruttore`) REFERENCES `ISTRUTTORE` (`Cf`) ON UPDATE CASCADE,
  CONSTRAINT `ATTIVITA_ibfk_2` FOREIGN KEY (`Id_Impianto`) REFERENCES `IMPIANTO` (`Id_Impianto`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ATTIVITA`
--

LOCK TABLES `ATTIVITA` WRITE;
/*!40000 ALTER TABLE `ATTIVITA` DISABLE KEYS */;
INSERT INTO `ATTIVITA` VALUES (1,'Corso Nuoto Base','CORSO','TUTTI','Corso per principianti',50.00,5,'BNCLGU85E10F205X',2),(2,'Gara Nuoto','GARA','SQUADRA','Gara interprovinciale',20.00,20,'BNCLGU85E10F205X',2),(3,'Allenamento Basket','ALLENAMENTO','SQUADRA','Allenamento intensivo',0.00,15,'VRDNNA90L62L219Y',1),(4,'Corso Basket','CORSO','TUTTI','Fondamentali basket',40.00,10,'VRDNNA90L62L219Y',1),(5,'Corso Pilates','CORSO','TUTTI','Pilates mattutino',35.00,20,'NREPLA88S30F839W',1),(6,'Crossfit WOD','EVENTO','TUTTI','Workout of the day',15.00,10,'NREPLA88S30F839W',1),(7,'Calcetto All aperto','EVENTO','TUTTI','Torneo amatoriale',10.00,10,'BNCLGU85E10F205X',3),(8,'Pallavolo Estiva','GARA','SQUADRA','Torneo di pallavolo',25.00,50,'VRDNNA90L62L219Y',3),(9,'Zumba Fitness','CORSO','TUTTI','Zumba serale',30.00,15,'NREPLA88S30F839W',1),(10,'Pilates Avanzato','CORSO','TUTTI','Masterclass',45.00,2,'NREPLA88S30F839W',1);
/*!40000 ALTER TABLE `ATTIVITA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CERTIFICATO_MEDICO`
--

DROP TABLE IF EXISTS `CERTIFICATO_MEDICO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CERTIFICATO_MEDICO` (
  `Id_Certificato` int NOT NULL AUTO_INCREMENT,
  `Cf` char(16) DEFAULT NULL,
  `Tipo` varchar(30) DEFAULT NULL,
  `Data_emissione` date DEFAULT NULL,
  `Data_scadenza` date DEFAULT NULL,
  `Medico_riferimento` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id_Certificato`),
  KEY `Cf` (`Cf`),
  CONSTRAINT `CERTIFICATO_MEDICO_ibfk_1` FOREIGN KEY (`Cf`) REFERENCES `UTENTE` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CERTIFICATO_MEDICO`
--

LOCK TABLES `CERTIFICATO_MEDICO` WRITE;
/*!40000 ALTER TABLE `CERTIFICATO_MEDICO` DISABLE KEYS */;
INSERT INTO `CERTIFICATO_MEDICO` VALUES (1,'BNCLGU85E10F205X','AGONISTICO','2025-01-01','2027-01-01','Dott. House'),(2,'VRDNNA90L62L219Y','AGONISTICO','2025-02-15','2027-02-15','Dott. Cox'),(3,'NREPLA88S30F839W','NON_AGONISTICO','2025-03-10','2027-03-10','Dott.ssa Grey'),(4,'PRTGCM75B15A944V','AGONISTICO','2025-04-20','2027-04-20','Dott. House'),(5,'STRGNN78M08D612U','AGONISTICO','2025-05-05','2027-05-05','Dott. Cox'),(6,'BGLLDA95C25G273T','AGONISTICO','2025-06-01','2026-06-01','Dott. House'),(7,'GLLCHR98T52L736S','NON_AGONISTICO','2025-07-10','2026-07-10','Dott.ssa Grey'),(8,'MRRMRC00D18D969R','AGONISTICO','2025-08-15','2026-08-15','Dott. Cox'),(9,'VLASRA02P45A662Q','NON_AGONISTICO','2025-09-20','2026-09-20','Dott. House'),(10,'GRGLCU96H20C351P','AGONISTICO','2025-10-05','2026-10-05','Dott.ssa Grey'),(11,'ZZRLNE99A70L781O','AGONISTICO','2025-11-11','2026-11-11','Dott. Cox'),(12,'RSOMRT01R55G224N','NON_AGONISTICO','2023-01-01','2024-01-01','Dott. House'),(13,'LLLNDR97E08L424M','AGONISTICO','2023-05-15','2024-05-15','Dott. Cox');
/*!40000 ALTER TABLE `CERTIFICATO_MEDICO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPOSIZIONE_SQUADRA`
--

DROP TABLE IF EXISTS `COMPOSIZIONE_SQUADRA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPOSIZIONE_SQUADRA` (
  `Id_Squadra` int NOT NULL,
  `Cf_Atleta` char(16) NOT NULL,
  PRIMARY KEY (`Id_Squadra`,`Cf_Atleta`),
  KEY `Cf_Atleta` (`Cf_Atleta`),
  CONSTRAINT `COMPOSIZIONE_SQUADRA_ibfk_1` FOREIGN KEY (`Id_Squadra`) REFERENCES `SQUADRA` (`Id_Squadra`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `COMPOSIZIONE_SQUADRA_ibfk_2` FOREIGN KEY (`Cf_Atleta`) REFERENCES `ATLETA` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPOSIZIONE_SQUADRA`
--

LOCK TABLES `COMPOSIZIONE_SQUADRA` WRITE;
/*!40000 ALTER TABLE `COMPOSIZIONE_SQUADRA` DISABLE KEYS */;
INSERT INTO `COMPOSIZIONE_SQUADRA` VALUES (3,'BGESMN03B28B157L'),(1,'BGLLDA95C25G273T'),(1,'GLLCHR98T52L736S'),(2,'GRGLCU96H20C351P'),(3,'LLLNDR97E08L424M'),(1,'MRRMRC00D18D969R'),(2,'RSOMRT01R55G224N'),(1,'VLASRA02P45A662Q'),(2,'ZZRLNE99A70L781O');
/*!40000 ALTER TABLE `COMPOSIZIONE_SQUADRA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATE_ATT`
--

DROP TABLE IF EXISTS `DATE_ATT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATE_ATT` (
  `Date` datetime NOT NULL,
  `Codice_Att` int NOT NULL,
  PRIMARY KEY (`Date`,`Codice_Att`),
  KEY `Codice_Att` (`Codice_Att`),
  CONSTRAINT `DATE_ATT_ibfk_1` FOREIGN KEY (`Codice_Att`) REFERENCES `ATTIVITA` (`Codice_Att`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATE_ATT`
--

LOCK TABLES `DATE_ATT` WRITE;
/*!40000 ALTER TABLE `DATE_ATT` DISABLE KEYS */;
INSERT INTO `DATE_ATT` VALUES ('2026-06-10 10:00:00',1),('2026-06-17 10:00:00',1),('2026-06-20 15:00:00',2),('2026-06-11 18:00:00',3),('2026-06-12 17:00:00',4),('2026-06-13 09:00:00',5),('2026-06-14 19:00:00',6),('2026-06-15 20:00:00',7),('2026-06-16 16:00:00',8),('2026-06-18 19:30:00',9),('2026-06-19 18:00:00',10);
/*!40000 ALTER TABLE `DATE_ATT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPIANTO`
--

DROP TABLE IF EXISTS `IMPIANTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IMPIANTO` (
  `Id_Impianto` int NOT NULL AUTO_INCREMENT,
  `Nome_I` varchar(100) DEFAULT NULL,
  `Tipo_Impianto` varchar(50) DEFAULT NULL,
  `Stato_I` varchar(50) DEFAULT NULL,
  `Omologazione` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Id_Impianto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPIANTO`
--

LOCK TABLES `IMPIANTO` WRITE;
/*!40000 ALTER TABLE `IMPIANTO` DISABLE KEYS */;
INSERT INTO `IMPIANTO` VALUES (1,'Palazzetto dello Sport','Palestra Coperta','ATTIVO','FIP, FIPAV'),(2,'Piscina Comunale','Piscina','ATTIVO','FIN'),(3,'Campo Esterno','Campo Polivalente','FUORI_SERVIZIO','Nessuna');
/*!40000 ALTER TABLE `IMPIANTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ISCR_SINGOLA`
--

DROP TABLE IF EXISTS `ISCR_SINGOLA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ISCR_SINGOLA` (
  `Codice_Att` int NOT NULL,
  `Id_Pagamento` int NOT NULL,
  `Cf` char(16) NOT NULL,
  `Data_Iscr` date DEFAULT NULL,
  `Qr_Code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Codice_Att`,`Id_Pagamento`,`Cf`),
  KEY `Id_Pagamento` (`Id_Pagamento`),
  KEY `Cf` (`Cf`),
  CONSTRAINT `ISCR_SINGOLA_ibfk_1` FOREIGN KEY (`Codice_Att`) REFERENCES `ATTIVITA` (`Codice_Att`) ON UPDATE CASCADE,
  CONSTRAINT `ISCR_SINGOLA_ibfk_2` FOREIGN KEY (`Id_Pagamento`) REFERENCES `PAGAMENTO` (`Id_Pagamento`) ON UPDATE CASCADE,
  CONSTRAINT `ISCR_SINGOLA_ibfk_3` FOREIGN KEY (`Cf`) REFERENCES `ATLETA` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ISCR_SINGOLA`
--

LOCK TABLES `ISCR_SINGOLA` WRITE;
/*!40000 ALTER TABLE `ISCR_SINGOLA` DISABLE KEYS */;
INSERT INTO `ISCR_SINGOLA` VALUES (1,5,'VLASRA02P45A662Q','2026-05-01','QR-ISCR-001'),(1,6,'GRGLCU96H20C351P','2026-05-02','QR-ISCR-002'),(1,7,'ZZRLNE99A70L781O','2026-05-03','QR-ISCR-003'),(1,8,'RSOMRT01R55G224N','2026-05-04','QR-ISCR-004'),(1,9,'LLLNDR97E08L424M','2026-05-05','QR-ISCR-005'),(10,10,'BGESMN03B28B157L','2026-05-06','QR-ISCR-006'),(10,11,'CNIGLI94S51G337K','2026-05-07','QR-ISCR-007');
/*!40000 ALTER TABLE `ISCR_SINGOLA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ISTRUTTORE`
--

DROP TABLE IF EXISTS `ISTRUTTORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ISTRUTTORE` (
  `Cf` char(16) NOT NULL,
  `Specializzazione` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Cf`),
  CONSTRAINT `ISTRUTTORE_ibfk_1` FOREIGN KEY (`Cf`) REFERENCES `UTENTE` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ISTRUTTORE`
--

LOCK TABLES `ISTRUTTORE` WRITE;
/*!40000 ALTER TABLE `ISTRUTTORE` DISABLE KEYS */;
INSERT INTO `ISTRUTTORE` VALUES ('BNCLGU85E10F205X','Nuoto'),('NREPLA88S30F839W','Fitness e Zumba'),('VRDNNA90L62L219Y','Basket e Pallavolo');
/*!40000 ALTER TABLE `ISTRUTTORE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PAGAMENTO`
--

DROP TABLE IF EXISTS `PAGAMENTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAGAMENTO` (
  `Id_Pagamento` int NOT NULL AUTO_INCREMENT,
  `Metodo_Pag` varchar(50) DEFAULT NULL,
  `Data_Pag` date DEFAULT NULL,
  `Stato_Pag` varchar(50) DEFAULT NULL,
  `Importo` decimal(10,2) DEFAULT NULL,
  `Fattura` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Id_Pagamento`),
  UNIQUE KEY `unq_fattura` (`Fattura`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PAGAMENTO`
--

LOCK TABLES `PAGAMENTO` WRITE;
/*!40000 ALTER TABLE `PAGAMENTO` DISABLE KEYS */;
INSERT INTO `PAGAMENTO` VALUES (1,'CARTA','2026-01-10','COMPLETATO',150.00,'FATT-2026-001'),(2,'BONIFICO','2025-05-15','COMPLETATO',150.00,'FATT-2025-001'),(3,'CARTA','2026-02-20','COMPLETATO',100.00,'FATT-2026-002'),(4,'CONTANTI','2026-03-01','COMPLETATO',200.00,'FATT-2026-003'),(5,'CARTA','2026-05-01','COMPLETATO',50.00,'FATT-2026-004'),(6,'CARTA','2026-05-02','COMPLETATO',50.00,'FATT-2026-005'),(7,'CARTA','2026-05-03','COMPLETATO',50.00,'FATT-2026-006'),(8,'CARTA','2026-05-04','COMPLETATO',50.00,'FATT-2026-007'),(9,'CARTA','2026-05-05','COMPLETATO',50.00,'FATT-2026-008'),(10,'CARTA','2026-05-06','COMPLETATO',45.00,'FATT-2026-009'),(11,'CARTA','2026-05-07','COMPLETATO',45.00,'FATT-2026-010');
/*!40000 ALTER TABLE `PAGAMENTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PARTECIPAZIONE_SQ`
--

DROP TABLE IF EXISTS `PARTECIPAZIONE_SQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PARTECIPAZIONE_SQ` (
  `Id_Squadra` int NOT NULL,
  `Codice_Att` int NOT NULL,
  PRIMARY KEY (`Id_Squadra`,`Codice_Att`),
  KEY `Codice_Att` (`Codice_Att`),
  CONSTRAINT `PARTECIPAZIONE_SQ_ibfk_1` FOREIGN KEY (`Id_Squadra`) REFERENCES `SQUADRA` (`Id_Squadra`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PARTECIPAZIONE_SQ_ibfk_2` FOREIGN KEY (`Codice_Att`) REFERENCES `ATTIVITA` (`Codice_Att`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PARTECIPAZIONE_SQ`
--

LOCK TABLES `PARTECIPAZIONE_SQ` WRITE;
/*!40000 ALTER TABLE `PARTECIPAZIONE_SQ` DISABLE KEYS */;
INSERT INTO `PARTECIPAZIONE_SQ` VALUES (3,2),(1,3);
/*!40000 ALTER TABLE `PARTECIPAZIONE_SQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SOTTOSCRIVE`
--

DROP TABLE IF EXISTS `SOTTOSCRIVE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SOTTOSCRIVE` (
  `Numero_Abb` int NOT NULL,
  `Id_Pagamento` int NOT NULL,
  `Cf` char(16) NOT NULL,
  PRIMARY KEY (`Numero_Abb`,`Id_Pagamento`,`Cf`),
  KEY `Id_Pagamento` (`Id_Pagamento`),
  KEY `Cf` (`Cf`),
  CONSTRAINT `SOTTOSCRIVE_ibfk_1` FOREIGN KEY (`Numero_Abb`) REFERENCES `ABBONAMENTO` (`Numero_Abb`) ON UPDATE CASCADE,
  CONSTRAINT `SOTTOSCRIVE_ibfk_2` FOREIGN KEY (`Id_Pagamento`) REFERENCES `PAGAMENTO` (`Id_Pagamento`) ON UPDATE CASCADE,
  CONSTRAINT `SOTTOSCRIVE_ibfk_3` FOREIGN KEY (`Cf`) REFERENCES `ATLETA` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SOTTOSCRIVE`
--

LOCK TABLES `SOTTOSCRIVE` WRITE;
/*!40000 ALTER TABLE `SOTTOSCRIVE` DISABLE KEYS */;
INSERT INTO `SOTTOSCRIVE` VALUES (1,1,'BGLLDA95C25G273T'),(2,2,'GLLCHR98T52L736S'),(3,3,'MRRMRC00D18D969R'),(4,4,'CNIGLI94S51G337K');
/*!40000 ALTER TABLE `SOTTOSCRIVE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SPONSOR`
--

DROP TABLE IF EXISTS `SPONSOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPONSOR` (
  `P_IVA` char(11) NOT NULL,
  `Azienda` varchar(100) DEFAULT NULL,
  `Contatto` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`P_IVA`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPONSOR`
--

LOCK TABLES `SPONSOR` WRITE;
/*!40000 ALTER TABLE `SPONSOR` DISABLE KEYS */;
/*!40000 ALTER TABLE `SPONSOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SPONSORIZZAZIONE`
--

DROP TABLE IF EXISTS `SPONSORIZZAZIONE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPONSORIZZAZIONE` (
  `Id_Sponsorizzazione` int NOT NULL AUTO_INCREMENT,
  `P_IVA` char(11) DEFAULT NULL,
  `Id_Squadra` int DEFAULT NULL,
  `Id_Impianto` int DEFAULT NULL,
  `Data_Inizio` date DEFAULT NULL,
  `Data_Fine` date DEFAULT NULL,
  `Importo` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`Id_Sponsorizzazione`),
  KEY `P_IVA` (`P_IVA`),
  KEY `Id_Squadra` (`Id_Squadra`),
  KEY `Id_Impianto` (`Id_Impianto`),
  CONSTRAINT `SPONSORIZZAZIONE_ibfk_1` FOREIGN KEY (`P_IVA`) REFERENCES `SPONSOR` (`P_IVA`) ON UPDATE CASCADE,
  CONSTRAINT `SPONSORIZZAZIONE_ibfk_2` FOREIGN KEY (`Id_Squadra`) REFERENCES `SQUADRA` (`Id_Squadra`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SPONSORIZZAZIONE_ibfk_3` FOREIGN KEY (`Id_Impianto`) REFERENCES `IMPIANTO` (`Id_Impianto`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPONSORIZZAZIONE`
--

LOCK TABLES `SPONSORIZZAZIONE` WRITE;
/*!40000 ALTER TABLE `SPONSORIZZAZIONE` DISABLE KEYS */;
/*!40000 ALTER TABLE `SPONSORIZZAZIONE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SQUADRA`
--

DROP TABLE IF EXISTS `SQUADRA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SQUADRA` (
  `Id_Squadra` int NOT NULL AUTO_INCREMENT,
  `Nome_Sq` varchar(100) DEFAULT NULL,
  `Sport` varchar(50) DEFAULT NULL,
  `Campionato` varchar(100) DEFAULT NULL,
  `Cf_Allenatore` char(16) DEFAULT NULL,
  PRIMARY KEY (`Id_Squadra`),
  KEY `Cf_Allenatore` (`Cf_Allenatore`),
  CONSTRAINT `SQUADRA_ibfk_1` FOREIGN KEY (`Cf_Allenatore`) REFERENCES `ALLENATORE` (`Cf`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SQUADRA`
--

LOCK TABLES `SQUADRA` WRITE;
/*!40000 ALTER TABLE `SQUADRA` DISABLE KEYS */;
INSERT INTO `SQUADRA` VALUES (1,'Polisportiva Basket','Basket','Serie D','PRTGCM75B15A944V'),(2,'Polisportiva Volley','Pallavolo','Prima Divisione','PRTGCM75B15A944V'),(3,'Polisportiva Nuoto','Nuoto','Regionale','STRGNN78M08D612U');
/*!40000 ALTER TABLE `SQUADRA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USA_ABB`
--

DROP TABLE IF EXISTS `USA_ABB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USA_ABB` (
  `Numero_Abb` int NOT NULL,
  `Codice_Att` int NOT NULL,
  `Cf` char(16) NOT NULL,
  `Data_Uso` date NOT NULL,
  `Qr_Code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Numero_Abb`,`Codice_Att`,`Cf`,`Data_Uso`),
  KEY `Codice_Att` (`Codice_Att`),
  KEY `Cf` (`Cf`),
  CONSTRAINT `USA_ABB_ibfk_1` FOREIGN KEY (`Numero_Abb`) REFERENCES `ABBONAMENTO` (`Numero_Abb`) ON UPDATE CASCADE,
  CONSTRAINT `USA_ABB_ibfk_2` FOREIGN KEY (`Codice_Att`) REFERENCES `ATTIVITA` (`Codice_Att`) ON UPDATE CASCADE,
  CONSTRAINT `USA_ABB_ibfk_3` FOREIGN KEY (`Cf`) REFERENCES `ATLETA` (`Cf`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USA_ABB`
--

LOCK TABLES `USA_ABB` WRITE;
/*!40000 ALTER TABLE `USA_ABB` DISABLE KEYS */;
INSERT INTO `USA_ABB` VALUES (1,5,'BGLLDA95C25G273T','2026-06-13','QR-USE-001'),(2,6,'GLLCHR98T52L736S','2026-06-14','QR-USE-002'),(4,9,'CNIGLI94S51G337K','2026-06-18','QR-USE-003');
/*!40000 ALTER TABLE `USA_ABB` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UTENTE`
--

DROP TABLE IF EXISTS `UTENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UTENTE` (
  `Cf` char(16) NOT NULL,
  `Nome` varchar(50) DEFAULT NULL,
  `Cognome` varchar(50) DEFAULT NULL,
  `Genere` varchar(10) DEFAULT NULL,
  `Data_Nascita` date DEFAULT NULL,
  `Citta_Residenza` varchar(50) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Username` varchar(30) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Tipo_Iscritto` varchar(30) DEFAULT NULL,
  `Stipendio` decimal(10,2) DEFAULT NULL,
  `Punti_Gamification` int DEFAULT '0',
  PRIMARY KEY (`Cf`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UTENTE`
--

LOCK TABLES `UTENTE` WRITE;
/*!40000 ALTER TABLE `UTENTE` DISABLE KEYS */;
INSERT INTO `UTENTE` VALUES ('BGESMN03B28B157L','Simone','Beige','M','2003-02-28','Brescia','password','atl9','atl9@pol.it','ATLETA',NULL,0),('BGLLDA95C25G273T','Aldo','Baglio','M','1995-03-25','Palermo','password','atl1','atl1@pol.it','ATLETA',NULL,0),('BNCLGU85E10F205X','Luigi','Bianchi','M','1985-05-10','Milano','password','istr1','istr1@pol.it','ISTRUTTORE',NULL,0),('CNIGLI94S51G337K','Giulia','Ciano','F','1994-11-11','Parma','password','atl10','atl10@pol.it','ATLETA',NULL,0),('GLLCHR98T52L736S','Chiara','Gialli','F','1998-12-12','Venezia','password','atl2','atl2@pol.it','ATLETA',NULL,0),('GRGLCU96H20C351P','Luca','Grigi','M','1996-06-20','Catania','password','atl5','atl5@pol.it','ATLETA',NULL,0),('LLLNDR97E08L424M','Andrea','Lilla','M','1997-05-08','Trieste','password','atl8','atl8@pol.it','ATLETA',NULL,0),('MRRMRC00D18D969R','Marco','Marroni','M','2000-04-18','Genova','password','atl3','atl3@pol.it','ATLETA',NULL,0),('NREPLA88S30F839W','Paolo','Neri','M','1988-11-30','Napoli','password','istr3','istr3@pol.it','ISTRUTTORE',NULL,0),('PRTGCM75B15A944V','Giacomo','Poretti','M','1975-02-15','Bologna','password','all1','all1@pol.it','ALLENATORE',NULL,0),('RSOMRT01R55G224N','Marta','Rosa','F','2001-10-15','Padova','password','atl7','atl7@pol.it','ATLETA',NULL,0),('RSSMRA80A01H501Z','Mario','Rossi','M','1980-01-01','Roma','password','admin','admin@pol.it','ADMIN',NULL,0),('STRGNN78M08D612U','Giovanni','Storti','M','1978-08-08','Firenze','password','all2','all2@pol.it','ALLENATORE',NULL,0),('VLASRA02P45A662Q','Sara','Viola','F','2002-09-05','Bari','password','atl4','atl4@pol.it','ATLETA',NULL,0),('VRDNNA90L62L219Y','Anna','Verdi','F','1990-07-22','Torino','password','istr2','istr2@pol.it','ISTRUTTORE',NULL,0),('ZZRLNE99A70L781O','Elena','Azzurri','F','1999-01-30','Verona','password','atl6','atl6@pol.it','ATLETA',NULL,0);
/*!40000 ALTER TABLE `UTENTE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-25 13:34:55
