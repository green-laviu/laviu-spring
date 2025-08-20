-- streams_tb 더미 데이터
INSERT INTO streams_tb (stream_key, title, thumbnail_url, viewer_count, status,
                        started_at, updated_at, ended_at, streamer_id)
VALUES ('cfy/aDktqoqESx6g1DGBEw==', '자바 기초 강의', 'https://example.com/thumb1.jpg', 100, 'LIVE',
        NOW(), null, NULL, 1),
       ('/qfje4Qw3Urq2WZTeFLXmA==', 'C언어 기초 강의', 'https://example.com/thumb2.jpg', 200, 'LIVE',
        NOW(), null, NULL, 2),
       ('vi8AP2rknBM800YI0l9Bog==', '파이썬 기초 강의', 'https://example.com/thumb3.jpg', 50, 'LIVE',
        NOW(), null, NULL, 3),
       ('PtFBBAJ//eTCrIkPunEaVg==', '코덱 기초 강의', 'https://example.com/thumb4.jpg', 10, 'LIVE',
        NOW(), null, NULL, 4);
