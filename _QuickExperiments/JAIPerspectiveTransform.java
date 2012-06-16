//package pers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PerspectiveTransform;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.WarpPerspective;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JAIPerspectiveTransform extends JFrame
{
  private BufferedImage m_image;

  private class DisplayPanel extends JPanel
  {
    @Override
    protected void paintComponent(Graphics g)
    {
      Graphics2D g2d = (Graphics2D) g;
      try
      {
        g2d.drawImage(m_image, 20, 20, null);
        g2d.drawImage(a(m_image), 20 + m_image.getWidth() + 10, 20, null);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (NoninvertibleTransformException e)
      {
        e.printStackTrace();
      }
      catch (CloneNotSupportedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static BufferedImage a(BufferedImage image)
      throws NoninvertibleTransformException, CloneNotSupportedException
  {
    int w = image.getWidth();
    int h = image.getHeight();

    // Creates a PerspectiveTransform that maps an arbitrary quadrilateral onto another arbitrary quadrilateral.
    PerspectiveTransform transform = PerspectiveTransform.getQuadToQuad(
        // x0, y0, x1, y1, x2, y2, x3, y3
        0, 0, w, 0, w, h, 0, h,
        // x0p, y0p, x1p, y1p, x2p, y2p, x3p, y3p
        0, 0, w, 50, w, h - 50, 0, h
    );

    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add(new WarpPerspective(transform.createInverse()));

    RenderedOp renderedOp = JAI.create("warp", pb);
    PlanarImage planarImage = renderedOp.createInstance();

    return planarImage.getAsBufferedImage();
  }

  private BufferedImage getImage(String path) throws IOException
  {
    BufferedImage src = ImageIO.read(JAIPerspectiveTransform.class.getResource("TestImage.jpg"));
    BufferedImage image = new BufferedImage(src.getWidth(), src.getHeight(),
        BufferedImage.TYPE_INT_ARGB_PRE);
    Graphics2D g2d = (Graphics2D) image.getGraphics();
    g2d.drawImage(src, 0, 0, null);

    return image;
  }

  public static void main(String[] args)
  {
    String path = "";
    if (args.length > 0)
    {
      path = args[0];
    }
    m_image = getImage(path);
    SwingUtilities.invokeLater(new Runnable()
    {
        public void run()
        {
          JAIPerspectiveTransform frame = new JAIPerspectiveTransform();
          frame.setTitle("JAI Perspective Transform");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.getContentPane().add(new DisplayPanel());
          frame.setSize(800, 600);
          frame.setVisible(true);
        }
    });
  }
}
