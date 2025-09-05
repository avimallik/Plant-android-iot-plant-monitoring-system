-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 03, 2025 at 07:58 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `turf_iq_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `irrigation_readings`
--

CREATE TABLE `irrigation_readings` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `ts` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `soil_moisture_pct` decimal(5,2) NOT NULL,
  `temperature_c` decimal(5,2) DEFAULT NULL,
  `humidity_pct` decimal(5,2) DEFAULT NULL,
  `sunlight_lux` int(11) DEFAULT NULL,
  `rain_detected` tinyint(1) NOT NULL DEFAULT 0,
  `turfgrass_type` varchar(50) NOT NULL DEFAULT 'Bermuda',
  `mad_pct` tinyint(4) NOT NULL DEFAULT 50,
  `irrigation_needed` enum('yes','no') NOT NULL,
  `device_ip` varchar(45) DEFAULT NULL,
  `rssi` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `irrigation_readings`
--

INSERT INTO `irrigation_readings` (`id`, `ts`, `soil_moisture_pct`, `temperature_c`, `humidity_pct`, `sunlight_lux`, `rain_detected`, `turfgrass_type`, `mad_pct`, `irrigation_needed`, `device_ip`, `rssi`, `created_at`) VALUES
(29, '2025-09-03 10:54:45', 75.00, 22.66, 81.65, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 10:54:45'),
(30, '2025-09-03 10:56:45', 75.00, 22.65, 81.67, 0, 1, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 10:56:45'),
(31, '2025-09-03 10:58:45', 75.00, 22.64, 81.70, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 10:58:45'),
(32, '2025-09-03 11:00:45', 75.00, 22.63, 81.72, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:00:45'),
(33, '2025-09-03 11:02:45', 75.00, 22.62, 81.74, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:02:45'),
(34, '2025-09-03 11:04:45', 75.00, 22.61, 81.76, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:04:45'),
(35, '2025-09-03 11:06:45', 75.00, 22.60, 81.78, 0, 1, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:06:45'),
(36, '2025-09-03 11:08:45', 75.00, 22.59, 81.80, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:08:45'),
(37, '2025-09-03 11:10:45', 75.00, 22.58, 81.82, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:10:45'),
(38, '2025-09-03 11:12:45', 75.00, 22.57, 81.84, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:12:45'),
(39, '2025-09-03 11:14:45', 75.00, 22.57, 81.86, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:14:45'),
(40, '2025-09-03 11:16:45', 75.00, 22.56, 81.87, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:16:45'),
(41, '2025-09-03 11:18:45', 75.00, 22.55, 81.89, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:18:45'),
(42, '2025-09-03 11:20:45', 75.00, 22.55, 81.90, 0, 1, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:20:45'),
(43, '2025-09-03 11:22:45', 75.00, 22.54, 81.91, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:22:45'),
(44, '2025-09-03 11:24:45', 75.00, 22.53, 81.92, 0, 0, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:24:45'),
(45, '2025-09-03 11:26:45', 75.00, 22.53, 81.94, 0, 1, 'Bermuda', 50, 'no', '192.168.0.193', NULL, '2025-09-03 11:26:45');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `irrigation_readings`
--
ALTER TABLE `irrigation_readings`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `irrigation_readings`
--
ALTER TABLE `irrigation_readings`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
