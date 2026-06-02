package ex01_File;

import java.io.File;
import java.util.Arrays;

public class Main {
  public static void main(String[] args) {

    // 자바 홈을 File 객체로 생성하기
    File javaHome = new File("C:/Program Files/Java/jdk-21.0.10");

    // 기본 정보 확인
    System.out.println("이름: " + javaHome.getName());
    System.out.println("절대경로: " + javaHome.getAbsolutePath());
    System.out.println("상대경로: " + javaHome.getPath());
    System.out.println(javaHome.isDirectory() ? "디렉토리" : "파일");
    System.out.println("크기: " + javaHome.length() + "Byte");
    System.out.println("최종수정일: " + javaHome.lastModified());

    // 하위 디렉터리/파일 객체 가져오기
    File[] files = javaHome.listFiles();
    Arrays.stream(files)
        .filter(file -> file.isFile())
        .forEach(file -> {
          System.out.println(file.getName());
          System.out.println(file.length());
        });

    // 디렉터리 조작 (생성, 삭제)
    File dir = new File("C:/JavaStudy/io_test");
    if (dir.exists()) {
      System.out.println(dir.getAbsolutePath() + " 존재합니다.");
    } else {
      dir.mkdirs(); // 폴더 만들기 (하위 폴더까지 만듬)
      System.out.println(dir.getAbsolutePath() + " 생성했습니다.");
    }

    dir.delete(); // 폴더 지우기 (비어 있는 폴더만 지울 수 있음)

  }
}
