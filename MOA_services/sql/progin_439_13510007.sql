-- phpMyAdmin SQL Dump
-- version 3.5.8.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 01, 2013 at 11:48 AM
-- Server version: 5.5.31-0ubuntu0.13.04.1
-- PHP Version: 5.4.9-4ubuntu2

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `applications`
--

INSERT INTO `applications` (`id`, `app_name`, `app_id`, `redirect_url`) VALUES
(1, 'MOA_web', '23192cc1851c9aec4201e2fb487a9c8b', 'http://localhost:8080/MOA/'),
(2, 'MOA_mobile', 'e55d47b937fee41b1cd75a01fbf76bc2', 'http://localhost:8080/MOA/'),
(3, 'Google', 'a5d0dae7872611a27c3e01591fdffb5d', 'http://google.com'),
(4, 'janice', 'fd4c6a9f93717cd136f3053def617c91', 'www.google.com'),
(5, 'Sharon', 'dda15a7a60ec3d93467e46a46f4ab9b7', 'www.google.com'),
(6, 'MOA_web_final', '0d2d2a7531376b3b05ff4203aeaa6b41', 'http://localhost:8080/MOA_web/login_check');

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
(13, 36),
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id_komentar`, `timestamp`, `komentar`, `id_user`, `id_task`) VALUES
(12, '2013-04-29 11:52:10', 'lalal', 14, 37),
(17, '2013-05-01 02:05:47', 'asdasd', 14, 36),
(18, '2013-05-01 02:06:14', 'asdasdas', 14, 36),
(19, '2013-05-01 02:06:29', 'asdasd', 14, 36),
(20, '2013-05-01 02:20:44', 'asdasd', 14, 36),
(21, '2013-05-01 02:22:40', 'asdasdasd', 14, 36),
(22, '2013-05-01 02:24:21', 'asdasd', 14, 36),
(23, '2013-05-01 02:25:44', 'asdasd', 14, 36),
(24, '2013-05-01 02:30:31', 'asdasd', 14, 36),
(25, '2013-05-01 02:30:31', 'asdasd', 14, 36),
(26, '2013-05-01 02:30:31', 'asdasd', 14, 36),
(27, '2013-05-01 02:30:53', 'adasda', 14, 36),
(28, '2013-05-01 02:32:24', 'asdasda', 14, 40),
(29, '2013-05-01 02:32:50', 'sadasd', 14, 40),
(30, '2013-05-01 02:35:07', 'adssad', 14, 40),
(31, '2013-05-01 02:37:02', 'adsad', 14, 40),
(32, '2013-05-01 02:37:13', 'aa', 14, 40),
(33, '2013-05-01 02:38:32', 'b', 14, 40),
(34, '2013-05-01 02:39:49', 'adsa', 14, 40),
(35, '2013-05-01 02:41:38', 'adsadas', 14, 40),
(36, '2013-05-01 02:41:44', 'sadasd', 14, 40),
(37, '2013-05-01 02:41:45', 'asdasd', 14, 40),
(38, '2013-05-01 02:41:48', 'adsasd', 14, 40);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=41 ;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`id_task`, `nama_task`, `status`, `deadline`, `id_kategori`, `id_user`) VALUES
(36, 'baru lagi abcd', 0, '2013-04-19', 37, 14),
(37, 'haha', 0, '2010-04-19', 37, 14),
(39, 'hellothere 2', 0, '2013-04-26', 37, 14),
(40, 'janice', 0, '2013-04-02', 37, 14);

-- --------------------------------------------------------

--
-- Table structure for table `task_attachment`
--

CREATE TABLE IF NOT EXISTS `task_attachment` (
  `id_task` int(10) NOT NULL,
  `attachment` varchar(200) NOT NULL,
  PRIMARY KEY (`id_task`,`attachment`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `task_attachment`
--

INSERT INTO `task_attachment` (`id_task`, `attachment`) VALUES
(36, '52CF7869C029B3332098607D6327E72C.'),
(36, 'D5D8BFE5D15540254115EF1DC7273C96.'),
(37, 'EB95EFCCE12C4AA4842E65A082C53122.'),
(39, '757432B3270F22D3F971CFB967E0D6D5.');

-- --------------------------------------------------------

--
-- Table structure for table `tokens`
--

CREATE TABLE IF NOT EXISTS `tokens` (
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `token` varchar(200) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_app` int(11) NOT NULL,
  PRIMARY KEY (`id_user`,`id_app`),
  UNIQUE KEY `UNIQUE_token` (`token`),
  KEY `id_app` (`id_app`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tokens`
--

INSERT INTO `tokens` (`timestamp`, `token`, `id_user`, `id_app`) VALUES
('2013-04-29 12:30:59', 'f5dc66645864a97b5efcfd06dd92de2f', 3, 1),
('2013-05-01 04:37:30', '2438368a7f0717b8cd6662d8459cface', 14, 1),
('2013-05-01 02:47:36', '2fae0d047c478f463a3bc7506569744b', 14, 6);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

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
(14, 'abraha', 'hahaha@gmail.com', 'abraham krisnanda', 'D398A0821C59E37E8035181BD8C54428.jpg', '2013-04-01', '1441f19754335ca4638bfdf1aea00c6d'),
(15, 'janice', 'janice@gmail.com', 'Janice Laksana', 'http://localhost:8080/MOA_web/upload/user_profile_pict/67A50AED125DD733BB9472BDA0BECFD1.jpg', '3913-05-09', 'fc4b99be5c61a36e67be03c2db25f4bd');

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

--
-- Constraints for table `tokens`
--
ALTER TABLE `tokens`
  ADD CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tokens_ibfk_2` FOREIGN KEY (`id_app`) REFERENCES `applications` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
