// javac -cp %JAVAFX_HOME%/lib/shared/javafxc.jar RunJavaFXScript.java
// java -cp %JAVAFX_HOME%/lib/shared/javafxc.jar;%JAVAFX_HOME%/lib/desktop/javafxgui.jar;%JAVAFX_HOME%/lib/desktop/Scenario.jar;. RunJavaFXScript
// or
// java -Xbootclasspath/p:%JAVAFX_HOME%/lib/shared/javafxc.jar -Djava.ext.dirs=%JAVAFX_HOME%/lib/shared;%JAVAFX_HOME%/lib/desktop -cp %JAVAFX_HOME%/lib/shared/javafxc.jar;. RunJavaFXScript Test2.fx
// I often get deadlocks with AWT and main...
import java.io.*;

import com.sun.javafx.api.JavaFXScriptEngine;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

// [Creating JavaFX Classes from Java Classes then running?|http://forums.sun.com/thread.jspa?threadID=5368159]
// [Loading JavaFX code from string|http://forums.sun.com/thread.jspa?threadID=5393585]

class RunJavaFXScript
{
  public static void main(String[] args)
  {
    // Load the script engine
    System.out.println("Getting the engines");
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByExtension("fx");
    System.out.println("Getting the generic ScriptEngine (by extension): " + engine);
    JavaFXScriptEngine fxEngine = (JavaFXScriptEngine) manager.getEngineByName("javafx");
    System.out.println("Getting the JavaFXScriptEngine: " + fxEngine);

    // Create a script on the fly
    String script = "function Demo() { println('Hello World!'); return 'It works!'; }";
    // Try to run it
    try
    {
      System.out.println("Running a built-in script");
      Object internalScript = fxEngine.eval(script);
      Object r = fxEngine.invokeFunction("Demo");
      System.out.println("Returned: " + (String) r);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    // Run script in file
    if (args.length > 0)
    {
      RunScriptFile(args[0], engine);
    }
  }

  static void RunScriptFile(String fileName, ScriptEngine engine)
  {
    BufferedReader reader = ReadFile(fileName);
    if (reader == null)
    {
      System.err.println("Cannot run " + fileName);
      return;
    }

    // Try to run it
    try
    {
      System.out.println("Running the script read from a file: " + fileName);
      Object readScript = engine.eval(reader);
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

  static BufferedReader ReadFile(String fileName)
  {
    // Get the current directory
    File curDir = new File(".");
    // Get the script's file content
    BufferedReader reader = null;
    File fScript = new File(curDir.getAbsolutePath(), fileName);
    System.out.println("Reading the " + fScript.getAbsolutePath() + " script");
    try
    {
      reader = new BufferedReader(new FileReader(fScript));
    }
    catch (FileNotFoundException e)
    {
      System.err.println("File not found: " + fScript.getAbsolutePath());
    }
    return reader;
  }
}
