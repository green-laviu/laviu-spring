INSERT INTO users_tb (nickname, email, password, profile_image_url, bio, fcm_token,
                      provider, type, created_at, updated_at, last_login_at)
VALUES ('ssar', 'ssarr@nate.com', '1234', 'https://nate.com/profile1.jpg', '안녕하세요', 'token1', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('cos', 'coss@nate.com', '1234', 'https://nate.com/profile2.jpg', '안녕하세요', 'token2', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('love', 'lovee@nate.com', '1234', 'https://nate.com/profile3.jpg', '안녕하세요', 'token3', 'NAVER', 'USER',
        NOW(), null, NOW());
