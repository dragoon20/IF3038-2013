-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 25, 2013 at 11:01 AM
-- Server version: 5.5.29
-- PHP Version: 5.4.6-1ubuntu1.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `progin_439_13510007`
--

-- --------------------------------------------------------

--
-- Table structure for table `applications`
--

CREATE TABLE IF NOT EXISTS `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(200) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `redirect_url` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_id` (`app_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `applications`
--

INSERT INTO `applications` (`id`, `app_name`, `app_id`, `redirect_url`) VALUES
(1, 'MOA_web', '23192cc1851c9aec4201e2fb487a9c8b', 'http://localhost:8080/MOA/');

-- --------------------------------------------------------

--
-- Table structure for table `assign`
--

CREATE TABLE IF NOT EXISTS `assign` (
  `id_user` int(10) NOT NULL,
  `id_task` int(10) NOT NULL,
  PRIMARY KEY (`id_user`,`id_task`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `assign`
--

INSERT INTO `assign` (`id_user`, `id_task`) VALUES
(13, 33),
(13, 35),
(13, 36),
(13, 37),
(4, 38),
(6, 39);

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id_komentar` int(10) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `komentar` text NOT NULL,
  `id_user` int(10) NOT NULL,
  `id_task` int(10) NOT NULL,
  PRIMARY KEY (`id_komentar`),
  KEY `id_user` (`id_user`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id_komentar`, `timestamp`, `komentar`, `id_user`, `id_task`) VALUES
(1, '2013-04-22 09:50:09', 'asd', 14, 33),
(2, '2013-04-22 09:50:16', 'asdasd', 14, 33),
(3, '2013-04-22 09:50:19', 'adasd', 14, 33),
(4, '2013-04-22 09:50:22', 'asdas', 14, 33),
(5, '2013-04-22 09:50:26', 'asdsa', 14, 33),
(6, '2013-04-22 09:50:28', 'aswdasd', 14, 33),
(7, '2013-04-22 09:50:31', 'sasadas', 14, 33),
(8, '2013-04-22 09:50:35', 'sadasd', 14, 33),
(9, '2013-04-22 09:50:38', 'dasdas', 14, 33),
(10, '2013-04-22 09:50:41', 'sadsada', 14, 33),
(11, '2013-04-22 09:50:45', 'asdasdas', 14, 33);

-- --------------------------------------------------------

--
-- Table structure for table `edit_kategori`
--

CREATE TABLE IF NOT EXISTS `edit_kategori` (
  `id_user` int(11) NOT NULL,
  `id_kategori` int(11) NOT NULL,
  PRIMARY KEY (`id_user`,`id_kategori`),
  KEY `id_katego` (`id_kategori`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `edit_kategori`
--

INSERT INTO `edit_kategori` (`id_user`, `id_kategori`) VALUES
(3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `have_tags`
--

CREATE TABLE IF NOT EXISTS `have_tags` (
  `id_task` int(10) NOT NULL,
  `id_tag` int(10) NOT NULL,
  PRIMARY KEY (`id_task`,`id_tag`),
  KEY `id_tag` (`id_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `have_tags`
--

INSERT INTO `have_tags` (`id_task`, `id_tag`) VALUES
(37, 2),
(38, 2),
(33, 13),
(35, 14),
(36, 19),
(39, 20);

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE IF NOT EXISTS `kategori` (
  `id_kategori` int(10) NOT NULL AUTO_INCREMENT,
  `nama_kategori` varchar(100) NOT NULL,
  `id_user` int(10) NOT NULL,
  PRIMARY KEY (`id_kategori`),
  KEY `id_user` (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=38 ;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`, `id_user`) VALUES
(1, 'IF 2034', 2),
(5, 'tesgaya', 3),
(9, 'tes', 4),
(10, 'tes', 11),
(14, 'adasd', 3),
(16, 'asdasdads', 3),
(22, 'adasd', 13),
(24, 'asdasdasd', 13),
(30, 'asdad', 13),
(32, 'asdadasdasd', 13),
(33, 'adsad', 13),
(35, 'c', 13),
(37, 'baru', 14);

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
  `id_tag` int(10) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id_tag`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `tag`
--

INSERT INTO `tag` (`id_tag`, `tag_name`) VALUES
(20, ''),
(3, 'adf'),
(18, 'baru'),
(1, 'basdat'),
(4, 'fd'),
(19, 'hekll'),
(5, 'intelegensia buatan'),
(15, 'lagidanlagi'),
(12, 'lol'),
(17, 'plisterakhior'),
(11, 'progiiin'),
(2, 'rpl'),
(16, 'terakhir'),
(13, 'tes'),
(14, 'tesdoang');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE IF NOT EXISTS `task` (
  `id_task` int(10) NOT NULL AUTO_INCREMENT,
  `nama_task` varchar(50) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `deadline` date NOT NULL,
  `id_kategori` int(10) NOT NULL,
  `id_user` int(10) NOT NULL,
  PRIMARY KEY (`id_task`),
  KEY `id_kategori` (`id_kategori`),
  KEY `id_user` (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=40 ;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`id_task`, `nama_task`, `status`, `deadline`, `id_kategori`, `id_user`) VALUES
(33, 'baru lagi abcd', 0, '2013-04-19', 37, 14),
(35, 'baru lagi abcd', 0, '2013-04-19', 37, 14),
(36, 'baru lagi abcd', 0, '2013-04-19', 37, 14),
(37, 'baru lagi abcd', 0, '2013-04-19', 37, 14),
(38, 'hellothere', 0, '2013-04-10', 37, 14),
(39, 'hellothere 2', 0, '2013-04-26', 37, 14);

-- --------------------------------------------------------

--
-- Table structure for table `task_attachment`
--

CREATE TABLE IF NOT EXISTS `task_attachment` (
  `id_task` int(10) NOT NULL,
  `attachment` varchar(100) NOT NULL,
  PRIMARY KEY (`id_task`,`attachment`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `task_attachment`
--

INSERT INTO `task_attachment` (`id_task`, `attachment`) VALUES
(33, '02755266F84C5BD7C046FA57E28169E5.jpg'),
(33, '587EBFB75A0F7C3AB2477438399C49FE.jpg'),
(33, '9AA53007115236051AC0DBE535487A74.jpg'),
(33, 'BEBBF55D71D25BCC9742B78F190735BA.jpg'),
(33, 'CA395312CB167B0C010A8A690A7D1B39.jpg'),
(33, 'D4BDB3C8078C71E3E0F074C1C58EF68C.mp4'),
(33, 'DB71433886025D9C0DD8C0D136A12833.'),
(33, 'DBD0CAC101873C2A2A969D91834E5FFD.jpg'),
(33, 'EB975E49A16582B4C225B80AB4E60400.jpg'),
(35, '1FB384395E7EC6BB9A26C495D007964E.jpg'),
(36, '52CF7869C029B3332098607D6327E72C.'),
(36, 'D5D8BFE5D15540254115EF1DC7273C96.'),
(37, 'EB95EFCCE12C4AA4842E65A082C53122.'),
(38, '30520FA01A72186C12709B35B2F1BDA5.'),
(39, '757432B3270F22D3F971CFB967E0D6D5.');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `fullname` varchar(50) NOT NULL,
  `avatar` varchar(100) NOT NULL,
  `birthdate` date NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `username`, `email`, `fullname`, `avatar`, `birthdate`, `password`) VALUES
(2, 'asdfghjkl', 'adsf@yahoo.com', 'asdf ghjkl', '51BF9CCEC6439E231D13043486B6C50A.jpg', '2013-03-15', '25d55ad283aa400af464c76d713c07ad'),
(3, 'abrahamks', 'abrahamkrisnanda@outlook.com', 'abraham krisnanda', '4897178F18808820BA169AC2DFC3568A.JPG', '2013-03-15', '1441f19754335ca4638bfdf1aea00c6d'),
(4, 'admin', 'asdad@dasdsa.com', 'Jordan Fernando', 'B18D024454540A7B459FEA497EFB9D05.jpg', '2013-03-20', '2f029a1250c44708d7865338918648af'),
(6, 'abraham', 'abraham@gmail.com', 'abraham krisnanda', '2E267CC7E1DB6A827FB822F378B2C1B9.jpg', '2013-01-14', 'tesdoang'),
(11, 'dragoon20', 'fernandojordan.92@gmail.com', 'Jordan Fernando', '2147EF717843A5FB29052685D7A83420.jpg', '2012-01-01', '8637f9cb4045689929a1628a74db5d81'),
(13, 'dronJaX', 'master_crusader20@hotmail.com', 'Jordan Fernando', '9BD8FCAAC9A4ACD3D1DC2757740D70C9.jpg', '1992-11-21', '4a5a19444716417f381a3dda2215da32'),
(14, 'abraha', 'hahaha@gmail.com', 'abraham krisnanda', 'D398A0821C59E37E8035181BD8C54428.jpg', '2013-04-01', '1441f19754335ca4638bfdf1aea00c6d');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assign`
--
ALTER TABLE `assign`
  ADD CONSTRAINT `assign_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `assign_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `edit_kategori`
--
ALTER TABLE `edit_kategori`
  ADD CONSTRAINT `edit_kategori_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `edit_kategori_ibfk_2` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`);

--
-- Constraints for table `have_tags`
--
ALTER TABLE `have_tags`
  ADD CONSTRAINT `have_tags_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `have_tags_ibfk_4` FOREIGN KEY (`id_tag`) REFERENCES `tag` (`id_tag`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `kategori`
--
ALTER TABLE `kategori`
  ADD CONSTRAINT `kategori_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

--
-- Constraints for table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `task_ibfk_3` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `task_attachment`
--
ALTER TABLE `task_attachment`
  ADD CONSTRAINT `task_attachment_ibfk_2` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
