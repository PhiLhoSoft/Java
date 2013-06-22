/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2013/06/22 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2013 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ColorQuantizerDescriptor;

import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

public class PNG32toPNG8
{
	public static BufferedImage changeImageType(Image image, int width, int height, int type)
	{
		BufferedImage dest = new BufferedImage(width, height, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return dest;
	}
	public static BufferedImage imageToRGB(BufferedImage image)
	{
		return changeImageType(image, image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/** Simple conversion: results in a quite ugly patterned image. */
	public static BufferedImage toSimpleIndexedImage(BufferedImage image)
	{
		return changeImageType(image, image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
	}

	//  http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/operator/ColorQuantizerDescriptor.html

	/** Quantized conversion, using JAI, Oct-tree algorithm.! */
	public static BufferedImage toQuantizedIndexedImageWithOctTree(BufferedImage image)
	{
		try
		{
			RenderedOp op = ColorQuantizerDescriptor.create(imageToRGB(image), // Image
					ColorQuantizerDescriptor.OCTTREE, // Algorithm
					256,	// Max color num
					300,	// upper bound (algorithm-dependent parameter)(here maximum size of the oct-tree)(default: 65536)
					null,	// ROI (region of interest)
					2,		// xPeriod (X subsample rate)
					2,		// yPeriod (Y subsample rate)
					null	// hints
					);

			return op.getAsBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error in oct-tree quantization: " + e.getMessage());
			return null;
		}
	}

	/** Quantized conversion, using JAI, Median-cut algorithm. */
	public static BufferedImage toQuantizedIndexedImageWithMedianCut(BufferedImage image)
	{
		try
		{
			RenderedOp op = ColorQuantizerDescriptor.create(imageToRGB(image), // Image
					ColorQuantizerDescriptor.MEDIANCUT, // Algorithm
					256,	// Max color num
					null,	// upper bound (algorithm-dependent parameter)(here maximum size of the three-dimensional histogram)(default: 32768)
					null,	// ROI (region of interest, allows a sampling region smaller than the image itself)
					2,		// xPeriod (X subsample rate)
					2,		// yPeriod (Y subsample rate)
					null	// hints
					);

			return op.getAsBufferedImage(null, op.getColorModel());
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error in median-cut quantization: " + e.getMessage());
			return null;
		}
	}

	/** Quantized conversion, using JAI, NeuQuant algorithm. */
	public static BufferedImage toQuantizedIndexedImageWithNeuQuant(BufferedImage image)
	{
		try
		{
			RenderedOp op = ColorQuantizerDescriptor.create(imageToRGB(image), // Image
					ColorQuantizerDescriptor.NEUQUANT, // Algorithm
					256,	// Max color num
					500,	// upper bound (algorithm-dependent parameter)(here number of cycles)(default: 300)
					null,	// ROI (region of interest, allows a sampling region smaller than the image itself)
					2,		// xPeriod (X subsample rate)
					2,		// yPeriod (Y subsample rate)
					null	// hints
					);

			return op.getAsBufferedImage(null, op.getColorModel());
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error in NeuQuant quantization: " + e.getMessage());
			return null;
		}
	}


	public static BufferedImage readImage(File sourceFile)
	{
		try
		{
			return ImageIO.read(sourceFile);
		}
		catch (IOException e)
		{
			System.out.println("Error reading file " + sourceFile.getAbsolutePath() + "\n" + e.getMessage());
			return null;
		}
	}
	public static void writePNGImage(BufferedImage bi, File destFile)
	{
		if (bi == null)
			return;

		PNGImageWriter writer = null;
		ImageOutputStream ios = null;
		try
		{
			ImageWriterSpi spi = new PNGImageWriterSpi();
			writer = (PNGImageWriter) spi.createWriterInstance();

			ImageWriteParam param = writer.getDefaultWriteParam();

			OutputStream fos = new BufferedOutputStream(new FileOutputStream(destFile));
			ios = ImageIO.createImageOutputStream(fos);

			writer.setOutput(ios);
			writer.write(null, new IIOImage(bi, null, null), param);
			ios.flush();
		}
		catch (IOException e)
		{
			System.out.println("Error writing file " + destFile.getAbsolutePath() + "\n" + e.getMessage());
		}
		finally
		{
			try
			{
				if (writer != null)
					writer.dispose();
				if (ios != null)
					ios.close();
			}
			catch (IOException e)
			{
				System.out.println("Error closing streams\n" + e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws IOException
	{
		final String path = "H:/Temp/Quantization/";

		File sourcePathFile = new File(path);
		File[] files = sourcePathFile.listFiles();
		for (File file : files)
		{
			BufferedImage image = readImage(file);
			if (image == null)
				continue;

			writePNGImage(toSimpleIndexedImage(image), new File(sourcePathFile, "simple_" + file.getName()));
			writePNGImage(toQuantizedIndexedImageWithOctTree(image), new File(sourcePathFile, "oct-tree_" + file.getName()));
			writePNGImage(toQuantizedIndexedImageWithMedianCut(image), new File(sourcePathFile, "median-cut_" + file.getName()));
			writePNGImage(toQuantizedIndexedImageWithNeuQuant(image), new File(sourcePathFile, "neu-quant" + file.getName()));
			return;
		}
		System.out.println("Done");
	}
}
