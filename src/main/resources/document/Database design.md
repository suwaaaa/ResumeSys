```mysql
//用户表 (user)
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

```

```mysql
//简历表 (resume)
CREATE TABLE resume (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    title VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    summary TEXT NOT NULL,
    skills TEXT,
    languages TEXT,
    certifications TEXT,
    publications TEXT,
    referencesName TEXT,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

```

```mysql
//工作经验表 (experience)
CREATE TABLE experience (
    id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT,
    job_title VARCHAR(255) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    job_description TEXT NOT NULL,
    FOREIGN KEY (resume_id) REFERENCES resume(id)
);

```

```mysql
//聊天表 (chat)
CREATE TABLE chat (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    message TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);

```

```mysql
//消息表 (message)
CREATE TABLE message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chat_id INT,
    sender_id INT,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (sender_id) REFERENCES user(id)
);

```

```mysql
//通知表 (notification)
CREATE TABLE notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    message TEXT NOT NULL,
    read_status BOOLEAN DEFAULT 0,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

```

