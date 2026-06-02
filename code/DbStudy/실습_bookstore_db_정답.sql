-- 도서 판매 SQL 실습: 정답 파일
DROP DATABASE IF EXISTS library_shop_db;
CREATE DATABASE library_shop_db;
USE library_shop_db;

DROP TABLE IF EXISTS book_orders;
DROP TABLE IF EXISTS readers;
DROP TABLE IF EXISTS books;

CREATE TABLE books (
    book_id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    publisher VARCHAR(50),
    price INT,
    CONSTRAINT pk_books PRIMARY KEY (book_id)
) ENGINE=InnoDB COMMENT '도서';

CREATE TABLE readers (
    reader_id INT NOT NULL AUTO_INCREMENT,
    reader_name VARCHAR(30) NOT NULL,
    region VARCHAR(30),
    phone VARCHAR(20),
    CONSTRAINT pk_readers PRIMARY KEY (reader_id)
) ENGINE=InnoDB COMMENT '독자';

CREATE TABLE book_orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    reader_id INT,
    book_id INT,
    quantity INT NOT NULL,
    ordered_on DATE NOT NULL,
    CONSTRAINT pk_book_orders PRIMARY KEY (order_id),
    CONSTRAINT fk_book_orders_reader FOREIGN KEY (reader_id)
        REFERENCES readers(reader_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_book_orders_book FOREIGN KEY (book_id)
        REFERENCES books(book_id)
        ON DELETE CASCADE
) ENGINE=InnoDB COMMENT '도서 주문';

INSERT INTO books (title, publisher, price) VALUES
('SQL 첫걸음', '데이터북스', 18000),
('클라우드 입문', '테크미디어', 22000),
('자바 객체지향', '코드하우스', 27000),
('리눅스 운영', '서버랩', 25000),
('데이터 모델링', '데이터북스', 30000),
('알고리즘 노트', '코드하우스', 24000),
('네트워크 기본', '서버랩', 21000),
('웹 서비스 설계', '테크미디어', 28000);

ALTER TABLE readers AUTO_INCREMENT = 100;
INSERT INTO readers (reader_name, region, phone) VALUES
('윤하늘', '서울', '연락처-A01'),
('노바다', '부산', '연락처-B02'),
('문서연', '대전', '연락처-C03'),
('차도현', '서울', NULL),
('백로운', '광주', '연락처-E05');

INSERT INTO book_orders (reader_id, book_id, quantity, ordered_on) VALUES
(100, 1, 1, '2026-05-01'),
(100, 3, 2, '2026-05-03'),
(101, 2, 1, '2026-05-04'),
(102, 5, 1, '2026-05-05'),
(102, 8, 2, '2026-05-07'),
(104, 6, 1, '2026-05-08');

COMMIT;

-- 8. 도서명에 '데이터'가 포함된 도서를 조회하시오.
SELECT book_id, title, publisher, price
FROM books
WHERE title LIKE '%데이터%';

-- 9. 가격이 가장 높은 도서를 조회하시오.
SELECT book_id, title, publisher, price
FROM books
WHERE price = (SELECT MAX(price) FROM books);

-- 10. 2026-05-03부터 2026-05-07 사이에 주문된 도서명과 주문일을 조회하시오.
SELECT o.order_id, b.title, o.ordered_on
FROM book_orders o
INNER JOIN books b
        ON o.book_id = b.book_id
WHERE o.ordered_on BETWEEN '2026-05-03' AND '2026-05-07'
ORDER BY o.ordered_on;

-- 11. 주문 이력이 없는 독자명을 조회하시오.
SELECT r.reader_name
FROM readers r
LEFT JOIN book_orders o
       ON r.reader_id = o.reader_id
WHERE o.order_id IS NULL;

-- 12. 판매된 적이 없는 도서명을 조회하시오.
SELECT b.title
FROM books b
LEFT JOIN book_orders o
       ON b.book_id = o.book_id
WHERE o.order_id IS NULL;

-- 13. 출판사별 판매 수량을 조회하시오. 판매 이력이 없으면 0으로 표시하시오.
SELECT b.publisher, IFNULL(SUM(o.quantity), 0) AS sales_quantity
FROM books b
LEFT JOIN book_orders o
       ON b.book_id = o.book_id
GROUP BY b.publisher;

-- 14. 독자별 총 구매금액과 주문 건수를 조회하시오. 구매 이력이 없는 독자도 0으로 표시하시오.
SELECT r.reader_name,
       IFNULL(SUM(b.price * o.quantity), 0) AS total_amount,
       COUNT(o.order_id) AS order_count
FROM readers r
LEFT JOIN book_orders o
       ON r.reader_id = o.reader_id
LEFT JOIN books b
       ON b.book_id = o.book_id
GROUP BY r.reader_id, r.reader_name
ORDER BY r.reader_id;

-- 15. 총 구매금액 상위 2명의 독자명과 총 구매금액을 조회하시오.
SELECT r.reader_name, SUM(b.price * o.quantity) AS total_amount
FROM readers r
INNER JOIN book_orders o
        ON r.reader_id = o.reader_id
INNER JOIN books b
        ON b.book_id = o.book_id
GROUP BY r.reader_id, r.reader_name
ORDER BY total_amount DESC
LIMIT 2;
