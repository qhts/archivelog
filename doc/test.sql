/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3309
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-05 19:37:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `server`
-- ----------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `IP` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `settingsId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sid` (`settingsId`),
  CONSTRAINT `sid` FOREIGN KEY (`settingsId`) REFERENCES `settings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of server
-- ----------------------------

-- ----------------------------
-- Table structure for `settings`
-- ----------------------------
DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `logSourceDir` varchar(255) NOT NULL,
  `zipPrefix` varchar(255) NOT NULL,
  `modifyTime` int(11) NOT NULL,
  `storageDir` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 MAX_ROWS=1;

-- ----------------------------
-- Records of settings
-- ----------------------------
INSERT INTO `settings` VALUES ('1', '应用日志', '/home/crmuser/logs', 'achieve', '8', 'd:/');
INSERT INTO `settings` VALUES ('2', 'nginx日志', '/usr/local/nginx/logs', 'achieve2', '7', 'd:/');
