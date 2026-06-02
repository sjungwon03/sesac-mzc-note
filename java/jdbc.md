# JDBC 학습 노트

## 1. JDBC 개요

### 1.1 JDBC란?

**Java Database Connectivity**

관계형 데이터베이스(RDBMS)에 접속하고 SQL문을 실행할 수 있도록 해주는 자바 표준 API

### 1.2 존재 이유

DB 벤더마다 데이터베이스 접근 방식이 다름 → 자바에서 통일된 인터페이스 제공

DB 종류가 바뀌어도 자바 코드는 동일하게 유지 가능

### 1.3 JDBC 아키텍처

```
자바 애플리케이션
        ↓
    JDBC API (java.sql 패키지)
        ↓
  JDBC DriverManager
        ↓
  ┌─────┼─────┐
MySQL  Oracle PostgreSQL  (각 벤더별 드라이버)
```

**핵심:** JDBC API는 인터페이스만 정의, 실제 구현은 각 데이터베이스 벤더의 드라이버가 담당

### 1.4 JDBC 프로그래밍 6단계

| 단계 | 설명 | 주요 클래스/인터페이스 |
|------|------|------------------------|
| 1. 드라이버 로딩 | 사용할 DB 드라이버를 메모리에 올림 | `Class.forName()` |
| 2. 데이터베이스 연결 | DB에 접속하여 Connection 객체 생성 | `DriverManager`, `Connection` |
| 3. SQL 실행 준비 | SQL문을 전달하고 Statement 객체 생성 | `PreparedStatement` |
| 4. SQL 실행 | 쿼리 실행 | `executeQuery()`, `executeUpdate()` |
| 5. 결과 처리 | 결과를 읽어와 가공 | `ResultSet`, `int` |
| 6. 자원 해제 | 사용한 자원을 반납 | `close()` |

---

## 2. JDBC 주요 인터페이스

### 2.1 Driver 인터페이스

**정의:** 데이터베이스와의 연결을 담당하는 드라이버

**특징:**
- DB 벤더에서 직접 제공 (JAR 파일 배포)
- JDBC 4.0 이후부터는 자동 로딩 지원 (Class.forName 생략 가능)
- 하지만 명시적 로딩이 안전하므로 권장

### 2.2 DriverManager 클래스

**정의:** 데이터베이스 드라이버를 관리하고 Connection 객체를 생성

**핵심 메서드:**

| 메서드 | 설명 |
|--------|------|
| `getConnection(url, user, password)` | DB에 연결하여 Connection 반환 |

### 2.3 Connection 인터페이스

**정의:** 데이터베이스와의 연결을 나타내는 객체

**역할:**
- 트랜잭션 관리 (commit, rollback)
- PreparedStatement, Statement 객체 생성
- 연결 종료

**핵심 메서드:**

| 메서드 | 설명 |
|--------|------|
| `prepareStatement(sql)` | PreparedStatement 객체 생성 |
| `setAutoCommit(boolean)` | 자동 커밋 설정 |
| `commit()` | 트랜잭션 커밋 |
| `rollback()` | 트랜잭션 롤백 |
| `close()` | 연결 해제 |

### 2.4 PreparedStatement 인터페이스

**정의:** SQL문을 미리 컴파일하여 실행하는 객체

**Statement vs PreparedStatement 비교:**

| 구분 | Statement | PreparedStatement |
|------|-----------|-------------------|
| SQL 컴파일 | 매번 실행 시 컴파일 | 미리 한 번 컴파일 |
| 파라미터 바인딩 | 문자열 결합 | `?` 플레이스홀더 |
| SQL Injection | 취약함 | 방어 가능 |
| 성능 | 상대적으로 느림 | 빠름 |
| 가독성 | 떨어짐 (`'` + 변수 + `'`) | 깔끔 |

**핵심 메서드:**

| 메서드 | 설명 |
|--------|------|
| `setInt(index, value)` | ? 위치에 int 값 바인딩 |
| `setString(index, value)` | ? 위치에 String 값 바인딩 |
| `executeQuery()` | SELECT 실행, ResultSet 반환 |
| `executeUpdate()` | INSERT/UPDATE/DELETE 실행, 영향받은 행 수 반환 |

### 2.5 ResultSet 인터페이스

**정의:** SELECT 쿼리의 결과 집합을 행 단위로 처리하는 커서

