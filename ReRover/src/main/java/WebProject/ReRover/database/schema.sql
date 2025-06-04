-- --------------------------------------------------------
-- Database: ReRover
-- Purpose: Lost and Found System
-- --------------------------------------------------------

DROP DATABASE IF EXISTS ReRover;
CREATE DATABASE ReRover /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE ReRover;

-- Creating structure for table ReRover.users
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    profile_picture VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    merit_point INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating structure for table ReRover.admins
CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    student_id VARCHAR(20) UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating structure for table ReRover.lost_items
CREATE TABLE IF NOT EXISTS lost_items (
    lost_item_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    type VARCHAR(50),
    location VARCHAR(100),
    lost_date DATE,
    image_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating structure for table ReRover.found_items
CREATE TABLE IF NOT EXISTS found_items (
    found_item_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    type VARCHAR(50),
    location VARCHAR(100),
    found_date DATE,
    image_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating structure for table ReRover.item_matches
CREATE TABLE IF NOT EXISTS item_matches (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    lost_item_id INT NOT NULL,
    found_item_id INT NOT NULL,
    matched_by_user INT,
    lost_item_user_confirmed BOOLEAN DEFAULT FALSE,
    found_item_user_confirmed BOOLEAN DEFAULT FALSE,
    admin_approved BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'pending',
    match_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lost_item_id) REFERENCES lost_items(lost_item_id) ON DELETE CASCADE,
    FOREIGN KEY (found_item_id) REFERENCES found_items(found_item_id) ON DELETE CASCADE,
    FOREIGN KEY (matched_by_user) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creating structure for table ReRover.admin_actions
CREATE TABLE IF NOT EXISTS admin_actions (
    action_id INT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT NOT NULL,
    match_id INT NOT NULL,
    action_type VARCHAR(50),
    notes TEXT,
    action_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id),
    FOREIGN KEY (match_id) REFERENCES item_matches(match_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
