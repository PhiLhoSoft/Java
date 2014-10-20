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
package org.philhosoft.experiments.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

// Based on SeeThroughComponent from http://java.sun.com/docs/books/tutorial/2d/images/drawimage.html
@SuppressWarnings("serial")
class FadeToGray extends Component implements Icon
{
	private BufferedImage m_srcImg;
	private BufferedImage m_grayImg;
	private int m_w, m_h;
	float[] m_scales = { 1f, 1f, 1f, 0.5f };
	float[] m_offsets = new float[4];
	RescaleOp m_rop;

	public FadeToGray(File imageSrc) throws IOException
	{
		BufferedImage img = null;
		img = ImageIO.read(imageSrc);
		m_w = img.getWidth(null);
		m_h = img.getHeight(null);
		m_srcImg = new BufferedImage(m_w, m_h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = m_srcImg.getGraphics();
		g.drawImage(img, 0, 0, null);
		cloneToGray();
	}

	public void setOpacity(float opacity)
	{
		m_scales[3] = opacity;
		m_rop = new RescaleOp(m_scales, m_offsets, null);
	}

	@Override
	public int getIconWidth() { return m_w; }
	@Override
	public int getIconHeight() { return m_h; }

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.white);
//		g2d.fillRect(x, y, m_w, m_h);
		g2d.drawImage(m_grayImg, x, y, null);
		g2d.drawImage(m_srcImg, m_rop, x, y);
	}

	private void cloneToGray()
	{
		m_grayImg = new BufferedImage(m_w, m_h, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2 = (Graphics2D) m_grayImg.getGraphics();
		g2.drawImage(m_srcImg, 0, 0, null);
	}
}
