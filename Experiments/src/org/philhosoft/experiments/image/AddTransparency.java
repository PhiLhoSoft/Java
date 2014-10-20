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
package org.philhosoft.experiments.image;


import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.philhosoft.util.ResourceUtil;


public class AddTransparency
{
	AddTransparency() throws IOException
	{
	}

	private Image transformGrayToTransparency(BufferedImage image)
	{
		ImageFilter filter = new RGBImageFilter()
		{
			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				return (rgb << 8) & 0xFF000000;
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private Image transformColorToTransparency(BufferedImage image, Color c1, Color c2)
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
			@Override
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

		File file = null;
		try
		{
			String imagePath = ResourceUtil.getClassPath(at);
			System.out.println(imagePath);

			file = new File("resources/" + imagePath, "map.png");
			BufferedImage image = ImageIO.read(file);
			Image transpImg1 = at.transformGrayToTransparency(image);
			BufferedImage resultImage1 = org.philhosoft.util.ImageUtil.imageToBufferedImage(
					transpImg1, image.getWidth(), image.getHeight());

			String outputPath = "output";
			file = new File(outputPath, "map_with_transparency1.png");
			ImageIO.write(resultImage1, "PNG", file);
			Image transpImg2 = at.transformColorToTransparency(image, new Color(0, 50, 77), new Color(200, 200, 255));
			BufferedImage resultImage2 = org.philhosoft.util.ImageUtil.imageToBufferedImage(
					transpImg2, image.getWidth(), image.getHeight());

			file = new File(outputPath, "map_with_transparency2.png");
			ImageIO.write(resultImage2, "PNG", file);

			// Save to Gif format
			BufferedImage resultImage3 = org.philhosoft.util.ImageUtil.convertRGBAToIndexed(resultImage2);
			file = new File(outputPath, "map_with_transparency2.gif");
			ImageIO.write(resultImage3, "GIF", file);
			file = new File(outputPath, "map_with_transparency_indexed.png");
			ImageIO.write(resultImage3, "PNG", file);

			file = new File("resources/" + imagePath, "globe-scene-fish-bowl.png");
			BufferedImage globe = ImageIO.read(file);
			BufferedImage resultImage4 = org.philhosoft.util.ImageUtil.convertRGBAToIndexed(globe);
			ImageIO.write(resultImage4, "GIF", new File(outputPath, "globe.gif"));
		}
		catch (Exception e)
		{
			System.out.println(e + ": " + file.getAbsolutePath());
			e.printStackTrace();
		}
	}
}
