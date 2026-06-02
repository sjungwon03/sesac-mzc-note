-- DDL 기본 예제: 학습자 출석 관리 데이터베이스
DROP DATABASE IF EXISTS academy_db;
CREATE DATABASE academy_db;
USE academy_db;

DROP TABLE IF EXISTS attendance_logs;
DROP TABLE IF EXISTS students;

CREATE TABLE students (
    student_id INT NOT NULL AUTO_INCREMENT,
    student_name VARCHAR(30) NOT NULL,
    track_name VARCHAR(40) NOT NULL,
    email VARCHAR(100) UNIQUE,
    joined_at DATE NOT NULL,
    active_yn CHAR(1) NOT NULL DEFAULT 'Y',
    CONSTRAINT pk_students PRIMARY KEY (student_id),
    CONSTRAINT ck_students_active CHECK (active_yn IN ('Y', 'N'))
) ENGINE=InnoDB COMMENT '학습자';

CREATE TABLE attendance_logs (
    log_id INT NOT NULL AUTO_INCREMENT,
    student_id INT,
    attended_on DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    CONSTRAINT pk_attendance_logs PRIMARY KEY (log_id),
    CONSTRAINT fk_attendance_student FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE SET NULL
) ENGINE=InnoDB COMMENT '출석 기록';
