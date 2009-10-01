import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;


public class XMLReadToDom
{
	public static void main(String[] args)
	{
		Document doc = ParseXMLFile("filename.xml", false);
		// Use the Dom document
		Element root = doc.getDocumentElement();
		root.normalize(); // Collapse text nodes
		NodeList nlOC = root.getElementsByTagName("one-child");
		OutputList(nlOC, "a");
		nlOC = root.getElementsByTagName("other-child");
		OutputList(nlOC, "param");
	}

	public static void OutputList(NodeList nlOC, String attrToShow)
	{
		for (int i = 0; i < nlOC.getLength(); i++)
		{
			Element el = (Element) nlOC.item(i);

			String attr = el.getAttribute(attrToShow);
			String txt = el.getTextContent();
			System.out.println("Element " + el.getTagName() +
			  (attr.length() > 0 ?
						" with attribute " + attrToShow + " = '" + attr + "'" :
						" without attribute") +
			  (txt.length() > 0 ? " containing '" + txt + "'" : " (empty)")
			);
		}
	}
	// Parses an XML file and returns a DOM document.
	// If validating is true, the contents is validated against the DTD
	// specified in the file.
	public static Document ParseXMLFile(String fileName, boolean validating)
	{
		try
		{
			// Create a builder factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// Validate or not
			factory.setValidating(validating);
			// Coalesce CDATA nodes to text nodes
			factory.setCoalescing(true);
			// Ignore comments
			factory.setIgnoringComments(true);
			// Prevent expansion of entity references
			factory.setExpandEntityReferences(false);

			// Create a builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Set an error listener
			builder.setErrorHandler(new XMLErrorHandler());

			// Parse the file
			Document doc = builder.parse(new File(fileName));
			return doc;
		}
		catch (SAXException e)
		{
			// A parsing error occurred; the XML input is not valid
			System.out.println("Invalid XML: " + e);
		}
		catch (ParserConfigurationException e)
		{
			System.out.println("Error: " + e);
		}
		catch (IOException e)
		{
			System.out.println("Cannot read file " + fileName);
		}
		return null;
	}
}

// This error handler uses a Logger to log error messages
class XMLErrorHandler implements ErrorHandler
{
	//  Called in the event of a non-recoverable error
	public void fatalError(SAXParseException e)
	{
		log(Level.SEVERE, "Fatal Error", e);
	}

	//  Called in the event of a recoverable error
	public void error(SAXParseException e)
	{
		log(Level.SEVERE, "Error", e);
	}

	//  Called in the event of a warning
	public void warning(SAXParseException e)
	{
		log(Level.WARNING, "Warning", e);
	}

	// Get logger to log errors
	private Logger logger = Logger.getLogger("org.philhosoft");

	// Dump a log record to a logger
	private void log(Level level, String message, SAXParseException e)
	{
		// Get details
		int line = e.getLineNumber();
		int col = e.getColumnNumber();
		String publicId = e.getPublicId();
		String systemId = e.getSystemId();

		// Append details to message
		message = message + ": " + e.getMessage() + ": line=" +
        line + ", col=" + col + ", PUBLIC=" +
        publicId + ", SYSTEM=" + systemId;

		// Log the message
		logger.log(level, message);
	}
}
