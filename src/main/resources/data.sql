DELETE FROM users;
DELETE FROM articles;

-- Insert sample users
INSERT INTO users (name, email, password) VALUES 
('John Doe', 'john.doe@example.com', 'password123'),
('Jane Smith', 'jane.smith@example.com', 'password456'),
('Mike Johnson', 'mike.johnson@example.com', 'password789');

INSERT INTO user_roles (user_id, role) VALUES 
(1, 'USER'),
(2, 'ADMIN'),
(3, 'USER');

-- Insert sample articles
INSERT INTO articles (title, content) VALUES
('Getting Started with GraphQL', 'GraphQL is a query language for APIs...'),
('Spring Boot Basics', 'Spring Boot makes it easy to create...'),
('JPA Tutorial', 'Java Persistence API (JPA) is...');