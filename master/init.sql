/*
 Navicat Premium Data Transfer

 Source Server         : 103.45.8.198
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : 103.45.8.198:3306
 Source Schema         : sale

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 11/09/2018 21:14:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sale_category
-- ----------------------------
DROP TABLE IF EXISTS `sale_category`;
CREATE TABLE `sale_category`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_device
-- ----------------------------
DROP TABLE IF EXISTS `sale_device`;
CREATE TABLE `sale_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `deviceName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '点位名称',
  `deviceNum` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备编码',
  `deviceType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `areaId` int(11) NULL DEFAULT NULL COMMENT '区域id',
  `pointName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名',
  `pointId` int(11) NULL DEFAULT NULL COMMENT '从属点位',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `isDeleted` tinyint(2) NULL DEFAULT 0 COMMENT '软删除  ',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_device_product
-- ----------------------------
DROP TABLE IF EXISTS `sale_device_product`;
CREATE TABLE `sale_device_product`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'guid',
  `productId` int(11) NOT NULL COMMENT '产品id',
  `deviceId` int(11) NOT NULL COMMENT '设备id',
  `isSale` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否售卖',
  `price` int(10) NULL DEFAULT NULL COMMENT '价格',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Ix_id`(`id`) USING BTREE,
  INDEX `fk_device_id`(`deviceId`) USING BTREE,
  CONSTRAINT `fk_device_id` FOREIGN KEY (`deviceId`) REFERENCES `sale_device` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_file
-- ----------------------------
DROP TABLE IF EXISTS `sale_file`;
CREATE TABLE `sale_file`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `size` bigint(11) NULL DEFAULT NULL COMMENT '大小',
  `ext` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ix_id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sale_log
-- ----------------------------
DROP TABLE IF EXISTS `sale_log`;
CREATE TABLE `sale_log`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `logDescription` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志类型',
  `actionArgs` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志名称',
  `userName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `className` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类名称',
  `method` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `succeed` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否成功',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sale_menu
-- ----------------------------
DROP TABLE IF EXISTS `sale_menu`;
CREATE TABLE `sale_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '1 菜单 2按钮',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限码',
  `parentId` int(11) NULL DEFAULT NULL COMMENT '父级id',
  `creationTime` datetime(0) NULL DEFAULT NULL,
  `creatorUserId` int(11) NULL DEFAULT NULL,
  `isDeleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sale_order
-- ----------------------------
DROP TABLE IF EXISTS `sale_order`;
CREATE TABLE `sale_order`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `productId` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `price` int(10) NULL DEFAULT NULL COMMENT '价格',
  `wechatOrder` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信订单',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `orderState` int(255) NULL DEFAULT NULL COMMENT '订单状态',
  `payState` int(10) NULL DEFAULT NULL COMMENT '支付状态',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `deviceId` int(10) NULL DEFAULT NULL COMMENT '设备id',
  `deviceName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名',
  `deviceType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `pointName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '点位名',
  `payType` int(10) NULL DEFAULT NULL COMMENT '支付类型',
  `backNum` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款单号',
  `deviceNum` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `pointId` int(11) NULL DEFAULT NULL COMMENT '点位id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_payfor
-- ----------------------------
DROP TABLE IF EXISTS `sale_payfor`;
CREATE TABLE `sale_payfor`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `operatorId` int(10) NULL DEFAULT NULL COMMENT '运营商key',
  `alipayId` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阿里key',
  `alipayKey` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '阿里密钥',
  `alipayAgent` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阿里保留',
  `wechatpayId` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信id',
  `wechatpayKey` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信key',
  `wechatpayAgent` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信保留',
  `creationTime` datetime(0) NULL DEFAULT NULL,
  `creatorUserId` int(11) NULL DEFAULT NULL,
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `cardUrl` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款证书地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_point
-- ----------------------------
DROP TABLE IF EXISTS `sale_point`;
CREATE TABLE `sale_point`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `pointName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '点位名',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'i奥数',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `isDeleted` tinyint(2) NULL DEFAULT 0 COMMENT '软删除  ',
  `x` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'x坐标',
  `y` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'y坐标',
  `areaId` int(11) NULL DEFAULT NULL COMMENT '区域id',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_product
-- ----------------------------
DROP TABLE IF EXISTS `sale_product`;
CREATE TABLE `sale_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `productName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名',
  `productNum` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号',
  `productType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类型',
  `price` int(10) NULL DEFAULT NULL COMMENT '默认价格',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `isDeleted` tinyint(2) NULL DEFAULT 0 COMMENT '软删除  ',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `description` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `imageUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_role
-- ----------------------------
DROP TABLE IF EXISTS `sale_role`;
CREATE TABLE `sale_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `roleName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'name',
  `displayName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `isActive` tinyint(2) NULL DEFAULT NULL COMMENT '启用状态',
  `isStatic` tinyint(4) NULL DEFAULT NULL COMMENT '是否静态',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `isDeleted` int(11) NULL DEFAULT 0,
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`, `roleName`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sale_rolemenu
-- ----------------------------
DROP TABLE IF EXISTS `sale_rolemenu`;
CREATE TABLE `sale_rolemenu`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'guid',
  `roleId` int(11) NOT NULL,
  `menuId` int(11) NOT NULL,
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  INDEX `ix_roleId`(`roleId`) USING BTREE,
  INDEX `ix_menuId`(`menuId`) USING BTREE,
  CONSTRAINT `fk_role_id` FOREIGN KEY (`roleId`) REFERENCES `sale_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sale_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sale_tenant`;
CREATE TABLE `sale_tenant`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `displayName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名',
  `tenantName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT 'd',
  `creatorUserId` int(11) NULL DEFAULT NULL,
  `isActive` tinyint(2) NULL DEFAULT NULL COMMENT '1启用  0禁用',
  `isDeleted` int(255) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_tree
-- ----------------------------
DROP TABLE IF EXISTS `sale_tree`;
CREATE TABLE `sale_tree`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名',
  `parentId` int(11) NULL DEFAULT NULL COMMENT '父级id',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `levelCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '级别code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sale_user
-- ----------------------------
DROP TABLE IF EXISTS `sale_user`;
CREATE TABLE `sale_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `account` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `creationTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creatorUserId` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `userName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `isActive` tinyint(2) NULL DEFAULT NULL COMMENT '1启用  0禁用',
  `isDeleted` tinyint(2) NULL DEFAULT 0 COMMENT '软删除  ',
  `lastLoginTime` datetime(0) NULL DEFAULT NULL COMMENT '最后登陆时间',
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  `areaId` int(11) NULL DEFAULT NULL COMMENT '区域id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ix_account`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sale_userrole
-- ----------------------------
DROP TABLE IF EXISTS `sale_userrole`;
CREATE TABLE `sale_userrole`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `userId` int(11) NOT NULL,
  `roleId` int(11) NOT NULL,
  `creationTime` datetime(0) NULL DEFAULT NULL,
  `creatorUserId` int(11) NULL DEFAULT NULL,
  `tenantId` int(11) NULL DEFAULT NULL COMMENT '租户id',
  INDEX `ix_userId`(`userId`) USING BTREE,
  INDEX `ix_roleId`(`roleId`) USING BTREE,
  CONSTRAINT `fk_roleId` FOREIGN KEY (`roleId`) REFERENCES `sale_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_userId` FOREIGN KEY (`userId`) REFERENCES `sale_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
