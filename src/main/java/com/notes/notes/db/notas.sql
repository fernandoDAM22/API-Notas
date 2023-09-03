-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-09-2023 a las 10:37:14
-- Versión del servidor: 10.4.24-MariaDB
-- Versión de PHP: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `notas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `description` varchar(100) NOT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `categories`
--

INSERT INTO `categories` (`id`, `name`, `description`, `user_id`) VALUES
(3, 'URLS interasantes', 'URLS interasantes de websites', 2),
(4, 'Apuntes', 'Notas con apuntes para no olvidarlos', 2),
(5, 'Prueba', 'Categoria de prueba', 3),
(6, 'Prueba', 'Categoria de prueba', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notes`
--

CREATE TABLE `notes` (
  `id` int(11) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `content` varchar(300) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `notes`
--

INSERT INTO `notes` (`id`, `title`, `content`, `user_id`, `category_id`) VALUES
(2, 'Lista de tareas', '-Ir a comprar\r\n-Ir al medico\r\n-Estudiar', 2, 4),
(3, 'nota de prueba', 'texto de la nota de prueba', 2, 4),
(4, 'Nota', 'texto de la nota de prueba', 3, 5),
(5, 'nota de prueba', 'texto de la nota de prueba', 3, 5),
(6, 'nota de prueba', 'texto de la nota de prueba', 3, 5),
(7, 'nota de prueba', 'texto de la nota de prueba', 3, 5),
(9, 'Lista de la compra', 'Huevos,leche,pan,azucar', 2, 4),
(10, 'Prueba 1', 'Contenido de la prueba 1', 2, 6),
(11, 'Prueba 2', 'Contenido de la prueba 2', 2, 6),
(12, 'Prueba 3', 'Contenido de la prueba 3', 2, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`) VALUES
(2, 'fernando', 'fernandoesmr@gmail.com', '$argon2id$v=19$m=1024,t=1,p=1$e2ADpAst5NiHAuR9y87HUQ$qwtc/2xOwqofEuCZ3jVlFVqmIwJY7+x1Hks5sH4xWnk'),
(3, 'Maria', 'maria@gmail.com', '$argon2id$v=19$m=1024,t=1,p=1$SlH6vTyHwGKobgEk1oZzEw$6LAGN7KGz5z1xT45TaDHPdN75KZFFSX/buW3Yjqn6OE'),
(4, 'ivan', 'ivan@gmail.com', '$argon2id$v=19$m=1024,t=1,p=1$+y9+UWo9YtxJsYKVMdHH9w$1vfty/kLqCEfW7J3tx1BS9PBDBOli5LExhLMeu1y6Wg');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `notes`
--
ALTER TABLE `notes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `notes`
--
ALTER TABLE `notes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `notes`
--
ALTER TABLE `notes`
  ADD CONSTRAINT `notes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `notes_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
