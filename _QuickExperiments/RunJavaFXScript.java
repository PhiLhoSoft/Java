import java.io.*;

import com.sun.javafx.api.JavaFXScriptEngine;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

// [Creating JavaFX Classes from Java Classes then running?|http://forums.sun.com/thread.jspa?threadID=5368159]
// [Loading JavaFX code from string|http://forums.sun.com/thread.jspa?threadID=5393585]

class RunJavaFXScript
{
  static String SCRIPT_NAME = "Test.fx";

  public static void main(String[] args)
  {
    // Get the current directory
    File curDir = new File(".");
    // Get the script's file content
    BufferedReader reader = null;
    File fScript = new File(curDir.getAbsolutePath(), SCRIPT_NAME);
    System.out.println("Reading the " + fScript.getAbsolutePath() + " script");
    try
    {
      reader = new BufferedReader(new FileReader(fScript));
    }
    catch (FileNotFoundException e)
    {
      System.err.println("File not found: " + fScript.getAbsolutePath());
      return;
    }

    // Create a script on the fly
    String script = "function Demo() { println('Hello World!'); return 'It works!'; }";

    System.out.println("Getting the engines");
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("fx");
    System.out.println("Getting the generic ScriptEngine (by extension): " + engine);
    JavaFXScriptEngine fxEngine = (JavaFXScriptEngine) manager.getEngineByName("javafx");
    System.out.println("Getting the JavaFXScriptEngine: " + fxEngine);

    // Try to run it
    try
    {
      System.out.println("Running the script read from a file: " + reader);
      Object readScript = engine.eval(reader);

      System.out.println("Running a built-in script");
      Object internalScript = fxEngine.eval(script);
      Object r = fxEngine.invokeFunction("Demo");
      System.out.println("Returned: " + (String) r);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      try { reader.close(); } catch (IOException e) {}
    }
  }
}
