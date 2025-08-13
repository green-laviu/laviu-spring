-- streams_tb 더미 데이터
INSERT INTO streams_tb (stream_key, title, thumbnail_url, viewer_count, status,
                        started_at, updated_at, ended_at, streamer_id)
VALUES ('abc123', '자바 기초 강의', 'https://example.com/thumb1.jpg', 100, 'LIVE',
        NOW(), null, NULL, 1),
       ('a1b2c3', '파이썬 기초 강의', 'https://example.com/thumb3.jpg', 50, 'LIVE',
        NOW(), null, NULL, 3),
       ('1a2b3c', '코덱 기초 강의', 'https://example.com/thumb4.jpg', 10, 'LIVE',
        NOW(), null, NULL, 4);
