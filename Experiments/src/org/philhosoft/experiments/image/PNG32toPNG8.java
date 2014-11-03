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
package org.philhosoft.experiments.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.SampleModel;
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
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ColorQuantizerDescriptor;
import javax.media.jai.operator.ErrorDiffusionDescriptor;

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
	// Drop alpha channel
	public static BufferedImage imageToRGB(BufferedImage image)
	{
//		return changeImageType(image, image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		return changeImageType(image, image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
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
//			return op.getAsBufferedImage(null, PlanarImage.createColorModel(op.getSampleModel()));
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
					500,	// upper bound (algorithm-dependent parameter)(here maximum size of the three-dimensional histogram)(default: 32768)
					null,	// ROI (region of interest, allows a sampling region smaller than the image itself)
					2,		// xPeriod (X subsample rate)
					2,		// yPeriod (Y subsample rate)
					null	// hints
					);

//			return op.getAsBufferedImage(null, op.getColorModel());
			return op.getAsBufferedImage();
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
					200,	// upper bound (algorithm-dependent parameter)(here number of cycles)(default: 300)
					null,	// ROI (region of interest, allows a sampling region smaller than the image itself)
					4,		// xPeriod (X subsample rate)
					4,		// yPeriod (Y subsample rate)
					null	// hints
					);

			return op.getAsBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error in NeuQuant quantization: " + e.getMessage());
			return null;
		}
	}

	/** Another method. */
	// http://code.google.com/p/color-reduction-experiments/source/browse/trunk/it/geosolutions/mapproducers/MapProducersTest.java?r=2
	public static BufferedImage toQuantizedIndexedImageWithSomeAlgorithm(BufferedImage image)
	{
		try
		{
// Copy the LUT from an indexed image
//			  IndexColorModel icm = ((IndexColorModel) imageT.getColorModel());
//            final byte[] r = new byte[icm.getMapSize()];
//            icm.getReds(r);
//            final byte[] g = new byte[icm.getMapSize()];
//            icm.getGreens(g);
//            final byte[] b = new byte[icm.getMapSize()];
//            icm.getBlues(b);
			final int size = 256;
			byte[] r = new byte[size];
			byte[] g = new byte[size];
			byte[] b = new byte[size];
			// Gray levels
//			for (int i = 0; i < size; i++)
//			{
//				r[i] = g[i] = b[i] = (byte) i;
//			}
			// Random LUT! Surprisingly, works not so bad... :-)
//			Random rnd = new Random();
//			rnd.nextBytes(r);
//			rnd.nextBytes(g);
//			rnd.nextBytes(b);
//			IndexColorModel icm = new IndexColorModel(8, size, r, g, b);

			// Use oct-tree for computing a LUT
			PlanarImage opForLut = ColorQuantizerDescriptor.create(image, ColorQuantizerDescriptor.OCTTREE, 256, null, null, null, null, null);
			LookupTableJAI lut = (LookupTableJAI) opForLut.getProperty("LUT");
			IndexColorModel icm = new IndexColorModel(8, lut.getByteData()[0].length, lut.getByteData()[0], lut.getByteData()[1], lut.getByteData()[2]);

			ImageLayout layout = new ImageLayout();
//			layout.setColorModel(image.getColorModel());
			layout.setColorModel(icm);

			// error diffusion
			KernelJAI ditherMask = new KernelJAI(1, 1, new float[] { 1.0f });
			LookupTableJAI colorMap = new LookupTableJAI(new byte[][] { r, g, b });
			RenderedOp op = ErrorDiffusionDescriptor.create(image, colorMap, ditherMask,
					new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));

			return op.getAsBufferedImage(null, icm);
//			return op.getAsBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error in custom quantization: " + e.getMessage());
			return null;
		}
	}

	// http://stackoverflow.com/questions/15322637/how-to-control-the-pixel-size-of-the-color-model-of-an-errordiffusiondescriptor
	public static BufferedImage toBitonalImage(BufferedImage image)
	{
		try
		{
		    byte[] map = new byte[] { (byte) 0x00, (byte) 0xff };
			LookupTableJAI lut = new LookupTableJAI(new byte[][] { map, map, map });
		    ImageLayout layout = new ImageLayout();
		    ColorModel cm = new IndexColorModel(1, 2, map, map, map);
		    layout.setColorModel(cm);
		    SampleModel sm = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE,
		            image.getWidth(),
		            image.getHeight(),
		            1);
		    layout.setSampleModel(sm);
		    RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
		    PlanarImage op = ErrorDiffusionDescriptor.create(image, lut, KernelJAI.ERROR_FILTER_FLOYD_STEINBERG, hints);

			return op.getAsBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Error in bitonal quantization: " + e.getMessage());
			return null;
		}
	}

	public static ColorModel getColorModel()
	{
		return new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8 }, false,
				false, Transparency.OPAQUE, DataBuffer.TYPE_INT);

