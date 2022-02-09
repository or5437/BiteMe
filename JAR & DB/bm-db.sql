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
-- Table structure for table `orders`
--
DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `orderNumber` int AUTO_INCREMENT,  
  `userType` enum('Business_Customer', 'Private_Customer'),
  `userIdNumber` int NULL,
  `phoneNumber` varchar(45) NULL,
  `orderConfirmDate` varchar(45) NULL,
  `orderConfirmHour` varchar(45) NULL,
  `orderDueDate` varchar(45) NULL,
  `orderDueHour` varchar(45) NULL,
  `orderAddress` varchar(45) NULL,
  `isPreOrder` boolean default 0,
  `branch` ENUM('North','Central','South') NULL,
  `restaurant` varchar(45) NULL,
  `finalPrice` float NULL,
  `typeOfOrder` varchar(45) NULL,
  `isConfirm` boolean default 0,
  PRIMARY KEY (`OrderNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `dish_in_order`
--
DROP TABLE IF EXISTS `dish_in_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish_in_order` (
  `dishInOrderId` int AUTO_INCREMENT,
  `orders_OrderNumber` int NULL,
  `branch` ENUM('North','Central','South') NULL,
  `restaurant` varchar(45) NULL,
  `item` varchar(45) NULL,
  `dish` varchar(45) NULL,
  `price` float NULL,
  `doneness` varchar(45) NULL,
  `size` varchar(45) NULL,
  `additionalRequest` varchar(45) NULL,
  `commission` float NULL,
   PRIMARY KEY (`dishInOrderId`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `restaurants`
--
DROP TABLE IF EXISTS `restaurants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
  CREATE TABLE `restaurants` (
  `restaurantId` int AUTO_INCREMENT,
  `restaurantName` varchar(45) NULL,
  `branch` ENUM('North','Central','South') NULL,
  `restaurantAddress` varchar(45) NULL,
  PRIMARY KEY (`restaurantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `restaurants`
--


--
-- Table structure for table `dishes`
--
DROP TABLE IF EXISTS `dishes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dishes` (
  `dishName` varchar(45) NOT NULL,
  `itemType` varchar(45) NULL, 
  `dishPrice` float NULL,
  `isDoneness` boolean default 0, 
  `isSize` boolean default 0, 
  `restaurants_restaurantId` int NOT NULL,
  KEY `fk1_restaurants_restaurantId` (`restaurants_restaurantId`),
  CONSTRAINT `fk1_restaurants_restaurantId` FOREIGN KEY (`restaurants_restaurantId`) REFERENCES `restaurants` (`restaurantId`),
  PRIMARY KEY (`dishName`, `restaurants_restaurantId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `shipments`
--
DROP TABLE IF EXISTS `shipments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipments` (
  `orders_OrderNumber` int NOT NULL,
  `shipmentType` enum ('Basic', 'Shared') NULL,
  `shipmentPrice` float NULL,
  `receiverAddress` varchar(45) NULL,
  `receiverName` varchar(45) NULL,
  `receiverPhone` varchar(45) NULL,
  `isRobot` boolean default 0,
   KEY `fk_orders_OrderNumber` (`orders_OrderNumber`),
   CONSTRAINT `fk_orders_OrderNumber` FOREIGN KEY (`orders_OrderNumber`) REFERENCES `orders` (`OrderNumber`),
   PRIMARY KEY (`orders_OrderNumber`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `import_users`
--
DROP TABLE IF EXISTS `import_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `import_users` (
  `userIdNumber` int NOT NULL,
  `firstName` varchar(45) NULL,
  `lastName` varchar(45) NULL,
  `email` varchar(45) NULL,
  `phoneNumber` varchar(45) NULL,
  `role` varchar(45) NULL,
  `isLoggedIn` BOOLEAN NULL,
  `userName` varchar(45) NULL,
  `password` varchar(45) NULL,
  `type` ENUM('BM_Manager','Supplier','CEO','HR_Manager','Business_Customer','Private_Customer','-') NULL,
  `status`ENUM('Active','Frozen','Locked') NULL,
   PRIMARY KEY (`userIdNumber`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `import_users`
--
LOCK TABLES `import_users` WRITE;
/*!40000 ALTER TABLE `import_users` DISABLE KEYS */;
INSERT INTO `import_users` VALUES 
(100,'Anat','Cohen','anat10@gmail.com','0509291294','CEO', 0, 'anat1', 'a1', 'CEO','Active'),
(101,'Raz','Avraham','razav33@gmail.com','0507641510','BM Manager North', 0,'raz1','r1','BM_Manager','Active'),
(102,'Dor','Levi','dorlevi@walla.com','0501234567','BM Manager Central', 0, 'dor1', 'd1', 'BM_Manager','Active'),
(103,'Niv','Yossef','niv55@gmail.com','0541234522','BM Manager South', 0, 'niv1', 'n1', 'BM_Manager','Active'),

(104,'Tal','Edri','taledri@walla.co.il','0501234523','HR Rafael North', 0, 'tal1', 't1', 'HR_Manager','Active'),
(105,'David','Peretz','davidper21@gmail.com','0546283947','HR Elbit North', 0, 'david1', 'd1', 'HR_Manager','Active'),
(106,'Shir','Malka','shirmalka@gmail.com','0552936794','HR Intel North', 0, 'shir1', 's1', 'HR_Manager','Active'),
(107,'Ofek','Dahan','ofek55@walla.co.il','0503461528','HR Rafael Central', 0, 'ofek1', 'o1', 'HR_Manager','Active'),
(108,'Shaked','Golan','shaked@gmail.com','0528650385','HR Elbit Central', 0, 'shaked1', 's1', 'HR_Manager','Active'),
(109,'Dan','Biton','danbiton1982@walla.co.il','0502856395','HR Intel Central', 0, 'dan1', 'd1', 'HR_Manager','Active'),
(110,'Noa','Malul','noa662@gmail.com','0542958243','HR Rafael South', 0, 'noa1', 'n1', 'HR_Manager','Active'),
(111,'Roni','Elimelech','liron12@walla.co.il','0509374892','HR Elbit South', 0, 'roni1', 'r1', 'HR_Manager','Active'),
(112,'Gil','Levin','gil31@walla.co.il','0509371234','HR Intel South', 0, 'gil1', 'g1', 'HR_Manager','Active'),

(113,'Ran','levi','ran1992@gmail.com','0544264495','Supplier BP North', 0, 'ran1', 'r1', '-','Active'),
(114,'Omer','Deri','omerderi@gmail.com','0542948439','Supplier Hahoza North ', 0, 'omer1', 'o1', '-','Active'),
(115,'Alon','Levy','alonlevy22@walla.co.il','0502942852','Supplier TYO North', 0, 'alon1', 'a1', '-','Active'),
(116,'Shai','Sabag','shaisabag@gmail.com','0524927453','Supplier BP Central', 0, 'shai1', 's1', '-','Active'),
(117,'Liron','Hayon','liron12@gmail.com','0501231891','Supplier Ruben Central', 0, 'liron1', 'l1', '-','Active'),
(118,'Maor','Peretz','maor99@walla.co.il','0548495835','Supplier Hahoza Central', 0, 'maor1', 'm1', '-','Active'),
(119,'Avi','Avitan','aviavi@gmail.com','0524938475','Supplier BP South', 0, 'avi1', 'a1', '-','Active'),
(120,'Shuval','Ohayon','shuvalohayon22@gmail.com','0585624782','Supplier Nash-Nash South', 0, 'shuval1', 's1', '-','Active'),
(121,'Roy','Malka','roymalka@gmail.com','0548374385','Supplier TYO South', 0, 'roy1', 'r1', '-','Active'),

(122,'Amit','Kempner','amitkemp10@gmail.com','0509291294','Customer North', 0, 'amit1', 'a1', '-','Active'),
(123,'Nevo','Rahamim','nevo33@walla.co.il','0528467388','Customer North', 0, 'nevo1', 'n1', '-','Active'),
(124,'Koral','Biton','koralb@gmail.com','0523829928','Customer North', 0, 'koral1', 'k1', '-','Active'),
(125,'Or','Hilo','or54377@gmail.com','0507909013','Customer Central', 0, 'or1', 'o1', '-','Active'),
(126,'Snir','Yehuda','sniryehuda@walla.co.il','0506643784','Customer Central', 0, 'snir1', 's1', '-','Active'),
(127,'Adi','Maman','adimaman1@gmail.com','0542338494','Customer Central', 0, 'adi1', 'a1', '-','Active'),
(128,'Dean','Otmazgin','dean1995@gmail.com','0523947539','Customer South', 0, 'dean1', 'd1', '-','Active'),
(129,'Nir','Cohen','nir1994@gmail.com','0542829483','Customer South', 0, 'nir1', 'n1', '-','Active'),
(130,'Lital','Yona','litalyona55@walla.co.il','0528339284','Customer South', 0, 'lital1', 'l1', '-','Active');
/*!40000 ALTER TABLE `import_users` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userIdNumber` int NOT NULL,
  `firstName` varchar(45) NULL,
  `lastName` varchar(45) NULL,
  `email` varchar(45) NULL,
  `phoneNumber` varchar(45) NULL,
  `role` varchar(45) NULL,
  `isLoggedIn` BOOLEAN NULL,
  `userName` varchar(45) NULL,
  `password` varchar(45) NULL,
  `type` ENUM('BM_Manager','Supplier','CEO','HR_Manager','Business_Customer','Private_Customer','-') NULL,
  `status`ENUM('Active','Frozen','Locked') NULL,
   PRIMARY KEY (`userIdNumber`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `private_w4c_card`
--
DROP TABLE IF EXISTS `private_w4c_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `private_w4c_card` (
  `userIdNumber` int NOT NULL,
  `privateCodeNumber` varchar(45) NULL,
  PRIMARY KEY (`userIdNumber`),
  KEY `fk5_userIdNumber` (`userIdNumber`),
  CONSTRAINT `fk5_userIdNumber` FOREIGN KEY (`userIdNumber`) REFERENCES `users` (`userIdNumber`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `employer_w4c_card`
--
DROP TABLE IF EXISTS `employer_w4c_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employer_w4c_card` (
  `businessName` varchar(45) NOT NULL,
  `employerCodeNumber` varchar(45) NULL,
  PRIMARY KEY (`businessName`),
  KEY `fk_businessName` (`businessName`),
  CONSTRAINT `fk_businessName` FOREIGN KEY (`businessName`) REFERENCES `registered_business` (`businessName`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `private_customers`
--
DROP TABLE IF EXISTS `private_customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `private_customers` (
  `userIdNumber` int NOT NULL,
  `firstName` varchar(45) NULL,
  `lastName` varchar(45) NULL,
  `email` varchar(45) NULL,
  `phoneNumber` varchar(45) NULL,
  `creditCard` varchar(45) NULL,
   PRIMARY KEY (`userIdNumber`),
   KEY `fk2_userIdNumber` (`userIdNumber`),
   CONSTRAINT `fk2_userIdNumber` FOREIGN KEY (`userIdNumber`) REFERENCES `users` (`userIdNumber`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `registered_business`
--
DROP TABLE IF EXISTS `registered_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registered_business` (
  `businessName` varchar(45) NOT NULL,
  `businessAddress` varchar(45) NULL,
  `businessPhone` varchar(45) NULL,
  `monthlyBudget` float NULL,
  PRIMARY KEY (`businessName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `business_customers`
--
DROP TABLE IF EXISTS `business_customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `business_customers` (
  `userIdNumber` int NOT NULL,
  `businessName` varchar(45) NULL,
  `firstName` varchar(45) NULL,
  `lastName` varchar(45) NULL,
  `email` varchar(45) NULL,
  `phoneNumber` varchar(45) NULL,
  `creditCard` varchar(45) NULL,
  `currentBudget` float NULL,
   PRIMARY KEY (`userIdNumber`),
   KEY `fk3_userIdNumber` (`userIdNumber`),
   CONSTRAINT `fk3_userIdNumber` FOREIGN KEY (`userIdNumber`) REFERENCES `users` (`userIdNumber`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `msg_to_supplier`
--
DROP TABLE IF EXISTS `msg_to_supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_to_supplier` (
  `msgId` int AUTO_INCREMENT,
  `orderId` int NOT NULL, 
  `fromUserId` int NULL,
  `toSupplierId` int NULL,
  `Date` varchar(45) NULL,
  `Hour` varchar(45) NULL,
  `Message` varchar(300)NULL,
  `status` ENUM('Waiting','Approved','Ready','Finish','-') default 'Waiting',
  `isRead` boolean DEFAULT 0,
   PRIMARY KEY (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `msg_to_customer`
--
DROP TABLE IF EXISTS `msg_to_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_to_customer` (
  `msgId` int AUTO_INCREMENT,  
  `orderId` int NOT NULL, 
  `fromSupplierId` int NULL,
  `toUserId` int NULL,
  `Date` varchar(45) NULL,
  `Hour` varchar(45) NULL,
  `Message` varchar(300)NULL,
  `status` ENUM('-','Waiting','Approved') default '-',
  `isRead` boolean DEFAULT 0,
   PRIMARY KEY (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `msg_to_manager`
--
DROP TABLE IF EXISTS `msg_to_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_to_manager` (
  `msgId` int AUTO_INCREMENT,  
  `fromHrId` int NULL,
  `toUserId` int NULL,
  `Date` varchar(45) NULL,
  `Hour` varchar(45) NULL,
  `Message` varchar(300)NULL,
  `status` ENUM('-','Waiting','Approved','Finish') default '-',
  `isRead` boolean DEFAULT 0,
   PRIMARY KEY (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `msg_to_hr`
--
DROP TABLE IF EXISTS `msg_to_hr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_to_hr` (
  `msgId` int AUTO_INCREMENT,  
  `fromManagerId` int NULL,
  `toUserId` int NULL,
  `Date` varchar(45) NULL,
  `Hour` varchar(45) NULL,
  `Message` varchar(300)NULL,
  `status` ENUM('-','Waiting','Approved','Finish') default '-',
  `isRead` boolean DEFAULT 0,
   PRIMARY KEY (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `msg_to_ceo`
--
DROP TABLE IF EXISTS `msg_to_ceo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_to_ceo` (
  `msgId` int AUTO_INCREMENT, 
  `fromManagerId` int NULL,
  `toUserId` int NULL,
  `Date` varchar(45) NULL,
  `Hour` varchar(45) NULL,
  `Message` varchar(300)NULL,
  `status` ENUM('-') default '-',
  `isRead` boolean DEFAULT 0,
   PRIMARY KEY (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `restaurants_restaurantId` int AUTO_INCREMENT,
  `userIdNumber` int NOT NULL,
   PRIMARY KEY (`restaurants_restaurantId`),
   KEY `fk2_restaurants_restaurantId` (`restaurants_restaurantId`),
   CONSTRAINT `fk2_restaurants_restaurantId` FOREIGN KEY (`restaurants_restaurantId`) REFERENCES `restaurants` (`restaurantId`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `refund`
--
DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund` ( 
  `userId` int NOT NULL, 
  `branch` varchar(45) NOT NULL,
  `restaurant` varchar(45) NOT NULL,
  `priceRefund` float NULL,
   PRIMARY KEY (`userId`,`branch`,`restaurant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `order_in_refund`
--
DROP TABLE IF EXISTS `order_in_refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_in_refund` ( 
  `orderId` int NOT NULL,
  `isRefund` boolean default 0,
   PRIMARY KEY (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `order_time_approve`
--
DROP TABLE IF EXISTS `order_time_approve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_time_approve` (
  `orderId` int NOT NULL, 
  `supplierApproveTime` varchar(45) NULL,
  `customerApproveTime` varchar(45) NULL,
  `customerApproveDate` varchar(45) NULL,
   PRIMARY KEY (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `waiting_business`
--
DROP TABLE IF EXISTS `waiting_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waiting_business` (
  `businessName` varchar(45) NOT NULL,
  `businessAddress` varchar(45) NULL,
  `businessPhone` varchar(45) NULL,
  `monthlyBudget` float NULL,
  `isConfirm` boolean default 0,
  PRIMARY KEY (`businessName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `waiting_business_customer`
--
DROP TABLE IF EXISTS `waiting_business_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waiting_business_customer` (
  `userIdNumber` int NOT NULL,
  `businessName` varchar(45) NULL,
  `firstName` varchar(45) NULL,
  `lastName` varchar(45) NULL,
  `email` varchar(45) NULL,
  `phoneNumber` varchar(45) NULL,
  `creditCard` varchar(45) NULL,
  `currentBudget` float NULL,
   `isConfirm` boolean default 0,
   PRIMARY KEY (`userIdNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `pdf_report`
--
DROP TABLE IF EXISTS `pdf_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pdf_report` (
`upload_file` LONGBLOB,
`branch` varchar(45) NOT NULL,
`quarter` varchar(45) NOT NULL,
PRIMARY KEY (`branch`,`quarter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;



/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;