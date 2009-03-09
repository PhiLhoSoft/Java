package network.myproptima.synchronisation;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/*
set CONFIG_DIR=D:/MyPrOptima/profiles/SGSN_2G_Nokia/config
set DAIE_JVM_EXTRA_OPTIONS=-Dsun.rmi.transport.tcp.handshakeTimeout=300000 -Dsun.rmi.dgc.client.gcInterval=900000 -Dsun.rmi.dgc.server.gcInterval=900000 -XX:+UseParallelGC -XX:+AggressiveHeap -DMomMessageClass=org.objectweb.joram.shared.messages.MessageSoftRef -XX:GCHeapFreeLimit=5
set JAVA=$_(RELOC)\j2re\bin\java -ms64m -mx196m -Djava.ext.dirs=$_(RELOC)\lib;$_(RELOC)\jar\lib;$_(RELOC)\jar
REM Extract
start %JAVA% -Detl-extr -server %DAIE_JVM_EXTRA_OPTIONS% -Djava.io.tmpdir=$_(RELOC)\temp -Djava.awt.headless=true com.mycom.etl.extr.ExtractApp -C %CONFIG_DIR% RMI.Server=localhost RMI.Port=1099
REM Transform
start %JAVA% -Detl-trnf -server %DAIE_JVM_EXTRA_OPTIONS% -Djava.awt.headless=true com.mycom.etl.trf.TransformApp -C %CONFIG_DIR% RMI.Server=localhost RMI.Port=1099
REM Load
start %JAVA% -Detl-load -server %DAIE_JVM_EXTRA_OPTIONS% -Djava.awt.headless=true com.mycom.etl.load.LoadApp -C %CONFIG_DIR% RMI.Server=localhost RMI.Port=1099
REM Write
start %JAVA% -Detl-writ -server %DAIE_JVM_EXTRA_OPTIONS% -Djava.awt.headless=true com.mycom.etl.write.WriteApp -C %CONFIG_DIR% RMI.Server=localhost RMI.Port=1099

exec nice -n 19 $javaBin/java -Detl-dimg -server -$XmsDIMgr -$XmxDIMgr $(COMMON_JAVA_OPTIONS) $diMgrJvmExtraOption -Djava.awt.headless=true $java64 com.mycom.etl.DIMgr -C /opt/mycom/config RMI.Server=$rmiServer RMI.Port=$rmiPort $*
*/

/**
 * Description of an ETLApp for MyPrOptima.
 */
public enum ETLAppDesc
{
   ETL_DIMGR("DIMgr",    "DIMG", "com.mycom.etl.DIMgr",
         new String[] { "-Detl-dimg" },
         new String[] { "-C", "?c" }
   ),
   ETL_EXTR("Extract",   "EXTR", "com.mycom.etl.extr.ExtractApp",
         new String[] { "-Detl-extr" },
         new String[] { "-C", "?c" }
   ),
   ETL_TRNF("Transform", "TRNF", "com.mycom.etl.trf.TransformApp",
         new String[] { "-Detl-trnf" },
         new String[] { "-C", "?c" }
   ),
   ETL_LOAD("Load",      "LOAD", "com.mycom.etl.load.LoadApp",
         new String[] { "-Detl-load" },
         new String[] { "-C", "?c" }
   ),
   ETL_WRIT("Write",     "WRIT", "com.mycom.etl.write.WriteApp",
         new String[] { "-Detl-writ" },
         new String[] { "-C", "?c" }
   );

   private final String m_strName;
   private final String m_strThreadName;
   private final String m_strClass;
   private final String[] m_straVMArgs;
   private final String[] m_straProgramArgs;

   private static final String[] s_straCommonVMArgs =
   {
      "-Dsun.rmi.transport.tcp.handshakeTimeout=300000",
      "-Dsun.rmi.dgc.client.gcInterval=900000",
      "-Dsun.rmi.dgc.server.gcInterval=900000",
      "-XX:+UseParallelGC",
      "-XX:+AggressiveHeap",
      "-DMomMessageClass=org.objectweb.joram.shared.messages.MessageSoftRef",
      "-XX:GCHeapFreeLimit=5",

//      "-server",
      "-Djava.awt.headless=true"
   };
   private static final String[] s_straCommonProgramArgs =
   {
      "RMI.Server=localhost",
      "RMI.Port=1099"
   };

   ETLAppDesc(String strName, String strThreadName, String strClass,
         String[] straVMArgs, String[] straProgramArgs)
   {
      m_strName = strName;
      m_strThreadName = strThreadName;
      m_strClass = strClass;
      m_straVMArgs = straVMArgs;
      m_straProgramArgs = straProgramArgs;
   }

   public String GetName()
   {
      return m_strName;
   }

   public String GetThreadName()
   {
      return m_strThreadName;
   }

   public String GetClass()
   {
      return m_strClass;
   }

   public String[] GetVMArgs()
   {
      return m_straVMArgs;
   }
   public String[] GetProgramArgs()
   {
      return m_straProgramArgs;
   }

   public static String[] GetCommonVMArgs()
   {
      return s_straCommonVMArgs;
   }
   public static String[] GetCommonProgramArgs()
   {
      return s_straCommonProgramArgs;
   }

   // Test function
   public static void main(String[] args)
   {
      for (ETLAppDesc ea : ETLAppDesc.values())
      {
         System.out.printf("ETL App Desc: %s (%s) = %d",
               ea, ea.name(), ea.ordinal());
      }
   }
}

