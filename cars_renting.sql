-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Czas generowania: 20 Sty 2018, 12:02
-- Wersja serwera: 5.7.19
-- Wersja PHP: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `cars_renting`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `cars`
--

DROP TABLE IF EXISTS `cars`;
CREATE TABLE IF NOT EXISTS `cars` (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_name` text COLLATE utf8_polish_ci NOT NULL,
  `c_type` text COLLATE utf8_polish_ci NOT NULL,
  `c_power` int(11) NOT NULL,
  `c_seats` int(11) NOT NULL,
  `c_year` int(11) NOT NULL,
  `c_price` int(11) NOT NULL,
  `c_plate` text COLLATE utf8_polish_ci NOT NULL,
  `c_added_by` int(11) NOT NULL,
  PRIMARY KEY (`c_id`),
  KEY `c_added_by` (`c_added_by`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `cars`
--

INSERT INTO `cars` (`c_id`, `c_name`, `c_type`, `c_power`, `c_seats`, `c_year`, `c_price`, `c_plate`, `c_added_by`) VALUES
(2, 'Audi A3 B4', 'Disel', 140, 4, 1996, 100, 'RZES170', 1),
(3, 'Mercedens Benz', 'Disel', 200, 4, 2010, 300, 'RZES500', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `cars_rented`
--

DROP TABLE IF EXISTS `cars_rented`;
CREATE TABLE IF NOT EXISTS `cars_rented` (
  `cr_id` int(11) NOT NULL AUTO_INCREMENT,
  `cr_car_id` int(11) NOT NULL,
  `cr_user` int(11) NOT NULL,
  `cr_date` text COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`cr_id`),
  KEY `cr_user` (`cr_user`),
  KEY `cr_car_id` (`cr_car_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `cars_rented`
--

INSERT INTO `cars_rented` (`cr_id`, `cr_car_id`, `cr_user`, `cr_date`) VALUES
(5, 3, 1, '19-01-2018 12:32:12');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_email` text COLLATE utf8_polish_ci NOT NULL,
  `u_password` text COLLATE utf8_polish_ci NOT NULL,
  `u_permission` int(11) NOT NULL DEFAULT '0',
  `u_name` text COLLATE utf8_polish_ci NOT NULL,
  `u_surname` text COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`u_id`, `u_email`, `u_password`, `u_permission`, `u_name`, `u_surname`) VALUES
(1, 'admin@admin.pl', '$2y$10$2EhzmLOsxMj6sEaJy.Jjduvn0DuZ.pYqFjA6g.aEAD2yGeYZeOnPa', 1, 'Admin', 'Adminowski');

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `cars`
--
ALTER TABLE `cars`
  ADD CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`c_added_by`) REFERENCES `users` (`u_id`);

--
-- Ograniczenia dla tabeli `cars_rented`
--
ALTER TABLE `cars_rented`
  ADD CONSTRAINT `cars_rented_ibfk_1` FOREIGN KEY (`cr_user`) REFERENCES `users` (`u_id`),
  ADD CONSTRAINT `cars_rented_ibfk_2` FOREIGN KEY (`cr_car_id`) REFERENCES `cars` (`c_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
