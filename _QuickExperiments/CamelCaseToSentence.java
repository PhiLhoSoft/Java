class CamelCaseToSentence
{
  static String ToSentence(String camel)
  {
    if (camel == null) return ""; // Or null...
    String[] words = camel.split("(?=[A-Z])");
    if (words == null) return "";
    if (words.length == 1) return words[0];
    StringBuilder sentence = new StringBuilder(camel.length());
    if (words[0].length() > 0) // Just in case of camelCase instead of CamelCase
    {
      sentence.append(words[0] + " " + words[1].toLowerCase());
    }
    else
    {
      sentence.append(words[1]);
    }
    for (int i = 2; i < words.length; i++)
    {
      sentence.append(" " + words[i].toLowerCase());
    }
    return sentence.toString();
  }

  public static void main(String[] args)
  {
    System.out.println(ToSentence("AwaitingAFeedbackDearMaster"));
    System.out.println(ToSentence(null));
    System.out.println(ToSentence(""));
    System.out.println(ToSentence("A"));
    System.out.println(ToSentence("Aaagh!"));
    System.out.println(ToSentence("stackoverflow"));
    System.out.println(ToSentence("disableGPS"));
    System.out.println(ToSentence("Ahh89Boo"));
    System.out.println(ToSentence("ABC"));
  }
}
