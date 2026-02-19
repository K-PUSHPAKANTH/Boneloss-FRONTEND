-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 03, 2026 at 03:04 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `boneloss`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(150) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_verified` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `email`, `phone`, `password`, `created_at`, `is_verified`) VALUES
(1, 'Sri Kanth', 'srikanth@gmail.com', '9876543210', '$2y$10$Mw/h5A5qf0.PNyv2uF1PuOjFYsCiK4n4ZElgfL0P.m6I0ObweSycq', '2026-02-02 16:45:09', 1),
(2, 'Praveen', 'praveen@gmail.com', '9876543210', '$2y$10$H7Bj1zavOKCOqAfMb3pgQeNml5coQKB2SFL3uEtPn/ePjU2Ygt.4i', '2026-02-02 16:45:48', 1),
(3, 'deva', 'deva@gmaul.com', '9848001113', '$2y$10$HmB1NJqR2qOEwHg5n0mbO.54yb6D7ScA9lEnHa76GSt8frYj6ujH2', '2026-02-03 07:23:07', 1),
(4, 'srinu', 'srinu@gmaul.com', '9705068572', '$2y$10$6Csvf1SiUCmoM/ibQKvxweLAHFhXedSwjjawbCoPPcSUYbO5Nt12S', '2026-02-03 07:23:47', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
