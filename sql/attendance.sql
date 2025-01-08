/*
Navicat MySQL Data Transfer

Source Server         : mysql8
Source Server Version : 80039
Source Host           : localhost:3307
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 80039
File Encoding         : 65001

Date: 2024-12-29 18:11:07
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
                              `inLocation` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
                              `outLocation` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `user_id` (`user_id`),
                              CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES ('1', '1', '2024-12-28', '00:00:04', '00:00:01', 'Leave Early', '上海', '内网ip');
INSERT INTO `attendance` VALUES ('2', '1', '2024-12-29', '00:00:17', '14:15:36', 'Leave Early', '北京', '上海');
INSERT INTO `attendance` VALUES ('4', '1', '2024-12-29', '01:58:51', '21:58:48', 'On Time', '成都', '内网ip');
INSERT INTO `attendance` VALUES ('5', '1', '2024-12-29', '02:01:04', '17:19:11', 'Leave Early', '北京', '上海');
INSERT INTO `attendance` VALUES ('6', '1', '2024-12-29', '02:42:53', '01:00:00', 'Absent', '内网ip', '内网ip');
INSERT INTO `attendance` VALUES ('8', '1', '2024-12-29', '15:00:46', '20:15:24', 'Late', '内网ip', '内网ip');
INSERT INTO `attendance` VALUES ('9', '1', '2024-12-29', '15:19:18', '15:30:18', 'Late And Leave Early', '上海', '北京');
INSERT INTO `attendance` VALUES ('10', '1', '2024-12-29', '15:37:32', '15:37:35', 'Late And Leave Early', '上海', '上海');
INSERT INTO `attendance` VALUES ('12', '2', '2024-12-29', '15:41:14', '20:41:16', 'Late', '成都', '上海');
INSERT INTO `attendance` VALUES ('14', '1', '2024-12-30', '01:06:41', '22:22:41', 'On Time', '上海', '内网ip');
INSERT INTO `attendance` VALUES ('17', '2', '2024-12-29', '18:00:40', '18:00:42', 'Late', '内网IP', '内网IP');
INSERT INTO `attendance` VALUES ('18', '2', '2024-12-28', '18:04:07', '18:04:07', 'Absent', '上海', '上海');
