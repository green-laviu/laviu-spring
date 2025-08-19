-- users_tb 더미 데이터
INSERT INTO users_tb (nickname, email, password, profile_image_url, bio, fcm_token,
                      provider, roles, created_at, updated_at, last_login_at)
VALUES ('ssar', 'ssar@nate.com', '1234', 'https://nate.com/profile1.jpg', '안녕하세요', 'token1', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('cos', 'cos@nate.com', '1234', 'https://nate.com/profile2.jpg', '안녕하세요', 'token2', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('love', 'love@nate.com', '1234', 'https://nate.com/profile3.jpg', '안녕하세요', 'token3', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('hate', 'hate@nate.com', '1234', 'https://nate.com/profile4.jpg', '안녕하세요', 'token4', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('testStreamer', 'testStreamer@nate.com', '1234', 'https://nate.com/profile5.jpg', '안녕하세요 테스트 스트리머입니다', 'token5',
        'NAVER', 'USER',
        NOW(), null, NOW()),
       ('testViewer', 'testViewer@nate.com', '1234', 'https://nate.com/profile6.jpg', '안녕하세요 테스트 시청자 입니다', 'token6',
        'NAVER', 'USER',
        NOW(), null, NOW());
