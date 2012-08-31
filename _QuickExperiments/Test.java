public class Test
{
  public static void main(String[] args)
  {
    System.out.println("A");
    A a = new A();
    System.out.println("B");
    B b1 = new B();
    System.out.println("B1");
    B b2 = new B("one");
    System.out.println("B2");
    B b3 = new B("ichi", "ni");
    System.out.println("B3");
    B b4 = new B("un", "deux", "trois");
  }
}

class A
{
  // Becomes default constructor for sub-classes
  public A()
  {
    System.out.println("A empty constructor");
  }
  public A(String p)
  {
    System.out.println("A constructor with " + p);
  }
}

class B extends A
{
  public B()
  {
    System.out.println("B empty constructor");
  }
  public B(String p)
  {
    System.out.println("B constructor with " + p);
  }
  public B(String p1, String p2)
  {
    this(p1 + p2);
    System.out.println("B constructor with " + p1 + " and " + p2);
  }
  public B(String p1, String p2, String p3)
  {
    super(p1 + p2 + p3);
    System.out.println("B constructor with " + p1 + " and " + p2 + " and " + p3);
  }
}
