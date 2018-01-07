DROP DATABASE IF EXISTS ims;

CREATE DATABASE ims;

USE ims;

CREATE TABLE user (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(500),
  `password` varchar(500),
  PRIMARY KEY (`id`));

INSERT INTO user VALUES(0,"ciprian","ciprian");
INSERT INTO user VALUES(2,"alex","alex");