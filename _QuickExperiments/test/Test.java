// http://stackoverflow.com/questions/13838472/list-of-classes-implementing-an-interface

import java.util.ArrayList;
import java.util.List;

enum NumberClass
{
  ONE("One"),
  TWO("Two"),
  THREE("Three");

  private final String className;

  NumberClass(String name)
  {
    className = name;
  }

  String getName()
  {
    return className;
  }
}

public class Test
{
  public static void main(String[] args)
  {
    List<NumberClass> numbers = new ArrayList<NumberClass>();

    numbers.add(NumberClass.ONE);
    numbers.add(NumberClass.THREE);
    numbers.add(NumberClass.TWO);
    numbers.add(NumberClass.ONE);
    numbers.add(NumberClass.THREE);
    numbers.add(NumberClass.ONE);
    numbers.add(NumberClass.TWO);

    SomeNumber[] nbs = new SomeNumber[numbers.size()];
    int i = 0;
    for (NumberClass nbC : numbers)
    {
      SomeNumber nb;
      try
      {
         nb = (SomeNumber) Class.forName(nbC.getName()).newInstance ();
         nbs[i++] = nb;
      }
      // Cleanly handle them!
      catch (InstantiationException e) { System.out.println(e); }
      catch (IllegalAccessException e) { System.out.println(e); }
      catch (ClassNotFoundException e) { System.out.println(e); }
    }
    for (SomeNumber sn : nbs)
    {
      System.out.println(sn.getClass().getName() + " " + sn.getValue());
    }
  }
}

/*
// The following must be in their own files, of course
public interface SomeNumber
{
  int getValue();
}

public class One implements SomeNumber
{
  public int getValue() { return 1; }
}
public class Two implements SomeNumber
{
  public int getValue() { return 2; }
}
public class Three implements SomeNumber
{
  public int getValue() { return 3; }
}
*/
