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
-- Table structure for table `trees`
--

DROP TABLE IF EXISTS `trees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trees` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `cost` bigint DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `description` text,
  `count` bigint DEFAULT NULL,
  `type_id` bigint DEFAULT NULL,
  `created` bigint DEFAULT NULL,
  `updated` bigint DEFAULT NULL,
  `images` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trees`
--

LOCK TABLES `trees` WRITE;
/*!40000 ALTER TABLE `trees` DISABLE KEYS */;
INSERT INTO `trees` VALUES (1,'CODE_T1','Cây đào bonsai',450000,0,'Cây đào đẹp',100,1,1676990547909,1676990547909,'[\"/resource/images/daobonsai.jpg\", \"/resource/images/daobonsai.jpg\"]'),(2,'CODE_T2','Cấy mai bonsai',500000,15,'Cây mai đẹp',100,2,1676990547909,1676990547909,'[\"/resource/images/maibonsai.jpg\"]'),(3,'CODE_T3','Cây táo bonsai',600000,20,'Cây táo đẹp',100,3,1676990547909,1676990547909,'[\"/resource/images/taobonsai.jpg\"]'),(4,'CODE_T4','Cây bưởi bonsai',700000,25,'Cây bưởi đẹp',100,4,1676990547909,1676990547909,'[\"/resource/images/buoibonsai.jpg\"]'),(5,'CODE_T5','Cây quất bonsai',800000,30,'Cây quất đẹp',0,5,1676990547909,1676990547909,'[\"/resource/images/quatbonsai.jpg\"]');
/*!40000 ALTER TABLE `trees` ENABLE KEYS */;
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
