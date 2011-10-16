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
package org.philhosoft.tests.text;

import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class XMLCreate2
{
	static final String NAMESPACE = "http://www.daisy.org/z3986/2005/ncx/";
	static final String NAMESPACE2 = "http://www.example.com/";
	static final String ROOT_ELEMENT = "xml"; // Level 0 tag name

	public static void main(String[] args)
	{
		createXMLFile("filename21.xml", false);
		createXMLFile("filename22.xml", true);
	}

	public static void createXMLFile(String fileName, boolean bUseNamespace)
	{
		// Set up the document builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try
		{
			docBuilder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			System.out.println(e);
		}
		// Create document
		Document document = docBuilder.newDocument();
		DOMImplementation di = document.getImplementation();

		Element root = document.createElementNS(NAMESPACE, ROOT_ELEMENT);
		if (bUseNamespace)
		{
			DocumentType doctype = di.createDocumentType(
					ROOT_ELEMENT, // Qualified name																// name
					"-//NISO//DTD xml 2005-1//EN", // publicId
					"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd" // systemId
			);
			document.appendChild(doctype);
		}
		document.appendChild(root);

		Attr rootAttr1 = document.createAttribute("version");
		rootAttr1.setValue("2005-1");
		root.setAttributeNode(rootAttr1);
		Attr rootAttr2 = document.createAttribute("xml:lang");
		rootAttr2.setValue("en-US");
		root.setAttributeNodeNS(rootAttr2);

		Element child1 = document.createElement("one-child");
		child1.setTextContent("Content of one child");
		root.appendChild(child1);

		// Don't re-use Element!
		Element child2 = document.createElement("one-child");
		child2.setTextContent("Another content\nof one child");
		Attr childAttr = document.createAttribute("a");
		childAttr.setValue("Fooo");
		child2.setAttributeNode(childAttr);
		root.appendChild(child2);

		Element child3 = document.createElement("other-child");
		child3.setAttribute("param", "Empty tag");
		root.appendChild(child3);

		Element child4 = document.createElement("other-child");
		child4.setTextContent("Content of the other child");
		child4.setAttribute("param", "Some value");
		root.appendChild(child4);

		Attr otherAttribute = null;
		if (bUseNamespace)
		{
			otherAttribute = document.createAttributeNS(NAMESPACE2, "attrNS");
		}
		else
		{
			otherAttribute = document.createAttribute("attrNoNS");
		}
		otherAttribute.setValue("Yo Man!");
		child4.setAttributeNode(otherAttribute);

		DOMImplementationLS implLS = (DOMImplementationLS) di.getFeature("LS", "3.0");
		LSSerializer serializer = implLS.createLSSerializer();
		serializer.getDomConfig().setParameter("format-pretty-print", true);

		LSOutput lsOutput = implLS.createLSOutput();
		lsOutput.setEncoding("UTF-8");

		// Output the result
		try
		{
			PrintWriter pw = new PrintWriter(fileName, "UTF-8");
			lsOutput.setCharacterStream(pw);
			serializer.write(document, lsOutput);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error on file: " + e);
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("Error: " + e);
		}
	}
}
