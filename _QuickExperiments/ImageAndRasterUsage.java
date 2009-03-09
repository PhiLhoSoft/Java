
//import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;

//-- Image to pixel array (array of color data in int format)

// java.awt.Image img
if (image instanceof BufferedImage)
{
	BufferedImage bi = (BufferedImage) image;
	width = bi.getWidth();
	height = bi.getHeight();
	pixels = new int[width * height];
	WritableRaster raster = bi.getRaster();
	raster.getDataElements(0, 0, width, height, pixels);
}

//-- Returns a BufferedImage from an array of pixels

// int[] pixels;
{
	int type = BufferedImage.TYPE_INT_ARGB; // Or BufferedImage.TYPE_INT_RGB;
	BufferedImage image = new BufferedImage(width, height, type);
	WritableRaster raster = image.getRaster();
	raster.setDataElements(0, 0, width, height, pixels);
	return image;	// As java.awt.Image
}

//-- Write pixels

{
	WritableRaster raster = ColorModel.getRGBdefault().createCompatibleWritableRaster(width, height);
	int[] pixelQuads = new int[width * height * 4];
	// Fill pixelQuads
	raster.setPixels(0, 0, width, height, pixelQuads);
}

//-- Erase image

{
	BufferedImage bi = (BufferedImage) image;
	WritableRaster raster = bi.getRaster();
	// Make a one-line buffer, as compromise between speed (full image)
	// and memory usage
	if (pixelLine == null) || pixelLine.length < width)
	{
		pixelLine = new int[width];
	}
	java.util.Arrays.fill(pixelLine, backgroundColor);
	for (int i = 0; i < height; i++)
	{
		raster.setDataElements(0, i, width, 1, pixelLine);
	}
}

//-- Get one pixel

static int onePixel[] = new int[1];

{
	BufferedImage bi = (BufferedImage) image;
	WritableRaster raster = bi.getRaster();
	raster.getDataElements(x, y, onePixel);
	return onePixel[0];
}

//-- Get an image area

{
	BufferedImage bi = (BufferedImage) image;
	WritableRaster raster = bi.getRaster();
	raster.getDataElements(x, y, w, h, pixels);
	return pixels;
}

//-- Set one pixel

static int onePixel[] = new int[1];

{
	BufferedImage bi = (BufferedImage) image;
	WritableRaster raster = bi.getRaster();
	onePixel[0] = argb;
	raster.setDataElements(x, y, onePixel);
}

//-- Set an image area

{
	BufferedImage bi = (BufferedImage) image;
	WritableRaster raster = bi.getRaster();
	raster.setDataElements(destX, destY, srcW, srcH, srcPixels);
}

//-- At the end, display the image

BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
Graphics2D g2 = (Graphics2D) image.getGraphics();
//...
g2.drawImage(image, x1, y1, x2, y2, u1, v1, u2, v2, null);

public void smooth()
{
	smooth = true;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BICUBIC);
}


public void noSmooth()
{
	smooth = false;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_OFF);
	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
}


