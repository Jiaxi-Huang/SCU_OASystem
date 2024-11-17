-- 创建用户信息表
CREATE TABLE user_infos (
    user_id INT PRIMARY KEY AUTO_INCREMENT,                   -- 用户ID，主键
    username VARCHAR(50),                        -- 用户名
    password VARCHAR(255),                       -- 密码，存储加密后的密码
    wechat_user_id VARCHAR(100),                 -- 微信用户ID
    email VARCHAR(64) CHECK (email LIKE '%_@__%.__%'),  -- 邮箱，约束邮箱格式
    department VARCHAR(64),                      -- 部门
    role VARCHAR(64),                            -- 职能
    intro VARCHAR(256)                           -- 自我介绍
);


-- 创建部门信息表
CREATE TABLE department_infos (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(50)
);

-- 创建用户所在部门信息表
CREATE TABLE user_department_infos (
    user_id INT PRIMARY KEY,             -- 用户ID
    department_id INT                   -- 部门ID
);


-- 创建用户权限表
CREATE TABLE user_permission_infos (
    user_id INT PRIMARY KEY,             -- 用户ID
    role VARCHAR(64),                    -- 职能
    permissions VARCHAR(64)              -- 权限，使用 `VARCHAR` 类型存储列表，可以考虑使用分隔符来表示多个权限
    -- 若权限是具体列表结构，可以考虑使用 JSON 类型存储权限列表
);


-- 创建请假申请表
CREATE TABLE leave_requests (
    leave_id INT PRIMARY KEY AUTO_INCREMENT,               -- 请假申请的唯一标识符
    user_id INT,                            -- 提交申请的用户ID
    start_date DATETIME,                    -- 请假的开始时间
    end_date DATETIME,                      -- 请假的结束时间
    reason VARCHAR(255),                    -- 请假理由
    status VARCHAR(20),                     -- 审批状态 (待审批、已批准、已拒绝)
    submitted_at DATETIME,                  -- 申请提交时间
    FOREIGN KEY (user_id) REFERENCES user_infos(user_id)  -- 外键，关联用户表
);


-- 创建报销申请表
CREATE TABLE reimbursement_requests (
    reimbursement_id INT PRIMARY KEY AUTO_INCREMENT,         -- 报销申请的唯一标识符
    user_id INT,                              -- 提交申请的用户ID
    amount DECIMAL(10, 2),                     -- 报销金额，精度为 10，总共 10 位，其中 2 位小数
    description VARCHAR(255),                  -- 报销描述
    status VARCHAR(20),                       -- 审批状态 (待审批、已批准、已拒绝)
    submitted_at DATETIME,                    -- 申请提交时间
    FOREIGN KEY (user_id) REFERENCES user_infos(user_id)  -- 外键，关联用户表
);


-- 创建抄送链表
CREATE TABLE notification_chain (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,               -- 抄送记录的唯一标识符
    notified_user_id INT,                          -- 被抄送的用户ID
    request_type VARCHAR(20),                       -- 申请类型（请假申请、报销申请）
    request_id INT,                                -- 对应的申请ID，关联 leave_requests 或 reimbursement_requests 的 ID
    notified_at DATETIME,                          -- 抄送通知时间
    FOREIGN KEY (notified_user_id) REFERENCES user_infos(user_id),  -- 外键，关联用户表
    FOREIGN KEY (request_id) REFERENCES leave_requests(leave_id) -- 外键，参照请假申请表
);


-- 创建用户和待办事项表
CREATE TABLE UserTodo (
    user_id INT NOT NULL,                              -- 用户ID，外键，参照用户表
    todo_id INT PRIMARY KEY AUTO_INCREMENT,                  -- 待办事项ID，主键且外键，参照待办事项表
    adder_id INT,                             -- 添加人ID，外键，参照用户表
    todo_title VARCHAR(255),                  -- 待办事项名
    todo_ctnt VARCHAR(255),                   -- 内容
    todo_fin VARCHAR(4),                            -- 已完成，布尔类型
    todo_crt VARCHAR(255),                    -- 创建日期
    todo_ddl VARCHAR(255),                    -- 截止日期
    FOREIGN KEY (user_id) REFERENCES user_infos(user_id),          -- 外键，关联用户表
    FOREIGN KEY (adder_id) REFERENCES user_infos(user_id)          -- 外键，关联用户表
);

-- 创建会议表
CREATE TABLE Meetings (
    mtin_id INT PRIMARY KEY AUTO_INCREMENT,                   -- 会议ID，主键
    mtin_title VARCHAR(255),                    -- 会议名
    mtin_ctnt VARCHAR(255),                     -- 会议简介
    mtin_fin BOOL,                              -- 已完成，布尔类型
    mtin_st VARCHAR(255),                       -- 会议时间
    mtin_len VARCHAR(255),                      -- 会议长度
    mtin_host VARCHAR(255),                     -- 会议发起人
    mtin_loc VARCHAR(255),                      -- 会议地点
    mtin_crt VARCHAR(255)                       -- 记录创建时间
);


-- 创建用户关联的会议表
CREATE TABLE UserMeetings (
    user_id INT,                              -- 用户ID，主键且外键，参照用户表
    mtin_id INT,                              -- 会议ID，主键且外键，参照会议表
    adder_id INT,                             -- 添加人ID，外键，参照用户表
    PRIMARY KEY (user_id, mtin_id),           -- 联合主键：用户ID和会议ID
    FOREIGN KEY (user_id) REFERENCES user_infos(user_id),          -- 外键，关联用户表
    FOREIGN KEY (mtin_id) REFERENCES Meetings(mtin_id),            -- 外键，关联会议表
    FOREIGN KEY (adder_id) REFERENCES user_infos(user_id)          -- 外键，关联用户表
);



