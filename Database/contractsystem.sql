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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract`
--

LOCK TABLES `t_contract` WRITE;
/*!40000 ALTER TABLE `t_contract` DISABLE KEYS */;
INSERT INTO `t_contract` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(2,'2017061005113161419','hello',2,'you','呵呵哒！真是有趣！！aaa','2017-06-07','2017-06-08',0),(3,'2017061111493934859','MU',2,'you','12131','2017-06-07','2017-06-08',0),(4,'2017061212105513680','mu',2,'123','rnm','2017-06-07','2017-06-08',0),(5,'2017061212133718173','admin',2,'123','123','2017-06-07','2017-06-08',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_process`
--

LOCK TABLES `t_contract_process` WRITE;
/*!40000 ALTER TABLE `t_contract_process` DISABLE KEYS */;
INSERT INTO `t_contract_process` VALUES (1,2,2,1,1,'Agree!','2017-06-10 15:52:23',0),(2,2,2,2,1,'123','2017-06-10 16:11:58',0),(3,2,2,3,1,'123','2017-06-10 16:14:46',0),(4,3,2,1,1,'OK','2017-06-11 15:51:09',0),(5,3,2,2,1,'123456','2017-06-11 15:53:00',0),(6,3,2,3,1,'123456','2017-06-11 15:53:07',0),(7,4,2,1,1,'123','2017-06-11 16:12:00',0),(8,4,2,2,1,'123','2017-06-11 16:12:10',0),(9,4,2,3,1,'123','2017-06-11 16:12:15',0),(10,5,2,1,0,'','2017-06-11 16:14:01',0),(11,5,2,2,0,'','2017-06-11 16:14:01',0),(12,5,2,3,0,'','2017-06-11 16:14:01',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_state`
--

LOCK TABLES `t_contract_state` WRITE;
/*!40000 ALTER TABLE `t_contract_state` DISABLE KEYS */;
INSERT INTO `t_contract_state` VALUES (1,2,1,'2017-06-10 15:49:09',0),(2,2,2,'2017-06-10 15:54:58',0),(3,2,3,'2017-06-10 15:58:57',0),(4,2,4,'2017-06-10 15:58:57',0),(5,2,5,'2017-06-10 16:15:06',0),(6,3,1,'2017-06-11 15:49:39',0),(7,3,2,'2017-06-11 15:51:09',0),(8,3,3,'2017-06-11 15:52:39',0),(9,3,4,'2017-06-11 15:53:00',0),(10,3,5,'2017-06-11 15:53:06',0),(11,4,1,'2017-06-11 16:10:55',0),(12,4,2,'2017-06-11 16:11:59',0),(13,4,3,'2017-06-11 16:12:04',0),(14,4,4,'2017-06-11 16:12:09',0),(15,4,5,'2017-06-11 16:12:15',0),(16,5,1,'2017-06-11 16:13:37',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_customer`
--

LOCK TABLES `t_customer` WRITE;
/*!40000 ALTER TABLE `t_customer` DISABLE KEYS */;
INSERT INTO `t_customer` VALUES (1,'Cus20131211182300001','HB company','hubei wuhan','11111111111','12121212','430000','Bank Of China','621661***',0),(2,'Cus20131211182300002','BJ company','beijing','22222222','34213467','100000','Agricultural Bank of China','622848***',0),(3,'Cus20131211182300003','Jack Wang','Shanghai','14231116','45678234','200000','Industrial and Commercial Bank of China Limited','530990***',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_function`
--

LOCK TABLES `t_function` WRITE;
/*!40000 ALTER TABLE `t_function` DISABLE KEYS */;
INSERT INTO `t_function` VALUES (1,'001','QCHT',NULL,NULL,0),(2,'002','DGHT',NULL,NULL,0),(3,'003','CXHT',NULL,NULL,0),(4,'004','SCHT',NULL,NULL,0),(5,'005','HQHT',NULL,NULL,0),(6,'006','SPHT',NULL,NULL,0),(7,'007','QDHT',NULL,NULL,0),(8,'008','FPHQ',NULL,NULL,0),(9,'009','FPSP',NULL,NULL,0),(10,'010','FPQD',NULL,NULL,0),(11,'011','LCCX',NULL,NULL,0),(12,'012','XZYH',NULL,NULL,0),(13,'013','BJYH',NULL,NULL,0),(14,'014','CXYH',NULL,NULL,0),(15,'015','SCYH',NULL,NULL,0),(16,'016','XZJS',NULL,NULL,0),(17,'017','BJJS',NULL,NULL,0),(18,'018','CXJS',NULL,NULL,0),(19,'019','SCJS',NULL,NULL,0),(20,'020','XZGN',NULL,NULL,0),(21,'021','BJGN',NULL,NULL,0),(22,'022','CXGN',NULL,NULL,0),(23,'023','SCGN',NULL,NULL,0),(24,'024','PZQX',NULL,NULL,0),(25,'025','XZKH',NULL,NULL,0),(26,'026','BJKH',NULL,NULL,0),(27,'027','CXKH',NULL,NULL,0),(28,'028','SCKH',NULL,NULL,0),(29,'029','CXRZ',NULL,NULL,0),(30,'030','SCRZ',NULL,NULL,0);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_right`
--

LOCK TABLES `t_right` WRITE;
/*!40000 ALTER TABLE `t_right` DISABLE KEYS */;
INSERT INTO `t_right` VALUES (1,1,1,'admin',0),(2,2,2,'operator',0),(3,3,2,'operator',0),(4,4,2,'operator',0),(5,5,2,'operator',0),(6,7,2,'update',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` VALUES (1,'admin','To implement the system management and contract management','003,004,008,009,010,011,012,013,014,015,016,017,018, 019,020,021,022,023,024,025,026,027,028,029,030',0),(2,'operator','operate contract','001,002,003,005,006,007,011,027',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','123456','123456',NULL,NULL,0,NULL,0,0),(2,'tom','123456','123456',NULL,NULL,0,NULL,0,0),(3,'lucy','123456','123456',NULL,NULL,0,NULL,0,0),(4,'lily','123456','123456',NULL,NULL,0,NULL,0,0),(5,'jack','123456','123456',NULL,NULL,0,NULL,0,0),(6,'sanri','123456','123456',NULL,NULL,0,NULL,0,0),(7,'y','1',NULL,NULL,NULL,0,NULL,0,0),(8,'You','123456','','garciiaa@163.com','86eb18a9fcdf5f721549e8de813329bf',1497434538767,'Tue Jun 13 18:02:18 CST 2017',0,0);
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

-- Dump completed on 2017-06-13 21:47:59
