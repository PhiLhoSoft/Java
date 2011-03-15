import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

class Test
{
  public static void main(String args[])
  {
    System.out.println("Hello World!");
    NumberFormat nf = NumberFormat.getInstance();
    long maxMem = Runtime.getRuntime().maxMemory();
    System.out.println("Max memory: " + nf.format(maxMem) + " (" + maxMem/1024/1024 + ")");

    int mµ = (int) maxMem;
    System.out.println("Max memory: " + nf.format(mµ) + " (" + mµ/1024/1024 + ")");
    System.out.println("Max int: " + nf.format(Integer.MAX_VALUE));
    System.out.println("Max long: " + nf.format(Long.MAX_VALUE));

    long tm1 = 17408 * 1024 * 1024;
    long tm2 = 17408L * 1024 * 1024;
    int m1 = (int) (tm1 / 2000000);
    int m2 = (int) (tm2 / 2000000);
    System.out.println("Max memory: " + nf.format(tm1) + " (" + m1 + ")");
    System.out.println("Max memory: " + nf.format(tm2) + " (" + m2 + ")");
    long tm0 = 17408 * 1000000;
    System.out.println("Max memory: " + nf.format(tm0));
//    e.printStackTrace();
  }
}

