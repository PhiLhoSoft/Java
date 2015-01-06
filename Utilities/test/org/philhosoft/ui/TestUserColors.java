package org.philhosoft.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class TestUserColors
{
	public static final String TEST_FILE = "NameList.txt";
	public static final String OUTPUT_PATH = "output";
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@BeforeClass
	public static void classSetUp() throws Exception
	{
		File file = new File(OUTPUT_PATH);
		file.mkdirs();
	}

	@Test
	public void testStatistics() throws IOException, URISyntaxException
	{
		URI fileUri = getClass().getResource(TEST_FILE).toURI();
		Path path = Paths.get(fileUri);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

		long hueTotal = 0;
		long maxHue = 0;
		long minHue = 360;
		long fgSaturationTotal = 0;
		long fgLightnessTotal = 0;
		long bgSaturationTotal = 0;
		long bgLightnessTotal = 0;

		for (String line : lines)
		{
			String[] names = line.split("\t");
			for (String name : names)
			{
				UserColors uc = new UserColors(name);
				int[] fg = uc.getForegroundColorComponents();
				int[] bg = uc.getBackgroundColorComponents();
				if (fg[0] > maxHue)
					maxHue = fg[0];
				if (bg[0] > maxHue)
					maxHue = bg[0];
				if (fg[0] < minHue)
					minHue = fg[0];
				if (bg[0] < minHue)
					minHue = bg[0];
				hueTotal += fg[0] + bg[0];
				fgSaturationTotal += fg[1];
				fgLightnessTotal += fg[2];
				bgSaturationTotal += bg[1];
				bgLightnessTotal += bg[2];
			}
		}
		int nameNb = lines.size() * 3;

		// Output to ensure we don't have aberrant values...
		System.out.println("Hue: " + hueTotal + ", average: " + (hueTotal / nameNb / 2) + ", min: " + minHue + ", max: " + maxHue);
		// Ensure good spreading
		assertThat(minHue).isLessThan(5); // Min 0
		assertThat(maxHue).isGreaterThan(355); // Max 360

		System.out.println("FG Saturation: " + fgSaturationTotal + ", average: " + (fgSaturationTotal / nameNb));
		System.out.println("FG Lightness: " + fgLightnessTotal + ", average: " + (fgLightnessTotal / nameNb));
		System.out.println("BG Saturation: " + bgSaturationTotal + ", average: " + (bgSaturationTotal / nameNb));
		System.out.println("BG Lightness: " + bgLightnessTotal + ", average: " + (bgLightnessTotal / nameNb));
		// Ensure good contrast
		assertThat(fgSaturationTotal).isGreaterThan(bgSaturationTotal);
		assertThat(fgLightnessTotal).isLessThan(bgLightnessTotal);
	}

	@Test
	public void testOutputTwoColors() throws IOException, URISyntaxException
	{
		URI fileUri = getClass().getResource(TEST_FILE).toURI();
		Path path = Paths.get(fileUri);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

		Path outputPath = Paths.get(OUTPUT_PATH, "UserList-TwoColors.html");
//		Path outputPath = temporaryFolder.newFile("UserList-TwoColors.html").toPath();
		try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8))
		{
			writer.write("<html>\n<head>\n<title>User List (two colors)</title>\n");
			writer.write("<link rel='stylesheet' href='http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css'>\n</head>\n");
			writer.write("<body>\n<table>\n");
			for (String line : lines)
			{
				String htmlLine = "<tr>\n";
				String[] names = line.split("\t");
				for (String name : names)
				{
					UserColors uc = new UserColors(name);
					String fg = uc.computeHslForegroundColor();
					String bg = uc.computeHslBackgroundColor();
					htmlLine += "<td><i class='fa fa-user fa-3x' style='background-color: " + bg + "; color: " + fg + ";'></i>&nbsp;"
							+ name + "</td>\n";
				}
				htmlLine += "</tr>\n";
				writer.write(htmlLine);
				writer.newLine();
			}
			writer.write("</table>\n</body>\n</html>\n");
		}
	}

	@Test
	public void testOutputOneColor() throws IOException, URISyntaxException
	{
		URI fileUri = getClass().getResource(TEST_FILE).toURI();
		Path path = Paths.get(fileUri);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

		Path outputPath = Paths.get(OUTPUT_PATH, "UserList-OneColor.html");
//		Path outputPath = temporaryFolder.newFile("UserList-OneColor.html").toPath();
		try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8))
		{
			writer.write("<html>\n<head>\n<title>User List (one color)</title>\n");
			writer.write("<link rel='stylesheet' href='http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css'>\n</head>\n");
			writer.write("<body>\n<table>\n");
			for (String line : lines)
			{
				String htmlLine = "<tr>\n";
				String[] names = line.split("\t");
				for (String name : names)
				{
					UserColors uc = new UserColors(name);
					String fg = uc.computeHslColor();
					htmlLine += "<td><i class='fa fa-user fa-3x' style='color: " + fg + "; border: 1px solid #CCC;'></i>&nbsp;"
							+ name + "</td>\n";
				}
				htmlLine += "</tr>\n";
				writer.write(htmlLine);
				writer.newLine();
			}
			writer.write("</table>\n</body>\n</html>\n");
		}
	}
}
