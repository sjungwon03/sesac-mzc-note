# DML 학습 노트

## 1. DML이란?

**Data Manipulation Language (데이터 조작어)**의 약자로, 데이터베이스에 데이터를 **삽입, 수정, 삭제**하는 역할을 수행하는 SQL 문이다.

**DDL vs DML:**

- **DDL** (CREATE, ALTER, DROP): 테이블 **구조** 정의
- **DML** (INSERT, UPDATE, DELETE): 테이블 **내부 데이터** 조작

**주요 명령어 (CUD):**

- **C**reate → `INSERT`
- **U**pdate → `UPDATE`
- **D**elete → `DELETE`

---

## 2. INSERT (데이터 삽입)

### 2.1 기본 문법

```sql
INSERT INTO 테이블_이름
    [(칼럼1, 칼럼2, ...)]
VALUES
    (값1, 값2, ...);
```

### 2.2 활용 예시

**1) 특정 칼럼 지정 (권장)**

```sql
INSERT INTO nation (code, name, continent)
VALUES (1, 'KOREA', 'ASIA');
```

**2) 전체 칼럼에 값 삽입**

```sql
INSERT INTO nation
VALUES (2, 'USA', 'AMERICA');
```

### 2.3 벌크 삽입 (Bulk Insert)

한 번에 여러 행을 삽입하여 성능 극대화

```sql
INSERT INTO nation (code, name, continent)
VALUES
    (1, 'KOREA', 'ASIA'),
    (2, 'USA', 'AMERICA');
```

---

## 3. UPDATE (데이터 수정)

### 3.1 기본 문법

```sql
UPDATE 테이블_이름
SET 칼럼1 = 값1, 칼럼2 = 값2, ...
[WHERE 조건절];
```

### 3.2 활용 예시

```sql
UPDATE nation
SET name = 'JAPAN', continent = 'ASIA'
WHERE code = 2;
```

### 3.3 주의사항

**WHERE 절 생략 시 모든 행이 수정됨**

- 실무에서 WHERE 절 생략은 거의 없음
- 항상 조건절 확인 필수

---

## 4. DELETE (데이터 삭제)

### 4.1 기본 문법

```sql
DELETE FROM 테이블_이름
[WHERE 조건절];
```

### 4.2 활용 예시

```sql
DELETE FROM nation
WHERE code = 2;
```

### 4.3 주의사항

**WHERE 절 생략 시 모든 행이 삭제됨**

- `UPDATE`와 동일하게 WHERE 절 생략 주의
- 실무에서는 거의 항상 WHERE 절 필요

---

## 5. MySQL 전용 DML

### 5.1 INSERT ... ON DUPLICATE KEY UPDATE

**목적:** 기본키나 유니크 인덱스 중복 시 INSERT 대신 UPDATE 수행

```sql
INSERT INTO employees (id, name, department, salary, hire_date)
VALUES (1, '홍길동', '개발팀', 5000000, '2026-03-01')
ON DUPLICATE KEY UPDATE salary = 6000000;
```

**동작:**

- id가 1인 데이터가 없으면 → INSERT
- id가 1인 데이터가 있으면 → salary UPDATE

### 5.2 REPLACE INTO

**목적:** 중복 시 기존 행 DELETE 후 새로 INSERT

```sql
REPLACE INTO employees (id, name, department, salary, hire_date)
VALUES (1, '홍길동', 'TF팀', 7000000, '2026-03-01');
```

**동작:**

- id가 1인 데이터가 없으면 → INSERT
- id가 1인 데이터가 있으면 → DELETE 후 INSERT

**차이점:**
| 명령어 | 중복 시 동작 |
|--------|-------------|
| INSERT ... ON DUPLICATE KEY UPDATE | 기존 행 UPDATE |
| REPLACE INTO | 기존 행 DELETE 후 INSERT |

---

## 6. 트랜잭션 (Transaction)

### 6.1 정의

데이터베이스의 상태를 변화시키는 **하나의 논리적 작업 단위**

### 6.2 예시: 계좌 이체

A가 B에게 10,000원 이체 시 필요한 연산:

1. A 계좌에서 10,000원 차감 (UPDATE)
2. B 계좌에 10,000원 입금 (UPDATE)

**문제 상황:** 연산 1 성공 후 연산 2 실패 시

- A의 돈은 사라지고 B는 돈을 받지 못함

**해결:** 두 연산을 하나의 트랜잭션으로 묶어서 **All or Nothing** 보장

---

## 7. 트랜잭션 ACID 특성

