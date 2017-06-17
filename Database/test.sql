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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract`
--

LOCK TABLES `t_contract` WRITE;
/*!40000 ALTER TABLE `t_contract` DISABLE KEYS */;
INSERT INTO `t_contract` VALUES (1,'2017061605125856040','1',2,'YYY','123','2017-06-07','2017-06-08',0),(2,'2017061701203450489','123',2,'YYY','121244','2017-06-07','2017-06-08',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_process`
--

LOCK TABLES `t_contract_process` WRITE;
/*!40000 ALTER TABLE `t_contract_process` DISABLE KEYS */;
INSERT INTO `t_contract_process` VALUES (1,1,3,1,1,'1324','2017-06-16 09:25:35',0),(2,1,4,2,0,'','2017-06-16 09:24:15',0),(3,1,5,3,0,'','2017-06-16 09:24:15',0),(4,2,3,1,1,'12324','2017-06-17 05:21:14',0),(5,2,4,2,0,'','2017-06-17 05:21:02',0),(6,2,5,3,0,'','2017-06-17 05:21:02',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contract_state`
--

LOCK TABLES `t_contract_state` WRITE;
/*!40000 ALTER TABLE `t_contract_state` DISABLE KEYS */;
INSERT INTO `t_contract_state` VALUES (1,1,1,'2017-06-16 09:12:58',0),(2,2,1,'2017-06-17 05:20:34',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
INSERT INTO `t_log` VALUES (1,2,'2017-06-15 21:11:42','User2insert data into t_contract',0),(2,2,'2017-06-15 21:12:58','User2insert data into t_contract',0),(3,NULL,'2017-06-15 21:12:58','User insert data into t_contract_state',0),(4,3,'2017-06-15 21:15:33','User3insert data into t_contract',0),(5,4,'2017-06-15 21:15:36','User4insert data into t_contract',0),(6,5,'2017-06-15 21:15:38','User5insert data into t_contract',0),(7,3,'2017-06-15 21:24:15','User3insert data into t_contract_process',0),(8,4,'2017-06-15 21:24:15','User4insert data into t_contract_process',0),(9,5,'2017-06-15 21:24:15','User5insert data into t_contract_process',0),(10,2,'2017-06-16 17:20:34','User2insert data into t_contract',0),(11,NULL,'2017-06-16 17:20:34','User insert data into t_contract_state',0),(12,3,'2017-06-16 17:21:02','User3insert data into t_contract_process',0),(13,4,'2017-06-16 17:21:02','User4insert data into t_contract_process',0),(14,5,'2017-06-16 17:21:02','User5insert data into t_contract_process',0);
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

-- Dump completed on 2017-06-17 22:34:47
