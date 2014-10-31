package org.philhosoft.experiments.libraries.poi;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

// https://issues.apache.org/bugzilla/buglist.cgi?email1=PhiLho%40GMX.net&emailassigned_to1=1&emailreporter1=1&emailtype1=exact&list_id=118271
public class POIBugs
{
	public static void main(String[] args) throws Exception
	{
		Locale.setDefault(Locale.US);

		String testFile = "Unexpected";

		// Bug 1: move active sheet, remove the others
		// #57171
		showBug1(testFile, "POI bug 1 - Incorrect XLSX (bad active sheet)");

		// Bug 2: delete any sheet except the last one
		// #57163
		showBug2(testFile, "POI bug 2 - Cannot delete sheet in XLSX");

		// Bug 3: remove all sheets but one, clone it
		// #57165
		showBug3(testFile, "POI bug 3 - Cannot clone sheet in XLSX");

		System.out.println("Done.");
	}

	private static void showBug1(String fileName, String bugName) throws InvalidFormatException, IOException
	{
		fileName = copyTestFile(getFile(fileName, false), bugName);
		Workbook wb = readWorkbook(fileName, false);
		removeAllSheetsBut(5, wb); // 5 is the active / selected sheet
		saveWorkbook(wb, fileName);
	}

	private static void showBug2(String fileName, String bugName) throws InvalidFormatException, IOException
	{
		fileName = copyTestFile(getFile(fileName, false), bugName);
		Workbook wb = readWorkbook(fileName, false);
		wb.removeSheetAt(0);
		saveWorkbook(wb, fileName);
	}

	private static void showBug3(String fileName, String bugName) throws InvalidFormatException, IOException
	{
		fileName = copyTestFile(getFile(fileName, false), bugName);
		Workbook wb = readWorkbook(fileName, false);
		try
		{
			removeAllSheetsBut(3, wb);
			wb.cloneSheet(0); // Throws exception here
			wb.setSheetName(1, "New Sheet");
			saveWorkbook(wb, fileName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeAllSheetsBut(int sheetIndex, Workbook wb)
	{
		int sheetNb = wb.getNumberOfSheets();
		// Move this sheet at the first position
		wb.setSheetOrder(wb.getSheetName(sheetIndex), 0);
		// Must make this sheet active (otherwise, for XLSX, Excel might protest that active sheet no longer exists)
		// I think POI should automatically handle this case when deleting sheets...
//		wb.setActiveSheet(0);
		for (int sn = sheetNb - 1; sn > 0; sn--)
		{
			wb.removeSheetAt(sn);
		}
	}

	private static String copyTestFile(File testFile, String bugName)
	{
		Path destination = new File(testFile.getParentFile().getAbsolutePath(), bugName + " " + testFile.getName()).toPath();
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(testFile)))
		{
			Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Could not read or copy Excel test file;" + testFile.getName(), e);
		}
		return destination.toFile().getName();
	}

	private static Workbook readWorkbook(String fileName, boolean useLegacy) throws InvalidFormatException, IOException
	{
		try (InputStream in = new BufferedInputStream(new FileInputStream(getFile(fileName, useLegacy))))
		{
			return WorkbookFactory.create(in);
		}
	}

	private static File saveWorkbook(Workbook wb, String fileName) throws IOException
	{
		File file = getFile(fileName, wb instanceof HSSFWorkbook);
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file)))
		{
			wb.write(out);
		}
		catch (IOException e)
		{
			System.out.println("Failed to write Excel file " + file.getAbsolutePath());
			e.printStackTrace();
		}

		System.out.println("Written " + file.getAbsolutePath());

		return file;
	}

	private static File getFile(String fileName, boolean useLegacy)
	{
		if (!(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")))
		{
			fileName += ".xls" + (useLegacy ? "" : "x");
		}
		return new File(System.getProperty("user.home"), fileName);
	}
}
