-- streams_tb 더미 데이터 (위에서 아래로 30분 간격으로 순서대로)
INSERT INTO streams_tb (stream_key, title, thumbnail_url, viewer_count, status,
                        started_at, updated_at, ended_at, streamer_id)
VALUES
-- 5시간 30분 전 (가장 오래된 방송)
('cfy_aDktqoqESx6g1DGBEw==', '자바 기초 강의', 'https://cdn.inflearn.com/wp-content/uploads/javavavava.png', 100, 'LIVE',
 DATEADD('MINUTE', -330, NOW()), null, NULL, 1),

-- 5시간 전
('_qfje4Qw3Urq2WZTeFLXmA==', 'C언어 기초 강의', 'https://cdn.inflearn.com/wp-content/uploads/c001.jpg', 200, 'LIVE',
 DATEADD('MINUTE', -300, NOW()), null, NULL, 2),

-- 4시간 30분 전
('vi8AP2rknBM800YI0l9Bog==', '파이썬 기초 강의',
 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScw1gz5eoUI-kUkvQo4qU8TXT90r49eNLX3w&s', 50, 'LIVE',
 DATEADD('MINUTE', -270, NOW()), null, NULL, 3),

-- 4시간 전
('PtFBBAJ__eTCrIkPunEaVg==', '코덱 기초 강의',
 'https://ittalk.co.kr/wp-content/uploads/2025/04/HEVC_%EC%BD%94%EB%8D%B1_%EB%AC%B4%EB%A3%8C_%EC%82%AC%EC%9A%A9%EB%B2%95_%EA%B0%80%EC%9D%B4%EB%93%9C_%EC%B4%88%EB%B3%B4%EC%9E%90%EC%9A%A9_%EC%8D%B8%EB%84%A4%EC%9D%BC.webp',
 10, 'LIVE',
 DATEADD('MINUTE', -240, NOW()), null, NULL, 4),

-- 3시간 30분 전
('i65e5Lkz8ijU5uWqWqziDg==', '토큰 기초 강의', 'https://lab.wallarm.com/wp-content/uploads/2024/12/647.2-min.jpg', 100,
 'PENDING',
 DATEADD('MINUTE', -210, NOW()), null, NULL, 8);

-- 추가 생방송 스트림 6개 (위에서 아래로 30분 간격으로 계속)
INSERT INTO streams_tb (stream_key, title, thumbnail_url, viewer_count, status,
                        started_at, updated_at, ended_at, streamer_id)
VALUES
-- 3시간 전
('gAmE_str34m_k3y_123==', '[LIVE] 롤 랭크 게임 - 다이아 승급전!', 'https://i.ytimg.com/vi/qXezAZT93So/maxresdefault.jpg',
 1250, 'LIVE',
 DATEADD('MINUTE', -180, NOW()), null, NULL, 11),

-- 2시간 30분 전
('c00k1nG_l1v3_456==', '🍳 간단한 파스타 만들기 LIVE',
 'https://help.miricanvas.com/hc/article_attachments/900004854466/___________8_.png', 340, 'LIVE',
 DATEADD('MINUTE', -150, NOW()), null, NULL, 12),

-- 2시간 전
('mus1c_str34m_789==', '🎵 새벽 감성 기타 연주 라이브',
 'https://asomemusic.com/wp-content/uploads/2025/02/%EA%B7%B8%EB%8C%80%EB%A7%8C-%EC%9E%88%EB%8B%A4%EB%A9%B4-%EC%8D%B8%EB%84%A4%EC%9D%BC-%ED%81%AC%EA%B2%8C.jpeg',
 890, 'LIVE',
 DATEADD('MINUTE', -120, NOW()), null, NULL, 13),

-- 1시간 30분 전
('f1tn3ss_l1v3_101==', '💪 아침 홈트레이닝 따라하기',
 'https://mblogthumb-phinf.pstatic.net/MjAxOTA3MTdfMjEg/MDAxNTYzMzM5NDAwMDE4.-EVRvfe331FwbM0B5LIPnFH0wwOBueF7Q_wqiKhNpFUg.IxXI5-0XIe8QR0ID9LZ4dxz0FIvQg_VvDEWHTKRXfgAg.JPEG.knoc3/%EC%8D%B8%EB%84%A4%EC%9D%BC.jpg?type=w800',
 560, 'LIVE',
 DATEADD('MINUTE', -90, NOW()), null, NULL, 14),

-- 1시간 전
('4rt_cr34t3_202==', '🎨 디지털 일러스트 그리기 과정',
 'https://celcliptipsprod.s3-ap-northeast-1.amazonaws.com/tips_article_body/82c2/3786006/9b4f7c4c5b5da5c0544a5544937b00d7',
 230, 'LIVE',
 DATEADD('MINUTE', -60, NOW()), null, NULL, 15),

-- 30분 전 (가장 최근 방송)
('t3ch_t4lk_303==', '🔥 2025 개발 트렌드 토크쇼',
 'https://mblogthumb-phinf.pstatic.net/MjAyNTAxMTBfMTE5/MDAxNzM2NDcyMDcxODQ1.f6JH64de6ECfRUvzeTnDmegBuUl_LTevm2tVf4I1HVIg.NaSLSEQL3BYndVpzH30vxOb44WdN9PYYzqMtxeMskDog.JPEG/%EB%B8%94%EB%A1%9C%EA%B7%B8%EC%8D%B8%EB%84%A4%EC%9D%BC.jpg?type=w800',
 720, 'LIVE',
 DATEADD('MINUTE', -30, NOW()), null, NULL, 16);
