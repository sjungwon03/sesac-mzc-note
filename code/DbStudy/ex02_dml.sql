-- DML 기본 예제: INSERT, UPDATE, DELETE
USE academy_db;

-- INSERT: 학습자와 출석 기록 추가
INSERT INTO students (student_name, track_name, email, joined_at)
VALUES ('김하늘', 'Backend', 'sky@example.com', '2026-03-02');

INSERT INTO attendance_logs (student_id, attended_on, status)
VALUES (1, '2026-03-03', '출석');

-- UPDATE: 기본 정보 변경
UPDATE students
   SET track_name = 'Cloud Backend'
 WHERE student_id = 1;

-- DELETE: 출석 기록 삭제 후 학습자 비활성화
DELETE FROM attendance_logs
 WHERE log_id = 1;

UPDATE students
   SET active_yn = 'N'
 WHERE student_id = 1;
