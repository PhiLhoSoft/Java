import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;


// javac -cp C:\Java\libraries\xstream-1.4.2.jar TestXStream.java
// java -cp .;C:\Java\libraries\xstream-1.4.2.jar;C:\Java\libraries\kxml2-2.3.0.jar TestXStream
class TestXStream
{
  public static void main(String args[])
  {
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
    doAlias(xstreamRaw);
    test(xstreamRaw, team);

    // Implicit dependency on kxml2; nice indented output
    System.out.println("\nKXML2 test:\n");
    XStream xstreamNice = new XStream();
    doAlias(xstreamNice);
    test(xstreamNice, team);

    // Reading an old version of the class
    // In the new version, a field was removed (hobby), one was changed (age to birthday) and a new one (fax) has been added
    String oldJoeXML = "<?xml version='1.0'?>\n" +
        "<person>\n<firstName>Joe</firstName>\n<lastName>Walnes</lastName>\n" +
        "<age>42</age>\n<hobby>Reading</hobby>\n" +
        "<phone><code>123</code><number>1234-456</number></phone>\n</person>";
    // According to the FAQ, we have to make a custom XStream to handle the case
    XStream oldXstream = new XStream()
    {
      @Override
      protected MapperWrapper wrapMapper(MapperWrapper next)
      {
        return new MapperWrapper(next)
        {
          @Override
          public boolean shouldSerializeMember(Class definedIn, String fieldName)
          {
//~ System.out.println("## " + definedIn + " " + fieldName);
            // Apparently hard to convert a value on the fly, I just ignore age too
            if (definedIn == Person.class &&
                (fieldName.equals("hobby") || fieldName.equals("age")))
              return false;
            return super.shouldSerializeMember(definedIn, fieldName);
          }
        };
      }
    };
    doAlias(oldXstream);

    Person oldJoe = (Person) oldXstream.fromXML(oldJoeXML);
    System.out.println("Old Joe:\n" + oldJoe);
  }

  private static void doAlias(XStream xstream)
  {
    xstream.alias("person", Person.class);
    xstream.alias("phone-number", PhoneNumber.class);
  }
  private static void test(XStream xstream, ArrayList<Person> team)
  {
    String xml = xstream.toXML(team);
    System.out.println("Generated XML:\n" + xml);

    String joeXML = xstream.toXML(team.get(1));
    System.out.println("Generated XML for Joe:\n" + joeXML);

    Person newJoe = (Person) xstream.fromXML(joeXML);
    System.out.println("New Joe:\n" + newJoe);
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
    computeInitials();
  }

  private Object readResolve()
  {
//~     System.out.println(">> readResolve");
    if (initials == null)
    {
      computeInitials();
    }
    return this;
  }
  private void computeInitials()
  {
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