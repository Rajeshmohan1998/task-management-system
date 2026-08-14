CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(50) NOT NULL,
                      description TEXT,
                      priority VARCHAR(20) NOT NULL,
                      status VARCHAR(20) DEFAULT 'TODO',
                      due_date DATE NOT NULL,
                      assignee_username VARCHAR(100),
                      created_by_username VARCHAR(100)
);