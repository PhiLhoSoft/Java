/*
 * org.philhosoft.*: A collection of utility classes for Java.
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
package org.philhosoft.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;

/**
 * Static functions for image operations.
 */
public class ImageUtil
{
	/**
	 * ImageIO.write needs a RenderedImage while some functions (like Toolkit.createImage)
	 * produces a more generic Image.
	 * Use this to get a BufferedImage with RGB+alpha out of the image.
	 *
	 * @param image    the image to convert
	 * @param width    width of the image
	 * @param height   height of the image
	 * @return a BufferedImage of TYPE_INT_ARGB type
	 */
	public static BufferedImage ImageToBufferedImage(Image image, int width, int height)
	{
		return ImageToBufferedImage(image, width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * ImageIO.write needs a RenderedImage while some functions (like Toolkit.createImage)
	 * produces a more generic Image.
	 * Use this to get a BufferedImage with given type out of the image.
	 *
	 * @param image    the image to convert
	 * @param width    width of the image
	 * @param height   height of the image
	 * @return a BufferedImage of given type
	 */
	public static BufferedImage ImageToBufferedImage(Image image, int width, int height, int type)
	{
		BufferedImage dest = new BufferedImage(width, height, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return dest;
	}

	/**
	 * Converts a full color will alpha image to byte-indexed with one transparent color image.
	 *
	 * @param srcImage   the image to convert
	 * @return a BufferedImage of TYPE_BYTE_INDEXED type
	 */
	public static BufferedImage ConvertRGBAToIndexed(BufferedImage srcImage)
	{
		// Create a non-transparent palletized image
		Image flattenedImage = TransformTransparencyToMagenta(srcImage);
		BufferedImage flatImage = ImageToBufferedImage(flattenedImage,
				srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
		BufferedImage destImage = MakeColorTransparent(flatImage, 0, 0);
		return destImage;
	}

	private static Image ConvertTranslucencyToTransparency(BufferedImage image)
	{
		ImageFilter filter = new RGBImageFilter()
		{
			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				int pixelValue = 0;
				int opacity = (rgb & 0xFF000000) >>> 24;
				if (opacity < 128)
				{
					// Quite transparent: replace color with transparent magenta
					// (traditional color for binary transparency)
					pixelValue = 0x00FF00FF;
				}
				else
				{
					// Quite opaque: get pure color
					pixelValue = (rgb & 0xFFFFFF) | 0xFF000000;
				}
				return pixelValue;
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}

	/**
	 * Makes the given indexed image to have a color transparent by choosing a pixel.
	 *
	 * @param image   image to convert
	 * @param x       the horizontal coordinate of the pixel whose color must be transparent
	 * @param y       the vertical coordinate of the pixel whose color must be transparent
	 * @return the converted image
	 */
	public static BufferedImage MakeColorTransparent(BufferedImage image, int x, int y)
	{
		ColorModel cm = image.getColorModel();
		if (!(cm instanceof IndexColorModel))
			return image; //sorry...
		IndexColorModel originalICM = (IndexColorModel) cm;
		WritableRaster raster = image.getRaster();
		int pixel = raster.getSample(x, y, 0); //pixel is offset in ICM's palette
		int size = originalICM.getMapSize();
		byte[] reds = new byte[size];
		byte[] greens = new byte[size];
		byte[] blues = new byte[size];
		originalICM.getReds(reds);
		originalICM.getGreens(greens);
		originalICM.getBlues(blues);
		IndexColorModel newICM = new IndexColorModel(8, size, reds, greens, blues, pixel);
		return new BufferedImage(newICM, raster, image.isAlphaPremultiplied(), null);
	}
}
