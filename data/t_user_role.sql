/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : chenet_website

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 08/05/2023 16:31:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `user_role_id` int NOT NULL AUTO_INCREMENT COMMENT '用户权限id，唯一，自增',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
  `is_permanent` int NULL DEFAULT NULL COMMENT '是否永久',
  `effective_time` datetime NULL DEFAULT NULL COMMENT '生效时间',
  `ineffective_time` datetime NULL DEFAULT NULL COMMENT '失效时间',
  `user_role_state` int NULL DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
