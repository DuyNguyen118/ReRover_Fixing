-- --------------------------------------------------------
-- Database: ReRover
-- Purpose: Lost and Found System
-- --------------------------------------------------------

-- Drop tables if they exist (in reverse order of foreign key dependencies)
DROP TABLE IF EXISTS admin_actions CASCADE;
DROP TABLE IF EXISTS item_matches CASCADE;
DROP TABLE IF EXISTS lost_items CASCADE;
DROP TABLE IF EXISTS found_items CASCADE;
DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS users_user_id_seq CASCADE;
DROP SEQUENCE IF EXISTS admins_admin_id_seq CASCADE;
DROP SEQUENCE IF EXISTS lost_items_lost_item_id_seq CASCADE;
DROP SEQUENCE IF EXISTS found_items_found_item_id_seq CASCADE;
DROP SEQUENCE IF EXISTS item_matches_match_id_seq CASCADE;
DROP SEQUENCE IF EXISTS admin_actions_action_id_seq CASCADE;

-- Create sequences
CREATE SEQUENCE IF NOT EXISTS users_user_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS admins_admin_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS lost_items_lost_item_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS found_items_found_item_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS item_matches_match_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS admin_actions_action_id_seq START WITH 1 INCREMENT BY 1;

-- Create tables
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY DEFAULT nextval('users_user_id_seq'),
    student_id VARCHAR(20) UNIQUE NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    profile_picture VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    merit_point INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY DEFAULT nextval('admins_admin_id_seq'),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    student_id VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS lost_items (
    lost_item_id INT PRIMARY KEY DEFAULT nextval('lost_items_lost_item_id_seq'),
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    type VARCHAR(50),
    location VARCHAR(100),
    lost_date DATE,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS found_items (
    found_item_id INT PRIMARY KEY DEFAULT nextval('found_items_found_item_id_seq'),
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    type VARCHAR(50),
    location VARCHAR(100),
    found_date DATE,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item_matches (
    match_id INT PRIMARY KEY DEFAULT nextval('item_matches_match_id_seq'),
    lost_item_id INT NOT NULL,
    found_item_id INT NOT NULL,
    matched_by_user INT,
    lost_item_user_confirmed BOOLEAN DEFAULT FALSE,
    found_item_user_confirmed BOOLEAN DEFAULT FALSE,
    admin_approved BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'pending',
    match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lost_item_id) REFERENCES lost_items(lost_item_id) ON DELETE CASCADE,
    FOREIGN KEY (found_item_id) REFERENCES found_items(found_item_id) ON DELETE CASCADE,
    FOREIGN KEY (matched_by_user) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS admin_actions (
    action_id INT PRIMARY KEY DEFAULT nextval('admin_actions_action_id_seq'),
    admin_id INT NOT NULL,
    match_id INT NOT NULL,
    action_type VARCHAR(50),
    notes TEXT,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id),
    FOREIGN KEY (match_id) REFERENCES item_matches(match_id)
);

-- Set ownership of sequences to their respective tables
ALTER SEQUENCE users_user_id_seq OWNED BY users.user_id;
ALTER SEQUENCE admins_admin_id_seq OWNED BY admins.admin_id;
ALTER SEQUENCE lost_items_lost_item_id_seq OWNED BY lost_items.lost_item_id;
ALTER SEQUENCE found_items_found_item_id_seq OWNED BY found_items.found_item_id;
ALTER SEQUENCE item_matches_match_id_seq OWNED BY item_matches.match_id;
ALTER SEQUENCE admin_actions_action_id_seq OWNED BY admin_actions.action_id;
