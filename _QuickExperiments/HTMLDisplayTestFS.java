import java.io.File;

import javax.swing.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * Testing Flying Saucer (xhtmlrenderer) with a local file and an URL.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2010/11/10
 */
public class HTMLDisplayTestFS
{
	private String m_strFileName;
	private String m_strURL;
	private static final boolean m_bUseURL = true;
  private static final boolean m_bCleanUp = true;

	public HTMLDisplayTestFS()
	{
		m_strFileName = "D:/Documents/JavaWebComponents.xhtml";
//      m_strURL = "http://en.wikipedia.org/wiki/Java_%28programming_language%29";
		m_strURL = "http://Phi.Lho.free.fr";
	}

	String GetFile()
	{
		return m_strFileName;
	}
	String GetURL()
	{
		return m_strURL;
	}

	// Kind of unit test
	public static void main(String[] args)
	{
		final HTMLDisplayTest hdt = new HTMLDisplayTest();

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				XHTMLPanel panel = new XHTMLPanel();
				try
				{
					if (m_bUseURL)
					{
            if (m_bCleanUp)
            {
              Connection conn = Jsoup.connect( hdt.GetURL() );
              Document doc = conn.get();
              String html = doc.outerHtml();
              panel.setDocumentFromString( html, hdt.GetURL(), null );
            }
            else
            {
              panel.setDocument(hdt.GetURL());
            }
					}
					else
					{
						panel.setDocument(new File(hdt.GetFile()));
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
          return;
				}
				JScrollPane scrollPane = new FSScrollPane(panel);
//            JScrollPane scrollPane = new JScrollPane(panel);

				JFrame f = new JFrame();
				f.setTitle("Testing HTML Display with Flying Saucer");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				f.setContentPane(scrollPane);
				f.pack();
				f.setSize(1200, 800);
				f.setVisible(true);
			}
		});
	}
}
