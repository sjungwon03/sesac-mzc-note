-- 온라인 마켓 SQL 실습: 정답 파일
DROP DATABASE IF EXISTS market_practice_db;
CREATE DATABASE market_practice_db;
USE market_practice_db;

DROP TABLE IF EXISTS purchase_items;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    customer_id INT NOT NULL AUTO_INCREMENT,
    login_id VARCHAR(20) NOT NULL UNIQUE,
    customer_name VARCHAR(30) NOT NULL,
    city VARCHAR(30),
    phone VARCHAR(20),
    joined_at DATE,
    CONSTRAINT pk_customers PRIMARY KEY (customer_id)
) ENGINE=InnoDB COMMENT '고객';

CREATE TABLE products (
    product_id INT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(40) NOT NULL,
    category VARCHAR(20) NOT NULL,
    price INT NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (product_id)
) ENGINE=InnoDB COMMENT '상품';

CREATE TABLE purchase_items (
    purchase_id INT NOT NULL AUTO_INCREMENT,
    customer_id INT,
    product_id INT,
    quantity INT NOT NULL,
    purchased_at DATE NOT NULL,
    CONSTRAINT pk_purchase_items PRIMARY KEY (purchase_id),
    CONSTRAINT fk_purchase_customer FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id),
    CONSTRAINT fk_purchase_product FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE SET NULL
) ENGINE=InnoDB COMMENT '구매 내역';

INSERT INTO customers (login_id, customer_name, city, phone, joined_at) VALUES
('river01', '강리버', '서울', '연락처-0001', '2024-01-05'),
('cloud02', '구름별', '부산', '연락처-0002', '2024-02-14'),
('forest03', '숲하나', '서울', NULL, '2024-03-20'),
('stone04', '돌민수', '대전', '연락처-0004', '2024-04-02'),
('ocean05', '바다윤', '인천', NULL, '2024-05-18'),
('field06', '들세라', '부산', '연락처-0006', '2024-06-01');

INSERT INTO products (product_name, category, price) VALUES
('무선 키보드', '디지털', 45000),
('텀블러', '생활', 18000),
('백팩', '패션', 62000),
('USB 허브', '디지털', 27000),
('스탠드 조명', '생활', 39000),
('운동 양말', '패션', 9000),
('노트 세트', '문구', 12000);

INSERT INTO purchase_items (customer_id, product_id, quantity, purchased_at) VALUES
(1, 1, 1, '2024-06-03'),
(1, 2, 2, '2024-06-04'),
(2, 4, 1, '2024-06-05'),
(2, 7, 3, '2024-06-06'),
(4, 3, 1, '2024-06-07'),
(4, 6, 5, '2024-06-08'),
(6, 1, 2, '2024-06-10'),
(6, 5, 1, '2024-06-11');

COMMIT;

/****************************** 정답 ******************************/

-- 1. 연락처가 등록되지 않은 고객의 아이디, 이름, 연락처를 조회하시오.
SELECT login_id, customer_name, phone
FROM customers
WHERE phone IS NULL;

-- 2. 2024년 4월 이후 가입한 고객의 아이디, 이름, 가입일을 조회하시오.
SELECT login_id, customer_name, joined_at
FROM customers
WHERE joined_at >= '2024-04-01';

-- 3. 고객의 이름과 연락처를 조회하시오. 연락처가 없으면 '미등록'으로 표시하시오.
SELECT customer_name, IFNULL(phone, '미등록') AS contact
FROM customers;

-- 4. 도시별 고객 수를 조회하시오.
SELECT city, COUNT(*) AS customer_count
FROM customers
GROUP BY city;

-- 5. 서울과 부산을 제외한 도시별 고객 수를 조회하시오.
SELECT city, COUNT(*) AS customer_count
FROM customers
WHERE city NOT IN ('서울', '부산')
GROUP BY city;

-- 6. 구매 이력이 없는 고객의 아이디와 이름을 조회하시오.
SELECT c.login_id, c.customer_name
FROM customers c
LEFT JOIN purchase_items p
       ON c.customer_id = p.customer_id
WHERE p.purchase_id IS NULL;

-- 7. 카테고리별 판매 건수를 조회하시오.
SELECT pr.category, COUNT(pi.purchase_id) AS purchase_count
FROM products pr
INNER JOIN purchase_items pi
        ON pr.product_id = pi.product_id
GROUP BY pr.category;

-- 8. 고객별 구매 건수를 조회하시오. 구매 이력이 없는 고객도 0으로 표시하시오.
SELECT c.login_id, c.customer_name, COUNT(pi.purchase_id) AS purchase_count
FROM customers c
LEFT JOIN purchase_items pi
       ON c.customer_id = pi.customer_id
GROUP BY c.customer_id, c.login_id, c.customer_name
ORDER BY c.login_id;

-- 9. 모든 상품의 상품명과 총 판매 수량을 조회하시오. 판매 이력이 없으면 0으로 표시하시오.
SELECT pr.product_name, IFNULL(SUM(pi.quantity), 0) AS total_quantity
FROM products pr
LEFT JOIN purchase_items pi
       ON pr.product_id = pi.product_id
GROUP BY pr.product_id, pr.product_name;

-- 10. 디지털 카테고리 상품을 구매한 고객명, 상품명, 구매금액을 조회하시오.
SELECT c.customer_name, pr.product_name, pr.price * pi.quantity AS purchase_amount
FROM customers c
INNER JOIN purchase_items pi
        ON c.customer_id = pi.customer_id
INNER JOIN products pr
        ON pr.product_id = pi.product_id
WHERE pr.category = '디지털';

-- 11. 총 구매금액이 70000원 이상인 고객명과 총 구매금액을 조회하시오.
SELECT c.customer_name, SUM(pr.price * pi.quantity) AS total_amount
FROM customers c
INNER JOIN purchase_items pi
        ON c.customer_id = pi.customer_id
INNER JOIN products pr
        ON pr.product_id = pi.product_id
GROUP BY c.customer_id, c.customer_name
HAVING total_amount >= 70000;

-- 12. 가장 최근 구매 내역의 고객명, 상품명, 구매일자를 조회하시오.
SELECT c.customer_name, pr.product_name, pi.purchased_at
FROM customers c
INNER JOIN purchase_items pi
        ON c.customer_id = pi.customer_id
INNER JOIN products pr
        ON pr.product_id = pi.product_id
WHERE pi.purchased_at = (SELECT MAX(purchased_at) FROM purchase_items);
