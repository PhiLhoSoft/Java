import java.awt.*;
import java.awt.font.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;


/**
 * Panel showing information.
 */
@SuppressWarnings("serial")
class InfoPanel extends JComponent
{
  private String m_information;
  private Font m_font;
  private Color m_colorForeground;
  private Color m_colorBackground;

  protected float heightFactor;
  protected int m_alignment = SwingConstants.CENTER;

  InfoPanel(String info, Font font, Color colorFore, Color colorBackground)
  {
    m_information = info;
    m_font = font;
    m_colorForeground = colorFore;

    if (colorFore == null)
    {
      m_colorForeground = Color.BLACK;
    }
    if (colorBackground == null)
    {
      m_colorBackground = Color.GRAY;
    }
    else
    {
      m_colorBackground = colorBackground;
    }
    setOpaque(true);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    final Graphics2D g2d = (Graphics2D) g.create();
    g2d.setFont(m_font);

    g2d.setColor(m_colorBackground);
    Dimension d = getSize();
    g2d.fillRect(0, 0, d.width, d.height);
    g2d.setColor(m_colorForeground);

    // Get the text bounds
    FontRenderContext fontContext = g2d.getFontRenderContext();
    TextLayout layout = new TextLayout(m_information, m_font, fontContext);
    layout.draw(g2d, 0, 0);
    Rectangle2D bounds = layout.getBounds();

    int posX = 10;
    int width = (int) bounds.getWidth();
    int height = (int) bounds.getHeight();
    if (m_alignment == SwingConstants.CENTER)
    {
      posX = (d.width - width) / 2;
    }
    else if (m_alignment == SwingConstants.RIGHT)
    {
      posX = d.width - width - posX;
    }
    // Center vertically
    FontMetrics fm = g2d.getFontMetrics();
    int posY = d.height / 2 + height / 2 - fm.getDescent();

    g2d.drawString(m_information, posX, posY);
    // Only drawString shows this problem!
    g2d.drawLine( posX, posY + 5, posX + width, posY + 5 );

    g2d.dispose();
  }
}
