/*
 * Tests: A collection of little test programs to explore Java language.
 * Test of standard XML handing in Java.
*/
/* File history:
 *  1.01.000 -- 2011/01/17 (PL) -- Normalize case of methods, moving to packages
 *  1.00.000 -- 2009/10/01 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.experiments.text;

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
		Document doc = parseXMLFile("filename.xml", false);
		// Use the Dom document
		Element root = doc.getDocumentElement();
		root.normalize(); // Collapse text nodes
		NodeList nlOC = root.getElementsByTagName("one-child");
		outputList(nlOC, "a");
		nlOC = root.getElementsByTagName("other-child");
		outputList(nlOC, "param");
	}

	public static void outputList(NodeList nlOC, String attrToShow)
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

	/**
	 * Parses an XML file and returns a DOM document.
	 * If validating is true, the contents is validated against the DTD
	 * specified in the file.
	 */
	public static Document parseXMLFile(String fileName, boolean validating)
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
	@Override
	public void fatalError(SAXParseException e)
	{
		log(Level.SEVERE, "Fatal Error", e);
	}

	//  Called in the event of a recoverable error
	@Override
	public void error(SAXParseException e)
	{
		log(Level.SEVERE, "Error", e);
	}

	//  Called in the event of a warning
	@Override
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
		message += ": " + e.getMessage() + ": line=" +
				line + ", col=" + col + ", PUBLIC=" +
				publicId + ", SYSTEM=" + systemId;

		// Log the message
		logger.log(level, message);
	}
}
