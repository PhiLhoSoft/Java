/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2005/12/13 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.experiments.ui;


import javax.swing.JFileChooser;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.event.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Window;
import java.beans.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import org.philhosoft.ui.ImagePreview;
import org.philhosoft.ui.SimpleFileFilter;


/**
 * Test of various capabilities of JFileChooser.
 *
 * Based on http://java.sun.com/developer/JDCTechTips/2004/tt0316.html
 *
 * @author John Zukowski & Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/13
 */
public class FileChooserTest2
{
	private static final int CHOSEN_MODE = 6;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new FileChooserTest2();
				System.exit(0);
			}
		});
	}

	private FileChooserTest2()
	{
		switch (CHOSEN_MODE)
		{
		case 1:
		default:
			openOne();
			break;
		case 2:
			openMulti();
			break;
		case 3:
			openMultiMode(JFileChooser.FILES_ONLY);
			break;
		case 4:
			openMultiMode(JFileChooser.DIRECTORIES_ONLY);
			break;
		case 5:
			openMultiMode(JFileChooser.FILES_AND_DIRECTORIES);
			break;
		case 6:
			openMultiFilter();
			break;
		}
	}

	private void openOne()
	{
		JFileChooser fileChooser = new ExtendedJFileChooser(".");

		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Selected: " +
					selectedFile.getParent() +
					" --- " +
					selectedFile.getName());
		}
	}

	private void openMulti()
	{
		JFileChooser fileChooser = new ExtendedJFileChooser(".");
		fileChooser.setMultiSelectionEnabled(true);

		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION)
		{
			File aSelectedFiles[] = fileChooser.getSelectedFiles();
			for (int i = 0, n = aSelectedFiles.length; i < n; i++)
			{
				System.out.println("Selected: " +
						aSelectedFiles[i].getParent() +
						" --- " +
						aSelectedFiles[i].getName());
			}
		}
	}

	private void openMultiMode(int mode)
	{
		JFileChooser fileChooser = new ExtendedJFileChooser(".");
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileSelectionMode(mode);

		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION)
		{
			File aSelectedFiles[] = fileChooser.getSelectedFiles();
			for (int i = 0, n = aSelectedFiles.length; i < n; i++)
			{
				System.out.println("Selected: " +
						aSelectedFiles[i].getParent() +
						" --- " +
						aSelectedFiles[i].getName());
			}
		}
	}

	private void openMultiFilter()
	{
		JFileChooser fileChooser = new ExtendedJFileChooser();
		fileChooser.setMultiSelectionEnabled(true);

		SimpleFileFilter filter = new SimpleFileFilter("java", "Java Source Files");
		fileChooser.addChoosableFileFilter(filter);
		filter = new SimpleFileFilter("class", "Java Class Files");
		fileChooser.addChoosableFileFilter(filter);
		filter = new SimpleFileFilter(
				new String[] { "java", "class" },
				"All Java Files");
		fileChooser.addChoosableFileFilter(filter);

		int status = fileChooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION)
		{
			File aSelectedFiles[] = fileChooser.getSelectedFiles();
			for (int i = 0, n = aSelectedFiles.length; i < n; i++)
			{
				System.out.println("Selected: " +
						aSelectedFiles[i].getParent() +
						" --- " +
						aSelectedFiles[i].getName());
			}
		}
	}
}

@SuppressWarnings("serial")
class ExtendedJFileChooser extends JFileChooser
{
   public ExtendedJFileChooser()
   {
      super( CreateFileSystemView( ) );
System.out.println(">> ExtendedJFileChooser");
      Init();
   }

   public ExtendedJFileChooser(FileSystemView fsv)
   {
      super(fsv);
System.out.println(">> ExtendedJFileChooser/fsv");
      Init();
   }
   public ExtendedJFileChooser(String sCurrentDirectoryPath)
   {
      super( sCurrentDirectoryPath );
System.out.println(">> ExtendedJFileChooser/s: " + sCurrentDirectoryPath);
   }