### 7.1 원자성 (Atomicity)

**All or Nothing**

- 트랜잭션 내 모든 작업이 전부 성공하거나, 하나라도 실패하면 전부 취소

**명령어:**

- `COMMIT`: 성공적으로 반영
- `ROLLBACK`: 실패 시 처음 상태로 복구

### 7.2 일관성 (Consistency)

트랜잭션 완료 후에도 데이터베이스 규칙(제약 조건)을 만족해야 함

**예시:** '잔고는 0원 미만이 될 수 없다'는 규칙이 있다면, 트랜잭션 후에도 이 규칙이 깨지지 않아야 함

### 7.3 격리성 (Isolation)

수행 중인 트랜잭션은 다른 트랜잭션의 간섭을 받지 않아야 함

- 여러 사용자가 동시에 동일 데이터 수정 시, 각 트랜잭션은 독립적으로 수행
- 격리 수준에 따라 동시 접근 범위 조절

### 7.4 지속성 (Durability)

성공적으로 완료(COMMIT)된 트랜잭션의 결과는 영구적으로 반영

- 시스템 장애, 정전이 발생해도 로그 등을 통해 복구 가능

---

## 8. 트랜잭션 제어어 (TCL)

### 8.1 START TRANSACTION (또는 BEGIN)

트랜잭션 수동 시작

```sql
START TRANSACTION;
-- 또는
BEGIN;
```

### 8.2 COMMIT

모든 DML 연산을 데이터베이스에 영구 반영 후 트랜잭션 종료

```sql
COMMIT;
```

### 8.3 ROLLBACK

START TRANSACTION 이후의 모든 DML 작업 취소

```sql
ROLLBACK;
```

---

## 9. 트랜잭션 실전 예시

### 9.1 기본 SQL 예시

```sql
-- 1. 트랜잭션 시작
START TRANSACTION;

-- 2. A의 잔액 10,000원 차감
UPDATE accounts SET balance = balance - 10000 WHERE user_name = 'A';

-- 3. B의 잔액 10,000원 증가
UPDATE accounts SET balance = balance + 10000 WHERE user_name = 'B';

-- 4. 성공 시 커밋
COMMIT;

-- 실패 시 롤백
-- ROLLBACK;
```

### 9.2 프로시저 활용 예시

```sql
DELIMITER //

CREATE PROCEDURE Transfer()
BEGIN
    -- 에러 발생 시 롤백 핸들러
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT '에러 발생! 이체 취소!' AS Result;
    END;

    -- 트랜잭션 시작
    START TRANSACTION;

    -- 비즈니스 로직
    UPDATE accounts SET balance = balance - 10000 WHERE user_name = 'A';
    UPDATE accounts SET balance = balance + 10000 WHERE user_name = 'B';

    -- 성공 시 커밋
    COMMIT;
    SELECT '이체 성공!' AS Result;
END //

DELIMITER ;
```

---

## 10. 실무 트랜잭션 처리

### 10.1 애플리케이션에서의 처리

일반적으로 SQL이 아닌 애플리케이션 코드에서 트랜잭션 처리

**Java 예시:**

```java
try {
    // 트랜잭션 시작
    connection.setAutoCommit(false);

    // DML 실행
    updateAccountA();
    updateAccountB();

    // 성공 시 커밋
    connection.commit();
} catch (Exception e) {
    // 실패 시 롤백
    connection.rollback();
}
```

---

## 11. 핵심 요약

### DML 명령어

1. **INSERT:** 데이터 삽입 (벌크 삽입으로 성능 향상)
2. **UPDATE:** 데이터 수정 (WHERE 절 필수 확인)
3. **DELETE:** 데이터 삭제 (WHERE 절 필수 확인)

### MySQL 전용

- **INSERT ... ON DUPLICATE KEY UPDATE:** 중복 시 UPDATE
- **REPLACE INTO:** 중복 시 DELETE 후 INSERT

### 트랜잭션 ACID

- **A**tomicity: 원자성 (All or Nothing)
- **C**onsistency: 일관성 (규칙 유지)
- **I**solation: 격리성 (독립적 수행)
- **D**urability: 지속성 (영구 반영)

### TCL 명령어

- **START TRANSACTION / BEGIN:** 트랜잭션 시작
- **COMMIT:** 영구 반영
- **ROLLBACK:** 취소

### 실무 팁

- UPDATE, DELETE는 항상 WHERE 절 확인
- 중요 작업은 트랜잭션으로 묶어서 처리
- 에러 처리를 위한 try-catch와 ROLLBACK 활용
