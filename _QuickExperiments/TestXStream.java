import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;


// javac -cp C:\Java\libraries\xstream-1.4.2.jar TestXStream.java
// java -cp .;C:\Java\libraries\xstream-1.4.2.jar;C:\Java\libraries\kxml2-2.3.0.jar TestXStream
class TestXStream
{
  public static void main(String args[])
  {
    String fileName;
    if (args.length == 0)
    {
      // Default to a known file
      fileName = "D:/Dev/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
//~       fileName = "H:/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
    }
    else
    {
      fileName = args[0];
    }

    ArrayList<Person> team = new ArrayList<Person>();

    PhoneNumber fax = new PhoneNumber(911, "9999-999");

    Person jack = new Person("Jack", "Vance");
    jack.setPhone(new PhoneNumber(123, "1234-455"));
    jack.setFax(fax);
    System.out.println("Meet Jack: " + jack);
    team.add(jack);

    Person joe = new Person("Joe", "Walnes");
    joe.setPhone(new PhoneNumber(123, "1234-456"));
    joe.setFax(fax);
    System.out.println("Meet Joe: " + joe);
    team.add(joe);

    // No additional dependency (StAX is in JRE 6); no indentation
    System.out.println("\nStAX test:\n");
    XStream xstreamRaw = new XStream(new StaxDriver());
    Test(xstreamRaw, team);

    // Implicit dependency on kxml2; nice indented output
    System.out.println("\nKXML2 test:\n");
    XStream xstreamNice = new XStream();
    Test(xstreamNice, team);
  }

  private static void Test(XStream xstream, ArrayList<Person> team)
  {
    xstream.alias("person", Person.class);
    xstream.alias("phone-number", PhoneNumber.class);

    String xml = xstream.toXML(team);
    System.out.println("Generated XML:\n" + xml);

    String joeXML = xstream.toXML(team.get(1));
    System.out.println("Generated XML for Joe:\n" + joeXML);

    Person newJoe = (Person) xstream.fromXML(joeXML);
    System.out.println("New Joe:\n" + newJoe);

//~     Person oldJoe = (Person) xstream.fromXML(xml.replaceAll("fax", "fix"));
//~     System.out.println("Old Joe:\n" + oldJoe);
  }
}

class Person
{
  private String firstName;
  private String lastName;
  private PhoneNumber phone;
  private PhoneNumber fax;
  private transient String initials;

  // ... constructors and methods
  public Person(String firstName, String lastName)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    initials = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
  }

  public void setPhone(PhoneNumber phone)
  {
    this.phone = phone;
  }
  public void setFax(PhoneNumber fax)
  {
    this.fax = fax;
  }

  @Override
  public String toString()
  {
    return firstName + " " + lastName + " [" + initials +  "] (" + phone + ")(" + fax + ")";
  }
}

class PhoneNumber
{
  private int code;
  private String number;

  // ... constructors and methods
  public PhoneNumber(int code, String number)
  {
    this.code = code;
    this.number = number;
  }

  @Override
  public String toString()
  {
    return code + " " + number;
  }
}