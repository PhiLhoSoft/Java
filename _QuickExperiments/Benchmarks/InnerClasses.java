import java.util.*;

// http://code.google.com/p/caliper/
import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import com.google.caliper.Runner;

public final class InnerClasses extends SimpleBenchmark
{
  public static void main(String[] args) throws Exception
  {
    Runner.main(InnerClasses.class, args);
  }

  public void timeInner(int reps)
  {
    TestInner baseClass = new TestInner();
    String[] s = new String[100];
    for (int i = 0; i < reps; i++)
    {
      TestInner.InnerClass inner = baseClass.new InnerClass("I" + i);
      s[i % s.length] = inner.prefix("I");
    }
  }

  public void timeNested(int reps)
  {
    String[] s = new String[100];
    for (int i = 0; i < reps; i++)
    {
      TestInner.NestedClass nested = new TestInner.NestedClass("N" + i);
      s[i % s.length] = nested.prefix("N");
    }
  }

  public void timeOther(int reps)
  {
    String[] s = new String[100];
    for (int i = 0; i < reps; i++)
    {
      OtherClass other = new OtherClass("O" + i);
      s[i % s.length] = other.prefix("O");
    }
  }
}
