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

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XMLCreate1
{
	public static void main(String[] args)
	{
		createXMLFile("filename1.xml");
	}

	public static void createXMLFile(String fileName)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element root = document.createElementNS("http://example.com", "root");
			document.appendChild(root);
			Attr rootAttribute = document.createAttributeNS("http://example.com", "attribute");
			rootAttribute.setValue("value");
			root.setAttributeNodeNS(rootAttribute);

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
			Attr childParam1 = document.createAttribute("param");
			childParam1.setValue("Empty tag");
			child3.setAttributeNode(childParam1);
			root.appendChild(child3);

			Element child4 = document.createElement("other-child");
			child4.setTextContent("Content of the other child");
			Attr childParam2 = document.createAttribute("param");
			childParam2.setValue("Some value");
			child4.setAttributeNode(childParam2);
			root.appendChild(child4);

			DOMSource source = new DOMSource(document);
			PrintStream ps = new PrintStream(fileName);
			StreamResult res = new StreamResult(ps);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transf = tf.newTransformer();

			// Output the generated XML
			transf.transform(source, res);
		}
		catch (ParserConfigurationException e)
		{
			System.out.println("Error: " + e);
		}
		catch (TransformerConfigurationException e)
		{
			System.out.println("Error: " + e);
		}
		catch (TransformerException e)
		{
			System.out.println("Error: " + e);
		}
		catch (IOException e)
		{
			System.out.println("Cannot write file " + fileName);
		}
	}
}
