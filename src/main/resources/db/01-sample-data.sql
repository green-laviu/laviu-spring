INSERT INTO users_tb
(nickname, email, password, profile_image_url, bio, fcm_token, provider, type, created_at, updated_at, last_login_at)
VALUES
    ('홍길동', 'hong@example.com', '$2a$10$Dow1eA8t0OTkbGbL5Gk8LuDzvZT7IvNKQa8AeQxFC1V2YyLkwG9QO', 'https://via.placeholder.com/150', '모험을 즐기는 개발자', 'fcm_token_1', 'NAVER', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('김개발', 'kimdev@example.com', '$2a$10$Dow1eA8t0OTkbGbL5Gk8LuDzvZT7IvNKQa8AeQxFC1V2YyLkwG9QO', 'https://via.placeholder.com/150/0000FF/FFFFFF?text=Kim', '풀스택 개발자입니다', 'fcm_token_2', 'NAVER', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('이승호', 'seungho@example.com', '$2a$10$Dow1eA8t0OTkbGbL5Gk8LuDzvZT7IvNKQa8AeQxFC1V2YyLkwG9QO', 'https://via.placeholder.com/150/FF0000/FFFFFF?text=Seungho', '나는 영상을 만드는 개발자', 'fcm_token_3', 'NAVER', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
