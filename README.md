# Yoga App (OCR Projet 5)

## Introduction
Après avoir récupéré le projet sur GitHub, il faudra démarrer 3 éléments pour utiliser l'application:
- La base de données
- Le serveur backend (API)
- Le serveur frontend

## Pré-requis
Avant de récupérer le projet, il faudra avoir installé un certain nombre d'éléments au préablable:

### Serveur MySQL
Disponible à cette adresse: 

`https://dev.mysql.com/downloads/installer/`

### Apache Maven
Disponible à cette adresse: 

`https://maven.apache.org/download.cgi`

### Java Development Kit (JDK), comprenant Java Runtime Environment (JRE)
Disponible à cette adresse: 

`https://www.oracle.com/java/technologies/downloads/`

Ce projet utilise Java version 8

### Node Package Manager (NPM)
Utiliser la commande `npm install` dans l'invite de commandes (cmd), en mode administrateur si besoin

## Récupérer le projet 
Pour récupérer le projet, il est possible de télécharger le projet sur GitHub en format compressé (.zip), ou de le récupérer en le clonant à l'aide de cette commande (à condition que Git soit installé sur votre poste): 

`git clone https://github.com/Keijur0/Testez-une-application-full-stack.git`

Décompresser l'archive (.zip) à l'emplacement souhaité si besoin.

## Mettre en place le serveur MySQL
Lors de l'installation de MySQL Server, prenez soin de créer l'utilisateur avec le mot de passe indiqué dans le fichier application.properties (chemin back/src/main/resources).

Après avoir effectué l'installation de MySQL Server, il faut créer la variable d'environnement MySQL, qui pointe vers le répertoire `/bin` de MySQL Server.

### Se connecter à la base de données
Une fois effectué, aller dans l'invite de commandes et utilisez la commande suivante:

`mysql -u <nom d'utilisateur> -p`

Utilisez le nom d'utilisateur et le mot de passe que vous avez créé lors de l'installation de MySQL Server.

Vous aurez besoin de ces identifiants lors du lancement de l'API. 

Je nommerai ces éléments `<nom d'utilisateur>` et  `<mot de passe>` dans la commande de lancement.

### Créer la base de données
Pour créer la base de données, utilisez la commande suivante:

`CREATE DATABASE test`

C'est le nom de la base de données indiquée dans les paramètres de l'API. Si vous souhaitez l'appeler autrement, il sera nécessaire d'ajuster les propriétés de l'API.

### Création des tables
Tout d'abord, quittez MySQL Server en tappant `exit` pour vous retrouver dans l'invite de commandes.

Tappez ensuite `mysql -u <nom d'utilisateur> -p chatop < <chemin/vers/le/fichier/script.sql>`

Puis tappez le mot de passe associé à ce compte utilisateur.

(Le fichier se trouve ici: `ressources\sql`)

### Vérification de la base
Pour vérifier si la base a été installée tappez:

`mysql -u <nom d'utilisateur> -p` puis Validez. Ensuite tappez le `<mot de passe>` pour vous connecter au serveur

`SHOW DATABASES;` pour vérifier si `test` se trouve parmi les bases existantes

`USE test;` pour choisir d'utiliser cette base

`SHOW TABLES;` pour montrer les tables de la base

`SHOW COLUMNS FROM <nom de la table>` pour voir le nom des colonnes de chaque table

## Installation des dépendances

### Frontend
Placez vous dans le dossier `/front` du projet, exécutez la commande `npm install`.

### Backend
Placez vous dans le dossier `/back` du projet, exécutez la commande `mvn clean install`.

## Lancer le projet Java (API / Backend)
Pour lancer le projet Java, il vous faut d'abord créer les variables d'environnement pour Java et Maven, pointant vers leurs dossiers respectifs `/bin`.

Une fois effectué, placez-vous dans le dossier `/back` du projet et tappez la commande:

`mvn spring-boot:run`

## Lancer le projet Angular (Frontend)
Pour lancer le projet Angular, placez-vous dans le dossier `/front` du projet et tappez la commande:

`npm run start`

## Utiliser l'application
Pour utiliser l'application, rendez vous à l'url suivante sur votre navigateur web: 

`http://localhost:4200`

## Tests

### Frontend
Placez-vous dans le dossier `/front` du projet.

Pour lancer les tests: `npm run test`.

Vous pourrez aussi accéder au tableau de bord des couvertures de tests en ouvrant le fichier `index.html`.
Il se trouve à ce chemin `\front\coverage\jest\lcov-report\`.

### Backend
Placez-vous dans le dossier `/back` du projet.

Pour lancer les tests: `mvn clean test`
Pour accéder au tableau de bord des couvertures de tests: `/back/target/site/jacoco/index.html`

### Tests end to end
Pour lancer les tests end to end: `npm run e2e`.
Pour générer la couverture: `npm run e2e:coverage`.

Vous pourrez aussi accéder au tableau de bord des couvertures de tests en ouvrant le fichier `index.html`.
Il se trouve à ce chemin `\front\coverage\lcov-report\`.

### Couvertures de tests
Les captures d'écran de couverture de tests se trouvent à ce chemin: `/ressources/coverage/`



