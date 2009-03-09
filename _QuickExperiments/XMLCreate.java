import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLCreate
{
	public static void main(String[] args)
	{
		CreateXMLFile("infilename.xml", false);
	}

	public static void CreateXMLFile(String fileName, boolean validating)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element root = document.createElementNS("http://test", "root");
			document.appendChild(root);
			Attr rootAttribute = document.createAttributeNS("http://test", "attribute");
			rootAttribute.setValue("value");
			root.setAttributeNodeNS(rootAttribute);
		}
		catch (SAXException e)
		{
			// A parsing error occurred; the xml input is not valid
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
