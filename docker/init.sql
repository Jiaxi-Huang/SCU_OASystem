/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80402 (8.4.2)
 Source Host           : db:3306
 Source Schema         : oa_database

 Target Server Type    : MySQL
 Target Server Version : 80402 (8.4.2)
 File Encoding         : 65001

 Date: 08/12/2024 13:27:26
*/
-- 创建数据库 oa_database
CREATE DATABASE IF NOT EXISTS oa_database;

-- 切换到 oa_database 数据库
USE oa_database;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department_infos
-- ----------------------------
DROP TABLE IF EXISTS `department_infos`;
CREATE TABLE `department_infos`  (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department_infos
-- ----------------------------

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ext` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` int NOT NULL,
  `dir_id` int NULL DEFAULT NULL,
  `url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of files
-- ----------------------------

-- ----------------------------
-- Table structure for folders
-- ----------------------------
DROP TABLE IF EXISTS `folders`;
CREATE TABLE `folders`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pid` int NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of folders
-- ----------------------------

-- ----------------------------
-- Table structure for leave_requests
-- ----------------------------
DROP TABLE IF EXISTS `leave_requests`;
CREATE TABLE `leave_requests`  (
  `leave_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `start_date` datetime NULL DEFAULT NULL,
  `end_date` datetime NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `submitted_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`leave_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `leave_requests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leave_requests
-- ----------------------------
INSERT INTO `leave_requests` VALUES (1, 1, '2024-11-20 16:50:49', '2024-11-29 16:50:52', '无', '待审批', '2024-11-20 16:51:26');

-- ----------------------------
-- Table structure for meetings
-- ----------------------------
DROP TABLE IF EXISTS `meetings`;
CREATE TABLE `meetings`  (
  `mtin_id` int NOT NULL AUTO_INCREMENT,
  `mtin_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mtin_ctnt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mtin_fin` tinyint(1) NULL DEFAULT NULL,
  `mtin_st` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mtin_len` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mtin_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mtin_loc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mtin_crt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`mtin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meetings
-- ----------------------------
INSERT INTO `meetings` VALUES (1, '你好', '这是一个测试', 1, '2024-11-26 12:00', '90min', '0', 'LA', '2022-7-31 ');
INSERT INTO `meetings` VALUES (2, '你好3', '测试3', 0, '2024-11-28 23:59', '92min', '1', 'UT', '2022.10.23 16:55');
INSERT INTO `meetings` VALUES (3, '你不应该看到这个', '测试4', 0, '2024-11-27 12:59', '1min', '0', 'Chem Lab', '2022.10.24');

-- ----------------------------
-- Table structure for notification_chain
-- ----------------------------
DROP TABLE IF EXISTS `notification_chain`;
CREATE TABLE `notification_chain`  (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `notified_user_id` int NULL DEFAULT NULL,
  `request_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_id` int NULL DEFAULT NULL,
  `notified_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `notified_user_id`(`notified_user_id` ASC) USING BTREE,
  INDEX `request_id`(`request_id` ASC) USING BTREE,
  CONSTRAINT `notification_chain_ibfk_1` FOREIGN KEY (`notified_user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `notification_chain_ibfk_2` FOREIGN KEY (`request_id`) REFERENCES `leave_requests` (`leave_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification_chain
-- ----------------------------

-- ----------------------------
-- Table structure for reimbursement_requests
-- ----------------------------
DROP TABLE IF EXISTS `reimbursement_requests`;
CREATE TABLE `reimbursement_requests`  (
  `reimbursement_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `submitted_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`reimbursement_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `reimbursement_requests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reimbursement_requests
-- ----------------------------

-- ----------------------------
-- Table structure for user_avatar
-- ----------------------------
DROP TABLE IF EXISTS `user_avatar`;
CREATE TABLE `user_avatar`  (
  `user_id` int NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `user_avatar_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_avatar
-- ----------------------------
INSERT INTO `user_avatar` VALUES (1, 'https://gitee.com/meloooooooo/PicgoRepo/raw/master/uploadimg/2024-12/1733293127957_899e0a0f-6fe7-4ce7-87bd-90cc60ebe42c.jpg');
INSERT INTO `user_avatar` VALUES (3, 'https://gitee.com/meloooooooo/PicgoRepo/raw/master/uploadimg/2024-12/1733288492565_7962f13b-17ad-4b13-b553-6d82cecadba4.jpg');

-- ----------------------------
-- Table structure for user_department_infos
-- ----------------------------
DROP TABLE IF EXISTS `user_department_infos`;
CREATE TABLE `user_department_infos`  (
  `user_id` int NOT NULL,
  `department_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_department_infos
-- ----------------------------

-- ----------------------------
-- Table structure for user_infos
-- ----------------------------
DROP TABLE IF EXISTS `user_infos`;
CREATE TABLE `user_infos`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wechat_user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `department` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `user_infos_chk_1` CHECK (`email` like _utf8'%_@__%.__%')
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_infos
-- ----------------------------
INSERT INTO `user_infos` VALUES (1, 'user1', '$2a$10$BVJfRM36RF9Qv.B0nhTg/OH3fvzJlZ4P8AUZBmsLK2p6PQfWrrPG6', 'wx_user1', 'super@outlook.com', 'IT', 'admin', 'I am admin', '1111111111');
INSERT INTO `user_infos` VALUES (2, 'user2', '$2a$10$BVJfRM36RF9Qv.B0nhTg/OH3fvzJlZ4P8AUZBmsLK2p6PQfWrrPG6', 'wx_user2', 'user2@example.com', 'HR', 'Recruiter', 'I am a recruiter.', '2222222222');
INSERT INTO `user_infos` VALUES (3, 'worker1', '$2a$10$CB6ak73AionZ2YLKZ.uNFOq5AAAZLJWCSVDOytN7mAMGk0vurKOlW', NULL, '1761475917@qq.com', 'HR', 'worker', NULL, '3333333333');
INSERT INTO `user_infos` VALUES (4, 'worker2', '$2a$10$b.yRXkmj0fs/5B2xX17foer.eeH86pKcmz.g5aCzzmDm/KRPD76Mu', NULL, '2022141460084@stu.scu.edu.cn', 'HR', 'worker', NULL, '4444444444');
INSERT INTO `user_infos` VALUES (5, 'worker3', '$2a$10$G/285JhtSbjgv.MZRHsReuNwAamWVLMGx3Uc9UNT0yhJITOVmwDVy', NULL, 'huangjiaxi1@stu.scu.edu.cn', 'IT', 'worker', NULL, '5555555555');
INSERT INTO `user_infos` VALUES (6, 'worker4', '$2a$10$tlbA7kYJednAU6PcbMIbqueidXZPTir1EaKGPxZy/8zJdf.f6Ygy2', NULL, 'huangjiaxi1@gmail.com', 'HR', 'worker', NULL, '6666666666');
INSERT INTO `user_infos` VALUES (7, 'admin', '$2a$10$BVJfRM36RF9Qv.B0nhTg/OH3fvzJlZ4P8AUZBmsLK2p6PQfWrrPG6', NULL, 'admin@outlook.com', 'HR', 'manager', '现在我可以更新啦', '1588417028');

-- ----------------------------
-- Table structure for user_permission_infos
-- ----------------------------
DROP TABLE IF EXISTS `user_permission_infos`;
CREATE TABLE `user_permission_infos`  (
  `user_id` int NOT NULL,
  `role` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permissions` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_permission_infos
-- ----------------------------

-- ----------------------------
-- Table structure for usermeetings
-- ----------------------------
DROP TABLE IF EXISTS `usermeetings`;
CREATE TABLE `usermeetings`  (
  `user_id` int NOT NULL,
  `mtin_id` int NOT NULL,
  `adder_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`, `mtin_id`) USING BTREE,
  INDEX `mtin_id`(`mtin_id` ASC) USING BTREE,
  INDEX `adder_id`(`adder_id` ASC) USING BTREE,
  CONSTRAINT `usermeetings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usermeetings_ibfk_2` FOREIGN KEY (`mtin_id`) REFERENCES `meetings` (`mtin_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usermeetings_ibfk_3` FOREIGN KEY (`adder_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usermeetings
-- ----------------------------
INSERT INTO `usermeetings` VALUES (1, 1, 1);
INSERT INTO `usermeetings` VALUES (1, 2, 1);
INSERT INTO `usermeetings` VALUES (2, 3, 1);

-- ----------------------------
-- Table structure for usertodo
-- ----------------------------
DROP TABLE IF EXISTS `usertodo`;
CREATE TABLE `usertodo`  (
  `user_id` int NOT NULL,
  `todo_id` int NOT NULL AUTO_INCREMENT,
  `adder_id` int NULL DEFAULT NULL,
  `todo_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `todo_ctnt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `todo_fin` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `todo_crt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `todo_ddl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`todo_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `adder_id`(`adder_id` ASC) USING BTREE,
  CONSTRAINT `usertodo_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usertodo_ibfk_2` FOREIGN KEY (`adder_id`) REFERENCES `user_infos` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usertodo
-- ----------------------------
INSERT INTO `usertodo` VALUES (1, 2, 1, 'compute', 'WhateverBro', '未完成', '20.12', '6.22');
INSERT INTO `usertodo` VALUES (1, 3, 1, 'compute2222', 'Whatever1Whatever111BroWhatever111BroWhatever111BroWhatever111BroWhatever111BroWhatever111BroWhatever111Bro11Bro', '已完成', '20.12', '3.15');
INSERT INTO `usertodo` VALUES (7, 4, 7, 'aaa', 'aaa', '未完成', NULL, '2024-11-21-15:45');
INSERT INTO `usertodo` VALUES (1, 5, 1, '完成项目计划书', '设计与攥写', '未完成', NULL, '2024-11-24-22:42');
INSERT INTO `usertodo` VALUES (1, 7, 1, '项目会议', '讨论项目进展', '未完成', '2024-05-20', '2024-06-01');
INSERT INTO `usertodo` VALUES (2, 8, 2, '市场调研', '收集市场数据', '已完成', '2024-05-25', '2024-07-15');
INSERT INTO `usertodo` VALUES (3, 9, 3, '产品发布', '准备产品发布材料', '未完成', '2024-06-01', '2024-08-01');
INSERT INTO `usertodo` VALUES (4, 10, 4, '财务报告', '编写财务报告', '已完成', '2024-06-15', '2024-09-01');
INSERT INTO `usertodo` VALUES (5, 11, 5, '年度总结', '总结年度工作', '未完成', '2024-07-01', '2024-10-20');
INSERT INTO `usertodo` VALUES (6, 12, 6, '再测试', '啊啊啊啊啊啊啊啊啊啊啊啊啊', '已完成', '2222', '2.22');
INSERT INTO `usertodo` VALUES (2, 13, 1, '添加测试', '添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试添加测试', '未完成', NULL, '2024-11-11-21:24');
INSERT INTO `usertodo` VALUES (1, 14, 1, '1', '2', '已完成', NULL, '2024-11-12-23:02');
INSERT INTO `usertodo` VALUES (3, 15, 1, '分发测试', '这是一个分发测试，对象是worker1和admin', '未完成', '2024-12-07 23:23:41', '2024-12-14 23:22');
INSERT INTO `usertodo` VALUES (7, 16, 1, '分发测试', '这是一个分发测试，对象是worker1和admin', '未完成', '2024-12-07 23:23:41', '2024-12-14 23:22');
INSERT INTO `usertodo` VALUES (2, 17, 7, '紧急加班', '所有人给我狠狠加班', '未完成', '2024-12-07 23:25:23', '2024-12-13 23:25');
INSERT INTO `usertodo` VALUES (3, 18, 7, '紧急加班', '所有人给我狠狠加班', '未完成', '2024-12-07 23:25:23', '2024-12-13 23:25');
INSERT INTO `usertodo` VALUES (4, 19, 7, '紧急加班', '所有人给我狠狠加班', '未完成', '2024-12-07 23:25:23', '2024-12-13 23:25');
INSERT INTO `usertodo` VALUES (6, 20, 7, '紧急加班', '所有人给我狠狠加班', '未完成', '2024-12-07 23:25:23', '2024-12-13 23:25');
INSERT INTO `usertodo` VALUES (7, 21, 7, '紧急加班', '所有人给我狠狠加班修改', '未完成', '2024-12-07 23:25:23', '2024-12-13 23:25');
-- ----------------------------
-- Table structure for userlogs
-- ----------------------------
DROP TABLE IF EXISTS `user_logs`;
CREATE TABLE `user_logs`  (
  id int PRIMARY KEY AUTO_INCREMENT,
  user_id int,
  date varchar(255) ,
  content varchar(255),
  FOREIGN KEY (user_id) REFERENCES user_infos(`user_id`)
)ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;;

SET FOREIGN_KEY_CHECKS = 1;
