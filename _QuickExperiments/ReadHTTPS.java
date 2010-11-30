import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class ReadHTTPS
{
  HttpsURLConnection m_connect;

  public static void main(String[] args)
  {
    ReadHTTPS rh = new ReadHTTPS();
    rh.PrintInfo("https://www.google.com/");
  }

  private void PrintInfo(String httpsURL)
  {
    URL url;
    try
    {
      url = new URL(httpsURL);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      return;
    }
    try
    {
      m_connect = (HttpsURLConnection) url.openConnection();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return;
    }
    System.out.println("Reading URL: " + url);
    System.out.println();
    PrintCert();
    PrintContent();
  }

  private void PrintCert()
  {
    assert m_connect != null;

    try
    {
      System.out.println("Response code: " + m_connect.getResponseCode());
      System.out.println("Cipher suite: " + m_connect.getCipherSuite());
      System.out.println();

      Certificate[] certs = m_connect.getServerCertificates();
      for (Certificate cert : certs)
      {
        System.out.println("Cert type: " + cert.getType());
        System.out.println("Cert hash code: " + cert.hashCode());
        System.out.println("Cert public key algorithm: " + cert.getPublicKey().getAlgorithm());
        System.out.println("Cert public key format: " + cert.getPublicKey().getFormat());
        System.out.println();
      }
    }
    catch (SSLPeerUnverifiedException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void PrintContent()
  {
    assert m_connect != null;

    System.out.println("Content of the URL:\n");
    BufferedReader br = null;
    try
    {
      br = new BufferedReader(new InputStreamReader(m_connect.getInputStream()));
      String input;
      while ((input = br.readLine()) != null)
      {
        System.out.println(input);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (br != null)
      {
        try { br.close(); } catch (IOException e) { e.printStackTrace(); }
      }
    }
  }
}