   /** Adding special listeners in order to set a wait cursor when changing directory.
    */
   private void Init( )
   {
      FileChooserUI fcUI = super.getUI();
      if (fcUI instanceof BasicFileChooserUI)
      {
         addPropertyChangeListener(new PropertyChangeListener()
         {
            @Override
			public void propertyChange(PropertyChangeEvent e)
            {
               String s = e.getPropertyName();
System.out.println("> ExtendedJFileChooser > propertyChange: " + s);
               if (s.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY))
               {
                  SetCursor( ExtendedJFileChooser.this, Cursor.WAIT_CURSOR );
               }
            }
         });
         ListModel model = ((BasicFileChooserUI)fcUI).getModel();
         model.addListDataListener(new ListDataListener()
         {
            private void ResetDefaultCursor( )
            {
               SetCursor( ExtendedJFileChooser.this, Cursor.DEFAULT_CURSOR );
            }
            // After reading a directory the model calls this method
            // but if the directory is empty this method is not called.
            // In this case, the method intervalRemoved will be called instead
            @Override
			public void contentsChanged(ListDataEvent e)
            {
               ResetDefaultCursor( );
            }
            @Override
			public void intervalAdded(ListDataEvent e)
            {
               ResetDefaultCursor( );
            }
            @Override
			public void intervalRemoved(ListDataEvent e)
            {
               ResetDefaultCursor( );
            }
         });
      }
   }

   /** Changing cursor for a component.
    * Cursor will be changed at application level (not only for the component).
    */
   private void SetCursor(JComponent aCmp, int nType)
   {
      Window aWindow = SwingUtilities.getWindowAncestor( aCmp );
      if ( aWindow != null )
      {
         Cursor cursor = Cursor.getPredefinedCursor( nType );
         aWindow.setCursor(cursor);
      }
   }

   // Need for fixing PR #702/900/1306
   // find the combo box manually
	private JComboBox GetCombo(Component aCmp, int nRecurs)
	{
		if (aCmp instanceof JComboBox) return (JComboBox) aCmp;
		else if (aCmp instanceof Container)
		{
			Container aCnt = (Container) aCmp;
			Component[] arChild = aCnt.getComponents();
			for (int i = 0; i < arChild.length; i++)
			{
				JComboBox aCmb = GetCombo(arChild[i], nRecurs + 1);
				if (aCmb != null)
				{
					return aCmb;
				}
			}
		}
		return null;
	}

   /**
    *   Bug workaround for Java Web Start 1.0
    *   Under Windows, use the special WindowsAltFileSystemView
    *   which does not display the box "no disk"
    */
	static private FileSystemView CreateFileSystemView()
	{
		FileSystemView fsv = null;
		if (IsWindows() && IsJavaVersionBelow14()) fsv = new WindowsAltFileSystemView();
		else
		{
			// If not Windows or recent Java, returns the default
			fsv = FileSystemView.getFileSystemView();
		}
		return fsv;
	}

   static private boolean IsWindows( )
   {
      String os = System.getProperty("os.name");
      return os != null && os.startsWith("Windows");
   }

   private static boolean IsJavaVersionBelow14()
   {
      String sJavaVersion = System.getProperty("java.version");
      return sJavaVersion.startsWith("1.2") || sJavaVersion.startsWith("1.3");
   }

   // Need for fixing PR #702/900/1306/1490
   // find the combo box manually and select the correct item!
   @Override
   public void setCurrentDirectory( File aPathFile )
   {
      super.setCurrentDirectory( aPathFile );
System.out.println("> ExtendedJFileChooser > setCurrentDirectory: " + aPathFile);

      if( aPathFile == null || !aPathFile.exists() )
      {
         // PR 3915: get the user's current directory as the user's home dir property is modified
         // by Proptima.jnlp and corresponds to the server home dir.
         // It's used by Proptima to get the user's property files on the server.
         aPathFile = new File( System.getProperty("user.dir") );
         if( !aPathFile.exists() )
         {
            // if none of these directories exist, we get the root dir
            aPathFile = getFileSystemView().getRoots()[0];
         }
      }

      File aFile = null;
      // retrieve the whole path file for further comparison
      if( aPathFile != null )
         aFile = aPathFile.getAbsoluteFile();

System.out.println("> ExtendedJFileChooser > GetCombo " + aFile);
      JComboBox aCmb = GetCombo( this, 0 );
      int nIdx = -1;
      boolean bFound = false;
      if ( aCmb != null && aFile != null )
      {
         File[] arRoots = getFileSystemView().getRoots();
         // Remove all elements from the combo by replacing the combobox model
         // In the new comboboxmodel, put the root dirs... and the new file!
         DefaultComboBoxModel cbm = new DefaultComboBoxModel();
         for (int i=0;i<arRoots.length;i++)
         {
            cbm.addElement(arRoots[i]);
         }
         for (int i = 0; i < arRoots.length; i++)
         {
            // caution: PR1490, pb with the file comparison
            // we need to be sure that we compare the files with their paths
            // if not the directory might not be found and therefore it will be displayed
            // twice in the combo box
            if( aFile.getAbsolutePath( ).equalsIgnoreCase( arRoots[i].getAbsolutePath( ) ) == true )
            {
               nIdx = i;
               bFound = true;
               break;
            }
         }
         if( aCmb.getItemCount() > 0 && bFound == true )
         {
            aCmb.setModel(cbm);
            aCmb.setSelectedIndex(nIdx);
         }
         else
         {
            // In the new comboboxmodel, put the new file!
            cbm.addElement(aFile);
            aCmb.setModel(cbm);
            aCmb.setSelectedItem(aFile);
         }
      }
   }
}

