-- =====================================================
-- Script de création de la base de données
-- Projet: Gestion de Stock et Commandes
-- Auteur: Harith - IODA
-- Date: 2025-12-10
-- =====================================================

-- =====================================================
-- 1. CRÉATION DES BASES DE DONNÉES
-- =====================================================

DROP DATABASE IF EXISTS productdb;
DROP DATABASE IF EXISTS orderdb;

CREATE DATABASE productdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE orderdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =====================================================
-- 2. BASE DE DONNÉES: productdb
-- =====================================================

USE productdb;

-- Table: categories
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    CONSTRAINT uk_category_name UNIQUE (name)
) ENGINE=InnoDB;

-- Table: products
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    category_id BIGINT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE SET NULL,
    INDEX idx_category_id (category_id),
    INDEX idx_product_name (name)
) ENGINE=InnoDB;

-- =====================================================
-- 3. BASE DE DONNÉES: orderdb
-- =====================================================

USE orderdb;

-- Table: orders
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    INDEX idx_order_date (order_date),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- Table: order_lines
CREATE TABLE order_lines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price > 0),
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_orderline_order FOREIGN KEY (order_id) 
        REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB;

-- =====================================================
-- 4. DONNÉES DE TEST (OPTIONNEL)
-- =====================================================

USE productdb;

-- Insertion de catégories de test
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Clothing', 'Clothes and fashion items'),
('Books', 'Books and publications');

-- Insertion de produits de test
INSERT INTO products (name, description, price, stock_quantity, category_id) VALUES
('Laptop Dell XPS 15', 'High performance laptop with 16GB RAM', 1500.00, 10, 1),
('iPhone 15 Pro', 'Latest Apple smartphone', 1200.00, 15, 1),
('T-Shirt Cotton', 'Comfortable cotton t-shirt', 25.00, 50, 2),
('Jeans Levi''s', 'Classic blue jeans', 80.00, 30, 2),
('Spring Boot in Action', 'Complete guide to Spring Boot', 45.00, 20, 3);

-- =====================================================
-- 5. VÉRIFICATIONS
-- =====================================================

-- Vérifier les tables de productdb
USE productdb;
SHOW TABLES;
SELECT COUNT(*) as 'Nombre de catégories' FROM categories;
SELECT COUNT(*) as 'Nombre de produits' FROM products;

-- Vérifier les tables de orderdb
USE orderdb;
SHOW TABLES;
SELECT COUNT(*) as 'Nombre de commandes' FROM orders;
SELECT COUNT(*) as 'Nombre de lignes de commande' FROM order_lines;

-- =====================================================
-- FIN DU SCRIPT
-- =====================================================
