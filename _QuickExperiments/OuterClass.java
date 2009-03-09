public class OuterClass
{
  private String name = "Instance name";
  private static String staticName = "Static name";

  public static void main(String[] args)
  {
    // Creating an instance of the enclosing class, as usual
    OuterClass oc = new OuterClass();

    // External classes can use  these classes
    // Non-static class need an instance of the enclosing class
    OuterClass.InnerClass ic = oc.new InnerClass(oc);
    // Static class can be instanciated alone
    OuterClass.StaticInnerClass nct = new OuterClass.StaticInnerClass(oc);

    // Static class can be instanciated from static context
    StaticInnerClass sic2 = new StaticInnerClass(oc);
  }

  OuterClass()
  {
    // Inside the enclosing class, no need for full name
    InnerClass ic = new InnerClass(this);
    ic.Display();
    StaticInnerClass sic = new StaticInnerClass(this);
    sic.Display();

    // Enclosing class can access private members of its nested classes too
    System.out.println("Value in inner class: " + ic.value);
    System.out.println("Value in static inner class: " + sic.value);
  }

  class InnerClass
  {
    private int value = 42;

    InnerClass(OuterClass oc)
    {
      System.out.println("Enclosing class: " + oc + " / " + oc.getClass());
      System.out.println("This class: " + this + " / " + this.getClass());
      System.out.println("Parent of this class: " + this.getClass().getEnclosingClass());
      System.out.println("Other way to parent: " + OuterClass.this);
    }

    void Display()
    {
      System.out.println("Name: " + name);
      System.out.println("Static name: " + staticName);
    }
  }

  static class StaticInnerClass
  {
    private int value = 4242;

    StaticInnerClass(OuterClass oc)
    {
      System.out.println("Enclosing class: " + oc + " / " + oc.getClass());
      System.out.println("This class: " + this + " / " + this.getClass());
      System.out.println("Parent of this class: " + this.getClass().getEnclosingClass());
      // OuterClass.this not available here
    }

    void Display()
    {
      // Not available there
//      System.out.println("Name: " + name);
      System.out.println("Static name: " + staticName);
    }
  }
}
