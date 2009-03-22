import java.io.*;

import com.sun.javafx.api.JavaFXScriptEngine;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

class RunJavaFXScript
{
  static String SCRIPT_NAME = "Test.fx";

  public static void main(String[] args)
  {
    // Get the current directory
    File curDir = new File(".");
    // Get the script's file content
    BufferedReader reader = null;
    File f = new File(curDir.getAbsolutePath(), SCRIPT_NAME);
    try
    {
      reader = new BufferedReader(new FileReader(f));
    }
    catch (FileNotFoundException e)
    {
      System.err.println("File not found: " + f.getAbsolutePath());
      return;
    }

    // Create a script on the fly
    String script = "function Demo() { println('Hello World!'); return 'It works!'; }";

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("fx");
    JavaFXScriptEngine fxEngine = (JavaFXScriptEngine) manager.getEngineByName("javafx");

    // Try to run it
    try
    {
      Object readScript = engine.eval(reader);

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
