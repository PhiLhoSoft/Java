/* Simple Excel sheet creation with Apache POI.
 * Based on http://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss/examples/TimesheetDemo.java
 * and some other demos.
 */

package org.philhosoft.tests.libraries.poi;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 * Making a simple Excel sheet using Apache POI.
 *
 * @author Philippe Lhoste
 */
public class SiteUserStats
{
	private static final String TITLE_STYLE_NAME = "title";
	private static final String HEADER_STYLE_NAME = "header";
	private static final String PLAIN_CELL_STYLE_NAME = "plain cell";
	private static final String DATE_CELL_STYLE_NAME = "date cell";
	private static final String INTEGER_FORMULA_STYLE_NAME = "integer formula";
	private static final String FLOAT_FORMULA_STYLE_NAME = "float formula";

	private static final String[] HEADERS =
	{
		"First\nName", "Last\nName", "E-mail", "Birthday", "Account\nCreation Date", "Topics\nCreated", "Answers",
		// Computed columns
		"Age", "Seniority", "Number\nof messages"
	};

	@SuppressWarnings("deprecation")
	private static final Object[][] DATA =
	{
		{
			"Victor", "Frankenstein", "Victor@Monster.org", new Date(14, 9, 31, 12, 42, 00), new Date(101, 1, 31, 12, 42, 00), 101, 2001
		},
		{
			"Vlad", "Dracula", "TheOne@Vampire.net", new Date(01, 10, 6, 10, 22, 00), new Date(114, 2, 14, 00, 00, 00), 1, 11
		},
		{
			"Were", "Wolf", "FullMoonWooh@Canine.me", new Date(51, 2, 14, 14, 14, 00), new Date(107, 3, 17, 23, 52, 00), 6, 666
		},
		{
			"Judah", "Golem", "emet@Prague.cz", new Date(55, 5, 5, 55, 55, 55), new Date(113, 4, 31, 16, 32, 00), 5, 55
		},
	};

	private static final int BIRTHDAY_DATE_COLUMN = 3;
	private static final int ACCOUNT_CREATION_DATE_COLUMN = 4;
	private static final int TOPIC_NUMBER_COLUMN = 5;
	private static final int ANSWER_NUMBER_COLUMN = 6;
	private static final int AGE_COLUMN = HEADERS.length - 3;
	private static final int SENIORITY_COLUMN = HEADERS.length - 2;
	private static final int POST_NUMBER_COLUMN = HEADERS.length - 1;

	public static void main(String[] args) throws Exception
	{
		Workbook wb = new HSSFWorkbook();

		Map<String, CellStyle> styles = createStyles(wb);

		Sheet sheet = wb.createSheet("Users");
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);

		int rowNum = 0;

		// Title row
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeightInPoints(45);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("List of users of Example.com site");
		for (int i = 0; i < HEADERS.length; i++)
		{
			Cell c = titleRow.getCell(i, Row.CREATE_NULL_AS_BLANK);
			c.setCellStyle(styles.get(TITLE_STYLE_NAME));
		}
		CellRangeAddress titleRange = CellRangeAddress.valueOf("A1:" + columnNumberToCoordinate(HEADERS.length - 1) + "1");
		sheet.addMergedRegion(titleRange);

		// Header row
		Row headerRow = sheet.createRow(rowNum++);
		headerRow.setHeightInPoints(45);
		for (int i = 0; i < HEADERS.length; i++)
		{
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(HEADERS[i]);
			headerCell.setCellStyle(styles.get(HEADER_STYLE_NAME));
		}

		// Create rows and set the formulae
		for (int i = 0; i < DATA.length; i++)
		{
			Row row = sheet.createRow(rowNum++);
			for (int j = 0; j < HEADERS.length; j++)
			{
				Cell cell = row.createCell(j);
				if (j == AGE_COLUMN)
				{
					// Age of the user
					cell.setCellFormula("(TODAY() - " + columnNumberToCoordinate(BIRTHDAY_DATE_COLUMN) + rowNum + ") / 365.2625");
					cell.setCellStyle(styles.get(FLOAT_FORMULA_STYLE_NAME));
				}
				else if (j == SENIORITY_COLUMN)
				{
					// Number of days of presence
					cell.setCellFormula("TODAY() - " + columnNumberToCoordinate(ACCOUNT_CREATION_DATE_COLUMN) + rowNum);
					cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));
				}
				else if (j == POST_NUMBER_COLUMN)
				{
					// Number of posts / messages
					String ref = columnNumberToCoordinate(TOPIC_NUMBER_COLUMN) + rowNum + ":" +
							columnNumberToCoordinate(ANSWER_NUMBER_COLUMN) + rowNum;
					cell.setCellFormula("SUM(" + ref + ")");
					cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));
				}
				else
				{
					cell.setCellStyle(styles.get(PLAIN_CELL_STYLE_NAME));
				}
			}
		}

		// row with totals below
		Row sumRow = sheet.createRow(rowNum++);
		sumRow.setHeightInPoints(35);
		Cell cell;
