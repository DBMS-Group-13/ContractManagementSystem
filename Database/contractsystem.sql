CREATE DATABASE  IF NOT EXISTS `contractsystem` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `contractsystem`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: contractsystem
-- ------------------------------------------------------
-- Server version	5.7.14-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_contract`
--

DROP TABLE IF EXISTS `t_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contract` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` varchar(20) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `customer` varchar(40) DEFAULT NULL,
  `content` text,
  `beginTime` date DEFAULT NULL,
  `endTime` date DEFAULT NULL,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract`
--

LOCK TABLES `t_contract` WRITE;
/*!40000 ALTER TABLE `t_contract` DISABLE KEYS */;
INSERT INTO `t_contract` VALUES (3,'2017061810170486916','1',2,'YYY','13214','2017-06-07','2017-06-08',0),(4,'2017061910452726002','1',2,'YYY','123214','2017-06-07','2017-06-08',0),(5,'2017061911063024885','2',2,'YYY','231','2017-06-07','2017-06-08',0),(6,'2017061911104054125','3',2,'YYY','1224','2017-06-07','2017-06-08',1),(7,'2017061911123293955','1',2,'YYY','141','2017-06-07','2017-06-08',0),(8,'2017061911384607667','12',2,'YYY','1341','2017-06-07','2017-06-08',0),(9,'2017061911385754536','1',2,'YYY','214123','2017-06-07','2017-06-08',0),(10,'2017061911390185089','1',2,'YYY','214','2017-06-07','2017-06-08',0),(11,'2017061911390511452','213',2,'YYY','214','2017-06-07','2017-06-08',0);
/*!40000 ALTER TABLE `t_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_contract_attachment`
--

DROP TABLE IF EXISTS `t_contract_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contract_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `con_id` int(11) DEFAULT NULL,
  `fileName` varchar(40) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `uploadTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_attachment`
--

