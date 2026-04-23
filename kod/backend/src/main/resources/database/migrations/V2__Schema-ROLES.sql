CREATE TABLE IF NOT EXISTS ROLES(
    role_id uuid,
    name varchar(26) NOT NULL UNIQUE,
    CONSTRAINT ROLES_PK PRIMARY KEY (role_id)
);