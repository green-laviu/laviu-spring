INSERT INTO notifications_tb (related_entity_id, content, is_read, type, user_id, created_at)
VALUES
    -- streamer_id가 2인 ssar의 방송이 시작되었다는 알림을 user_id 1인 사용자에게 보냄
    (1, 'ssar님의 방송이 시작되었습니다. 시청하러 가볼까요?', FALSE, 'LIVE_STARTED', 2, NOW());