**동작 방식:**
- 초기 커서는 첫 번째 행 **이전**에 위치
- `next()` 메서드를 호출하면 커서가 다음 행으로 이동
- `next()`가 false를 반환하면 더 이상 행이 없음

**데이터 읽기 메서드:**

| 메서드 | 설명 |
|--------|------|
| `getInt(column)` | int 값 읽기 |
| `getString(column)` | String 값 읽기 |
| `getDouble(column)` | double 값 읽기 |
| `getDate(column)` | Date 값 읽기 |
| `next()` | 다음 행으로 이동 (boolean) |

> `column`은 칼럼명(문자열) 또는 칼럼 인덱스(1부터 시작)를 사용

---

## 3. 데이터베이스 드라이버 및 URL

### 3.1 주요 데이터베이스 드라이버

| DB 종류 | 드라이버 클래스 | DB URL 형식 |
|---------|----------------|-------------|
| MySQL | `com.mysql.cj.jdbc.Driver` | `jdbc:mysql://[IP]:[Port]/[DB명]` |
| Oracle XE | `oracle.jdbc.driver.OracleDriver` | `jdbc:oracle:thin:@[IP]:[Port]:[SID]` |
| PostgreSQL | `org.postgresql.Driver` | `jdbc:postgresql://[IP]:[Port]/[DB명]` |
| MariaDB | `org.mariadb.jdbc.Driver` | `jdbc:mariadb://[IP]:[Port]/[DB명]` |

### 3.2 MySQL URL 상세

```
jdbc:mysql://localhost:3306/my_database?serverTimezone=UTC&characterEncoding=UTF-8
```

| 부분 | 설명 |
|------|------|
| `jdbc:mysql://` | 프로토콜 (JDBC + DB 종류) |
| `localhost` | 서버 IP (로컬: localhost 또는 127.0.0.1) |
| `3306` | 포트번호 (MySQL 기본: 3306) |
| `my_database` | 접속할 데이터베이스명 |
| `serverTimezone=UTC` | 타임존 설정 (MySQL 8.x 필수) |
| `characterEncoding=UTF-8` | 문자 인코딩 |

---

## 4. 단계별 코드 상세

### 4.1 드라이버 로딩

```java
Class.forName("com.mysql.cj.jdbc.Driver");
```

**주의:**
- MySQL 5.x: `com.mysql.jdbc.Driver`
- MySQL 8.x: `com.mysql.cj.jdbc.Driver` (cj 추가됨)
- ClassNotFoundException 발생 가능 → 예외 처리 필요

### 4.2 데이터베이스 연결

```java
String url = "jdbc:mysql://localhost:3306/my_database?serverTimezone=UTC";
String user = "root";
String password = "P@ssw0rd";

Connection conn = DriverManager.getConnection(url, user, password);
```

**주의사항:**
- 비밀번호가 틀리면 SQLException 발생
- DB 서버가 꺼져 있으면 연결 실패
- URL이 잘못되면 연결 실패

### 4.3 SQL 실행 (조회 - SELECT)

```java
String sql = "SELECT id, name, email FROM users WHERE id = ?";

PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, 10);  // 첫 번째 ?에 10 바인딩

ResultSet rs = pstmt.executeQuery();

while (rs.next()) {
    int id = rs.getInt("id");              // rs.getInt(1) 도 가능
    String name = rs.getString("name");    // rs.getString(2) 도 가능
    String email = rs.getString("email");  // rs.getString(3) 도 가능
    System.out.println(id + " | " + name + " | " + email);
}
```

**파라미터 바인딩 규칙:**
- `?`의 순서는 1부터 시작 (0이 아님)
- 자료형에 맞는 set 메서드 사용 (`setInt`, `setString`, `setDouble` 등)

### 4.4 SQL 실행 (삽입 - INSERT)

```java
String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, "홍길동");
pstmt.setString(2, "hong@example.com");

int result = pstmt.executeUpdate();

System.out.println(result + "행이 추가되었습니다.");
```

### 4.5 SQL 실행 (수정 - UPDATE)

```java
String sql = "UPDATE users SET email = ? WHERE id = ?";

PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, "new@email.com");
pstmt.setInt(2, 10);

int result = pstmt.executeUpdate();

System.out.println(result + "행이 수정되었습니다.");
```

### 4.6 SQL 실행 (삭제 - DELETE)

```java
String sql = "DELETE FROM users WHERE id = ?";

PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, 10);

int result = pstmt.executeUpdate();

System.out.println(result + "행이 삭제되었습니다.");
```

