/*
Navicat MySQL Data Transfer

Source Server         : 192.168.59.3
Source Server Version : 50624
Source Host           : 192.168.59.3:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-03-22 20:18:43
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `login_plat`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `login_plat` (
  `login_plat` int(1) DEFAULT NULL,
  `login_plat_desc` varchar(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `permission`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_code` varchar(45) DEFAULT NULL COMMENT '唯一标识权限的字符串',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission_id_UNIQUE` (`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `permission_resources`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `permission_resources` (
  `permission_id` int(11) NOT NULL,
  `resource` varchar(128) NOT NULL,
  PRIMARY KEY (`permission_id`,`resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `resource`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_path` varchar(255) NOT NULL COMMENT '资源请求路径，如：/order/list/*',
  `token_type_id` int(11) DEFAULT NULL COMMENT '令牌类型id',
  `allow_reset_header` int(11) DEFAULT '0' COMMENT '是否允许转发header',
  `default_permission` varchar(32) DEFAULT 'authc' COMMENT '默认权限.如果默认允许访问所有,则设为anon',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `resource_filters`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `resource_filters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filter_type` varchar(32) DEFAULT NULL COMMENT '过滤类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `role_permissions`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `role_permissions` (
  `permission_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `plat_type` varchar(32) NOT NULL DEFAULT '' COMMENT '平台类型',
  `method` varchar(8) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色权限关联表';

-- ----------------------------
-- Table structure for `token_type`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `token_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '名称',
  `describe` varchar(255) DEFAULT NULL COMMENT '描述',
  `expire` int(11) NOT NULL DEFAULT '172800000' COMMENT '超时时间默认2天  单位是毫秒 ',
  `check_times` int(2) DEFAULT '0' COMMENT 'token验证次数 0表示不限次数',
  `required` tinyint(1) DEFAULT '0' COMMENT '0为不是必须 1为必须 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user_login_type`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_login_type` (
  `plat_type` int(1) NOT NULL COMMENT '平台类型(0控制台 1劳务 2商城)',
  `user_role` int(2) NOT NULL COMMENT '用户角色id',
  `login_plat` int(1) NOT NULL COMMENT '登录平台(0.web 1.ios 2.app)',
  `login_type` int(1) NOT NULL DEFAULT '0' COMMENT '登录类型 (0单点登录,1多点登录)',
  PRIMARY KEY (`plat_type`,`user_role`,`login_plat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
