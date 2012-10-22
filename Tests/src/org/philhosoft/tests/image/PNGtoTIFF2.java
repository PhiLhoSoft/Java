/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/09/20 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriter;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

public class PNGtoTIFF2
{
	/**
	 * Converts grayscale image to bilevel image.
	 */
	public static synchronized RenderedOp
			convertGrayscaleToBlackWhiteImage(RenderedImage ri)
	{
		// Generate a histogram.
		Histogram histogram = (Histogram)JAI.create("histogram", ri).getProperty("histogram");

		// Get a threshold equal to the median.
		double[] threshold = histogram.getPTileThreshold(0.5);

		// if background and foreground could not be separated
		if (threshold[0] == 0.0 || threshold[0] == 1.0)
		{
			threshold[0] = 127.5;
		}

		return JAI.create("binarize", ri, new Double(threshold[0]));
	}
	/**
	 * Converts RGB image to grayscale.
	 *
	 * Typical weights for converting RGB to grayscale:
	 * gray = 0.3*red + 0.59*green + 0.11*blue
	 */
	public static synchronized RenderedOp
			convertRGBToGrayscaleImage(RenderedImage ri)
	{
		double[][] matrix = { { 0.3D, 0.59D, 0.11D, 0D } };
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(ri);
		pb.add(matrix);
		return JAI.create("BandCombine", pb, null);
	}

	public static void test() throws IOException
	{
        String fileName = "4848970_1";
        // String fileName = "color";
        String inFileType = ".PNG";
        String outFileType = ".TIFF";

        File fInputFile = new File(/*"I:/HPF/UU/" +*/ fileName + inFileType);
        BufferedImage bi = ImageIO.read(fInputFile);

        int[] xi = bi.getSampleModel().getSampleSize();

        for (int i : xi)
        {
            System.out.println("bitsize " + i);
        }

        // com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriter
        ImageWriterSpi tiffspi = new TIFFImageWriterSpi();
        TIFFImageWriter writer = (TIFFImageWriter) tiffspi.createWriterInstance();

        // TIFFImageWriteParam param = (TIFFImageWriteParam) writer.getDefaultWriteParam();
        TIFFImageWriteParam param = new TIFFImageWriteParam(Locale.US);
        String[] strings = param.getCompressionTypes();
        for (String string : strings)
        {
            System.out.println(string);
        }

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionType("LZW");

        File fOutputFile = new File(/*"I:\\HPF\\UU\\" +*/ fileName + outFileType);
        OutputStream fos = new BufferedOutputStream(new FileOutputStream(fOutputFile));
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);

        writer.setOutput(ios);
        writer.write(null, new IIOImage(bi, null, null), param);

        ios.flush();
        writer.dispose();
        ios.close();
	}
}
