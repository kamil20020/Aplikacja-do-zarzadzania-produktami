INSERT INTO ROLES
VALUES ('974d2cf7-8ba2-4280-a89e-343e193b1117', 'ADMIN');

INSERT INTO USERS
VALUES
   ('2ba6998e-f90c-4bbe-a8a4-ae08ce70ea8f', 'user', '$2a$10$qM1I98etD3te7IqPAnsOcOvK0NgWjHTvWuiiq1l5dXr/UNAdJJOr.', 'Kamil', 'Nowak'),
   ('792c8a7e-d36e-4a8e-a379-45d22c614523', 'admin', '$2a$10$qM1I98etD3te7IqPAnsOcOvK0NgWjHTvWuiiq1l5dXr/UNAdJJOr.', 'Adam', 'Kowalski');

INSERT INTO USERS_ROLES
VALUES
    ('792c8a7e-d36e-4a8e-a379-45d22c614523', '974d2cf7-8ba2-4280-a89e-343e193b1117');

INSERT INTO PRODUCTS_CATEGORIES
VALUES
    ('65d01898-f620-473d-9443-7d1ba31e3a6b', 'Elektronika'),
    ('80a962eb-962d-459f-8b8c-31cdbf1d697b', 'Jedzenie'),
    ('b30f7414-5be9-4485-ac84-e500d8de6271', 'Ubrania'),
    ('d2bd99a8-0023-41db-aa15-955522ed2ac4', 'Książki');

INSERT INTO PRODUCTS(product_id, name, category_id, price, description)
VALUES
    ('83d0fdc1-b259-434d-a718-e96f8e11e639', 'Myszka', '65d01898-f620-473d-9443-7d1ba31e3a6b', 23.26, 'Dobry produkt'),
    ('46842a77-f211-4132-8d12-33c3137107dc', 'Klawiatura', '65d01898-f620-473d-9443-7d1ba31e3a6b', 125.22, null),
    ('f037e1a6-bc20-4601-87a8-42f6fd969469', 'Komputer', null, 2000.28, 'Dobra maszyna'),
    ('dfa3d9e9-5515-45b2-b7d0-92ca8dc90e9e', 'Monitor', null, 262.48, null),
    ('9d4933dd-68c4-4585-ad74-08111ebae055', 'Koszulka', 'b30f7414-5be9-4485-ac84-e500d8de6271', 46.80, 'Dobry materiał');