package ex03_dql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import ex01_db_connect.DBConnect;

public class Main {

  // 부서 목록 반환 메서드
  public static List<Department> findDepartments() throws Exception {
    List<Department> departments = new ArrayList<>();

    Connection conn = DBConnect.getConnection();

    StringBuilder sb = new StringBuilder();
    sb.append("SELECT dept_id, ");
    sb.append("dept_name, ");
    sb.append("location ");
    sb.append("FROM departments ");
    sb.append("LIMIT 0, 10");
    String sql = sb.toString();

    PreparedStatement ps = conn.prepareStatement(sql);

    ResultSet rs = ps.executeQuery();

    while (rs.next()) {

      Department dept = Department.builder()
          .deptId(rs.getInt("dept_id"))
          .deptName(rs.getString("dept_name"))
          .location(rs.getString("location"))
          .build();

      departments.add(dept);

    }

    if (rs != null)
      rs.close();
    if (ps != null)
      ps.close();
    if (conn != null)
      conn.close();

    return departments;
  }

  // 부서 수 반환 메서드
  public static int getDepartmentsCount() throws Exception {

    Connection conn = DBConnect.getConnection();

    String sql = "SELECT COUNT(*) AS dept_count FROM departments";

    PreparedStatement ps = conn.prepareStatement(sql);

    ResultSet rs = ps.executeQuery();

    int deptCount = 0;
    if (rs.next()) {
      deptCount = rs.getInt("dept_count");
      System.out.println(deptCount + "개 부서가 조회되었습니다.");
    }

    if (rs != null)
      rs.close();
    if (ps != null)
      ps.close();
    if (conn != null)
      conn.close();

    return deptCount;
  }

  public static void main(String[] args) {

    try {

      int deptCount = getDepartmentsCount();
      System.out.println("받아 온 부서 수: " + deptCount);

      List<Department> departments = findDepartments();
      departments.stream()
          .forEach(d -> System.out.println(d)); // .forEach(System.out::println)

      Gson gson = new Gson();
      String jsonResult = gson.toJson(departments);
      System.out.println(jsonResult);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
