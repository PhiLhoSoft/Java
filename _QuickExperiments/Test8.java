import java.util.*;
import java.text.*;

// Java 8 test...
public class Test8
{
  public static void main(String args[])
  {
    Optional<String> optOK = Optional.ofNullable("Foo");
    Optional<String> optKO = Optional.ofNullable(null);
    show(optOK);
    show(optKO);
    System.out.println(isEmpty(optOK));
    System.out.println(isEmpty(optKO));
    System.out.println(isEmpty(Optional.of("")));
  }

  static void show(Optional<String> opt)
  {
    opt.ifPresent(s -> System.out.println(s));
  }

  static boolean isEmpty(Optional<String> opt)
  {
    return opt.map(s -> s.isEmpty()).orElse(true);
//~     return opt.isPresent() ? opt.get().isEmpty() : true;
  }
}
