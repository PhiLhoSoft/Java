public class ThreadExample
{
  public static void main(String[] argv)
  {
    System.out.println("Starting...");
    new MainClass();
    try { Thread.sleep(5000); } catch (Exception e) { e.printStackTrace(); }
    System.out.println("And ending...");
  }
}

class MainClass
{
  String information = "I am the king of the world!";

  public MainClass()
  {
    Thread th = new BackgroundTask(this);
    th.start();
  }
}

class BackgroundTask extends Thread
{
  private MainClass mainClass;

  public BackgroundTask(MainClass _mainClass)
  {
    System.out.println("Hello there");
    mainClass = _mainClass;
  }

  public void run()
  {
    System.out.println(mainClass.information);
  }
}
