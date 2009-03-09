import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;

class URLEncoding
{
  static String str = "http://ws.audioscrobbler.com/1.0/artist/múm/toptags.xml";

  static String EncodeURL(String url)
  {
    // Use URI to encode low Ascii characters depending on context of various parts
    // For some reason, uri = new URI(url) chokes on space, so we have to split the URL
    String scheme = null;  // http, ftp, etc.
    String ssp = null; // scheme-specific part
    String fragment = null; // #anchor for example
    int colonPos = url.indexOf(":");
    if (colonPos < 0) return "Not an URL";
    scheme = url.substring(0, colonPos);
    ssp = url.substring(colonPos + 1);
    int fragPos = ssp.lastIndexOf("#");
    if (fragPos >= 0)
    {
      // Won't work if there is no real anchor/fragment
      // but this char is part of one parameter of the query,
      // but it is a bit unlikely...
      // That's probably why Java doesn't want to do it automatically,
      // it must be disambiguated manually
      fragment = ssp.substring(fragPos + 1);
      ssp = ssp.substring(0, fragPos);
    }

    URI uri = null;
    try
    {
      uri = new URI(scheme, ssp, fragment);
    } catch (URISyntaxException use) { return use.toString(); }
    String encodedURL1 = null;
    try
    {
      encodedURL1 = uri.toURL().toString();
    } catch (MalformedURLException mue) { return mue.toString(); }
    // Here, we still have Unicode chars unchanged

    byte[] utf8 = null;
    // Convert whole string to UTF-8 at once: low Ascii (below 0x80) is unchanged, other stuff is converted
    // to UTF-8, which always have the high bit set.
    try
    {
      utf8 = encodedURL1.getBytes("UTF-8");
    } catch (UnsupportedEncodingException uee) { return uee.toString(); }

    StringBuffer encodedURL = new StringBuffer();

    for (int i = 0; i < utf8.length; i++)
    {
      if (utf8[i] < 0) // Beyond Ascii: high bit is set, hence negative byte
      {
        encodedURL.append("%" + Integer.toString(0xFF + utf8[i], 16));
      }
      else
      {
        encodedURL.append((char)utf8[i]);
      }
    }

    return encodedURL.toString();
  }

  public static void main(String[] args)
  {
    System.out.println(EncodeURL(str));
    System.out.println(EncodeURL("http://www.example.com/you & I 10%? weird & weirder neé"));
    System.out.println(EncodeURL("http://www.example.com/Éric.html#CV"));
    System.out.println(EncodeURL("http://www.example.com/éditer.php?p1=déjà vu&p2=sl/ash#meh"));
  }
}
