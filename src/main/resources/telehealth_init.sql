/*
Telehealth database initialization script

Source Server         : localhost_3306
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : telehealth

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2017-01-15 14:43:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for medicalcondition
-- ----------------------------
DROP TABLE IF EXISTS `medicalcondition`;
CREATE TABLE `medicalcondition` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '',
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medicalcondition
-- ----------------------------
INSERT INTO `medicalcondition` VALUES ('2', 'Crohnova bolezen', '0');
INSERT INTO `medicalcondition` VALUES ('3', 'Diabetes', '0');
INSERT INTO `medicalcondition` VALUES ('4', 'Srčno popuščanje', '0');
INSERT INTO `medicalcondition` VALUES ('5', 'Hipertenzija', '0');

-- ----------------------------
-- Table structure for medicalcondition_medicalparameter
-- ----------------------------
DROP TABLE IF EXISTS `medicalcondition_medicalparameter`;
CREATE TABLE `medicalcondition_medicalparameter` (
  `medicalcondition_id` bigint(20) unsigned NOT NULL,
  `medicalparameter_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`medicalcondition_id`,`medicalparameter_id`),
  KEY `fk_condition_parameter_parameter_id` (`medicalparameter_id`),
  CONSTRAINT `fk_condition_parameter_condition_id` FOREIGN KEY (`medicalcondition_id`) REFERENCES `medicalcondition` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_condition_parameter_parameter_id` FOREIGN KEY (`medicalparameter_id`) REFERENCES `medicalparameter` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medicalcondition_medicalparameter
-- ----------------------------
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '1');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('3', '1');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('4', '1');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('5', '1');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('3', '2');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '3');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('3', '3');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('4', '3');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('5', '3');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '4');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '5');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '6');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '7');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '8');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '9');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '10');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('2', '11');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('4', '12');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('4', '13');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('4', '14');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('4', '16');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('5', '17');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('5', '18');
INSERT INTO `medicalcondition_medicalparameter` VALUES ('5', '19');

-- ----------------------------
-- Table structure for medicaldata
-- ----------------------------
DROP TABLE IF EXISTS `medicaldata`;
CREATE TABLE `medicaldata` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `medicalparameter_id` bigint(20) unsigned NOT NULL,
  `datavalue` float NOT NULL,
  `datavaluedate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createdat` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedat` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` varchar(16) DEFAULT '',
  `updatedby` varchar(16) DEFAULT '',
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_data_parameter_id` (`medicalparameter_id`),
  KEY `fk_data_cratedby_user_id` (`createdby`),
  KEY `fk_data_updatedby_user_id` (`updatedby`),
  KEY `fk_data_user_id` (`user_id`),
  CONSTRAINT `fk_data_parameter_id` FOREIGN KEY (`medicalparameter_id`) REFERENCES `medicalparameter` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_data_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for medicalparameter
-- ----------------------------
DROP TABLE IF EXISTS `medicalparameter`;
CREATE TABLE `medicalparameter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '',
  `title` varchar(32) NOT NULL DEFAULT '',
  `details` varchar(256) NOT NULL DEFAULT '',
  `medicaldataperiod` varchar(2) NOT NULL DEFAULT 'MI',
  `voiceprompt` varchar(256) NOT NULL DEFAULT '',
  `version` bigint(20) NOT NULL DEFAULT '0',
  `medicaldatatype` varchar(2) NOT NULL DEFAULT '',
  `datavaluedefault` float NOT NULL,
  `datavalueunit` varchar(11) DEFAULT NULL,
  `datavaluelabel` varchar(24) NOT NULL DEFAULT '',
  `datavaluestep` float NOT NULL,
  `datavaluemin` float NOT NULL,
  `datavaluemax` float NOT NULL,
  `dataentriesperperiod` int(11) NOT NULL DEFAULT '1',
  `instructions` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_parameter_dataperiod_id` (`medicaldataperiod`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medicalparameter
-- ----------------------------
INSERT INTO `medicalparameter` VALUES ('1', 'Telesna teža', 'Telesna teža', 'Vnesite Vašo telesno težo', 'DA', 'input_data_bodyweight.wav', '0', 'NU', '75', 'kg', 'Telesna teža', '0.1', '40', '160', '1', null);
INSERT INTO `medicalparameter` VALUES ('2', 'Nivo glukoze v krvi', 'Glukoza v krvi', 'Vnesite podatke o nivoju glukoze v krvi', 'DA', 'input_data_gloukoze.wav', '0', 'NU', '6.1', 'mmol/l', 'Nivo glukoze v krvi', '0.1', '4', '12', '1', null);
INSERT INTO `medicalparameter` VALUES ('3', 'Počutje', 'Počutje', 'Kako se počutite?', 'DA', 'input_data_mood.wav', '0', 'SC', '2', null, 'Počutje', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('4', 'Utrujenost', 'Utrujenost', 'Ste pogosto utrujeni?', 'DA', 'input_data_utrujenost.wav', '0', 'SF', '2', null, 'Občutek utrujenosti', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('5', 'Nezadovoljstvo', 'Nezadovoljstvo', 'Ste pogosto nezadovojlni?', 'DA', 'input_data_nezadovoljstvo.wav', '0', 'SF', '2', null, 'Občutek nezadovoljstva', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('6', 'Potreba', 'Potreba', 'Imate pogosto občutek potrebe?', 'DA', 'input_data_potreba.wav', '0', 'SF', '2', null, 'Občutek potrebe', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('7', 'Sproščenost', 'Sproščenost', 'Ste pogosto sproščeni?', 'DA', 'input_data_sproscenost.wav', '0', 'SF', '2', null, 'Občutek sproščenosti', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('8', 'Vetrovi', 'Vetrovi', 'Imate težave z vetrovi?', 'DA', 'input_data_vetrovi.wav', '0', 'BO', '0', null, 'Težave z vetrovi', '1', '0', '1', '1', null);
INSERT INTO `medicalparameter` VALUES ('9', 'Depresija', 'Depresija', 'Imate občutek depresije?', 'DA', 'input_Data_depresija.wav', '0', 'BO', '0', null, 'Občutek depresije', '1', '0', '1', '1', null);
INSERT INTO `medicalparameter` VALUES ('10', 'Bolečine v trebuhu', 'Bolečine v trebuhu', 'Imate bolečine v trebuhu?', 'DA', 'input_data_bolecina_trebuh.wav', '0', 'BO', '0', null, 'Bolečine v trebuhu', '1', '0', '1', '1', null);
INSERT INTO `medicalparameter` VALUES ('11', 'Krvi v blatu', 'Kri v blatu', 'Imate kri v blatu?', 'DA', 'input_data_kri_blato.wav', '0', 'BO', '0', null, 'Kri v blatu', '1', '0', '1', '1', null);
INSERT INTO `medicalparameter` VALUES ('12', 'Težko dihanje', 'Težko dihanje', 'Pogosto težko dihate?', 'DA', 'input_data_tezko_dihanje.wav', '0', 'SF', '2', null, 'Težko dihate', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('13', 'Tiščanje na vodo', 'Tiščanje na vodo', 'Vas ponoči pogosto tišči na vodo?', 'DA', 'input_data_pogosto_voda.wav', '0', 'SF', '2', null, 'Pogosto tišči', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('14', 'Kašelj ob naporu', 'Kašelj ob naporu', 'Koliko kašljate ob naporu?', 'DA', 'input_data_kaselj_naport.wav', '0', 'SQ', '2', null, 'Koliko kašljate', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('15', 'Hladne okončine', 'Hladne okončine', 'Imate hladne okončine', 'DA', 'input_data_hladne_okoncine.wav', '0', 'BO', '0', null, 'Hladne okončine', '1', '0', '1', '1', null);
INSERT INTO `medicalparameter` VALUES ('16', 'Otekanje nog', 'Otekanje nog', 'Koliko vam otekajo noge?', 'DA', 'input_data_otekanje_nog.wav', '0', 'SQ', '2', null, 'Otekanje', '1', '0', '4', '1', null);
INSERT INTO `medicalparameter` VALUES ('17', 'Spodnji krvni tlak', 'Spodnji krvni tlak', 'Vnesite spodnji krvni tlak', 'DA', 'input_data_spodnji_krvni_tlak.wav', '0', 'NU', '80', 'mm Hg', 'Spodnji krvni tlak', '1', '40', '180', '1', null);
INSERT INTO `medicalparameter` VALUES ('18', 'Zgornji krvni tlak', 'Zgornji krvni tlak', 'Vnesite zgornji krvni tlak', 'DA', 'input_data_zgornji_krvni_tlak.wav', '0', 'NU', '120', 'mm Hg', 'Zgornji krvni tlak', '1', '60', '260', '1', null);
INSERT INTO `medicalparameter` VALUES ('19', 'Gibanje', 'Gibanje', 'Koliko se gibate?', 'DA', 'input_data_gibanje.wav', '0', 'SQ', '2', null, 'Gibanje', '1', '0', '4', '1', null);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', 'administration', '0');
INSERT INTO `permission` VALUES ('2', 'administration_users', '0');
INSERT INTO `permission` VALUES ('3', 'data', '0');
INSERT INTO `permission` VALUES ('4', 'data_dashboard', '0');
INSERT INTO `permission` VALUES ('5', 'data_input', '0');
INSERT INTO `permission` VALUES ('6', 'data_history', '0');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'Administrator', '0');
INSERT INTO `role` VALUES ('2', 'Uporabnik', '0');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` bigint(20) unsigned NOT NULL,
  `permission_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `role_permission_permission_id_fk` (`permission_id`),
  CONSTRAINT `role_permission_permission_id_fk` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_permission_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('2', '3');
INSERT INTO `role_permission` VALUES ('2', '4');
INSERT INTO `role_permission` VALUES ('2', '5');
INSERT INTO `role_permission` VALUES ('2', '6');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL DEFAULT '',
  `password` varchar(64) NOT NULL DEFAULT '',
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `firstname` varchar(32) NOT NULL DEFAULT '',
  `lastname` varchar(32) NOT NULL DEFAULT '',
  `email` varchar(64) NOT NULL DEFAULT '',
  `phone` varchar(16) NOT NULL DEFAULT '',
  `version` bigint(20) NOT NULL DEFAULT '0',
  `gender` varchar(1) NOT NULL DEFAULT '',
  `birthDate` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_unq` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '$2a$10$a.QimKXXUv0l3HiqbqFbrOBAN8wjrwalV6W26eAer8Yc5nEprsJwO', '1', 'Administrator', 'Administrator', 'admin@example.tld', '000000000', '2', 'M', '2001-01-01');

-- ----------------------------
-- Table structure for user_medicalcondition
-- ----------------------------
DROP TABLE IF EXISTS `user_medicalcondition`;
CREATE TABLE `user_medicalcondition` (
  `user_id` bigint(20) unsigned NOT NULL,
  `medicalcondition_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`medicalcondition_id`),
  KEY `user_condition_condition_id_fk` (`medicalcondition_id`),
  CONSTRAINT `fk_user_conditon_condition_id` FOREIGN KEY (`medicalcondition_id`) REFERENCES `medicalcondition` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_conditon_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `user_role_role_id_fk` (`role_id`),
  CONSTRAINT `user_role_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
