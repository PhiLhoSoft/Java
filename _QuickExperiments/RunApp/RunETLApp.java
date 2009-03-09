package network.myproptima.synchronisation;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import mycom.awt.docview.BaseApp;


/**
 * Launcher of an ETLApp for MyPrOptima.
 */
public class RunETLApp extends Thread
{
   Process m_process;
   ETLAppDesc m_ead;
   String m_strInstallDir;
   String m_strConfigDir;

// set JAVA=$_(RELOC)\j2re\bin\java -ms64m -mx196m -Djava.ext.dirs=$_(RELOC)\lib;$_(RELOC)\jar\lib;$_(RELOC)\jar
   private static final String[] s_straJavaArgs =
   {
      "?i/j2re/bin/java",
      "-ms64m",
      "-mx64m",
      "-Djava.ext.dirs=?i/lib;?i/jar/lib;?i/jar"
   };

   public RunETLApp( ETLAppDesc ead, String strInstallDir, String strConfigDir )
   {
      super( ead.GetThreadName() );
      m_ead = ead;
      m_strInstallDir = strInstallDir;
      m_strConfigDir = strConfigDir;
   }

   public void run()
   {
      try
      {
         RunApp();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (InterruptedException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   protected int RunApp()
         throws IOException, InterruptedException
   {
      List<String> params = new ArrayList<String>();
      // Java exe and parameters
      params.addAll(ExpandStrings(s_straJavaArgs));
      // Common VM arguments
      params.addAll(Arrays.asList(ETLAppDesc.GetCommonVMArgs()));
      // Specific VM arguments
      params.addAll(ExpandStrings(m_ead.GetVMArgs()));
      // The program to run
      params.add(m_ead.GetClass());
      // Its arguments
      params.addAll(ExpandStrings(m_ead.GetProgramArgs()));
      // The common arguments
      params.addAll(ExpandStrings(ETLAppDesc.GetCommonProgramArgs()));

      ProcessBuilder processBuilder = new ProcessBuilder(params);
//~       Map<String, String> env = processBuilder.environment();
//~       // Manipulate system properties
//~       System.setProperty(sName, sVal);
//~       processBuilder.directory(new File(m_strConfigDir));

      m_process = processBuilder.start();
      return CaptureProcessOutput();
   }

   /**
    * Launches stream gobblers for stderr and stdout of the given process
    * in separate threads to avoid buffer overflowing.
    *
    * @param process
    * @return error code returned by the process when it ends
    * @throws IOException
    * @throws InterruptedException
    */
   protected int CaptureProcessOutput()
         throws IOException, InterruptedException
   {
      // Capture error messages
      StreamGobbler errorGobbler = new
            StreamGobbler( m_process.getErrorStream(), "E", m_ead.GetThreadName() );

      // Capture output
      StreamGobbler outputGobbler = new
            StreamGobbler( m_process.getInputStream(), "O", m_ead.GetThreadName() );

      // Start output gobbler threads
      errorGobbler.start();
      outputGobbler.start();

      // Get exit value
      return m_process.waitFor();
   }
   
   protected void Stop()
   {
      m_process.destroy();
   }

   protected ArrayList<String> ExpandStrings(String[] stra)
   {
      ArrayList<String> alResult = new ArrayList<String>();
      for (int i = 0; i < stra.length; i++)
      {
         // Super flexible, eh? Ad hoc for the current task, at least...
         alResult.add(stra[i]
               .replaceAll("\\?i", m_strInstallDir)
               .replaceAll("\\?c", m_strConfigDir)
         );
      }
      return alResult;
   }
}
