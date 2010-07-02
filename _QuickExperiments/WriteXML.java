import java.util.*;
import java.io.*;
import javax.xml.stream.*;

class WriteXML
{
  public static void main(String args[])
  {
    String fileName = "Test.xml";
    XMLOutputFactory xof =  XMLOutputFactory.newInstance();
    XMLStreamWriter xtw = null;
    try
    {
      xtw = xof.createXMLStreamWriter(new FileOutputStream(fileName), "UTF-8");
      xtw.writeStartDocument("UTF-8", "1.0");
      xtw.writeStartElement("root");
      xtw.writeComment("This is an attempt to create an XML file with StAX");

      xtw.writeStartElement("foo");
      xtw.writeAttribute("order", "1");
        xtw.writeStartElement("meuh");
        xtw.writeAttribute("active", "true");
          xtw.writeCharacters("The cows are flying high this Spring");
        xtw.writeEndElement();
      xtw.writeEndElement();

      xtw.writeEmptyElement("separator");

      xtw.writeStartElement("bar");
      xtw.writeAttribute("order", "2");
        xtw.writeStartElement("tcho");
        xtw.writeAttribute("kola", "K");
          xtw.writeCharacters("Content of tcho tag");
        xtw.writeEndElement();
      xtw.writeEndElement();

      xtw.writeEndElement();
      xtw.writeEndDocument();
    }
    catch (XMLStreamException e)
    {
      e.printStackTrace();
    }
    catch (IOException ie)
    {
      ie.printStackTrace();
    }
    finally
    {
      if (xtw != null)
      {
        try
        {
          xtw.close();
        }
        catch (XMLStreamException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
