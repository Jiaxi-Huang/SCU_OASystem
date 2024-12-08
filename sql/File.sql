CREATE TABLE `files` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `ext` varchar(10),
  `size` int NOT NULL,
  `dir_id` int,
  `url` varchar(1024) NOT NULL,
  `remark` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int,
  `department` varchar(255),
  PRIMARY KEY (`id`)
);
CREATE TABLE `folders` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `pid` int DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `user_id` int,
    `department` varchar(255),
    PRIMARY KEY (`id`)
) 
