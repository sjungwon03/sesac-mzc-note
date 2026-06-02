package ex02_output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Main {
  public static void main(String[] args) {

    // File 객체 (우리가 만드려는 대상)
    File dir = new File("/storage");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir, "test.txt");

    // 파일로 데이터를 보내는 스트림:통로 (출력 스트림)
    try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file, true))) {

      // 실제로 데이터 내보내기 (int, byte[])
      int c = 'A';
      byte[] b = "pple".getBytes();
      fos.write(c);
      fos.write(b);

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
