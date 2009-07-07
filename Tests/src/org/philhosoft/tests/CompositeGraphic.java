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
package org.philhosoft.tests;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CompositeGraphic {
	private JScrollPane getContent(BufferedImage[] images) {
		JPanel panel = new JPanel(new GridLayout(1,0));
//		panel.add(wrap(copyToType(images[0], BufferedImage.TYPE_BYTE_GRAY)));
//		panel.add(wrap(copyToType(images[1], BufferedImage.TYPE_BYTE_GRAY)));
		panel.add(wrap(images[0]));
		panel.add(wrap(images[1]));
		panel.add(wrap(getComposite(images, 0.2f)));
		return new JScrollPane(panel);
	}

	private BufferedImage copyToType(BufferedImage src, int type) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage copy = new BufferedImage(w, h, type);
		Graphics2D g2 = copy.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return copy;
	}

	private BufferedImage getComposite(BufferedImage[] images, float alpha) {
		int w = Math.max(images[0].getWidth(), images[1].getWidth());
		int h = Math.max(images[0].getHeight(), images[1].getHeight());
		int type = BufferedImage.TYPE_INT_ARGB;
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		int x = (w - images[0].getWidth())/2;
		int y = (h - images[0].getHeight())/2;
		g2.drawImage(images[0], x, y, null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, alpha);
		g2.setComposite(ac);
		x = (w - images[1].getWidth())/2;
		y = (h - images[1].getHeight())/2;
		g2.drawImage(images[1], x, y, null);
		g2.dispose();
		return dest;
	}

	private JLabel wrap(BufferedImage image) {
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon, JLabel.CENTER);
	}

	public static void main(String[] args) throws IOException {
		String[] ids = { "Color.png", "people.gif" };
		BufferedImage[] images = new BufferedImage[ids.length];
		for(int j = 0; j < images.length; j++)
			images[j] = ImageIO.read(new File("E:/Documents/images/" + ids[j]));
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new CompositeGraphic().getContent(images));
		f.setSize(400,400);
		f.setLocation(200,200);
		f.setVisible(true);
	}
}


