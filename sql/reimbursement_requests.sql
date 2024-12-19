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

 Date: 19/12/2024 20:17:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for reimbursement_requests
-- ----------------------------
DROP TABLE IF EXISTS `reimbursement_requests`;
CREATE TABLE `reimbursement_requests`  (
  `reimbursement_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `submitted_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `review_user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`reimbursement_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `review_user_id`(`review_user_id` ASC) USING BTREE,
  CONSTRAINT `reimbursement_requests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reimbursement_requests_ibfk_2` FOREIGN KEY (`review_user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
