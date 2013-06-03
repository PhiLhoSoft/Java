public class OuterClass
{
  private String name = "Instance name";
  private static String staticName = "Static name";

  public static void main(String[] args)
  {
    System.out.println("\n\n## Outer, inner and nested classes\n");

    // Creating an instance of the enclosing class, as usual
    OuterClass oc = new OuterClass();

    // External classes can use  these classes
    // Non-static nested class = inner class needs an instance of the enclosing class
    OuterClass.InnerClass ic = oc.new InnerClass(oc);
    // Static class can be instanciated alone
    OuterClass.StaticNestedClass nct = new OuterClass.StaticNestedClass(oc);

    // Static class can be instanciated from static context
    StaticNestedClass sic2 = new StaticNestedClass(oc);

    final int someValue = 121;
    abstract class Anony { abstract void display(); }
    Anony sz = new Anony()
    {
      void display()
      {
        System.out.println("- Anonymous class' display");
        System.out.println("Some value: " + someValue);
      }
    };
    sz.display();

    System.out.println("\n\n## Constructor Hierarchy\n");

    System.out.println("# A");
    A a = new A();
    System.out.println("# B");
    B b1 = new B();
    System.out.println("# B 1");
    B b2 = new B("one");
    System.out.println("# B 2");
    B b3 = new B("ichi", "ni");
    System.out.println("# B 3");
    B b4 = new B("un", "deux", "trois");
  }

  OuterClass()
  {
    System.out.println("\n# OuterClass constructor");
    // Inside the enclosing class, no need for full name
    InnerClass ic = new InnerClass(this);
    ic.display();
    StaticNestedClass sic = new StaticNestedClass(this);
    sic.display();

    // Enclosing class can access private members of its nested classes too
    System.out.println("Value in inner class: " + ic.value);
    System.out.println("Value in static inner class: " + sic.value);
  }

  class InnerClass
  {
    private int value = 42;

    InnerClass(OuterClass oc)
    {
      System.out.println("\n# InnerClass constructor");

      System.out.println("Enclosing class: " + oc + " / " + oc.getClass());
      System.out.println("This class: " + this + " / " + this.getClass());
      System.out.println("Parent of this class: " + this.getClass().getEnclosingClass());
      System.out.println("Other way to parent: " + OuterClass.this);
    }

    void display()
    {
      System.out.println("- InnerClass.display");
      System.out.println("Name: " + name);
      System.out.println("Static name: " + staticName);
    }
  }

  static class StaticNestedClass
  {
    private int value = 4242;

    StaticNestedClass(OuterClass oc)
    {
      System.out.println("\n# StaticNestedClass constructor");

      System.out.println("Enclosing class: " + oc + " / " + oc.getClass());
      System.out.println("This class: " + this + " / " + this.getClass());
      System.out.println("Parent of this class: " + this.getClass().getEnclosingClass());
      // OuterClass.this not available here
    }

    void display()
    {
      System.out.println("- StaticNestedClass.display");
//      System.out.println("Name: " + name); // Not available there
      System.out.println("Static name: " + staticName);
    }
  }
}

class A
{
  // Becomes default constructor for sub-classes
  A()
  {
    System.out.println("A empty constructor");
  }
  A(String p)
  {
    System.out.println("A constructor with " + p);
  }
}

class B extends A
{
  B()
  {
    System.out.println("B empty constructor");
  }
  B(String p)
  {
    System.out.println("B constructor with " + p);
  }
  B(String p1, String p2)
  {
    this(p1 + p2);
    System.out.println("B constructor with " + p1 + " and " + p2);
  }
  B(String p1, String p2, String p3)
  {
    super(p1 + p2 + p3);
    System.out.println("B constructor with " + p1 + " and " + p2 + " and " + p3);
  }
}
