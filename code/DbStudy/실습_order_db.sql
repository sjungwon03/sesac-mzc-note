-- 카페 주문 SQL 실습: JOIN, SELF JOIN, 상관 서브쿼리
DROP DATABASE IF EXISTS cafe_order_db;
CREATE DATABASE cafe_order_db;
USE cafe_order_db;

DROP TABLE IF EXISTS order_drinks;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS drinks;
DROP TABLE IF EXISTS drink_categories;

CREATE TABLE drink_categories (
    category_id INT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(30) NOT NULL,
    parent_category_id INT,
    CONSTRAINT pk_drink_categories PRIMARY KEY (category_id),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_category_id)
        REFERENCES drink_categories(category_id)
) ENGINE=InnoDB COMMENT '음료 카테고리';

CREATE TABLE drinks (
    drink_id INT NOT NULL AUTO_INCREMENT,
    drink_name VARCHAR(50) NOT NULL,
    price INT NOT NULL,
    category_id INT NOT NULL,
    available_yn CHAR(1) NOT NULL DEFAULT 'Y',
    CONSTRAINT pk_drinks PRIMARY KEY (drink_id),
    CONSTRAINT fk_drink_category FOREIGN KEY (category_id)
        REFERENCES drink_categories(category_id)
) ENGINE=InnoDB COMMENT '음료';

CREATE TABLE orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    ordered_on DATE NOT NULL,
    ordered_time TIME NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (order_id)
) ENGINE=InnoDB COMMENT '주문';

CREATE TABLE order_drinks (
    order_id INT NOT NULL,
    drink_id INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT pk_order_drinks PRIMARY KEY (order_id, drink_id),
    CONSTRAINT fk_order_drinks_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id),
    CONSTRAINT fk_order_drinks_drink FOREIGN KEY (drink_id)
        REFERENCES drinks(drink_id)
) ENGINE=InnoDB COMMENT '주문 음료';

INSERT INTO drink_categories (category_name, parent_category_id) VALUES
('커피', NULL),
('논커피', NULL),
('디저트', NULL),
('에스프레소', 1),
('라떼', 1),
('티', 2),
('스무디', 2),
('케이크', 3);

INSERT INTO drinks (drink_name, price, category_id, available_yn) VALUES
('아메리카노', 3500, 4, 'Y'),
('카페라떼', 4500, 5, 'Y'),
('바닐라라떼', 5000, 5, 'Y'),
('유자차', 4300, 6, 'Y'),
('딸기스무디', 6200, 7, 'Y'),
('망고스무디', 6200, 7, 'N'),
('치즈케이크', 5800, 8, 'Y'),
('초코케이크', 6100, 8, 'Y');

INSERT INTO orders (ordered_on, ordered_time) VALUES
('2026-06-01', '09:15:00'),
('2026-06-01', '12:20:00'),
('2026-06-02', '10:05:00'),
('2026-06-02', '14:40:00');

INSERT INTO order_drinks (order_id, drink_id, quantity) VALUES
(1, 1, 2),
(1, 7, 1),
(2, 2, 1),
(2, 5, 2),
(3, 3, 1),
(3, 8, 1),
(4, 1, 1),
(4, 4, 2);

COMMIT;

-- 1. 음료명, 가격, 하위 카테고리명을 조회하시오.
SELECT d.drink_name, d.price, c.category_name
FROM drinks d
INNER JOIN drink_categories c
        ON d.category_id = c.category_id;

-- 2. 카테고리명과 상위 카테고리명을 조회하시오.
SELECT child.category_name AS category_name,
       parent.category_name AS parent_category_name
FROM drink_categories child
LEFT JOIN drink_categories parent
       ON child.parent_category_id = parent.category_id;

-- 3. 주문번호, 주문일자, 음료명, 수량, 주문금액을 조회하시오.
SELECT o.order_id,
       o.ordered_on,
       d.drink_name,
       od.quantity,
       d.price * od.quantity AS order_amount
FROM orders o
INNER JOIN order_drinks od
        ON o.order_id = od.order_id
INNER JOIN drinks d
        ON d.drink_id = od.drink_id;

-- 4. 판매된 적이 없는 음료를 조회하시오.
SELECT d.drink_id, d.drink_name
FROM drinks d
LEFT JOIN order_drinks od
       ON d.drink_id = od.drink_id
WHERE od.order_id IS NULL;

-- 5. 각 음료가 속한 카테고리 평균 가격보다 비싼 음료를 조회하시오.
SELECT d.drink_id, d.drink_name, d.price, d.category_id
FROM drinks d
WHERE d.price > (
    SELECT AVG(d2.price)
    FROM drinks d2
    WHERE d2.category_id = d.category_id
);
