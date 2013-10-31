package test;

import java.util.prefs.*;

public class UsingPreferences
{
  static public void main( String args[] ) throws Exception
  {
    Preferences root = Preferences.userNodeForPackage(UsingPreferences.class);

    root.put("some", "value");

    Preferences subnode = root.node("subnode");
    subnode.put("another", "value too");

    root.exportSubtree(System.out);
  }
}
