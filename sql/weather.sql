/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80040
Source Host           : localhost:3306
Source Database       : oa_database

Target Server Type    : MYSQL
Target Server Version : 80040
File Encoding         : 65001

Date: 2024-12-19 20:26:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for weather
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(100) DEFAULT NULL,
  `temperature` varchar(10) DEFAULT NULL,
  `humidity` varchar(10) DEFAULT NULL,
  `wind` varchar(50) DEFAULT NULL,
  `creator_id` varchar(50) DEFAULT NULL,
  `creator` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of weather
-- ----------------------------
INSERT INTO `weather` VALUES ('12', '成都', '5~10', '多云', '1级', '60065', '桑郎平措', null, '2024-12-19');
INSERT INTO `weather` VALUES ('13', '成都', '5~10', '阴', '1级', '60065', '桑郎平措', null, '2024-12-18');
INSERT INTO `weather` VALUES ('14', '成都', '5~10', '晴', '1级', '60065', '桑郎平措', null, '2024-12-17');
INSERT INTO `weather` VALUES ('15', '成都', '5~10', '多雨', '1级', '60065', '桑郎平措', null, '2024-12-16');
INSERT INTO `weather` VALUES ('16', '成都', '5~10', '小雨', '1级', '60065', '桑郎平措', null, '2024-12-15');
INSERT INTO `weather` VALUES ('17', '成都', '5~10', '阴', '1级', '60065', '桑郎平措', null, '2024-12-14');
INSERT INTO `weather` VALUES ('18', '成都', '5~10', '阴', '1级', '60065', '桑郎平措', null, '2024-12-13');
