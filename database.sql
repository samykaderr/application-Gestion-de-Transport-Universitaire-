-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Nov 28, 2025 at 08:59 PM
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
    `etat` varchar(20) DEFAULT 'Disponible',
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `bus`
--

INSERT INTO `bus` (`id`, `matricule`, `marque`, `capacite`, `etat`) VALUES
    (1, '1234530516', 'Toyouta', 50, 'Disponible');

-- --------------------------------------------------------

--
-- Table structure for table `chauffeurs`
--

DROP TABLE IF EXISTS `chauffeurs`;
CREATE TABLE IF NOT EXISTS `chauffeurs` (
                                            `id` int NOT NULL AUTO_INCREMENT,
                                            `nom` varchar(50) DEFAULT NULL,
    `prenom` varchar(50) DEFAULT NULL,
    `permis` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `chauffeurs`
--

INSERT INTO `chauffeurs` (`id`, `nom`, `prenom`, `permis`) VALUES
    (1, 'Achouche', 'Said', 'C2');

-- --------------------------------------------------------

--
-- Table structure for table `etudiants`
--

DROP TABLE IF EXISTS `etudiants`;
CREATE TABLE IF NOT EXISTS `etudiants` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `nom_complet` varchar(100) DEFAULT NULL,
    `num_carte` varchar(50) DEFAULT NULL,
    `arret_principal` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
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
    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
    `role` enum('admin','student') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `nom` (`nom`)
    ) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nom`, `prenom`, `password`, `role`) VALUES
                                                                    (1, 'admin', 'admin', 'admin', 'admin'),
                                                                    (2, 'Moussaoui', 'Ryad', 'password123', 'student'),
                                                                    (3, '', '', '', 'student');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
