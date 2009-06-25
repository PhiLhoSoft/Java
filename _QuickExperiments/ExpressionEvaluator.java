import java.io.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

class ExpressionEvaluator
{
  public static void main(String[] args)
  {
    // Create an expression on the fly
    String script1 = "((5 + 3) * 2 / 11 - 6) * 10";
    String script2 = "c = 0xFF55AA; (c >> 8) & 0xFF";

    System.out.println("Getting the engines");
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine bejsEngine = manager.getEngineByExtension("js");
    System.out.println("Getting the generic ScriptEngine (by extension): " + bejsEngine);
    ScriptEngine bnjsEngine = manager.getEngineByName("JavaScript");
    System.out.println("Getting the JavaScriptEngine: " + bnjsEngine);

    // Try to run it
    try
    {
      System.out.println("Running the script with first engine (by extension)");
      Object r1 = bejsEngine.eval(script1);
      System.out.println("Got: " + (Double) r1);

      System.out.println("Running the script with second engine (by name)");
      Object r2 = bnjsEngine.eval(script1);
//~       Object r = fxEngine.invokeFunction("Demo");
      System.out.println("Got: " + (Double) r2);

      System.out.println("Running the second script (with variable) with second engine");
      Object r3 = bnjsEngine.eval(script2);
      System.out.println(String.format("Got: 0x%02X", ((Double) r3).intValue()));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
