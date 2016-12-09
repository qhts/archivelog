/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3309
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-08 16:28:29
*/
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `history`
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `beginTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `details` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of history
-- ----------------------------

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
  UNIQUE KEY `ip` (`IP`,`settingsId`) USING BTREE,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 MAX_ROWS=1;

-- ----------------------------
-- Records of settings
-- ----------------------------