class WindowsAltFileSystemView extends FileSystemView
{
   /**
    * Returns true if the given file is a root.
    */
   @Override
   public boolean isRoot(File f)
   {
      if( !f.isAbsolute() )
      {
         return false;
      }

      String parentPath = f.getParent();
      if( parentPath == null )
      {
         return true;
      }
      else
      {
         File parent = new File(parentPath);
         return parent.equals(f);
      }
   }

   /**
    * creates a new folder with a default folder name.
    */
   @Override
public File createNewFolder(File containingDir) throws IOException
   {
System.out.println("> MycomJFileChooser > createNewFolder: " + containingDir);
      if( containingDir == null )
      {
         throw new IOException("Containing directory is null:");
      }
      File newFolder = null;
      // Using NT's default folder name (English locale...)
      newFolder = createFileObject(containingDir, "New Folder");
      int i = 2;
      while (newFolder.exists() && (i < 100))
      {
         newFolder = createFileObject(containingDir, "New Folder (" + i + ")");
         i++;
      }

      if( newFolder.exists() )
      {
         throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
      }
      else
      {
         newFolder.mkdirs();
      }
      return newFolder;
   }

   /**
    * Returns whether a file is hidden or not. On Windows
    * there is currently no way to get this information from
    * io.File, therefore always return false.
    */
   @Override
   public boolean isHiddenFile(File f)
   {
      return false;
   }

   /**
    * Returns all root partitions on this system. On Windows, this
    * will be the A: through Z: drives.
    */
   @Override
   public File[] getRoots()
   {
      List<File> rootList = new ArrayList<File>();

      // Create the A: drive whether it is mounted or not
      FileSystemRoot floppy = new FileSystemRoot("A" + ":" + "\\");
      rootList.add(floppy);

      // Run through all possible mount points and check
      // for their existence.
      for (char c = 'C'; c <= 'Z'; c++)
      {
         char device[] = { c, ':', '\\' };
         String deviceName = new String(device);
         File deviceFile = new FileSystemRoot(deviceName);
         if (deviceFile.exists())
         {
            rootList.add(deviceFile);
         }
      }
      File[] roots = new File[rootList.size()];
      rootList.toArray(roots);
      return roots;
   }

   @SuppressWarnings("serial")
   class FileSystemRoot extends File
   {
      public FileSystemRoot(File f)
      {
         super(f, "");
      }

      public FileSystemRoot(String s)
      {
         super(s);
      }

      @Override
      public boolean isDirectory()
      {
         return true;
      }
   }
}