### 4.7 DML 실행 결과 정리

| 쿼리 | 실행 메서드 | 반환값 | 의미 |
|------|------------|--------|------|
| SELECT | `executeQuery()` | `ResultSet` | 결과 집합 |
| INSERT | `executeUpdate()` | `int` | 삽입된 행 수 |
| UPDATE | `executeUpdate()` | `int` | 수정된 행 수 |
| DELETE | `executeUpdate()` | `int` | 삭제된 행 수 |

### 4.8 자원 해제

**원칙:** 생성의 역순으로 해제

```
ResultSet → PreparedStatement → Connection
```

```java
try { if (rs != null) rs.close(); } catch (Exception e) {}
try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
try { if (conn != null) conn.close(); } catch (Exception e) {}
```

**try-with-resources 활용 (권장):**

```java
try (Connection conn = DriverManager.getConnection(url, user, password);
     PreparedStatement pstmt = conn.prepareStatement(sql)) {

    pstmt.setString(1, "홍길동");
    int result = pstmt.executeUpdate();

} catch (SQLException e) {
    e.printStackTrace();
}
// try 블록 종료 시 자동으로 close() 호출됨
```

**try-with-resources 장점:**
- 자원 해제 코드를 직접 작성할 필요 없음
- finally 블록에서 close() 예외가 발생하는 문제 해결
- 코드가 간결해짐

---

## 5. 트랜잭션 관리

### 5.1 트랜잭션이란?

데이터베이스에서 하나의 논리적 작업을 구성하는 여러 SQL문의 집합

### 5.2 ACID 특성

| 특성 | 설명 |
|------|------|
| 원자성 (Atomicity) | 작업이 모두 완료되거나 모두 취소됨 |
| 일관성 (Consistency) | 트랜잭션 전후로 데이터 무결성 유지 |
| 고립성 (Isolation) | 다른 트랜잭션과 격리되어 실행 |
| 지속성 (Durability) | 커밋된 결과는 영구적으로 저장 |

### 5.3 JDBC에서 트랜잭션 처리

**기본 동작:** auto-commit 모드 (SQL 실행마다 자동 커밋)

```java
// 수동 커밋 모드로 전환
conn.setAutoCommit(false);

try {
    // 작업 1
    pstmt1.executeUpdate();

    // 작업 2
    pstmt2.executeUpdate();

    // 모두 성공 시 커밋
    conn.commit();

} catch (SQLException e) {
    // 하나라도 실패 시 롤백
    conn.rollback();
    e.printStackTrace();

} finally {
    // 다시 auto-commit 모드로 복귀
    conn.setAutoCommit(true);
}
```

### 5.4 트랜잭션 예시: 계좌 이체

```java
conn.setAutoCommit(false);

try {
    // 출금
    String outSql = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
    pstmt = conn.prepareStatement(outSql);
    pstmt.setInt(1, 100000);
    pstmt.setString(2, "110-001");
    pstmt.executeUpdate();

    // 입금
    String inSql = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
    pstmt = conn.prepareStatement(inSql);
    pstmt.setInt(1, 100000);
    pstmt.setString(2, "110-002");
    pstmt.executeUpdate();

    // 모두 성공 시 커밋
    conn.commit();
    System.out.println("이체 완료");

} catch (SQLException e) {
    // 실패 시 롤백 (출금도 취소됨)
    conn.rollback();
    System.out.println("이체 실패 - 롤백");
    e.printStackTrace();
}
```

---

## 6. SQL Injection 대응

### 6.1 SQL Injection이란?

사용자 입력을 SQL문에 직접 삽입하여 악의적인 SQL을 실행시키는 공격

### 6.2 취약한 코드 (Statement + 문자열 결합)

```java
// 취약! - SQL Injection 가능
String userId = request.getParameter("userId");
String sql = "SELECT * FROM users WHERE id = '" + userId + "'";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

**공격 예시:**
```
userId 입력값: ' OR '1'='1
```
→ 실행되는 SQL: `SELECT * FROM users WHERE id = '' OR '1'='1'` (모든 행 조회)

### 6.3 안전한 코드 (PreparedStatement)

```java
// 안전! - PreparedStatement가 파라미터를 데이터로만 처리
String sql = "SELECT * FROM users WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, userId);
ResultSet rs = pstmt.executeQuery();
```

**핵심:** PreparedStatement는 `?`를 데이터 값으로만 취급 → SQL 구문이 바뀔 수 없음

---

## 7. 전체 예시 코드

### 7.1 조회(DQL) 전체 예시

**미리 실행할 쿼리:**
```sql
CREATE DATABASE IF NOT EXISTS my_database;
USE my_database;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

