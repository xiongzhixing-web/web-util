/*
Navicat MySQL Data Transfer

Source Server         : arcManager
Source Server Version : 50522
Source Host           : localhost:3306
Source Database       : ssm

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2020-06-11 21:44:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `group`
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL DEFAULT '0',
  `group_id` varchar(64) DEFAULT NULL,
  `enevt_id` varchar(64) DEFAULT NULL,
  `vip_type` varchar(64) DEFAULT NULL,
  `union_vip_type` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES ('1', 'a', 'b', 'c', null);
INSERT INTO `group` VALUES ('2', 'a', 'b', 'e', 'f');
INSERT INTO `group` VALUES ('3', 'a', 'b', 'e', 'g');
INSERT INTO `group` VALUES ('4', 'a', 'b', 'c', 'h');
INSERT INTO `group` VALUES ('5', 'a', 'b', 'i', null);
INSERT INTO `group` VALUES ('6', 'a', 'b', 'e', 'f');
INSERT INTO `group` VALUES ('7', 'a', 'b', 'e', 'g');
INSERT INTO `group` VALUES ('8', 'a', 'b', 'c', null);


SELECT
	CONCAT(group_id, enevt_id, vip_type, IFNULL(union_vip_type, "")),
	count(*)
FROM
	`group`
GROUP BY
	group_id,
	enevt_id,
	vip_type,
	IFNULL(union_vip_type, "")