INSERT INTO `oa_database`.`meetings` (`mtin_id`, `mtin_title`, `mtin_ctnt`, `mtin_fin`, `mtin_st`, `mtin_len`, `mtin_host`, `mtin_loc`, `mtin_crt`) VALUES ('0', '你好', '这是一个测试', '0', '2024-11-26 12:00', '90min', '0', 'LA', '2022-7-31 ');
INSERT INTO `oa_database`.`meetings` (`mtin_id`, `mtin_title`, `mtin_ctnt`, `mtin_fin`, `mtin_st`, `mtin_len`, `mtin_host`, `mtin_loc`, `mtin_crt`) VALUES ('1', '你好1', '这是一个测试1', '0', '2024-11-27 23:59  ', '91min', '0', 'CA', '2022-10-22 23:59 ');
INSERT INTO `oa_database`.`meetings` (`mtin_id`, `mtin_title`, `mtin_ctnt`, `mtin_fin`, `mtin_st`, `mtin_len`, `mtin_host`, `mtin_loc`, `mtin_crt`) VALUES ('2', '你好3', '测试3', '0', '2024-11-28 23:59', '92min', '0', 'UT', '2022.10.23');
INSERT INTO `oa_database`.`meetings` (`mtin_id`, `mtin_title`, `mtin_ctnt`, `mtin_fin`, `mtin_st`, `mtin_len`, `mtin_host`, `mtin_loc`, `mtin_crt`) VALUES ('3', '你不应该看到这个', '测试4', '0', '2024-11-27 12:59', '1min', '0', 'Chem Lab', '2022.10.24');


INSERT INTO `oa_database`.`usermeetings` (`user_id`, `mtin_id`, `adder_id`) VALUES ('1', '0', '1');
INSERT INTO `oa_database`.`usermeetings` (`user_id`, `mtin_id`, `adder_id`) VALUES ('1', '1', '1');
INSERT INTO `oa_database`.`usermeetings` (`user_id`, `mtin_id`, `adder_id`) VALUES ('1', '2', '1');
INSERT INTO `oa_database`.`usermeetings` (`user_id`, `mtin_id`, `adder_id`) VALUES ('2', '3', '1');
