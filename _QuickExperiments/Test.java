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
}
