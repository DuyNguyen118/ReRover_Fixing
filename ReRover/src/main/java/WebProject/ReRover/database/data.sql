-- Sample data for users table 
INSERT INTO users (student_id, fullname, email, phone_number, profile_picture, password, merit_point, created_at) VALUES
('ITCSIU23006', 'Nguyen Van Bao Duy', 'baoduy.song@gmail.com', '0933132794', 'profile1.jpg', '$2a$12$ze8FwxJ3hgE8F.NVVi3FCuM8o5tx2kuZznhtSXFOZo1nlrW/SPtj2', 10, '2025-05-28 15:00:00'),
('ITCSIU23050', 'Nguyen Ngoc Van Anh', 'nnvanh.ityu@gmail.com', '0912345679', 'profile2.jpg', '$2a$12$ze8FwxJ3hgE8F.NVVi3FCuM8o5tx2kuZznhtSXFOZo1nlrW/SPtj2', 5, '2025-05-27 14:30:00'),
('SE12347', 'Bob Johnson', 'bob.johnson@example.com', '0912345680', 'profile3.jpg', '$2a$12$ze8FwxJ3hgE8F.NVVi3FCuM8o5tx2kuZznhtSXFOZo1nlrW/SPtj2', 20, '2025-05-26 10:15:00'),
('SE12348', 'Alice Brown', 'alice.brown@example.com', '0912345681', 'profile4.jpg', '$2a$12$ze8FwxJ3hgE8F.NVVi3FCuM8o5tx2kuZznhtSXFOZo1nlrW/SPtj2', 2, '2025-05-25 09:45:00'),
('SE12349', 'Charlie Davis', 'charlie.davis@example.com', '0912345682', 'profile5.jpg', '$2a$12$ze8FwxJ3hgE8F.NVVi3FCuM8o5tx2kuZznhtSXFOZo1nlrW/SPtj2', 15, '2025-05-24 16:20:00');

-- Sample data for admins table 
INSERT INTO admins (name, email, password, student_id) VALUES
('Admin One', 'admin1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq1qrM4o4qgVDtb36yoO6Ziof73NMS', 'ADM001'),
('Admin Two', 'admin2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq1qrM4o4qgVDtb36yoO6Ziof73NMS', 'ADM002');

-- Sample data for lost_items table
INSERT INTO lost_items (user_id, title, description, category, type, location, lost_date, image_url, created_at) VALUES
(1, 'Black Wallet', 'Lost my black leather wallet with student ID and some cash', 'Accessories', 'lost', 'A1.109', '2025-05-20', 'wallet.jpg', '2025-05-20 10:30:00'),
(2, 'Silver Laptop Charger', 'Dell 65W charger, lost near the library', 'Electronics', 'lost', 'Library 2nd floor', '2025-05-21', 'charger.jpg', '2025-05-21 14:15:00'),
(3, 'Blue Water Bottle', 'Blue Hydro Flask with stickers on it', 'Other', 'lost', 'Canteen', '2025-05-22', 'bottle.jpg', '2025-05-22 09:45:00'),
(4, 'Wireless Earbuds', 'White Samsung Galaxy Buds in a case', 'Electronics', 'lost', 'A1.208', '2025-05-23', 'earbuds.jpg', '2025-05-23 12:30:00'),
(5, 'Textbook: Advanced Math', 'Calculus 2 textbook with notes inside', 'Documents', 'lost', 'A2.104', '2025-05-24', 'textbook.jpg', '2025-05-24 16:20:00');

-- Sample data for found_items table
INSERT INTO found_items (user_id, title, description, category, type, location, found_date, image_url, created_at) VALUES
(2, 'Black Wallet', 'Found in the cafeteria with some cash and cards', 'Accessories', 'found', 'Canteen', '2025-05-20', 'found_wallet.jpg', '2025-05-20 11:30:00'),
(3, 'Silver Laptop Charger', 'Found plugged in the library study area', 'Electronics', 'found', 'Library 2nd floor', '2025-05-21', 'found_charger.jpg', '2025-05-21 15:30:00'),
(4, 'Blue Water Bottle', 'Left in the gym locker room', 'Other', 'found', 'Canteen', '2025-05-22', 'found_bottle.jpg', '2025-05-22 10:30:00'),
(5, 'Wireless Earbuds', 'Found on a bench outside Building C', 'Electronics', 'found', 'A1.612', '2025-05-23', 'found_earbuds.jpg', '2025-05-23 13:45:00'),
(1, 'Math Textbook', 'Found in the study area, has notes inside', 'Documents', 'found', 'A2.104', '2025-05-24', 'found_textbook.jpg', '2025-05-24 17:30:00');

-- Sample data for item_matches table
INSERT INTO item_matches (lost_item_id, found_item_id, matched_by_user, lost_item_user_confirmed, found_item_user_confirmed, admin_approved, status, match_date) VALUES
(1, 1, 3, 1, 1, 1, 'completed', '2025-05-20 12:00:00'),
(2, 2, 4, 1, 0, 0, 'pending', '2025-05-21 16:00:00'),
(3, 3, 5, 0, 1, 0, 'pending', '2025-05-22 11:00:00');

-- Sample data for admin_actions table
INSERT INTO admin_actions (admin_id, match_id, action_type, notes, action_date) VALUES
(1, 1, 'approve', 'Verified both users and items match', '2025-05-20 14:30:00'),
(1, 2, 'comment', 'Waiting for found item user confirmation', '2025-05-21 17:30:00'),
(2, 3, 'comment', 'Waiting for lost item user confirmation', '2025-05-22 13:45:00');