LOCK TABLES `t_contract_attachment` WRITE;
/*!40000 ALTER TABLE `t_contract_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_contract_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_contract_process`
--

DROP TABLE IF EXISTS `t_contract_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contract_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `con_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `content` text,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_contract_p` (`con_id`),
  KEY `fk_operate` (`user_id`),
  CONSTRAINT `t_contract_process_ibfk_1` FOREIGN KEY (`con_id`) REFERENCES `t_contract` (`id`),
  CONSTRAINT `t_contract_process_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_process`
--

LOCK TABLES `t_contract_process` WRITE;
/*!40000 ALTER TABLE `t_contract_process` DISABLE KEYS */;
INSERT INTO `t_contract_process` VALUES (7,3,3,1,1,'12214','2017-06-18 14:17:38',0),(8,3,4,2,1,'1323','2017-06-18 14:20:59',0),(9,3,5,3,1,'124','2017-06-18 14:21:27',0),(10,4,2,1,1,'1224','2017-06-19 14:46:13',0),(11,4,2,2,1,'132','2017-06-19 15:10:02',0),(12,4,2,3,1,'123','2017-06-19 15:10:20',0),(13,5,2,1,1,'123','2017-06-19 15:08:51',0),(14,5,2,2,1,'123','2017-06-19 15:10:05',0),(15,5,2,3,1,'1312','2017-06-19 15:10:22',0),(16,6,2,1,1,'12','2017-06-19 15:11:08',0),(17,6,2,2,2,'123','2017-06-19 15:11:17',0),(18,6,2,3,0,'','2017-06-19 15:10:54',0),(19,7,2,1,1,'13','2017-06-19 15:13:03',0),(20,7,2,2,1,'13','2017-06-19 15:13:13',0),(21,7,2,3,0,'','2017-06-19 15:12:48',0),(22,8,2,1,1,'123','2017-06-19 15:42:20',0),(23,8,2,2,0,'','2017-06-19 15:40:15',0),(24,8,2,3,0,'','2017-06-19 15:40:15',0),(25,9,2,1,1,'13','2017-06-19 15:42:21',0),(26,9,3,1,1,'123','2017-06-19 15:42:51',0),(27,9,2,2,0,'','2017-06-19 15:41:52',0),(28,9,2,3,0,'','2017-06-19 15:41:52',0),(29,10,2,1,0,'','2017-06-19 15:59:22',0),(30,10,2,1,0,'','2017-06-19 15:59:23',0),(31,10,2,2,0,'','2017-06-19 15:59:24',0),(32,10,2,1,0,'','2017-06-19 15:59:25',0),(33,10,2,2,0,'','2017-06-19 15:59:25',0),(34,10,2,3,0,'','2017-06-19 15:59:25',0),(35,10,2,2,0,'','2017-06-19 15:59:26',0),(36,10,2,3,0,'','2017-06-19 15:59:26',0),(37,10,2,3,0,'','2017-06-19 15:59:26',0),(38,11,2,1,0,'','2017-06-19 16:00:41',0),(39,11,2,2,0,'','2017-06-19 16:00:41',0),(40,11,2,3,0,'','2017-06-19 16:00:42',0);
/*!40000 ALTER TABLE `t_contract_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_contract_state`
--

DROP TABLE IF EXISTS `t_contract_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_contract_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `con_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_belong` (`con_id`),
  CONSTRAINT `t_contract_state_ibfk_1` FOREIGN KEY (`con_id`) REFERENCES `t_contract` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_state`
--

LOCK TABLES `t_contract_state` WRITE;
/*!40000 ALTER TABLE `t_contract_state` DISABLE KEYS */;
INSERT INTO `t_contract_state` VALUES (3,3,1,'2017-06-18 14:17:04',0),(4,3,2,'2017-06-18 14:17:38',0),(5,3,3,'2017-06-18 14:20:45',0),(6,3,4,'2017-06-19 15:11:16',1),(7,3,5,'2017-06-18 14:21:27',0),(8,4,1,'2017-06-19 14:45:27',0),(9,4,2,'2017-06-19 14:46:12',0),(10,4,3,'2017-06-19 15:06:01',0),(11,5,1,'2017-06-19 15:06:30',0),(12,5,2,'2017-06-19 15:08:51',0),(13,5,3,'2017-06-19 15:08:56',0),(14,4,4,'2017-06-19 15:10:01',0),(15,5,4,'2017-06-19 15:10:05',0),(16,4,5,'2017-06-19 15:10:20',0),(17,5,5,'2017-06-19 15:10:21',0),(18,6,1,'2017-06-19 15:10:40',0),(19,6,2,'2017-06-19 15:11:08',0),(20,6,3,'2017-06-19 15:11:11',0),(21,7,1,'2017-06-19 15:12:32',0),(22,7,2,'2017-06-19 15:13:03',0),(23,7,3,'2017-06-19 15:13:07',0),(24,7,4,'2017-06-19 15:13:13',0),(25,8,1,'2017-06-19 15:38:46',0),(26,9,1,'2017-06-19 15:38:57',0),(27,10,1,'2017-06-19 15:39:01',0),(28,11,1,'2017-06-19 15:39:05',0),(29,8,2,'2017-06-19 15:42:19',0),(30,8,3,'2017-06-19 15:42:29',0),(31,9,2,'2017-06-19 15:42:50',0),(32,9,3,'2017-06-19 15:42:59',0);
/*!40000 ALTER TABLE `t_contract_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_customer`
--

DROP TABLE IF EXISTS `t_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` varchar(20) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `fax` varchar(20) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `bank` varchar(50) DEFAULT NULL,
  `account` varchar(50) DEFAULT NULL,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_customer`
--

LOCK TABLES `t_customer` WRITE;
/*!40000 ALTER TABLE `t_customer` DISABLE KEYS */;
INSERT INTO `t_customer` VALUES (1,'123456','YYY','BJTU','7777','7','77777','77',NULL,0);
/*!40000 ALTER TABLE `t_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_function`
--

DROP TABLE IF EXISTS `t_function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` varchar(10) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `URL` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_function`
--

LOCK TABLES `t_function` WRITE;
/*!40000 ALTER TABLE `t_function` DISABLE KEYS */;
INSERT INTO `t_function` VALUES (1,'001','SM',NULL,NULL,0),(2,'002','CM',NULL,NULL,0),(3,'003','DC',NULL,NULL,0),(4,'004','CC',NULL,NULL,0),(5,'005','AC',NULL,NULL,0),(6,'006','SC',NULL,NULL,0);
/*!40000 ALTER TABLE `t_function` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log`
--

DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` text,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` VALUES (1,2,'2017-06-15 21:11:42','User2insert data into t_contract',0),(2,2,'2017-06-15 21:12:58','User2insert data into t_contract',0),(3,NULL,'2017-06-15 21:12:58','User insert data into t_contract_state',0),(4,3,'2017-06-15 21:15:33','User3insert data into t_contract',0),(5,4,'2017-06-15 21:15:36','User4insert data into t_contract',0),(6,5,'2017-06-15 21:15:38','User5insert data into t_contract',0),(7,3,'2017-06-15 21:24:15','User3insert data into t_contract_process',0),(8,4,'2017-06-15 21:24:15','User4insert data into t_contract_process',0),(9,5,'2017-06-15 21:24:15','User5insert data into t_contract_process',0),(10,2,'2017-06-16 17:20:34','User2insert data into t_contract',0),(11,NULL,'2017-06-16 17:20:34','User insert data into t_contract_state',0),(12,3,'2017-06-16 17:21:02','User3insert data into t_contract_process',0),(13,4,'2017-06-16 17:21:02','User4insert data into t_contract_process',0),(14,5,'2017-06-16 17:21:02','User5insert data into t_contract_process',0),(15,2,'2017-06-18 02:17:04','User2insert data into t_contract',0),(16,NULL,'2017-06-18 02:17:04','User insert data into t_contract_state',0),(17,3,'2017-06-18 02:17:30','User3insert data into t_contract_process',0),(18,4,'2017-06-18 02:17:30','User4insert data into t_contract_process',0),(19,5,'2017-06-18 02:17:30','User5insert data into t_contract_process',0),(20,3,'2017-06-18 02:17:38','User3update data in t_contract_process',0),(21,NULL,'2017-06-18 02:17:38','User insert data into t_contract_state',0),(22,2,'2017-06-18 02:20:45','User2update data in t_contract',0),(23,NULL,'2017-06-18 02:20:45','User insert data into t_contract_state',0),(24,4,'2017-06-18 02:20:58','User4update data in t_contract_process',0),(25,NULL,'2017-06-18 02:20:59','User insert data into t_contract_state',0),(26,5,'2017-06-18 02:21:27','User5update data in t_contract_process',0),(27,NULL,'2017-06-18 02:21:27','User insert data into t_contract_state',0),(28,2,'2017-06-19 02:45:27','User2insert data into t_contract',0),(29,NULL,'2017-06-19 02:45:27','User insert data into t_contract_state',0),(30,2,'2017-06-19 02:45:56','User2insert data into t_contract_process',0),(31,2,'2017-06-19 02:45:56','User2insert data into t_contract_process',0),(32,2,'2017-06-19 02:45:56','User2insert data into t_contract_process',0),(33,2,'2017-06-19 02:46:12','User2update data in t_contract_process',0),(34,NULL,'2017-06-19 02:46:12','User insert data into t_contract_state',0),(35,2,'2017-06-19 03:06:01','User2update data in t_contract',0),(36,NULL,'2017-06-19 03:06:01','User insert data into t_contract_state',0),(37,2,'2017-06-19 03:06:30','User2insert data into t_contract',0),(38,NULL,'2017-06-19 03:06:30','User insert data into t_contract_state',0),(39,2,'2017-06-19 03:07:53','User2insert data into t_contract_process',0),(40,2,'2017-06-19 03:07:53','User2insert data into t_contract_process',0),(41,2,'2017-06-19 03:07:53','User2insert data into t_contract_process',0),(42,2,'2017-06-19 03:08:51','User2update data in t_contract_process',0),(43,NULL,'2017-06-19 03:08:51','User insert data into t_contract_state',0),(44,2,'2017-06-19 03:08:56','User2update data in t_contract',0),(45,NULL,'2017-06-19 03:08:56','User insert data into t_contract_state',0),(46,2,'2017-06-19 03:10:01','User2update data in t_contract_process',0),(47,NULL,'2017-06-19 03:10:01','User insert data into t_contract_state',0),(48,2,'2017-06-19 03:10:05','User2update data in t_contract_process',0),(49,NULL,'2017-06-19 03:10:05','User insert data into t_contract_state',0),(50,2,'2017-06-19 03:10:20','User2update data in t_contract_process',0),(51,NULL,'2017-06-19 03:10:20','User insert data into t_contract_state',0),(52,2,'2017-06-19 03:10:21','User2update data in t_contract_process',0),(53,NULL,'2017-06-19 03:10:21','User insert data into t_contract_state',0),(54,2,'2017-06-19 03:10:40','User2insert data into t_contract',0),(55,NULL,'2017-06-19 03:10:40','User insert data into t_contract_state',0),(56,2,'2017-06-19 03:10:54','User2insert data into t_contract_process',0),(57,2,'2017-06-19 03:10:54','User2insert data into t_contract_process',0),(58,2,'2017-06-19 03:10:54','User2insert data into t_contract_process',0),(59,2,'2017-06-19 03:11:08','User2update data in t_contract_process',0),(60,NULL,'2017-06-19 03:11:08','User insert data into t_contract_state',0),(61,2,'2017-06-19 03:11:11','User2update data in t_contract',0),(62,NULL,'2017-06-19 03:11:11','User insert data into t_contract_state',0),(63,2,'2017-06-19 03:11:16','User2update data in t_contract_process',0),(64,NULL,'2017-06-19 03:11:16','User update data in t_contract,t_contract_attachment,t_contract_process,t_contract_state',0),(65,2,'2017-06-19 03:12:32','User2insert data into t_contract',0),(66,NULL,'2017-06-19 03:12:32','User insert data into t_contract_state',0),(67,2,'2017-06-19 03:12:48','User2insert data into t_contract_process',0),(68,2,'2017-06-19 03:12:48','User2insert data into t_contract_process',0),(69,2,'2017-06-19 03:12:48','User2insert data into t_contract_process',0),(70,2,'2017-06-19 03:13:03','User2update data in t_contract_process',0),(71,NULL,'2017-06-19 03:13:03','User insert data into t_contract_state',0),(72,2,'2017-06-19 03:13:07','User2update data in t_contract',0),(73,NULL,'2017-06-19 03:13:07','User insert data into t_contract_state',0),(74,2,'2017-06-19 03:13:13','User2update data in t_contract_process',0),(75,NULL,'2017-06-19 03:13:13','User insert data into t_contract_state',0),(76,2,'2017-06-19 03:38:46','User2insert data into t_contract',0),(77,NULL,'2017-06-19 03:38:46','User insert data into t_contract_state',0),(78,2,'2017-06-19 03:38:57','User2insert data into t_contract',0),(79,NULL,'2017-06-19 03:38:57','User insert data into t_contract_state',0),(80,2,'2017-06-19 03:39:01','User2insert data into t_contract',0),(81,NULL,'2017-06-19 03:39:01','User insert data into t_contract_state',0),(82,2,'2017-06-19 03:39:05','User2insert data into t_contract',0),(83,NULL,'2017-06-19 03:39:05','User insert data into t_contract_state',0),(84,2,'2017-06-19 03:40:15','User2insert data into t_contract_process',0),(85,2,'2017-06-19 03:40:15','User2insert data into t_contract_process',0),(86,2,'2017-06-19 03:40:15','User2insert data into t_contract_process',0),(87,2,'2017-06-19 03:41:52','User2insert data into t_contract_process',0),(88,3,'2017-06-19 03:41:52','User3insert data into t_contract_process',0),(89,2,'2017-06-19 03:41:52','User2insert data into t_contract_process',0),(90,2,'2017-06-19 03:41:52','User2insert data into t_contract_process',0),(91,2,'2017-06-19 03:42:19','User2update data in t_contract_process',0),(92,NULL,'2017-06-19 03:42:19','User insert data into t_contract_state',0),(93,2,'2017-06-19 03:42:21','User2update data in t_contract_process',0),(94,2,'2017-06-19 03:42:29','User2update data in t_contract',0),(95,NULL,'2017-06-19 03:42:29','User insert data into t_contract_state',0),(96,3,'2017-06-19 03:42:50','User3update data in t_contract_process',0),(97,NULL,'2017-06-19 03:42:50','User insert data into t_contract_state',0),(98,2,'2017-06-19 03:42:59','User2update data in t_contract',0),(99,NULL,'2017-06-19 03:42:59','User insert data into t_contract_state',0),(100,2,'2017-06-19 03:59:22','User2insert data into t_contract_process',0),(101,2,'2017-06-19 03:59:23','User2insert data into t_contract_process',0),(102,2,'2017-06-19 03:59:24','User2insert data into t_contract_process',0),(103,2,'2017-06-19 03:59:25','User2insert data into t_contract_process',0),(104,2,'2017-06-19 03:59:25','User2insert data into t_contract_process',0),(105,2,'2017-06-19 03:59:25','User2insert data into t_contract_process',0),(106,2,'2017-06-19 03:59:26','User2insert data into t_contract_process',0),(107,2,'2017-06-19 03:59:26','User2insert data into t_contract_process',0),(108,2,'2017-06-19 03:59:26','User2insert data into t_contract_process',0),(109,2,'2017-06-20 04:00:41','User2insert data into t_contract_process',0),(110,2,'2017-06-20 04:00:41','User2insert data into t_contract_process',0),(111,2,'2017-06-20 04:00:42','User2insert data into t_contract_process',0);
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_right`
--

DROP TABLE IF EXISTS `t_right`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_right` (`user_id`),
  KEY `fk_right_r` (`role_id`),
  CONSTRAINT `t_right_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `t_right_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_right`
--

LOCK TABLES `t_right` WRITE;
/*!40000 ALTER TABLE `t_right` DISABLE KEYS */;
INSERT INTO `t_right` VALUES (1,1,1,'admin',0),(2,2,2,'',0),(3,3,4,'',0),(4,4,5,'',0),(5,5,6,'',0);
/*!40000 ALTER TABLE `t_right` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `function_ids` varchar(500) DEFAULT NULL,
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` VALUES (1,'admin','To implement the system management and contract management','001,002',0),(2,'operator','operate contract','003,004,005,006',0),(3,'operator','123','003',0),(4,'operator','','004',0),(5,'operator','','005',0),(6,'operator','','006',0);
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `sec_password` varchar(20) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `token` varchar(50) DEFAULT NULL,
  `activatetime` bigint(10) DEFAULT '0',
  `createdate` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `del` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','123456','123456','garcia@163.com','9a573e224ba32cb1c886c02c59dbc9d6',1497690425492,'Fri Jun 16 17:07:05 CST 2017',1,0),(2,'You','123456','','garciiaa@163.com','7d6c292cc5da9eb0cec7b477b3af2387',1,'Fri Jun 16 17:10:01 CST 2017',1,0),(3,'C','123456','','garciia@163.com','7d6c292cc5da9eb0cec7b477b3af2387',1,'Fri Jun 16 17:10:01 CST 2017',1,0),(4,'A','123456','','garcii@163.com','7d6c292cc5da9eb0cec7b477b3af2387',1,'Fri Jun 16 17:10:01 CST 2017',1,0),(5,'S','123456','','garci@163.com','7d6c292cc5da9eb0cec7b477b3af2387',1,'Fri Jun 16 17:10:01 CST 2017',1,0);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'contractsystem'
--

--
-- Dumping routines for database 'contractsystem'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-20  0:02:55
