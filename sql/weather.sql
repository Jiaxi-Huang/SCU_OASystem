CREATE TABLE weather (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(100),
    temperature VARCHAR(10),
    humidity VARCHAR(10),
    wind VARCHAR(50),
    creator_id VARCHAR(50),
    creator VARCHAR(100),
    create_time DATETIME,
    date DATE
);
