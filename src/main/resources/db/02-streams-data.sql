INSERT INTO streams_tb (stream_key, title, thumbnail_url, viewer_count, status,
                        started_at, updated_at, ended_at, streamer_id)
VALUES ('cfy_aDktqoqESx6g1DGBEw==', '자바 기초 강의', 'https://example.com/thumb1.jpg', 100, 'LIVE',
        CURRENT_TIMESTAMP, null, NULL, 1),
       ('_qfje4Qw3Urq2WZTeFLXmA==', 'C언어 기초 강의', 'https://example.com/thumb2.jpg', 200, 'LIVE',
        DATEADD('MINUTE', -10, CURRENT_TIMESTAMP), null, NULL, 2),
       ('vi8AP2rknBM800YI0l9Bog==', '파이썬 기초 강의', 'https://example.com/thumb3.jpg', 50, 'LIVE',
        DATEADD('MINUTE', -20, CURRENT_TIMESTAMP), null, NULL, 3),
       ('PtFBBAJ__eTCrIkPunEaVg==', '코덱 기초 강의', 'https://example.com/thumb4.jpg', 10, 'LIVE',
        DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), null, NULL, 4),
       ('i65e5Lkz8ijU5uWqWqziDg==', '토큰 기초 강의', 'https://example.com/thumb5.jpg', 100, 'PENDING',
        DATEADD('MINUTE', -40, CURRENT_TIMESTAMP), null, NULL, 8);