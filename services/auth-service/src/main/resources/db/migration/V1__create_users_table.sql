CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE users
(
    id            BIGINT       NOT NULL DEFAULT nextval('users_id_seq'),
    username      VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    role          VARCHAR(50)  NOT NULL,
    status        VARCHAR(50)  NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

ALTER SEQUENCE users_id_seq
    OWNED BY users.id;