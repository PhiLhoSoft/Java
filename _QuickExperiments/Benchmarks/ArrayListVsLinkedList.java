import java.util.*;

// http://code.google.com/p/caliper/
import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import com.google.caliper.Runner;

// Caliper confirms reality: LinkedList vs ArrayList <http://blog.publicobject.com/2010/07/caliper-confirms-reality-linkedlist-vs.html>
// Lists as queues
public final class ArrayListVsLinkedList extends SimpleBenchmark {

  @Param({"0", "10", "100", "1000"}) int size;

  private final ArrayList<String> arrayList = new ArrayList<String>();
  private final LinkedList<String> linkedList = new LinkedList<String>();
  private final Deque<String> arrayDeque = new ArrayDeque<String>();

  @Override protected void setUp() throws Exception {
    for (int i = 0; i < size; i++) {
      arrayList.add("A");
      linkedList.add("A");
      arrayDeque.add("A");
    }
  }

  public void timeArrayList(int reps) {
    for (int i = 0; i < reps; i++) {
      arrayList.add("A");
      arrayList.remove(0);
    }
  }

  public void timeLinkedList(int reps) {
    for (int i = 0; i < reps; i++) {
      linkedList.add("A");
      linkedList.remove(0);
    }
  }

  public void timeArrayDeque(int reps) {
    for (int i = 0; i < reps; i++) {
      arrayDeque.addLast("A");
      arrayDeque.removeFirst();
    }
  }
}