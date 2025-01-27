INSERT INTO Role(id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER')
ON CONFLICT (id) DO NOTHING;

INSERT INTO AppUser(id, username, firstname, surname, email, phone_num, enc_password)
VALUES (1, 'Admin', 'ADMIN', 'ADMIN', 'Admin@gmail.com', '+380654785412', '$2a$10$e.lC9uZMHSaOBHjgRFMUJen3TBJLrroMIcxpeBQKB6j8dZbDsBIYG'),
       (2, 'Danchick', 'Daniil', 'Soboliev', 'DanSob@gmail.com', '+380794785433', '$2a$10$uHVMb6KYlogP7T4C0UPLZ.mkR2zmh4eM/rxPk82KQ76.3bmV8uYGC');

INSERT INTO UserRole(id, user_id, role_id)
VALUES (1, 1, 1),
       (2, 2, 2);