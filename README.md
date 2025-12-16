# 🚌 UniTransit - Application de Gestion de Transport Universitaire

**UniTransit** est une application de bureau (Desktop) développée en **Java**, conçue pour centraliser et faciliter la gestion du transport universitaire. Elle permet de gérer les acteurs (étudiants, chauffeurs), les ressources (bus, trajets) et le suivi des incidents via une interface graphique intuitive.

Ce projet a été réalisé dans le cadre du module de **Génie Logiciel (GL)** à l'Université A. MIRA de Béjaïa (2025/2026).

---

## 🚀 Fonctionnalités Principales

L'application sécurise l'accès via un système d'authentification et adapte le tableau de bord (Dashboard) selon le rôle de l'utilisateur :

### 👤 Administrateur
* **Gestion des Ressources :** Ajout et modification des Bus et des Chauffeurs.
* **Gestion des Utilisateurs :** Administration des comptes étudiants et chauffeurs.
* **Planification :** Création et gestion des Trajets (assignation Bus/Chauffeur).
* **Suivi :** Visualisation des incidents signalés et suivi des statuts de paiement.

### 🎓 Étudiant
* **Consultation :** Voir la liste des trajets disponibles et les horaires.
* **Inscription :** S'inscrire à un trajet spécifique.
* **Statut :** Vérifier son statut de paiement (Payé / Non Payé).

### 🚍 Chauffeur
* **Planning :** Consulter son emploi du temps et ses trajets assignés.
* **Incidents :** Signaler un incident survenu durant un trajet (panne, retard, etc.).

---

## 🛠 Architecture Technique

Le projet respecte une architecture **3-Tiers** stricte pour assurer la maintenabilité et l'évolution du code :

1.  **Package UI (Vue) :** Interfaces graphiques en **Java Swing**. Interagit uniquement avec la couche Service.
2.  **Package Service (Logique Métier) :** Orchestre les opérations, valide les règles métier (ex : vérification de solde) et fait le lien entre l'UI et les données.
3.  **Package DATA (DAO) :** Gestion de la persistance des données via **JDBC**. Utilisation du pattern DAO (Data Access Object) pour les opérations CRUD.
4.  **Package Model :** Objets Java (POJO) représentant les entités de la base de données.

---

## 💾 Modèle de Données (MySQL)

La persistance est assurée par une base de données MySQL `transport_db`. Le schéma relationnel inclut les tables suivantes :

* `comptes_utilisateurs` : Gestion centralisée des identifiants et rôles.
* `etudiant`, `chauffeur`, `admin` : Profils liés aux comptes.
* `bus`, `trajet` : Gestion logistique.
* `incident` : Suivi des problèmes liés aux bus/chauffeurs.
* `paiements`, `etudiant_trajet` : Tables de liaison et de suivi.

---

## ⚙️ Prérequis et Installation

### Prérequis
* **Java JDK** (version 8 ou supérieure).
* **MySQL Server** (ou WAMP/XAMPP).
* **Connecteur MySQL JDBC** (ajouté au Classpath du projet).

### Installation

1.  **Cloner le dépôt :**
    ```bash
    git clone [https://github.com/samykaderr/UniTransit.git](https://github.com/samykaderr/UniTransit.git)
    ```

2.  **Configuration de la Base de Données :**
    * Ouvrez votre gestionnaire SQL (phpMyAdmin, MySQL Workbench).
    * Créez une base de données nommée `transport_db` (ou adaptez le fichier de config).
    * Importez le script SQL fourni dans le dossier `database/script.sql` (voir Annexe du rapport pour le script complet).

3.  **Lancer l'application :**
    * Importez le projet dans votre IDE favori (IntelliJ, Eclipse, NetBeans).
    * Vérifiez que la configuration de connexion à la BDD (URL, User, Password) dans la classe de connexion correspond à votre installation locale.
    * Exécutez la classe principale (contenant le `main`, souvent `LoginFrame` ou `App`).

---

## 👥 Auteur

**ACHOUCHE Samy**
* Étudiant en 3ème année Licence Informatique.
* Université A. MIRA - Béjaïa.
* [GitHub](https://github.com/samykaderr/)

---

*Encadré par : Dr. Mohamed MOHAMMEDI*
