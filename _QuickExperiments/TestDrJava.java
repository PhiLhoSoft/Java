import java.util.Comparator;
import java.util.Arrays;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class TestDrJava
{
public static void main(String[] a)
{
//~ NumberFormat[] nfs =
//~ {
//~ new DecimalFormat("#,###.##"),
//~ new DecimalFormat("#,###.00"),
//~ new DecimalFormat("#,##0.00")
//~ };
DecimalFormat[] nfs =
{
new DecimalFormat("#,##0.##"),
new DecimalFormat("#,###.##"),
new DecimalFormat("#,###.00"),
new DecimalFormat("#,##0.00")
};
double[] vals = { 3.57, 0.98, 0.6 };
for (DecimalFormat nf : nfs)
  for (double v : vals)
    System.out.println(nf.format(v));
}
}
