INSERT INTO app_users (username, password, hotel, role) VALUES ('admin1', 'pass', 'Radisson', 'ADMIN');
INSERT INTO app_users (username, password, hotel, role) VALUES ('admin1', 'pass', 'Lalit', 'ADMIN');
INSERT INTO app_users (username, password, hotel, role) VALUES ('admin1', 'pass', 'Taj', 'ADMIN');

-- ===================== ROOMS =====================

INSERT INTO rooms 
(hotel, room_number, type, price, is_available, breakfast, payment_status, checked_in, checked_out, customer_id) 
VALUES 
('Radisson', '101', 'Deluxe', 1500.0, true, false, 'PENDING', false, false, null);

INSERT INTO rooms 
(hotel, room_number, type, price, is_available, breakfast, payment_status, checked_in, checked_out, customer_id) 
VALUES 
('Radisson', '102', 'Suite', 3000.0, true, false, 'PENDING', false, false, null);

INSERT INTO rooms 
(hotel, room_number, type, price, is_available, breakfast, payment_status, checked_in, checked_out, customer_id) 
VALUES 
('Taj', '501', 'Presidential Suite', 12000.0, true, false, 'PENDING', false, false, null);

INSERT INTO rooms 
(hotel, room_number, type, price, is_available, breakfast, payment_status, checked_in, checked_out, customer_id) 
VALUES 
('Lalit', '201', 'Deluxe', 2000.0, true, false, 'PENDING', false, false, null);

INSERT INTO rooms 
(hotel, room_number, type, price, is_available, breakfast, payment_status, checked_in, checked_out, customer_id) 
VALUES 
('Lalit', '202', 'Suite', 4000.0, true, false, 'PENDING', false, false, null);