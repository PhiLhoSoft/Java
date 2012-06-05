public class Constructors_ExtClass extends Constructors_BaseClass
{
  protected Constructors_ExtClass()
  {
//~     super();
    System.out.println("ExtClass: No-args constructor");
  }

  public Constructors_ExtClass(int n)
  {
    System.out.println("ExtClass: Constructor with " + n);
  }

  public Constructors_ExtClass(String m)
  {
    super(m);
    System.out.println("ExtClass: Constructor with " + m);
  }

  public static void main(String args[])
  {
    Constructors_ExtClass ec1 = new Constructors_ExtClass();
    Constructors_ExtClass ec2 = new Constructors_ExtClass(42);
    Constructors_ExtClass ec3 = new Constructors_ExtClass("main");
  }
}
