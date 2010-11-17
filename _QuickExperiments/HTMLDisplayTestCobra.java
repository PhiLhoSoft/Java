import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.logging.*;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.html2.HTMLElement;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.lobobrowser.html.*;
import org.lobobrowser.html.gui.*;
import org.lobobrowser.html.parser.*;
import org.lobobrowser.html.test.*;


/**
 * Testing Cobra (from the Lobo project) with a local file and an URL.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2010/11/10
 */
public class HTMLDisplayTestCobra
{
	private String m_strFileName;
	private String m_strURL;
	private static final boolean m_bUseURL = false;

	public HTMLDisplayTestCobra()
	{
		m_strFileName = "D:/Documents/JavaWebComponents.xhtml";
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
		final HTMLDisplayTestCobra hdt = new HTMLDisplayTestCobra();

		// Initialize logging so only Cobra warnings are seen.
		Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);

		// Open a connection on the URL we want to render first.
		InputStream in = null;
		if (m_bUseURL)
		{
			try
			{
				URL url = new URL(hdt.GetURL());
				URLConnection connection = url.openConnection();
				in = connection.getInputStream();
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				in = new FileInputStream(new File(hdt.GetFile()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// A Reader should be created with the correct charset,
		// which may be obtained from the Content-Type header
		// of an HTTP response.
		Reader reader = new InputStreamReader(in);

		// InputSourceImpl constructor with URI recommended
		// so the renderer can resolve page component URLs.
		InputSource is = null;
		if (m_bUseURL)
		{
			is = new InputSourceImpl(reader, hdt.GetURL());
		}
		else
		{
			is = new InputSourceImpl(reader, "file:/" + hdt.GetFile());
		}
		final HtmlPanel htmlPanel = new HtmlPanel();
		UserAgentContext ucontext = new SimpleUserAgentContext();
		HtmlRendererContext rendererContext =
				new SimpleHtmlRendererContext(htmlPanel, ucontext);

		// Set a preferred width for the HtmlPanel, which will allow getPreferredSize() to
		// be calculated according to block content.
		// We do this here to illustrate the feature, but is generally not
		// recommended for performance reasons.
		htmlPanel.setPreferredWidth(800);

		// Note: This example does not perform incremental
		// rendering while loading the initial document.
		DocumentBuilderImpl builder = new DocumentBuilderImpl(
				rendererContext.getUserAgentContext(),
				rendererContext);

		Document document = null;
		try
		{
			document = builder.parse(is);
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// Set the document in the HtmlPanel. This method
		// schedules the document to be rendered in the
		// GUI thread.
		htmlPanel.setDocument(document, rendererContext);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame f = new JFrame();
				f.setTitle("Testing HTML Display with Cobra");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				f.setContentPane(htmlPanel);
				f.pack();
				f.setSize(1200, 800);
				f.setVisible(true);
			}
		});
	}
}
