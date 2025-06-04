-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create enum types if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'item_status') THEN
        CREATE TYPE item_status AS ENUM ('PENDING', 'MATCHED', 'RESOLVED', 'REJECTED');
    END IF;
END$$;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    profile_picture VARCHAR(255),
    merit_point INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Lost items table
CREATE TABLE IF NOT EXISTS lost_items (
    lost_item_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    lost_date DATE,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status item_status DEFAULT 'PENDING'
);

-- Found items table
CREATE TABLE IF NOT EXISTS found_items (
    found_item_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    found_date DATE,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status item_status DEFAULT 'PENDING'
);

-- Item matches table
CREATE TABLE IF NOT EXISTS item_matches (
    match_id SERIAL PRIMARY KEY,
    lost_item_id INTEGER REFERENCES lost_items(lost_item_id) ON DELETE CASCADE,
    found_item_id INTEGER REFERENCES found_items(found_item_id) ON DELETE CASCADE,
    matched_by_user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status item_status DEFAULT 'PENDING',
    admin_notes TEXT,
    UNIQUE(lost_item_id, found_item_id)
);

-- Admin actions table
CREATE TABLE IF NOT EXISTS admin_actions (
    action_id SERIAL PRIMARY KEY,
    admin_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    action_type VARCHAR(50) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id INTEGER NOT NULL,
    action_details TEXT,
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_lost_items_user_id ON lost_items(user_id);
CREATE INDEX IF NOT EXISTS idx_found_items_user_id ON found_items(user_id);
CREATE INDEX IF NOT EXISTS idx_item_matches_lost_id ON item_matches(lost_item_id);
CREATE INDEX IF NOT EXISTS idx_item_matches_found_id ON item_matches(found_item_id);
CREATE INDEX IF NOT EXISTS idx_item_matches_matched_by ON item_matches(matched_by_user_id);
CREATE INDEX IF NOT EXISTS idx_admin_actions_admin_id ON admin_actions(admin_id);

-- Create a function to update the updated_at column
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add updated_at column to tables if they don't exist
DO $$
BEGIN
    -- For users table
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                  WHERE table_name = 'users' AND column_name = 'updated_at') THEN
        ALTER TABLE users ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        CREATE TRIGGER update_users_modtime
            BEFORE UPDATE ON users
            FOR EACH ROW EXECUTE FUNCTION update_modified_column();
    END IF;

    -- For lost_items table
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                  WHERE table_name = 'lost_items' AND column_name = 'updated_at') THEN
        ALTER TABLE lost_items ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        CREATE TRIGGER update_lost_items_modtime
            BEFORE UPDATE ON lost_items
            FOR EACH ROW EXECUTE FUNCTION update_modified_column();
    END IF;

    -- For found_items table
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                  WHERE table_name = 'found_items' AND column_name = 'updated_at') THEN
        ALTER TABLE found_items ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        CREATE TRIGGER update_found_items_modtime
            BEFORE UPDATE ON found_items
            FOR EACH ROW EXECUTE FUNCTION update_modified_column();
    END IF;

    -- For item_matches table
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                  WHERE table_name = 'item_matches' AND column_name = 'updated_at') THEN
        ALTER TABLE item_matches ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        CREATE TRIGGER update_item_matches_modtime
            BEFORE UPDATE ON item_matches
            FOR EACH ROW EXECUTE FUNCTION update_modified_column();
    END IF;
END $$;
