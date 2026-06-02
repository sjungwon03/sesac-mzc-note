package ex05_interface;

public interface Shape {
  public double getPerimeter();  // 둘레 길이 구하기 (public abstract 생략)
  public double getArea();  // 넓이 구하기
}

/* 
public abstract class Shape {
  public abstract double getPerimeter();  // 둘레 길이 구하기
  public abstract double getArea();  // 넓이 구하기
}
*/