CREATE TABLE IF NOT EXISTS USERS(
    user_id uuid,
    username varchar(32) NOT NULL UNIQUE,
    password varchar(72) NOT NULL,
    firstname varchar(42) NOT NULL,
    lastname varchar(58) NOT NULL,
    CONSTRAINT USERS_PK PRIMARY KEY (user_id)
);