public class Test
{
  int x = 42;

  public Test()
  {
  }

  public static void main(String args[])
  {
    Test t1 = new Test();
    System.out.println(t1.hashCode());
    Test t2 = new Test();
    System.out.println(t2.hashCode());
  }
}
