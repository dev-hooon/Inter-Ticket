INSERT INTO place_table
    (place_name, place_contact_info, place_address, place_url)
VALUES ('블루스퀘어 신한카드홀', '1544-1591', '서울특별시 용산구 이태원로 294 블루스퀘어(한남동)', 'http://www.bluesquare.kr/index.asp');


INSERT INTO show_table
(show_id, show_name, show_category, show_start_date, show_end_date, show_total_minutes, show_intermission_minutes,
 show_age_limit, show_total_seats, show_place_id)
VALUES (1, '레미제라블', 'MUSICAL', '2024-01-01', '2024-12-31', 150, 15, '만 8세 이상', 1000, 1);

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'VIP', true, '1층', 'A', 2, 100000, '2024-01-01', 2, '13:00:00', 'AVAILABLE');

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'VIP', true, '1층', 'A', 3, 100000, '2024-01-01', 2, '13:00:00', 'AVAILABLE');

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'S', true, '2층', 'A', 2, 70000, '2024-01-01', 2, '13:00:00', 'AVAILABLE');

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'S', true, '2층', 'A', 3, 70000, '2024-01-01', 2, '13:00:00', 'AVAILABLE');

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'A', true, '3층', 'A', 2, 50000, '2024-01-01', 2, '13:00:00', 'AVAILABLE');

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'S', true, '1층', 'A', 7, 70000, '2024-01-01', 2, '13:00:00', 'BOOKED');

INSERT INTO seat_table
(seat_show_id, seat_grade, seat_is_seat, seat_sector, seat_row, seat_col, seat_price, seat_show_date,
 seat_show_round, seat_start_time, seat_status)
VALUES (1, 'A', true, '1층', 'A', 8, 50000, '2024-01-01', 2, '13:00:00', 'BOOKED');
