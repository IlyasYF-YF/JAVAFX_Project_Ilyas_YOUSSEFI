CREATE DATABASE IF NOT EXISTS gestion_produits;
USE gestion_produits;
CREATE TABLE produit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    quantite_stock INT DEFAULT 0,
    disponibilite BOOLEAN DEFAULT TRUE
);
INSERT INTO plat VALUES (NULL,'P001','Soupe de tomates','Entree',5.50,20,1);
INSERT INTO plat VALUES (NULL,'P002','Salade nicoise','Entree',7.00,15,1);
INSERT INTO plat VALUES (NULL,'P003','Steak frites','Plat',14.50,10,1);
INSERT INTO plat VALUES (NULL,'P004','Poulet roti','Plat',12.00,8,1);
INSERT INTO plat VALUES (NULL,'P005','Pasta carbonara','Plat',11.00,12,1);
INSERT INTO plat VALUES (NULL,'P007','Tiramisu','Dessert',5.00,18,1);
