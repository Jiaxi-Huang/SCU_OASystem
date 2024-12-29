/*
Navicat MySQL Data Transfer

Source Server         : mysql8
Source Server Version : 80039
Source Host           : localhost:3307
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 80039
File Encoding         : 65001

Date: 2024-12-29 02:50:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `user_id` int DEFAULT NULL,
                              `attendance_date` date DEFAULT NULL,
                              `check_in` time DEFAULT NULL,
                              `check_out` time DEFAULT NULL,
                              `status` varchar(255) DEFAULT NULL,
                              `location` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `user_id` (`user_id`),
                              CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES ('1', '1', '2024-12-29', '00:00:01', '00:00:01', '123', '1');
INSERT INTO `attendance` VALUES ('2', '1', '2024-12-29', '00:00:01', '00:15:36', 'Leave Early', '1');
INSERT INTO `attendance` VALUES ('4', '1', '2024-12-29', '01:58:48', '21:58:48', 'On Time', null);
INSERT INTO `attendance` VALUES ('5', '1', '2024-12-29', '02:01:04', '17:19:11', 'Leave Early', '成都');
INSERT INTO `attendance` VALUES ('6', '2', '2024-12-29', '02:42:53', '23:48:53', 'On Time', '上海');
