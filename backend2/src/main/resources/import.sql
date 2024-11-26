/* ROLES*/
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_CLIENTE');

/* TIPOS*/


/* USERS*/
INSERT INTO usuarios (email,nombre,password,username) VALUES ('diego@gmail.com','diego','$2a$10$6WjuMGXunqUjSrU7g9vkwexZPrnv1Ei97xNCYz7BIzVSm0V1Li9Ie','diegocode001');
INSERT INTO usuarios (email,nombre,password,username) VALUES ('user001@gmail.com','userPrueba','$2a$10$6WjuMGXunqUjSrU7g9vkwexZPrnv1Ei97xNCYz7BIzVSm0V1Li9Ie','user001');
INSERT INTO usuarios (email,nombre,password,username) VALUES ('cliente01@gmail.com','client1','$2a$10$6WjuMGXunqUjSrU7g9vkwexZPrnv1Ei97xNCYz7BIzVSm0V1Li9Ie','client1');
INSERT INTO usuarios (email,nombre,password,username) VALUES ('cliente02@gmail.com','client2','$2a$10$6WjuMGXunqUjSrU7g9vkwexZPrnv1Ei97xNCYz7BIzVSm0V1Li9Ie','client2');
INSERT INTO usuarios (email,nombre,password,username) VALUES ('cliente03@gmail.com','client3','$2a$10$6WjuMGXunqUjSrU7g9vkwexZPrnv1Ei97xNCYz7BIzVSm0V1Li9Ie','client3');

INSERT INTO user_roles (user_id,role_id) VALUES (1,2);
INSERT INTO user_roles (user_id,role_id) VALUES (2,1);
INSERT INTO user_roles (user_id,role_id) VALUES (3,3);
INSERT INTO user_roles (user_id,role_id) VALUES (4,3);
INSERT INTO user_roles (user_id,role_id) VALUES (5,3);
