package ex02_dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ex01_db_connect.DBConnect;

// java.sql.PreparedStatement
// 1. 쿼리문(문자열로 준비)을 실행하는 인터페이스
// 2. 미리 쿼리문을 컴파일 해 둔 뒤, 실행 직전 필요한 값을 전달하는 방식을 사용 
// 3. Placeholder(?)를 이용한 파라미터 바인딩 지원 (SQL Injection 방지)

// DML (INSERT, UPDATE, DELETE) 실행 결과
// 영향을 받은 행의 갯수 반환 (int)

public class Main {

  public static void insert() throws Exception {

    // 커넥션 받아 오기
    Connection conn = DBConnect.getConnection();

    // 쿼리문 만들기
    String sql = "INSERT INTO departments (dept_name, location) VALUES (?, ?)";

    // 쿼리문 실행 객체 받아 오기
    PreparedStatement ps = conn.prepareStatement(sql);

    // 파라미터 바인딩 (?에 값 채우기): 채울 때 문자열은 작은 따옴표('')로 자동으로 감쌈
    ps.setString(1, "QA");
    ps.setString(2, "Incheon");

    // 쿼리문 실행하기 (실행 결과는 int 타입)
    int result = ps.executeUpdate();

    // 결과 확인
    System.out.println(result + " 행이 등록되었습니다.");

    // 자원 반납 (생성의 역순으로 반납: Connection 객체, PreparedStatement 객체 닫기)
    if (ps != null)
      ps.close();
    if (conn != null)
      conn.close();
  }

  public static void update() {

    // try, catch, finally 블록에서 사용할 수 있도록 Scope 조정
    Connection conn = null;
    PreparedStatement ps1 = null;
    PreparedStatement ps2 = null;

    try {
      conn = DBConnect.getConnection();
      conn.setAutoCommit(false); // 현재 커넥션은 수동으로 커밋

      // 트랜잭션의 첫 번째 작업
      String sql1 = "UPDATE departments SET dept_name = ? WHERE dept_id = ?";
      ps1 = conn.prepareStatement(sql1);
      ps1.setString(1, "개발이뭔지");
      ps1.setInt(2, 5);
      ps1.executeUpdate();

      // 만약, 첫 번째 작업 이후 예외가 발생했다면 (네트워크 오류 등)
      if (Math.random() > 0.0001) {
        throw new SQLException("네트워크 예외 발생");
      }

      // 트랜잭션의 두 번째 작업
      String sql2 = "UPDATE departments SET location = ? WHERE dept_id = ?";
      ps2 = conn.prepareStatement(sql2);
      ps2.setString(1, "Seoul");
      ps2.setInt(2, 5);
      ps2.executeUpdate();

      // 커밋 완료
      conn.commit();
      System.out.println("트랜잭션이 성공했습니다.");

    } catch (Exception e) {

      // 예외 발생 시 모든 작업 취소
      if (conn != null) {
        try {
          conn.rollback();
          System.err.println("트랜잭션을 롤백했습니다.");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    } finally {

      // 자원 반납
      try {
        if (conn != null) {
          conn.setAutoCommit(true); // 기본 세팅으로 변경
          conn.close();
        }
        if (ps1 != null)
          ps1.close();
        if (ps2 != null)
          ps2.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

  }

  public static void delete() {

  }

  public static void main(String[] args) {
    try {
      // insert();
    } catch (Exception e) {
      e.printStackTrace();
    }

    update();

  }
}
