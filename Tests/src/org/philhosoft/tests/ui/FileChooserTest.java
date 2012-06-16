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
package org.philhosoft.tests.ui;

import javax.swing.JFileChooser;

import java.io.File;
import javax.swing.*;

import org.philhosoft.ui.ImagePreview;
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
public class FileChooserTest
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
				new FileChooserTest();
				System.exit(0);
			}
		});
	}

	private FileChooserTest()
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
		case 7:
			openImageChooser();
			break;
		}
	}

	private void openOne()
	{
		JFileChooser fileChooser = new JFileChooser(".");

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
		JFileChooser fileChooser = new JFileChooser(".");
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
		JFileChooser fileChooser = new JFileChooser(".");
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
		JFileChooser fileChooser = new JFileChooser(".");
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

	private void openImageChooser()
	{
		JFileChooser fileChooser = new JFileChooser("/");
		fileChooser.setMultiSelectionEnabled(false);

		// Add an accessory to display a miniature of the chosen image
		ImagePreview accessory = new ImagePreview(fileChooser);
		fileChooser.setAccessory(accessory);
		fileChooser.addPropertyChangeListener(
				JFileChooser.SELECTED_FILE_CHANGED_PROPERTY,
				accessory);

		// Display only understood image formats
		SimpleFileFilter filter = new SimpleFileFilter(
				new String[] { "jpg", "jpe", "jpeg", "gif", "png", "tif", "tiff" },
				"Image Files");
		fileChooser.addChoosableFileFilter(filter);

		// Customize the file view (listing of files)
		fileChooser.setFileView(new ImageFileView());

		int status = fileChooser.showDialog(null, "Wow!");
		if (status == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Selected: " +
					selectedFile.getParent() +
					" --- " +
					selectedFile.getName());
		}
	}
}