//		cell = sumRow.createCell(0);
//		cell.setCellStyle(styles.get(HIDDEN_FORMULA_STYLE_NAME));
//		cell.setCellFormula("TODAY()");

		cell = sumRow.createCell(TOPIC_NUMBER_COLUMN - 1);
		cell.setCellValue("Total Message Number:");
		cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));

		cell = sumRow.createCell(TOPIC_NUMBER_COLUMN);
		String colRef = columnNumberToCoordinate(TOPIC_NUMBER_COLUMN);
		String ref = colRef + "2:" + colRef + (2 + DATA.length);
		cell.setCellFormula("SUM(" + ref + ")");
		cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));

		cell = sumRow.createCell(ANSWER_NUMBER_COLUMN);
		colRef = columnNumberToCoordinate(ANSWER_NUMBER_COLUMN);
		ref = colRef + "2:" + colRef + (2 + DATA.length);
		cell.setCellFormula("SUM(" + ref + ")");
		cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));

		// Fill in with formula color for continuity
		cell = sumRow.createCell(ANSWER_NUMBER_COLUMN + 1);
		cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));
		cell = sumRow.createCell(POST_NUMBER_COLUMN - 1);
		cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));

		cell = sumRow.createCell(POST_NUMBER_COLUMN);
		colRef = columnNumberToCoordinate(POST_NUMBER_COLUMN);
		ref = colRef + "2:" + colRef + (2 + DATA.length);
		cell.setCellFormula("SUM(" + ref + ")");
		cell.setCellStyle(styles.get(INTEGER_FORMULA_STYLE_NAME));

		// Set sample data
		for (int i = 0; i < DATA.length; i++)
		{
			Row row = sheet.getRow(2 + i);
			for (int j = 0; j < DATA[i].length; j++)
			{
				if (DATA[i][j] == null) continue;

				if (DATA[i][j] instanceof String)
				{
					row.getCell(j).setCellValue((String) DATA[i][j]);
				}
				else if (DATA[i][j] instanceof Integer)
				{
					row.getCell(j).setCellValue((Integer) DATA[i][j]);
				}
				else if (DATA[i][j] instanceof Date)
				{
					row.getCell(j).setCellValue((Date) DATA[i][j]);
					row.getCell(j).setCellStyle(styles.get(DATE_CELL_STYLE_NAME));
				}
				else
				{
					row.getCell(j).setCellErrorValue((byte) 1);
				}
			}
		}

		// Finally set column widths. The width is measured in units of 1/256th of a character width.
		for (int i = 0; i < BIRTHDAY_DATE_COLUMN; i++)
		{
			sheet.setColumnWidth(i, 30 * 256); // 30 characters wide
		}
		sheet.setColumnWidth(BIRTHDAY_DATE_COLUMN, 14 * 256);
		sheet.setColumnWidth(ACCOUNT_CREATION_DATE_COLUMN, 14 * 256);
		for (int i = ACCOUNT_CREATION_DATE_COLUMN + 1; i < HEADERS.length; i++)
		{
			sheet.setColumnWidth(i, 11 * 256);
		}

		// Write the output to a file
		File file = new File(System.getProperty("user.home"), "Example.com's users.xls");
		FileOutputStream out = new FileOutputStream(file);
		wb.write(out);
		out.close();
	}

	/**
	 * Create a library of cell styles
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb)
	{
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 18);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setFont(titleFont);
		borderCell(titleStyle, CellStyle.BORDER_THICK, IndexedColors.BLUE.getIndex());
		styles.put(TITLE_STYLE_NAME, titleStyle);

		Font headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);
		headerStyle.setWrapText(true);
		styles.put(HEADER_STYLE_NAME, headerStyle);

		CellStyle plainCellStyle = wb.createCellStyle();
		plainCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		borderCell(plainCellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
		styles.put(PLAIN_CELL_STYLE_NAME, plainCellStyle);

		CellStyle dateCellStyle = wb.createCellStyle();
		dateCellStyle.cloneStyleFrom(plainCellStyle);
		dateCellStyle.setDataFormat(df.getFormat("yyyy-mm-dd"));
		styles.put(DATE_CELL_STYLE_NAME, dateCellStyle);

		CellStyle integerFormulaStyle = wb.createCellStyle();
		integerFormulaStyle.setAlignment(CellStyle.ALIGN_CENTER);
		integerFormulaStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		integerFormulaStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		integerFormulaStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		borderCell(integerFormulaStyle, CellStyle.BORDER_DOTTED, IndexedColors.GREY_80_PERCENT.getIndex());
		integerFormulaStyle.setDataFormat(df.getFormat("0"));
		integerFormulaStyle.setWrapText(true);
		styles.put(INTEGER_FORMULA_STYLE_NAME, integerFormulaStyle);

		CellStyle floatFormulaStyle = wb.createCellStyle();
		floatFormulaStyle.cloneStyleFrom(integerFormulaStyle);
		floatFormulaStyle.setDataFormat(df.getFormat("0.00"));
		styles.put(FLOAT_FORMULA_STYLE_NAME, floatFormulaStyle);

		return styles;
	}

	private static CellStyle borderCell(CellStyle style, short borderWeight, short color)
	{
		style.setBorderRight(borderWeight);
		style.setRightBorderColor(color);
		style.setBorderLeft(borderWeight);
		style.setLeftBorderColor(color);
		style.setBorderTop(borderWeight);
		style.setTopBorderColor(color);
		style.setBorderBottom(borderWeight);
		style.setBottomBorderColor(color);

		return style;
	}
	private static char numberToLetter(int nb)
	{
		return (char)('A' + nb);
	}
	private static String columnNumberToCoordinate(int nb)
	{
		char[] letters;
		if (nb < 26)
		{
			letters = new char[1];
			letters[0] = numberToLetter(nb);
		}
		else
		{
			letters = new char[2];
			letters[0] = numberToLetter(nb % 26);
			letters[1] = numberToLetter(nb / 26);
		}
		return new String(letters);
	}
}
