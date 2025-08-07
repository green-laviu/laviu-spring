INSERT INTO users_tb (id, nickname, email, password, profile_image_url, bio, fcm_token, provider, type, created_at, updated_at, last_login_at)
VALUES
    (1, '홍길동', 'hong@example.com', '$2a$10$Dow1eA8t0OTkbGbL5Gk8LuDzvZT7IvNKQa8AeQxFC1V2YyLkwG9QO', 'https://example.com/images/user1.jpg', '모험을 즐기는 개발자', 'fcm_token_1', 'LOCAL', 'NORMAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (2, '김개발', 'kimdev@example.com', '$2a$10$Dow1eA8t0OTkbGbL5Gk8LuDzvZT7IvNKQa8AeQxFC1V2YyLkwG9QO', 'https://example.com/images/user2.png', '풀스택 개발자입니다', 'fcm_token_2', 'GOOGLE', 'NORMAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    (3, '이승호', 'seungho@example.com', '$2a$10$Dow1eA8t0OTkbGbL5Gk8LuDzvZT7IvNKQa8AeQxFC1V2YyLkwG9QO', 'https://example.com/images/user3.jpeg', '나는 영상을 만드는 개발자', 'fcm_token_3', 'KAKAO', 'NORMAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