//		java.awt.color.ColorSpace foo;
//		return new ComponentColorModel(IHSColorSpace.getInstance(), new int[] { 8, 8, 8 }, false, false, Transparency.OPAQUE,
//				DataBuffer.TYPE_INT);
	}


	public static BufferedImage readImage(File sourceFile)
	{
		try
		{
			System.out.println("Reading file " + sourceFile.getAbsolutePath());
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
		test1();
		test2();

		System.out.println("Done");
	}

	private static void test1()
	{
//		File testFile = new File("H:/Temp/Test.jpg");
		File testFile = new File("C:/PersonnelPhilippeLhoste/Images/Zoe+Marine aout 2000.jpg");
		BufferedImage image = readImage(testFile);
		if (image == null)
			return;

//		System.out.println("Simple quantization (dithering)");
//		writePNGImage(toSimpleIndexedImage(image), new File(testFile.getParentFile(), rename(testFile, "simple")));
//		System.out.println("Oct-tree quantization");
//		writePNGImage(toQuantizedIndexedImageWithOctTree(image), new File(testFile.getParentFile(), rename(testFile, "oct-tree")));
//		System.out.println("Median-cut quantization");
//		writePNGImage(toQuantizedIndexedImageWithMedianCut(image), new File(testFile.getParentFile(), rename(testFile, "median-cut")));
//		System.out.println("NeuQuant quantization");
//		writePNGImage(toQuantizedIndexedImageWithNeuQuant(image), new File(testFile.getParentFile(), rename(testFile, "neu-quant")));
		System.out.println("Other quantization");
		writePNGImage(toQuantizedIndexedImageWithSomeAlgorithm(image), new File(testFile.getParentFile(), rename(testFile, "other")));
//		System.out.println("Bitonal quantization");
//		writePNGImage(toBitonalImage(image), new File(testFile.getParentFile(), rename(testFile, "bitonal")));
	}

	private static String rename(File file, String type)
	{
		return file.getName().replaceAll("^(.*)\\.\\w+$", "$1_" + type + ".png");
	}

	private static void test2()
	{
		final String path = "H:/Temp/Quantization/";

		File sourcePathFile = new File(path);
		File[] files = sourcePathFile.listFiles();
		for (File file : files)
		{
			BufferedImage image = readImage(file);
			if (image == null)
				continue;

			writePNGImage(toSimpleIndexedImage(image), new File(sourcePathFile, rename(file, "simple")));
			writePNGImage(toQuantizedIndexedImageWithOctTree(image), new File(sourcePathFile, rename(file, "oct-tree")));
			writePNGImage(toQuantizedIndexedImageWithMedianCut(image), new File(sourcePathFile, rename(file, "median-cut")));
			writePNGImage(toQuantizedIndexedImageWithNeuQuant(image), new File(sourcePathFile, rename(file, "neu-quant")));
//			writePNGImage(toQuantizedIndexedImageWithSomeAlgorithm(image), new File(sourcePathFile, rename(file, "other")));
//			writePNGImage(toBitonalImage(image), new File(sourcePathFile, rename(file, "bitonal")));
//			return;
		}
	}
}
