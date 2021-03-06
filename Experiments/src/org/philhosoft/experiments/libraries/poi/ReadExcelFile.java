package org.philhosoft.experiments.libraries.poi;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.philhosoft.util.ResourceUtil;


/**
 *
 */
public class ReadExcelFile
{
	private Workbook workbook;
	private FormulaEvaluator evaluator;
	private DataFormatter formatter;

	private void openWorkbook(File file) throws FileNotFoundException, IOException, InvalidFormatException
	{
		System.out.println("Opening workbook [" + file.getName() + "]");
		try (FileInputStream fis = new FileInputStream(file))
		{
			// Open the workbook and then create the FormulaEvaluator and
			// DataFormatter instances that will be needed to, respectively,
			// force evaluation of formulae found in cells and create a
			// formatted String encapsulating the cells contents.
			workbook = WorkbookFactory.create(fis);
			evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			formatter = new DataFormatter(true);
		}
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " sheets");
	}

	public String readCellValue(Cell cell)
	{
		switch (cell.getCellType())
		{
		case Cell.CELL_TYPE_BLANK:
			return "(blank)";
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_ERROR:
			return String.valueOf(cell.getErrorCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return readFormattedCellValue(cell);
		case Cell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return "Unknown type!";
		}
	}

	public String readFormattedCellValue(Cell cell)
	{
		try
		{
			return formatter.formatCellValue(cell, evaluator);
		}
		catch (RuntimeException e)
		{
			return e.getMessage(); // Error from evaluator, for example "Don't know how to evaluate name 'xxx'" if we have =xxx() in cell
		}
	}

	public IndexedColors indexToColor(short index)
	{
		for (IndexedColors c : IndexedColors.values())
		{
			if (index == c.getIndex())
				return c;
		}
		return null;
	}

	public void printCell(Cell cell)
	{
		CellStyle cellStyle = cell.getCellStyle();
		short fontIndex = cellStyle.getFontIndex();
		Font font = workbook.getFontAt(fontIndex);
		System.out.println(
				"## Cell coordinates    : " + cell.getRowIndex() + ":" + cell.getColumnIndex() +
				"\n Cell type             : " + cell.getCellType() +
				"\n Cell value (raw)      : " + readCellValue(cell) +
				"\n Cell value (formatted): " + readFormattedCellValue(cell) +
				"\n Format                : " + cellStyle.getDataFormatString() +
				"\n BG / FG color         : " + indexToColor(cellStyle.getFillForegroundColor()) + " / " + indexToColor(font.getColor()) +
				"\n Font                  : " + font.getFontName()
				);
	}

	public void readCells()
	{
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(0);

		int lastCellNum = row.getLastCellNum();
		for (int i = 0; i < lastCellNum; i++)
		{
			Cell cell = row.getCell(i);
			printCell(cell);
		}
	}

	public void readCell(int rowNb, int colNb)
	{
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(rowNb);
		Cell cell = row.getCell(colNb);
		printCell(cell);
	}

	public static void main(String[] args) throws FileNotFoundException, InvalidFormatException, IOException
	{
		ReadExcelFile ref = new ReadExcelFile();
		// File is in resources, in same package than this class.
		// The following two lines get the path of the file in the bin folder.
		// If you don't want to depend on the Util project, just replace excelPath with an absolute path on your system.
		String classPath = ResourceUtil.getClassPath(ref);
		String excelPath = ResourceUtil.getBinaryPath() + classPath;
		ref.openWorkbook(new File(excelPath, "TestExcel.xls"));
		ref.readCells();
		// To verify the file generated by SiteUserStats.
//		ref.openWorkbook(new File(System.getProperty("user.home"), "Example.com's users.xls"));
//		ref.readCell(2, 7);
	}
}
