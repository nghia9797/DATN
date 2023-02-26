-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bonsai
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `sender` bigint DEFAULT NULL,
  `receiver` bigint DEFAULT NULL,
  `address` varchar(250) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  `typepay` int DEFAULT NULL,
  `feeship` bigint DEFAULT NULL,
  `created` bigint DEFAULT NULL,
  `updated` bigint DEFAULT NULL,
  `sender_name` varchar(200) DEFAULT NULL,
  `receiver_name` varchar(200) DEFAULT NULL,
  `amount` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (7,'BILLd9b577eb7398',0,NULL,8,'Ha noi','',0,100000,1677265220144,1677265220144,NULL,'user a',450000),(8,'BILLd96534f6fce2',0,NULL,8,'Ha noi','',0,100000,1677266220413,1677266220413,NULL,'user a',450000),(9,'BILL38e7bc155c34',0,NULL,8,'Ha noi','',0,100000,1677267461199,1677267461199,NULL,'user a',1275000),(10,'BILL5a096fdabf45',0,NULL,8,'Ha noi','',0,100000,1677267583523,1677267583523,NULL,'user a',450000),(11,'BILLf7579a536a5f',0,NULL,8,'Ha noi Ha noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa noiHa','aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',1,100000,1677268902732,1677268902732,NULL,'user a',1980000);
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-25  3:22:56
