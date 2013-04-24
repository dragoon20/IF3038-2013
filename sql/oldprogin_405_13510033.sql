-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 12, 2013 at 04:08 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `progin_405_13510033`
--

-- --------------------------------------------------------

--
-- Table structure for table `assign`
--

CREATE TABLE IF NOT EXISTS `assign` (
  `username` varchar(10) NOT NULL,
  `id_task` varchar(10) NOT NULL,
  PRIMARY KEY (`username`,`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `username` varchar(50) NOT NULL,
  `id_task` varchar(10) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `komentar` text NOT NULL,
  PRIMARY KEY (`username`,`id_task`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`username`, `id_task`, `timestamp`, `komentar`) VALUES
('abrahamks', 'IF3038_001', '2013-03-11 14:06:09', 'saya buat media queries loh');

-- --------------------------------------------------------

--
-- Table structure for table `have_tags`
--

CREATE TABLE IF NOT EXISTS `have_tags` (
  `id_task` varchar(10) NOT NULL,
  `id_tag` int(5) NOT NULL,
  PRIMARY KEY (`id_task`,`id_tag`),
  KEY `id_tag` (`id_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `have_tags`
--

INSERT INTO `have_tags` (`id_task`, `id_tag`) VALUES
('IF3038_001', 1);

-- --------------------------------------------------------

--
-- Table structure for table `have_task`
--

CREATE TABLE IF NOT EXISTS `have_task` (
  `username` varchar(10) NOT NULL,
  `id_task` varchar(10) NOT NULL,
  PRIMARY KEY (`username`,`id_task`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE IF NOT EXISTS `kategori` (
  `id_kategori` varchar(10) NOT NULL,
  `nama_kategori` varchar(100) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `id_task` varchar(10) NOT NULL,
  PRIMARY KEY (`id_kategori`),
  KEY `username` (`username`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`, `username`, `id_task`) VALUES
('123', 'IF 2034 Basis Data', 'abrahamks', '0');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE IF NOT EXISTS `task` (
  `id_task` varchar(10) NOT NULL,
  `nama_task` varchar(50) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `attachment` varchar(225) NOT NULL,
  `deadline` date NOT NULL,
  PRIMARY KEY (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`id_task`, `nama_task`, `status`, `attachment`, `deadline`) VALUES
('IF3038_001', 'Tugas HTML5 CSS3', 1, 'asdfaf', '2013-03-12');

-- --------------------------------------------------------

--
-- Table structure for table `ttag`
--

CREATE TABLE IF NOT EXISTS `ttag` (
  `id_tag` int(5) NOT NULL,
  `tag` varchar(50) NOT NULL,
  PRIMARY KEY (`id_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ttag`
--

INSERT INTO `ttag` (`id_tag`, `tag`) VALUES
(1, 'RPL'),
(2, 'Basis Data');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(50) NOT NULL,
  `e-mail` varchar(50) NOT NULL,
  `fullname` varchar(50) NOT NULL,
  `avatar` varchar(100) NOT NULL,
  `birthdate` date NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `e-mail`, `fullname`, `avatar`, `birthdate`) VALUES
('abrahamks', 'abraham.k@students.itb.ac.id', 'Abraham Krisnanda S', 'adf', '2013-03-15');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  ADD CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`);

--
-- Constraints for table `have_tags`
--
ALTER TABLE `have_tags`
  ADD CONSTRAINT `have_tags_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`),
  ADD CONSTRAINT `have_tags_ibfk_4` FOREIGN KEY (`id_tag`) REFERENCES `ttag` (`id_tag`);

--
-- Constraints for table `have_task`
--
ALTER TABLE `have_task`
  ADD CONSTRAINT `have_task_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`),
  ADD CONSTRAINT `have_task_ibfk_2` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`);

--
-- Constraints for table `kategori`
--
ALTER TABLE `kategori`
  ADD CONSTRAINT `kategori_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
