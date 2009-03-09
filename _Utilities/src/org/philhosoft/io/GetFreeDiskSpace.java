package org.philhosoft.io;

import java.util.regex.*;
import java.io.*;


public class GetFreeDiskSpace
{
   public static long GetFreeSpaceWindows(String strPath) throws IOException
   {
      // Build and run the 'dir' command
      String[] straCmdAttribs = new String[4];
      straCmdAttribs[0] = "cmd.exe";   // WinNT/Win2k/WinXP specific, but so is the /-C option of dir
      straCmdAttribs[1] = "/C";
      // Dir with no thousand separator
      straCmdAttribs[2] = "dir/-C";
      straCmdAttribs[3] = strPath;
      Process proc = Runtime.getRuntime().exec(straCmdAttribs);

      // Read the output until we find the last non-empty line
      String prevLine = null;
      String line = null;
      // We will parse the last line of the dir command
      // In French system, we have "              17 Rép(s)     72054841344 octets libres"
      // In English one, probably something like "              17 Dir(s)     72054841344 bytes free"
      Pattern p = Pattern.compile("^\\s*\\d+\\D+(\\d+)");

      long bytes = -1;
      BufferedReader brIn = null;
      try
      {
         brIn = new BufferedReader(new InputStreamReader(proc.getInputStream()));
         do
         {
            line = brIn.readLine();
            if (line == null || line.length() == 0)
               continue;
            prevLine = line;
         } while (line != null);
         if (prevLine != null)
         {
            Matcher m = p.matcher(prevLine);
            if (m.find())
            {
               bytes = Long.parseLong(m.group(1));
            }
         }
      }
      finally
      {
         try
         {
            if (brIn != null)
               brIn.close();
         }
         catch (IOException e) {} // Quietly ignore that...
      }

      if (bytes == -1)
      {
         throw new IOException("Unable to get free disk space for path '" + strPath + "'");
      }
      return bytes;
   }

   // Testing...
	public static void main(String[] args)
	{
      long space = 0;
      try
      {
         space = GetFreeSpaceWindows("C:\\");
         System.out.println("Free disk space for C: " + space);
         space = GetFreeSpaceWindows("E:\\");
         System.out.println("Free disk space for E: " + space);
      }
      catch (IOException e)
      {
         System.err.println("Error getting free disk space: " + e.toString());
      }
	}
}
