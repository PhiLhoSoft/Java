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
    System.out.println("ExtClass: Constructor with " + m + " calling super");
  }

  public Constructors_ExtClass(String m, boolean dummy)
  {
    System.out.println("ExtClass: Constructor with " + m);
  }

  public static void main(String args[])
  {
    System.out.println("# Building empty constructor");
    Constructors_ExtClass ec1 = new Constructors_ExtClass();
    System.out.println("# Building constructor with int");
    Constructors_ExtClass ec2 = new Constructors_ExtClass(42);
    System.out.println("# Building constructor with String 1, calling super() explicitly");
    Constructors_ExtClass ec3 = new Constructors_ExtClass("main");
    System.out.println("# Building constructor with String 2");
    Constructors_ExtClass ec4 = new Constructors_ExtClass("two", true);
  }
}
