package ex04_abstract;

public class Main {
  public static void main(String[] args) {

    Coffee coffee1 = new Espresso("브라질");
    Coffee coffee2 = new Americano("니카라과");

    coffee1.taste();
    coffee2.taste();

    coffee1.info();

    if (coffee1 instanceof Espresso) {
      ((Espresso) coffee1).drink();
    }

    // 추상 클래스는 객체를 생성할 수 없다. 미완성 상태이기 때문에.
    // Coffee coffee3 = new Coffee("과테말라");

    // 미완성 된 추상 메서드를 만든다면, 객체를 생성할 수 있다.
    Coffee coffee3 = new Coffee("과테말라") {
      @Override
      public void taste() {
        // TODO Auto-generated method stub
        System.out.println("커피taste");
      }
    };
    coffee3.taste();
    coffee3.info();

  }
}
