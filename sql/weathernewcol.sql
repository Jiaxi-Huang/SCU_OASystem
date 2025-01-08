-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS oa_database;
USE oa_database;

-- 创建 weather 表
CREATE TABLE IF NOT EXISTS weather (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    city VARCHAR(100) NOT NULL COMMENT '城市名称',
    temperature VARCHAR(50) NOT NULL COMMENT '温度',
    humidity VARCHAR(50) NOT NULL COMMENT '天气情况（如晴、雨等）',
    wind VARCHAR(50) NOT NULL COMMENT '风力（如2级）',
    win VARCHAR(50) COMMENT '风向（如北风）',
    win_meter VARCHAR(50) COMMENT '风速（如7km/h）',
    visibility VARCHAR(50) COMMENT '能见度（如30km）',
    pressure VARCHAR(50) COMMENT '气压（如1007）',
    air VARCHAR(50) COMMENT '空气质量指数（如67）',
    air_pm25 VARCHAR(50) COMMENT 'PM2.5（如37）',
    air_level VARCHAR(50) COMMENT '空气质量等级（如良）',
    creator_id VARCHAR(50) NOT NULL COMMENT '创建者ID',
    creator VARCHAR(100) NOT NULL COMMENT '创建者姓名',
    date DATE NOT NULL COMMENT '日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='天气数据表';

-- 插入测试数据（可选）
INSERT INTO weather (city, temperature, humidity, wind, win, win_meter, visibility, pressure, air, air_pm25, air_level, creator_id, creator, date)
VALUES
('成都', '15~22', '晴', '2级', '北风', '7km/h', '30km', '1007', '67', '37', '良', '60065', '桑郎平措', '2023-10-01'),
('绵阳', '14~20', '多云', '3级', '南风', '10km/h', '25km', '1005', '50', '25', '优', '60065', '桑郎平措', '2023-10-01');