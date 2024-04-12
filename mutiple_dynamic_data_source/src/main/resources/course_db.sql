/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : course_db

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2024-04-12 15:06:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for course_1
-- ----------------------------
DROP TABLE IF EXISTS `course_1`;
CREATE TABLE `course_1` (
  `cid` bigint(20) NOT NULL,
  `cname` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `cstatus` varchar(10) NOT NULL,
  `date` bigint(20) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of course_1
-- ----------------------------
INSERT INTO `course_1` VALUES ('767434211391963136', 'java_1', '100', 'Normal2', '1');
INSERT INTO `course_1` VALUES ('767434212339875840', 'JS_1', '100', 'Normal4', '2');
INSERT INTO `course_1` VALUES ('767434213157765120', 'C++_1', '100', 'Normal6', '3');
INSERT INTO `course_1` VALUES ('767434214000820224', 'GO_1', '100', 'Normal8', '4');

SET FOREIGN_KEY_CHECKS=1;
