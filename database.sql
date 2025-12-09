-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 09, 2025 at 11:03 AM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `transport_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
CREATE TABLE IF NOT EXISTS `bus` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `matricule` varchar(50) DEFAULT NULL,
    `marque` varchar(50) DEFAULT NULL,
    `capacite` int DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bus`
--

INSERT INTO `bus` (`id`, `matricule`, `marque`, `capacite`) VALUES
                                                                (1, '1234530516', 'Toyota', 50),
                                                                (2, '2233530516', 'Hyunidai', 50),
                                                                (3, '1234530816', 'Mercides', 80);

-- --------------------------------------------------------

--
-- Table structure for table `chauffeur`
--

DROP TABLE IF EXISTS `chauffeur`;
CREATE TABLE IF NOT EXISTS `chauffeur` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `nom` varchar(50) DEFAULT NULL,
    `prenom` varchar(50) DEFAULT NULL,
    `password` varchar(255) NOT NULL,
    `type_permis` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `chauffeur`
--

INSERT INTO `chauffeur` (`id`, `nom`, `prenom`, `password`, `type_permis`) VALUES
    (2, 'Tadjine', 'Said', 'saidc1', 'c1');

-- --------------------------------------------------------

--
-- Table structure for table `etudiant`
--

DROP TABLE IF EXISTS `etudiant`;
CREATE TABLE IF NOT EXISTS `etudiant` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `nom` varchar(255) NOT NULL,
    `prenom` varchar(255) DEFAULT NULL,
    `num_carte` varchar(100) NOT NULL,
    `mot_de_passe` varchar(255) NOT NULL,
    `arret_principal` varchar(255) DEFAULT NULL,
    `statut_paiement` varchar(50) DEFAULT 'Non Payé',
    PRIMARY KEY (`id`),
    UNIQUE KEY `num_carte` (`num_carte`)
    ) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `etudiant`
--

INSERT INTO `etudiant` (`id`, `nom`, `prenom`, `num_carte`, `mot_de_passe`, `arret_principal`, `statut_paiement`) VALUES
    (10, 'Abdelfetah', 'NADINE', '232320051122', 'nadine2005', 'campus targa ouzemoure', 'Actif');

-- --------------------------------------------------------

--
-- Table structure for table `incidents`
--

DROP TABLE IF EXISTS `incidents`;
CREATE TABLE IF NOT EXISTS `incidents` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `bus_id` int NOT NULL,
                                           `chauffeur_id` int NOT NULL,
                                           `description` text,
                                           `date` datetime DEFAULT NULL,
                                           `status` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `bus_id` (`bus_id`),
    KEY `chauffeur_id` (`chauffeur_id`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `lignes`
--

DROP TABLE IF EXISTS `lignes`;
CREATE TABLE IF NOT EXISTS `lignes` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `nom_ligne` varchar(50) DEFAULT NULL,
    `trajet` varchar(255) DEFAULT NULL,
    `chauffeur_nom` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `paiements`
--

DROP TABLE IF EXISTS `paiements`;
CREATE TABLE IF NOT EXISTS `paiements` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `etudiant_id` int DEFAULT NULL,
                                           `montant` decimal(10,2) DEFAULT NULL,
    `date_paiement` date DEFAULT NULL,
    `statut` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `paiements`
--

INSERT INTO `paiements` (`id`, `etudiant_id`, `montant`, `date_paiement`, `statut`) VALUES
    (1, 1, 1500.00, '2023-09-01', 'ACTIF');

-- --------------------------------------------------------

--
-- Table structure for table `trajets`
--

DROP TABLE IF EXISTS `trajets`;
CREATE TABLE IF NOT EXISTS `trajets` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `point_depart` varchar(255) NOT NULL,
    `point_arrivee` varchar(255) NOT NULL,
    `heure_depart` time NOT NULL,
    `id_bus` int DEFAULT NULL,
    `id_chauffeur` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `id_bus` (`id_bus`),
    KEY `id_chauffeur` (`id_chauffeur`)
    ) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `trajets`
--

INSERT INTO `trajets` (`id`, `point_depart`, `point_arrivee`, `heure_depart`, `id_bus`, `id_chauffeur`) VALUES
    (1, 'residence  Iriyahen', 'Campus Targa ouzemour', '07:40:00', 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `nom` varchar(50) NOT NULL,
    `prenom` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `role` enum('admin','etudiant','chauffeur') NOT NULL,
    `num_carte` varchar(50) DEFAULT NULL,
    `arret_principal` varchar(100) DEFAULT NULL,
    `statut_paiement` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `nom` (`nom`)
    ) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nom`, `prenom`, `password`, `role`, `num_carte`, `arret_principal`, `statut_paiement`) VALUES
                                                                                                                       (12, 'Achouche', 'Adel', 'Adel1985', 'chauffeur', '2222331107845', 'Residance 17 octobre', 'Actif'),
                                                                                                                       (1, 'admin', 'admin', 'admin', 'admin', '', NULL, ''),
                                                                                                                       (13, 'Haggani ', 'Maria', 'maria2005', 'etudiant', '23233311987645', 'targa ouzemour', NULL),
                                                                                                                       (14, 'Benzaid', 'Islem', 'islem2005', 'etudiant', '232333102425', 'campus Targa ouzemour', NULL);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
