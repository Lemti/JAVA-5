# SETUP — Examen Java V

Fiche de démarrage : créer un projet Spring Boot et le rendre opérationnel.
Objectif : passer de « rien » à « BUILD SUCCESS » en moins de 10 minutes.

---

## 0. Vérifications préalables (à faire AVANT l'examen)

Dans un terminal :

```
java -version     → doit afficher 17 ou plus
mvn -version      → optionnel (le wrapper mvnw suffit)
```

Si `java` n'est pas reconnu alors qu'un JDK est installé, il manque au PATH.
Chercher où il se trouve :

```
Get-ChildItem "C:\Program Files\Java", "C:\Program Files\Eclipse Adoptium", "$env:USERPROFILE\.jdks" -ErrorAction SilentlyContinue
```

L'ajouter (permanent, sans droits admin — adapter le chemin) :

```
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot", "User")
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot\bin", "User")
```

Puis **fermer complètement VS Code et le rouvrir** (un nouveau terminal ne suffit pas).
Dépannage immédiat pour la session courante uniquement :

```
$env:Path += ";C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot\bin"
```

**Extensions VS Code requises :**
- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack (VMware) ← celle qui apporte Spring Initializr

Sans Lombok fonctionnel, tout le code annoté `@Getter` / `@RequiredArgsConstructor`
affiche des centaines de fausses erreurs. Vérifier avant, pas pendant.

---

## Méthode A — Spring Initializr dans VS Code

1. `Ctrl+Shift+P` → **Spring Initializr: Create a Maven Project**
2. Version Spring Boot → la dernière stable (éviter les SNAPSHOT)
3. Langage → **Java**
4. Group Id → `com.helb`
5. Artifact Id → `java5-examen`
6. Packaging → **Jar**
7. Java version → **17**
8. Dépendances → **Spring Web**, **Spring Data JPA**, **MySQL Driver**, **Lombok**
9. Dossier de destination → un dossier vide
10. Ouvrir le projet généré, laisser VS Code indexer

`spring-boot-starter-test` est ajouté automatiquement, inutile de le cocher.

---

## Méthode B — start.spring.io (navigateur)

Même service, version web. Utile si l'extension bugue.

1. Aller sur **start.spring.io**
2. Project → **Maven** | Language → **Java**
3. Spring Boot → dernière stable
4. Group `com.helb`, Artifact `java5-examen`
5. Packaging **Jar**, Java **17**
6. **ADD DEPENDENCIES** → Spring Web, Spring Data JPA, MySQL Driver, Lombok
7. **GENERATE** → télécharge un `.zip`
8. Décompresser, puis ouvrir le dossier dans VS Code

---

## Méthode C — repartir de son projet du cours (LA PLUS RAPIDE)

1. Copier le dossier d'un service existant, le renommer
2. Vider `src/main/java/...` en gardant la classe `@SpringBootApplication`
3. Renommer le projet dans `pom.xml` (`artifactId`, `name`)
4. Recréer les dossiers : `entity`, `repository`, `dao`, `service`, `controller`, `dto`

Avantage : le `pom.xml` et le `application.properties` sont déjà bons.
Aucune correction de version, aucun retéléchargement.

**→ Préparer un dossier `TEMPLATE-EXAMEN` prêt à copier.**

---

## ⚠️ Corriger le pom.xml (méthodes A et B)

Spring Boot 4 a **renommé** les starters. Comme le cours et toute la doc
disponible en ligne parlent de la 3.x, il faut redescendre de version.

**1. Version du parent :**
```xml
<version>3.5.5</version>
```

**2. Remplacer `spring-boot-starter-webmvc` par :**
```xml
<artifactId>spring-boot-starter-web</artifactId>
```

**3. Remplacer `-data-jpa-test` et `-webmvc-test` par une seule dépendance :**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Puis **enregistrer** (`Ctrl+S`) et lancer :

```
.\mvnw clean compile
```

Correspondance des noms :

| Spring Boot 3.x            | Spring Boot 4.x                                    |
|----------------------------|----------------------------------------------------|
| `spring-boot-starter-web`  | `spring-boot-starter-webmvc`                       |
| `spring-boot-starter-test` | `-data-jpa-test` + `-webmvc-test` (séparés)        |

---

## Erreurs courantes

| Message | Cause réelle |
|---|---|
| `'dependencies.dependency.version' ... is missing` | L'artefact n'existe pas dans cette version de Spring Boot (nom 4.x sur un parent 3.x, ou faute de frappe). **Pas** un numéro de version oublié. |
| Maven relit l'ancien pom | Fichier non enregistré → `Ctrl+S` |
| `java n'est pas reconnu` | JDK absent du PATH (voir section 0) |
| Erreurs `getX() undefined` partout | Lombok non actif → extensions VS Code |
| `Communications link failure` | MySQL n'est pas démarré |

---

## Ordre de construction du projet

Chaque couche doit compiler avant de passer à la suivante :

```
pom.xml + application.properties
      ↓
entity (+ relations JPA)
      ↓
repository (interfaces JpaRepository)
      ↓
dao
      ↓
dto
      ↓
service (logique métier + mapping entité ↔ DTO)
      ↓
controller (@RestController)
      ↓
tests unitaires
      ↓
README.md
```

---

## application.properties (MySQL)

```properties
spring.application.name=java5-examen
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/java5_examen?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

`ddl-auto=update` génère les **tables**, mais pas la base elle-même.
D'où `createDatabaseIfNotExist=true` dans l'URL : Spring crée la base au
démarrage, plus besoin d'ouvrir MySQL avant de coder.

Sans ce paramètre, créer la base à la main :
```sql
CREATE DATABASE java5_examen;
```

### Vérifier MySQL

```
Get-Service | Where-Object {$_.Name -like "*mysql*"}     → doit être Running
Start-Service MySQL80                                     → si arrêté
```

Le client CLI n'est pas dans le PATH par défaut (l'appli Spring n'en a pas
besoin, mais c'est pratique pour inspecter les données) :

```
$env:Path += ";C:\Program Files\MySQL\MySQL Server 8.0\bin"
mysql -u root -p
```

Mot de passe = celui de `spring.datasource.password` dans le projet du cours.
Rien ne s'affiche pendant la saisie, c'est normal.

### Lancer l'application

```
.\mvnw spring-boot:run
```

Succès = ligne `Started ...Application in X seconds`.
