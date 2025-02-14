CREATE TABLE `users`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `username`   VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `password`   VARCHAR(512) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `role`       ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `first_name` VARCHAR(255) NULL COLLATE 'utf8mb4_0900_ai_ci',
    `last_name`  VARCHAR(255) NULL COLLATE 'utf8mb4_0900_ai_ci',
    `email`      VARCHAR(255) NULL COLLATE 'utf8mb4_0900_ai_ci',
    `birthday`   DATE NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username` (`username`) USING BTREE
);
