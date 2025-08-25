-- users_tb 더미 데이터
INSERT INTO users_tb (nickname, email, password, profile_image_url, bio, fcm_token,
                      provider, roles, created_at, updated_at, last_login_at)
VALUES ('ssar', 'ssar@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://plus.unsplash.com/premium_photo-1682095606317-50dec75d283c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8a29yZWF8ZW58MHx8MHx8fDA%3D',
        '안녕하세요', 'token1', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('cos', 'cos@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://plus.unsplash.com/premium_photo-1682095664848-014a0a2bfd8a?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fCVFRCU5NSU5QyVFQSVCNSVBRCUyMCVFQyU4NiU4QyVFQiU4NSU4MHxlbnwwfHwwfHx8MA%3D%3D',
        '안녕하세요', 'token2', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('love', 'love@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://www.thiswaifudoesnotexist.net/example-73125.jpg', '안녕하세요', 'token3', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('hate', 'hate@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://www.thiswaifudoesnotexist.net/example-66057.jpg', '안녕하세요', 'token4', 'NAVER', 'USER',
        NOW(), null, NOW()),
       ('testStreamer', 'testStreamer@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://nate.com/profile5.jpg', '안녕하세요 테스트 스트리머입니다', 'token5',
        'NAVER', 'USER',
        NOW(), null, NOW()),
       ('testViewer', 'testViewer@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://nate.com/profile6.jpg', '안녕하세요 테스트 시청자 입니다', 'token6',
        'NAVER', 'USER',
        NOW(), null, NOW()),
       ('admin', 'admin@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe', NULL, NULL, NULL,
        NULL, 'ADMIN',
        NOW(), null, NOW()),
       ('testToken', 'testToken@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://nate.com/profile7.jpg', '안녕하세요', 'token7',
        'NAVER', 'USER',
        NOW(), null, NOW()),
       ('good', 'good@nate.com', '1234', 'https://www.thiswaifudoesnotexist.net/example-96719.jpg', '안녕하세요', 'token9',
        'NAVER', 'USER',
        NOW(), null, NOW()),
       ('testAdminLogin', 'testAdminLogin@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        NULL,
        NULL,
        NULL,
        NULL, 'ADMIN', NOW(), null, NOW());

-- 추가 유저 6명 (ID 11-16)
INSERT INTO users_tb (nickname, email, password, profile_image_url, bio, fcm_token,
                      provider, roles, created_at, updated_at, last_login_at)
VALUES ('gameStreamer', 'gameStreamer@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://www.thiswaifudoesnotexist.net/example-14999.jpg', '게임 방송합니다!', 'token11', 'NAVER', 'USER',
        DATEADD('HOUR', -3, NOW()), null, DATEADD('HOUR', -1, NOW())),

       ('cookingMaster', 'cookingMaster@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://www.thiswaifudoesnotexist.net/example-38360.jpg', '요리 레시피 공유해요', 'token12', 'NAVER', 'USER',
        DATEADD('HOUR', -5, NOW()), null, DATEADD('HOUR', -2, NOW())),

       ('musicLover', 'musicLover@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://cdn-icons-png.flaticon.com/512/6325/6325109.png', '음악과 함께하는 라이브', 'token13', 'NAVER', 'USER',
        DATEADD('HOUR', -8, NOW()), null, DATEADD('MINUTE', -30, NOW())),

       ('fitnessCoach', 'fitnessCoach@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://plus.unsplash.com/premium_photo-1682095606317-50dec75d283c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8a29yZWF8ZW58MHx8MHx8fDA%3D',
        '건강한 운동 라이프', 'token14', 'NAVER', 'USER',
        DATEADD('HOUR', -2, NOW()), null, DATEADD('MINUTE', -45, NOW())),

       ('artCreator', 'artCreator@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://www.thiswaifudoesnotexist.net/example-50749.jpg', '그림 그리는 과정을 공유합니다', 'token15', 'NAVER', 'USER',
        DATEADD('HOUR', -6, NOW()), null, DATEADD('MINUTE', -15, NOW())),

       ('techTalk', 'techTalk@nate.com', '$2a$10$FpS0L/H5Te23KLSIqdB2DOoYkpRqvPDs9YubWYNJoMl7I09NzOMEe',
        'https://www.thiswaifudoesnotexist.net/example-38360.jpg', '최신 기술 트렌드 이야기', 'token16', 'NAVER', 'USER',
        DATEADD('HOUR', -1, NOW()), null, DATEADD('MINUTE', -10, NOW()));

