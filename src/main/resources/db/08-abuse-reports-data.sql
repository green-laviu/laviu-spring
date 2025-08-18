-- abuse_reports_tb 더미 데이터
insert into abuse_reports_tb
(snapshot_stream_title, snapshot_streamer_nickname, details, status, created_at, processed_at, abuse_reporter_id,
 abuse_reported_stream_id, abuse_reported_streamer_id, category_id)
values ('신고된 방송제목', '신고된 스트리머 닉네임', '상세사유', 'PENDING', now(), null, 1, 1, 2, 1);
