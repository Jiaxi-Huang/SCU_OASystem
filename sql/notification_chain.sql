/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80040
 Source Host           : localhost:3306
 Source Schema         : oa_database

 Target Server Type    : MySQL
 Target Server Version : 80040
 File Encoding         : 65001

 Date: 19/12/2024 20:17:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for notification_chain
-- ----------------------------
DROP TABLE IF EXISTS `notification_chain`;
CREATE TABLE `notification_chain`  (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `cc_user_id` int NULL DEFAULT NULL,
  `notified_user_id` int NULL DEFAULT NULL,
  `request_id` int NULL DEFAULT NULL,
  `request_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notified_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `notification_chain_ibfk_1`(`notified_user_id` ASC) USING BTREE,
  INDEX `request_id`(`request_id` ASC) USING BTREE,
  INDEX `user_id`(`cc_user_id` ASC) USING BTREE,
  CONSTRAINT `notification_chain_ibfk_1` FOREIGN KEY (`notified_user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notification_chain_ibfk_3` FOREIGN KEY (`request_id`) REFERENCES `reimbursement_requests` (`reimbursement_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notification_chain_ibfk_4` FOREIGN KEY (`cc_user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
