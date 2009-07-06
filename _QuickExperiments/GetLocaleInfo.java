//~ import netscape.javascript.*;
import java.applet.*;
import java.util.Locale;
import java.text.*;

public class GetLocaleInfo extends Applet
{
  Locale loc;
  NumberFormat nf;
  NumberFormat cnf;
  NumberFormat pnf;

  // For running as plain application
  public static void main(String args[])
  {
    final Applet applet = new GetLocaleInfo();
    applet.init();
    applet.start();
  }

  public void init() // Applet is loaded
  {
    // Use current locale
    loc = Locale.getDefault();
    nf = NumberFormat.getInstance();
    cnf = NumberFormat.getCurrencyInstance();
    pnf = NumberFormat.getPercentInstance();
  }

  public void start() // Applet should start
  {
    // Following output goes to Java console
    System.out.println(GetLocaleInformation());
    System.out.println(nf.format(0.1));
    System.out.println(cnf.format(1.0));
    System.out.println(pnf.format(0.01));
  }

  public void stop() // Applet should stop its execution
  {
  }

  public void destroy() // Applet should destroy any resources that it has allocated
  {
  }

  public String GetLocaleInformation()
  {
    return String.format("Locale for %s: country=%s (%s / %s), lang=%s (%s / %s), variant=%s (%s)",
        loc.getDisplayName(),
        loc.getDisplayCountry(),
        loc.getCountry(),
        loc.getISO3Country(),

        loc.getDisplayLanguage(),
        loc.getLanguage(),
        loc.getISO3Language(),

        loc.getDisplayVariant(),
        loc.getVariant()
    );
  }

  public String FormatNumber(String number)
  {
    double value = 0;
    try
    {
      value = Double.parseDouble(number);
    }
    catch (NumberFormatException nfe)
    {
      return "!";
    }
    return nf.format(value);
  }

  public String FormatCurrency(String number)
  {
    double value = 0;
    try
    {
      value = Double.parseDouble(number);
    }
    catch (NumberFormatException nfe)
    {
      return "!";
    }
    return cnf.format(value);
  }

  public String FormatPercent(String number)
  {
    double value = 0;
    try
    {
      value = Double.parseDouble(number);
    }
    catch (NumberFormatException nfe)
    {
      return "!";
    }
    return pnf.format(value);
  }
}
