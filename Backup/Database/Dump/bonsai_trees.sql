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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trees`
--

LOCK TABLES `trees` WRITE;
/*!40000 ALTER TABLE `trees` DISABLE KEYS */;
INSERT INTO `trees` VALUES (10,'TREE76e9E1','Cây đào D1',400000,10,'Đào bonsai loại 1',100,1,1677396332901,1677423081149,'[\"/resource/images/8cc941a8-9fbb-44b1-b12c-04719d5cf640\",\"/resource/images/dao13.jpg\"]'),(11,'TREE0144CF','Cây đào D2',450000,10,'Đào bonsai loại 2',100,1,1677396665529,1677408457759,'[\"/resource/images/0c180dd3-5370-42a3-8b24-8cd5d4fb9529\"]'),(12,'TREED4434C','Cây đào D3',450000,10,'Đào bonsai loại 3',100,1,1677396727464,1677408649110,'[\"/resource/images/97db28b1-f167-4509-9037-64d282cc1ca2\"]'),(13,'TREE03576F','Cây đào D4',600000,10,'Đào bonsai loại 4',100,1,1677397273052,1677408803418,'[\"/resource/images/e692cb84-b7da-4520-985f-50ba42ed60a4\"]'),(14,'TREEC75B8E','Cây đào D5',700000,10,'Đào bonsai loại 5',100,1,1677397312730,1677397312730,'[\"/resource/images/37a92087-0a16-466d-ab35-75cfac54e86a\"]'),(15,'TREE14E07B','Cây đào D6',900000,10,'Đào bonsai loại 6',100,1,1677397445533,1677397445533,'[\"/resource/images/5828f64b-3620-4ba0-af96-600980d3eda0\"]'),(16,'TREE3AB66F','Cây Mai M1',400000,15,'Mai bonsai loại 1',100,2,1677397515245,1677409441198,'[\"/resource/images/452e6d9e-0064-4e48-9001-1db98c3f8f24\"]'),(17,'TREE307C35','Cây Mai M2',500000,15,'Mai bonsai loại 2',100,2,1677397567422,1677409256564,'[\"/resource/images/a57175ba-83e5-4c0b-90db-39b0d42dd219\"]'),(18,'TREEC3EC57','Cây Mai M3',600000,15,'Mai bonsai loại 3',100,2,1677397598563,1677397598563,'[\"/resource/images/687c9a27-3394-41bc-9214-025aa8e43bf8\"]'),(19,'TREEDC149B','Cây Mai M4',700000,15,'Mai bonsai loại 4',100,2,1677397846780,1677397846780,'[\"/resource/images/4477a316-c478-40b3-b1a0-f73d06b93728\"]'),(20,'TREE84F438','Cây Mai M5',800000,15,'Mai bonsai loại 5',100,2,1677397895324,1677409286007,'[\"/resource/images/27606935-7727-445d-878c-70e69645d4d0\"]'),(21,'TREEF5BAF6','Cây Mai M6',900000,15,'Mai bonsai loại 6',100,2,1677397927029,1677409305387,'[\"/resource/images/4c6787e7-4f8a-42c5-b117-798f28969003\"]'),(22,'TREE5EE72A','Cây Táo T1',400000,20,'Táo bonsai loại 1',100,3,1677398302295,1677410584244,'[\"/resource/images/25e39d79-411b-4901-a6d5-9715c6008b45\"]'),(23,'TREE51CD13','Cây Táo T2',500000,20,'Táo bonsai loại 2',100,3,1677398341571,1677410600448,'[\"/resource/images/35ef4461-3dce-4bde-a7b8-b101afd84127\"]'),(24,'TREECFC697','Cây Táo T3',600000,20,'Táo bonsai loại 3',100,3,1677398385753,1677410606746,'[\"/resource/images/7c17440d-b6be-4c4e-a075-2a69e563f0cd\"]'),(25,'TREE40C5DE','Cây Táo T4',700000,20,'Táo bonsai loại 4',100,3,1677398428573,1677410713297,'[\"/resource/images/1b0f6360-707d-460d-885e-fe176646d45c\"]'),(26,'TREE7F8D67','Cây Táo T5',900000,20,'Táo bonsai loại 5',0,3,1677398470945,1677410975369,'[\"/resource/images/2a554f5e-d9c2-45e1-a341-6387435eab8d\"]'),(27,'TREE5FAB5A','Cây Bưởi B1',500000,25,'Bưởi bonsai loại 1',50,4,1677398552486,1677398552486,'[\"/resource/images/f2b49ef7-d7b5-4ae4-a49d-c495775fe42b\"]'),(28,'TREE2F2F26','Cây Bưởi B2',700000,25,'Bưởi bonsai loại 2',50,4,1677398585736,1677398585736,'[\"/resource/images/20021bea-34f4-4558-bec7-47c55a76ac36\"]'),(29,'TREE2C8744','Cây Bưởi B3',800000,25,'Bưởi bonsai loại 3',0,4,1677398618098,1677398618098,'[\"/resource/images/4cabf54d-ead6-4555-b8f9-c172229728a6\"]'),(30,'TREE95F73A','Cây Bưởi B4',850000,25,'Bưởi bonsai loại 4',60,4,1677398653522,1677398653522,'[\"/resource/images/5366ee22-df25-41b4-80f2-01616a92e6cd\"]'),(31,'TREE22762A','Cây Quất Q1',450000,30,'Quất bonsai loại 1',60,5,1677398717918,1677398717918,'[\"/resource/images/9e1dc5cf-6087-43ae-8e4b-7010a8fce7de\"]'),(32,'TREEE451C6','Cây Quất Q2',550000,30,'Quất bonsai loại 2',60,5,1677398751573,1677398751573,'[\"/resource/images/e940e548-824c-4b32-8e2e-4f8e006026ab\"]'),(33,'TREE464727','Cây Quất Q3',500000,30,'Quất bonsai loại 3',0,5,1677398791386,1677398791386,'[\"/resource/images/73f482f5-c5e6-4034-8a69-54b01fcff3f3\"]'),(34,'TREE07BAE4','Cây Quất Q4',700000,30,'Quất bonsai loại 4',70,5,1677398845543,1677398845543,'[\"/resource/images/b76dd28b-3a94-43fc-b6a8-f6f5f7a57dd0\"]');
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

-- Dump completed on 2023-02-26 22:54:46
