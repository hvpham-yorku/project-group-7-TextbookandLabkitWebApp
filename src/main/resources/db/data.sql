
-- Rebook Platform — Sample / Seed Data
-- PostgreSQL
--
-- Matches the data seeded in the stub repositories exactly.
-- Uses ON CONFLICT DO NOTHING so re-running is safe.
--
-- Run manually:  psql -U postgres -d rebook -f data.sql
-- Or let Spring Boot auto-run it via application-db.properties.



INSERT INTO users (email, password, name, student_id, phone_number, about_me, program, campus)
VALUES
    ('abc123@my.yorku.ca', 'pass123', 'Alex York',    '100234567', '416-555-0101', 'Hi! I''m Alex, a CS student at York.', 'Computer Science',       'Keele'),
    ('saif0@my.yorku.ca',  '1234',    'Saif',         '100345678', '647-555-0202', 'Software Engineering student at York University.', 'Software Engineering', 'Keele'),
    ('student1@my.yorku.ca','welcome','Student One',  '100456789', NULL,           NULL,                                  'Business Administration', 'Glendon')
ON CONFLICT (email) DO NOTHING;

-- -------------------------------------------------------

INSERT INTO listings (id, seller_email, title, description, price, course_code, semester, material_type, condition, exchange_type, isbn, bookstore_price, date_posted, status)
VALUES
    (1, 'abc123@my.yorku.ca',
     'EECS 2311 Textbook (Used)',
     'Good condition, a few highlights in chapter 3. No missing pages.',
     40.00, 'EECS 2311', 'Winter 2025', 'Textbook', 'Good', 'Sell',
     '9780134685991', 120.00,
     NOW() - INTERVAL '3 days', 'AVAILABLE'),
     
     
     
     
     
     

    (2, 'abc123@my.yorku.ca',
     'EECS 1021 Lab Kit - Breadboard & Components',
     'Full kit, everything included. Used for one semester only.',
     20.00, 'EECS 1021', 'Fall 2024', 'Lab Kit', 'Good', 'Sell',
     NULL, NULL,
     NOW() - INTERVAL '10 days', 'AVAILABLE'),
     
     
     
     
    

    (3, 'student1@my.yorku.ca',
     'MATH 1013 Printed Notes + Past Midterms',
     'Full set of handwritten notes and 3 past midterms with solutions.',
     10.00, 'MATH 1013', 'Winter 2025', 'Notes', 'Good', 'Sell',
     NULL, NULL,
     NOW() - INTERVAL '1 day', 'AVAILABLE'),
     

    (4, 'student1@my.yorku.ca',
     'EECS 2030 Object-Oriented Programming Textbook',
     'Lightly used. Will trade for EECS 3311 textbook.',
     0.00, 'EECS 2030', 'Fall 2024', 'Textbook', 'Fair', 'Trade',
     '9780135166307', 110.00,
     NOW() - INTERVAL '7 days', 'AVAILABLE'),

    (5, 'student2@my.yorku.ca',
     'PHYS 1010 Lab Manual (Latest Edition)',
     'Brand new, never written in. Bought the wrong section.',
     15.00, 'PHYS 1010', 'Winter 2025', 'Lab Kit', 'New', 'Sell',
     NULL, NULL,
     NOW() - INTERVAL '2 days', 'AVAILABLE'),

    (6, 'student2@my.yorku.ca',
     'NATS 1840 Science and Technology Textbook',
     'Free to a good home. Some underlining but fully readable.',
     0.00, 'NATS 1840', 'Fall 2024', 'Textbook', 'Fair', 'Giveaway',
     NULL, NULL,
     NOW() - INTERVAL '15 days', 'AVAILABLE')
ON CONFLICT (id) DO NOTHING;

-- Reset the BIGSERIAL sequence so the next auto-generated ID starts after 6.
-- This prevents duplicate-key errors when the app creates new listings.
SELECT setval('listings_id_seq', (SELECT MAX(id) FROM listings));


