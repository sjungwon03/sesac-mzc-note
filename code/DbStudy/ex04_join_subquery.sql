-- JOIN 및 서브쿼리 기본 예제
USE training_db;

-- 내부 조인: 수강생이 등록된 과정 정보 함께 조회
SELECT l.learner_id, l.learner_name, c.course_name, c.category
FROM learners l
INNER JOIN courses c
        ON l.course_id = c.course_id;

-- 특정 카테고리 과정의 수강생 조회
SELECT l.learner_name, c.course_name, l.score
FROM courses c
INNER JOIN learners l
        ON c.course_id = l.course_id
WHERE c.category = 'Programming';

-- 카테고리별 수강생 수
SELECT c.category, COUNT(l.learner_id) AS learner_count
FROM courses c
INNER JOIN learners l
        ON c.course_id = l.course_id
GROUP BY c.category;

-- 외부 조인: 수강생이 없어도 모든 과정 조회
SELECT c.course_id, c.course_name, COUNT(l.learner_id) AS learner_count
FROM courses c
LEFT OUTER JOIN learners l
             ON c.course_id = l.course_id
GROUP BY c.course_id, c.course_name;

-- 단일 행 서브쿼리: 전체 평균보다 높은 점수 조회
SELECT learner_name, score
FROM learners
WHERE score > (SELECT AVG(score) FROM learners);

-- 다중 행 서브쿼리: Database 카테고리 과정의 수강생 조회
SELECT learner_name, course_id, score
FROM learners
WHERE course_id IN (
    SELECT course_id
    FROM courses
    WHERE category = 'Database'
);
