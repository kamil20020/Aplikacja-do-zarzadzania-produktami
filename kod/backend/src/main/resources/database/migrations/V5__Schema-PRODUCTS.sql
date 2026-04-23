CREATE TABLE IF NOT EXISTS PRODUCTS(
    product_id uuid,
    category_id uuid,
    name varchar(52) NOT NULL UNIQUE,
    price decimal(8, 2),
    description text NOT NULL ,
    CONSTRAINT PRODUCTS_PK PRIMARY KEY (product_id),
    CONSTRAINT PRODUCTS_CATEGORY_FK FOREIGN KEY (category_id) REFERENCES PRODUCTS_CATEGORIES(category_id)
);