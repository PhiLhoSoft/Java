/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2009/03/20 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.*;

import javax.imageio.ImageIO;

public class AddTransparency
{
	AddTransparency() throws IOException
	{
		String imagePath = "E:/Documents/images/";
		File inFile = new File(imagePath, "map.png");
		BufferedImage image = ImageIO.read(inFile);

		Image transpImg1 = TransformGrayToTransparency(image);
		BufferedImage resultImage1 = org.philhosoft.util.ImageUtil.ImageToBufferedImage(
				transpImg1, image.getWidth(), image.getHeight());

		File outFile1 = new File(imagePath, "map_with_transparency1.png");
		ImageIO.write(resultImage1, "PNG", outFile1);

		Image transpImg2 = TransformColorToTransparency(image, new Color(0, 50, 77), new Color(200, 200, 255));
		BufferedImage resultImage2 = org.philhosoft.util.ImageUtil.ImageToBufferedImage(
				transpImg2, image.getWidth(), image.getHeight());

		File outFile2 = new File(imagePath, "map_with_transparency2.png");
		ImageIO.write(resultImage2, "PNG", outFile2);

		// Save to Gif format
		BufferedImage resultImage3 = org.philhosoft.util.ImageUtil.ConvertRGBAToIndexed(resultImage2);

		File outFile3 = new File(imagePath, "map_with_transparency2.gif");
		ImageIO.write(resultImage3, "GIF", outFile3);

		File globeFile = new File("E:/Documents/Images/TransparentPNG/globe-scene-fish-bowl-pngcrush.png");
		BufferedImage globe = ImageIO.read(globeFile);
		BufferedImage resultImage4 = org.philhosoft.util.ImageUtil.ConvertRGBAToIndexed(globe);
		ImageIO.write(resultImage4, "GIF", new File(imagePath, "globe.gif"));
	}

	private Image TransformGrayToTransparency(BufferedImage image)
	{
		ImageFilter filter = new RGBImageFilter()
		{
			public final int filterRGB(int x, int y, int rgb)
			{
				return (rgb << 8) & 0xFF000000;
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private Image TransformColorToTransparency(BufferedImage image, Color c1, Color c2)
	{
		// Primitive test, just an example
		final int r1 = c1.getRed();
		final int g1 = c1.getGreen();
		final int b1 = c1.getBlue();
		final int r2 = c2.getRed();
		final int g2 = c2.getGreen();
		final int b2 = c2.getBlue();
		ImageFilter filter = new RGBImageFilter()
		{
			public final int filterRGB(int x, int y, int rgb)
			{
				int r = (rgb & 0xFF0000) >> 16;
				int g = (rgb & 0xFF00) >> 8;
				int b = rgb & 0xFF;
				if (r >= r1 && r <= r2 &&
						g >= g1 && g <= g2 &&
						b >= b1 && b <= b2)
				{
					// Set fully transparent but keep color
					return rgb & 0xFFFFFF;
				}
				return rgb;
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public static void main(String[] args) throws IOException
	{
		AddTransparency at = new AddTransparency();
	}
}
