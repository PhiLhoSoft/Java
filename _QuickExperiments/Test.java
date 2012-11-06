public class Test
{
  public static void main(String[] args)
  {
    for (float x = -1; x < 1; x += 0.2)
    {
      for (float y = -1; y < 1; y += 0.2)
      {
        double v1 = -Math.atan2(-y, x);
        double v2 = Math.atan2(y, x);
        if (v1 != v2)
          System.out.println(String.format("x=%.2f, y=%.2f -> %.2f  %.2f)", x, y, v1, v2));
      }
    }
  }
}
