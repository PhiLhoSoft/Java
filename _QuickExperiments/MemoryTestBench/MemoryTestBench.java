// From http://www.javaspecialists.eu/archive/Issue193.html

public class MemoryTestBench
{
  public long calculateMemoryUsage(ObjectFactory factory)
  {
    Object handle = factory.makeObject();
    long memory = usedMemory();
    handle = null;
    lotsOfGC();
    memory = usedMemory();
    handle = factory.makeObject();
    lotsOfGC();
    return usedMemory() - memory;
  }

  private long usedMemory()
  {
    return Runtime.getRuntime().totalMemory() -
        Runtime.getRuntime().freeMemory();
  }

  private void lotsOfGC()
  {
    for (int i = 0; i < 20; i++)
    {
      System.gc();
      try
      {
        Thread.sleep(100);
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void showMemoryUsage(ObjectFactory factory)
  {
    long mem = calculateMemoryUsage(factory);
    System.out.println(
        factory.getClass().getSimpleName() + " produced " +
        factory.makeObject().getClass().getSimpleName() +
        " which took " + mem + " bytes");
  }

  public static void main(String[] args)
  {
    if (args.length == 0)
    {
      System.out.println("Usage: MemoryTestBench ClassName");
      System.exit(0);
    }
    final MemoryTestBench mtb = new MemoryTestBench();
    ObjectFactory obj = null;
    try
    {
      obj = (ObjectFactory) Class.forName(args[0]).newInstance();
    }
    catch (ClassNotFoundException e)
    {
      System.out.println("Bad class name");
      System.exit(1);
    }
    catch (InstantiationException e)
    {
      System.out.println("Abstract, interface, or no public constructor");
      System.exit(2);
    }
    catch (IllegalAccessException e)
    {
      System.out.println("Class is no visible");
      System.exit(3);
    }
    mtb.showMemoryUsage(obj);
  }
}