INSERT INTO users (id, name, email) VALUES (10, '홍길동', 'hong@example.com');
INSERT INTO users (name, email) VALUES ('김철수', 'kim@example.com');
```

**Main.java (조회):**
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class User {
    private int id;
    private String name;
    private String email;

    public User() {}
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // getters, setters, toString 생략
}

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/my_database?serverTimezone=UTC";
        String user = "root";
        String password = "P@ssw0rd";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("DB 연결 성공!");

            String sql = "SELECT id, name, email FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 10);

            rs = pstmt.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                users.add(new User(id, name, email));
            }
            System.out.println(users);

        } catch (ClassNotFoundException e) {
            System.err.println("드라이버를 찾을 수 없습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
}
```

### 7.2 삽입(DML) 전체 예시 (try-with-resources)

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/my_database?serverTimezone=UTC";
        String user = "root";
        String password = "P@ssw0rd";

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("DB 연결 성공!");

            pstmt.setString(1, "이순신");
            pstmt.setString(2, "lee@example.com");

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("회원 등록 성공! (" + result + "건)");
            } else {
                System.out.println("회원 등록 실패.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 8. Connection Pool (커넥션 풀)

### 8.1 문제점

`DriverManager.getConnection()` 매번 새로운 연결 생성 → 오버헤드 큼

| 작업 | 시간 |
|------|------|
| 연결 생성 | 10~100ms |
| SQL 실행 | 1~5ms |

→ 연결 생성이 SQL 실행보다 훨씬 비쌈

### 8.2 Connection Pool 개념

**아이디어:** 미리 DB 연결을 만들어두고 재사용

```
[Connection Pool]
┌─────────────────┐
│ Connection #1   │ ← 사용 중
│ Connection #2   │ ← 대기
│ Connection #3   │ ← 대기
│ ...             │
│ Connection #10  │ ← 사용 중
└─────────────────┘
```

1. 애플리케이션 시작 시 미리 연결 생성
2. 필요할 때 풀에서 빌려쓰고 반납
3. 연결을 닫는 것이 아니라 풀로 반환

### 8.3 HikariCP

현재 가장 널리 사용되는 커넥션 풀 라이브러리

**특징:**
- 빠르고 가벼움
- Spring Boot 기본 커넥션 풀

---

## 9. 예외 처리

### 9.1 주요 예외

| 예외 | 발생 상황 |
|------|-----------|
| `ClassNotFoundException` | 드라이버 클래스를 찾을 수 없음 |
| `SQLException` | SQL 실행 오류 (문법 에러, 연결 실패 등) |

### 9.2 SQLException 상세 정보

```java
catch (SQLException e) {
    System.err.println("SQL 상태: " + e.getSQLState());
    System.err.println("에러 코드: " + e.getErrorCode());
    System.err.println("메시지: " + e.getMessage());
    e.printStackTrace();
}
```

---

## 10. 핵심 요약

### JDBC 프로그래밍 6단계
1. **드라이버 로딩** - `Class.forName("com.mysql.cj.jdbc.Driver")`
2. **연결** - `DriverManager.getConnection(url, user, password)`
3. **준비** - `conn.prepareStatement(sql)` (`?` 파라미터 사용)
4. **실행** - `executeQuery()` (SELECT), `executeUpdate()` (INSERT/UPDATE/DELETE)
5. **처리** - `ResultSet` (조회), `int` (DML 영향 행 수)
6. **해제** - 생성 역순으로 `close()` 또는 try-with-resources

### 핵심 인터페이스
- **Connection:** DB 연결, 트랜잭션 관리
- **PreparedStatement:** 미리 컴파일된 SQL, SQL Injection 방어
- **ResultSet:** SELECT 결과 행 단위 처리

### 트랜잭션
- `setAutoCommit(false)` → 작업 → `commit()` / `rollback()`
- ACID: 원자성, 일관성, 고립성, 지속성

### 보안
- Statement + 문자열 결합 → SQL Injection 취약
- PreparedStatement → `?` 파라미터로 안전

### 성능
- DriverManager.getConnection() → 매번 새 연결 (느림)
- Connection Pool (HikariCP) → 연결 재사용 (빠름)
