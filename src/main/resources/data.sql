INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (username, password, created_at, updated_at)
VALUES ('user1', '$2a$10$WWHRPyOHjzZZceRHOTD59.PA81kiCiaz8nClPY4SUwoVs0/Sfzmdy', NOW(), NOW());

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

INSERT INTO users (username, password, created_at, updated_at)
VALUES ('admin1', '$2a$10$WWHRPyOHjzZZceRHOTD59.PA81kiCiaz8nClPY4SUwoVs0/Sfzmdy', NOW(), NOW());

INSERT INTO user_roles (user_id, role_id)
VALUES (2, 2);