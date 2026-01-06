# Projet Java V - Stock and Order Management System

**Auteur:** Harith  
**Institution:** IODA - Mobile Development  
**Cours:** Java Programming V  
**Date:** Décembre 2025

---

## 📋 Vue d'ensemble du projet

Ce projet implémente un système de gestion de stock et de commandes basé sur une architecture microservices utilisant Spring Boot et MySQL.

---

## 📦 Structure du projet

```
Projet_Java_V_Harith_SUBMISSION/
├── services/
│   ├── product-service/          # Service de gestion des produits
│   └── order-service/             # Service de gestion des commandes
├── documentation/
│   ├── Technical_Documentation_Complete.docx
│   ├── Database_Schema.docx
│   └── Test_Documentation.docx
└── database/
    └── database_creation.sql      # Script SQL de création des bases
```

---

## 🚀 Démarrage rapide

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- IntelliJ IDEA (recommandé) ou tout autre IDE Java

### Étape 1 : Créer les bases de données MySQL

```sql
CREATE DATABASE productdb;
CREATE DATABASE orderdb;
SHOW DATABASES;
```

### Étape 2 : Configurer les services

Les services sont déjà configurés avec :
- **product-service** : Port 8081, Base productdb
- **order-service** : Port 8082, Base orderdb
- **Mot de passe MySQL** : root (à changer si nécessaire dans application.properties)

### Étape 3 : Lancer les services

#### Dans IntelliJ IDEA :

1. Ouvrir **product-service**
   - File → Open → `services/product-service`
   - Attendre le téléchargement des dépendances Maven
   - Run `ProductServiceApplication.java`

2. Ouvrir **order-service** (dans une nouvelle fenêtre)
   - File → Open → `services/order-service`
   - Attendre le téléchargement des dépendances Maven
   - Run `CommandeServiceApplication.java`

#### En ligne de commande :

```bash
# Terminal 1 - Product Service
cd services/product-service
mvn spring-boot:run

# Terminal 2 - Order Service
cd services/order-service
mvn spring-boot:run
```

---

## 🧪 Tester l'application

### Avec Postman

#### 1. Créer une catégorie
```
POST http://localhost:8081/api/categories
Content-Type: application/json

{
  "name": "Electronics",
  "description": "Electronic devices"
}
```

#### 2. Créer un produit
```
POST http://localhost:8081/api/products
Content-Type: application/json

{
  "name": "Laptop Dell XPS 15",
  "description": "High performance laptop",
  "price": 1500.00,
  "stockQuantity": 10,
  "categoryId": 1
}
```

#### 3. Créer une commande
```
POST http://localhost:8082/api/orders
Content-Type: application/json

{
  "orderLines": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

#### 4. Vérifier la diminution du stock
```
GET http://localhost:8081/api/products/1
```
Le stock devrait être passé de 10 à 8.

---

## 📚 Documentation

### 1. Documentation technique (EN ANGLAIS)
`documentation/Technical_Documentation_Complete.docx`
- Définition complète des endpoints API
- Explication de la logique métier
- Workflow détaillé de l'application
- Choix de conception justifiés

### 2. Schéma de base de données
`documentation/Database_Schema.docx`
- Structure détaillée des tables
- Relations entre les tables
- Justification de la conception

### 3. Documentation des tests
`documentation/Test_Documentation.docx`
- Tests unitaires (43 tests au total)
- Tests d'intégration
- Guide d'exécution des tests

---

## 🗄️ Architecture de la base de données

### productdb
- **categories** : Catégories de produits
- **products** : Produits avec stock

### orderdb
- **orders** : Commandes clients
- **order_lines** : Lignes de commande (détails)

**Note importante :** Les bases sont séparées selon le principe "database per service" des microservices.

---

## 🏗️ Architecture technique

```
┌─────────────────┐         REST API         ┌─────────────────┐
│  product-service│ ◄────────────────────────│  order-service  │
│   (Port 8081)   │                          │   (Port 8082)   │
└────────┬────────┘                          └────────┬────────┘
         │                                            │
         ▼                                            ▼
   ┌──────────┐                                 ┌──────────┐
   │productdb │                                 │ orderdb  │
   └──────────┘                                 └──────────┘
```

### Architecture en couches (chaque service)
```
Controller → Service → DAO → Repository → Entity
```

---

## ✅ Fonctionnalités implémentées

- ✅ Gestion complète des produits (CRUD)
- ✅ Gestion des catégories de produits
- ✅ Création de commandes avec validation
- ✅ Vérification automatique du stock
- ✅ Diminution automatique du stock lors d'une commande
- ✅ Communication REST entre microservices
- ✅ Gestion des erreurs et validation
- ✅ Architecture microservices complète
- ✅ Base de données MySQL (pas H2)

---

## 📊 Technologies utilisées

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **Spring Web**
- **MySQL 8.0.44**
- **Maven**
- **Lombok**
- **Hibernate**

---

## 🔧 Configuration

### Modifier le mot de passe MySQL

Si votre mot de passe MySQL n'est pas "root", modifiez dans les deux services :
- `product-service/src/main/resources/application.properties`
- `order-service/src/main/resources/application.properties`

Ligne à modifier :
```properties
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

---

## ⚙️ Commandes Maven utiles

```bash
# Compiler le projet
mvn clean compile

# Lancer les tests
mvn test

# Créer le JAR
mvn clean package

# Lancer l'application
mvn spring-boot:run
```

---

## 📝 Livrables du projet

1. ✅ **Code source** : 2 microservices complets
2. ✅ **Documentation technique** : EN ANGLAIS, complète
3. ✅ **Tests** : Unitaires et d'intégration (43 tests)
4. ✅ **Schéma BDD** : Documentation + Script SQL
5. ✅ **Base de données** : MySQL (productdb, orderdb)
6. ✅ **Fonctionnalités** : Toutes implémentées et testées

---

## 🎯 Points techniques clés

### Communication inter-services
order-service communique avec product-service via REST API pour :
- Vérifier l'existence des produits
- Vérifier le stock disponible
- Diminuer le stock après validation de la commande

### Gestion des transactions
- Utilisation de `@Transactional` pour garantir la cohérence des données
- Rollback automatique en cas d'erreur

### Dénormalisation intelligente
Les informations produit (nom, prix) sont stockées dans order_lines pour :
- Préserver l'historique exact de la commande
- Ne pas être affectées par les modifications ultérieures des produits

---

## 🐛 Résolution des problèmes

### Erreur de connexion MySQL
```
Access denied for user 'root'@'localhost'
```
**Solution :** Vérifier que MySQL est démarré et que le mot de passe est correct dans application.properties

### Port déjà utilisé
```
Port 8081 was already in use
```
**Solution :** Arrêter l'autre application sur ce port ou modifier le port dans application.properties

### Base de données inexistante
```
Unknown database 'productdb'
```
**Solution :** Exécuter le script database_creation.sql

---

## 👨‍💻 Auteur

**Harith**  
IODA - Mobile Development  
Année 2024-2025

---

## 📞 Support

Pour toute question concernant le projet, se référer à la documentation technique complète dans le dossier `documentation/`.

---

**Projet réalisé dans le cadre du cours Java Programming V**
