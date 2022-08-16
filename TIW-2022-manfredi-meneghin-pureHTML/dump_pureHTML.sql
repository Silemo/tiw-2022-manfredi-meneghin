DROP DATABASE IF EXISTS `bankDB`;
CREATE DATABASE  IF NOT EXISTS `bankDB` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bankDB`;
-- MySQL dump 10.13  Distrib 8.0.28, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: bankDB
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `code` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL,
  `balance` decimal(15,2) unsigned NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`code`),
  CONSTRAINT `account_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE ON DELETE NO ACTION,
  CONSTRAINT `account_balance_ck` CHECK (`balance` >= 0)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,1,1000.05),(2,2,2000.99),(3,3,1000000.85),(4,3,2000000.95),(5,1,285.60);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer`
--

DROP TABLE IF EXISTS `transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transfer` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp,
  `account_code_orderer` int unsigned NOT NULL,
  `account_code_beneficiary` int unsigned NOT NULL,
  `amount` decimal(15,2) unsigned NOT NULL,
  `reason` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `transfer_account_code_beneficiary_fk` FOREIGN KEY (`account_code_beneficiary`) REFERENCES `account` (`code`) ON UPDATE CASCADE ON DELETE NO ACTION,
  CONSTRAINT `transfer_account_code_orderer_fk` FOREIGN KEY (`account_code_orderer`) REFERENCES `account` (`code`) ON UPDATE CASCADE ON DELETE NO ACTION,
  CONSTRAINT `transfer_amount_ck` CHECK (`amount` > 0)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer`
--

LOCK TABLES `transfer` WRITE;
/*!40000 ALTER TABLE `transfer` DISABLE KEYS */;
INSERT INTO `transfer` VALUES (1,'2022-03-03 00:05:55',1000.00,'project funding',3,1),(2,'2022-04-05 10:35:01',500.00,'cash',5,2);
/*!40000 ALTER TABLE `transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(320) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_username_un` UNIQUE (`username`),
  CONSTRAINT `user_email_un` UNIQUE (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Giovanni','Manfredi','giovanni@polimi.it','Gio','passwordDifficile23'),(2,'Sebastiano','Meneghin','sebastiano@polimi.it','Seba','passwordDifficile25'),(3,'Jeff','Bezos','boss@amazon.com','Jeffrey','youCanDoIt');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-11 18:49:07
