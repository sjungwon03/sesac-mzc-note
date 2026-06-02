-- DQL 기본 예제: 교육 운영 데이터 조회
DROP DATABASE IF EXISTS training_db;
CREATE DATABASE training_db;
USE training_db;

DROP TABLE IF EXISTS learners;
DROP TABLE IF EXISTS courses;

CREATE TABLE courses (
    course_id INT NOT NULL AUTO_INCREMENT,
    course_name VARCHAR(50) NOT NULL,
    category VARCHAR(20) NOT NULL,
    hours INT NOT NULL,
    CONSTRAINT pk_courses PRIMARY KEY (course_id)
) ENGINE=InnoDB COMMENT '과정';

CREATE TABLE learners (
    learner_id INT NOT NULL AUTO_INCREMENT,
    course_id INT,
    learner_name VARCHAR(30) NOT NULL,
    level_name VARCHAR(20),
    registered_at DATE,
    score INT,
    CONSTRAINT pk_learners PRIMARY KEY (learner_id),
    CONSTRAINT fk_learners_course FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
) ENGINE=InnoDB COMMENT '수강생';

INSERT INTO courses (course_name, category, hours) VALUES
('Java 기초', 'Programming', 60),
('MySQL 활용', 'Database', 40),
('AWS 입문', 'Cloud', 30),
('Spring Boot', 'Programming', 60);

INSERT INTO learners (course_id, learner_name, level_name, registered_at, score) VALUES
(1, '이서준', 'beginner', '2026-03-01', 85),
(1, '박지민', 'beginner', '2026-03-02', 72),
(2, '최유진', 'intermediate', '2026-03-04', 91),
(2, '정도윤', 'beginner', '2026-03-05', 66),
(3, '한서아', 'intermediate', '2026-03-07', 88),
(4, '오민재', 'advanced', '2026-03-10', 95);

-- SELECT 절만 사용해 계산 또는 함수 결과 확인
SELECT 10 + 20 AS result;
SELECT NOW() AS current_time;

-- 필요한 칼럼만 조회하고 별명 지정
SELECT learner_id AS 번호, learner_name AS 이름, score AS 점수
FROM learners;

-- 조건 필터링
SELECT learner_name, score
FROM learners
WHERE score >= 80;

-- 범위 조건
SELECT learner_name, registered_at
FROM learners
WHERE registered_at BETWEEN '2026-03-01' AND '2026-03-05';

-- 여러 값 중 하나와 패턴 검색
SELECT learner_name, level_name
FROM learners
WHERE level_name IN ('intermediate', 'advanced');

SELECT learner_name
FROM learners
WHERE learner_name LIKE '박%';

-- 그룹화와 집계
SELECT course_id, COUNT(*) AS learner_count, AVG(score) AS avg_score
FROM learners
GROUP BY course_id;

-- 그룹 조건
SELECT course_id, AVG(score) AS avg_score
FROM learners
GROUP BY course_id
HAVING avg_score >= 80;

-- 정렬과 행 제한
SELECT learner_name, score
FROM learners
ORDER BY score DESC
LIMIT 3;
