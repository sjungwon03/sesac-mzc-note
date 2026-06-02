package ex05_interface;

public class Main {
  public static void main(String[] args) {
    // Shape[] 배열
    Shape[] shapes = {
        new Circle(1),
        new Rectangle(1, 2)
    };

    // 순회하며 확인하기
    for (Shape s : shapes) {
      System.out.println("둘레: " + s.getPerimeter());
      System.out.println("넓이: " + s.getArea());
    }
  }
}
