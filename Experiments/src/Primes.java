
public class Primes
{
	// Original code
	public static void first()
	{
		int topPrime = 150003;
		int current = 2;
		int count = 0;
		int lastPrime = 2;

		long start = System.currentTimeMillis();

		while (count < topPrime) {

		  boolean prime = true;

		  int top = (int)Math.sqrt(current) + 1;

		  for (int i = 2; i < top; i++) {
		    if (current % i == 0) {
		      prime = false;
		      break;
		    }
		  }

		  if (prime) {
		    count++;
		    lastPrime = current;
//			System.out.print(lastPrime + " "); // Checking algo is correct...
		  }
		  if (current == 2) {
		    current++;
		  } else {
		    current = current + 2;
		  }
		}

		System.out.println("\n-- First");
		System.out.println("Last prime = " + lastPrime);
		System.out.println("Total time = " + (double)(System.currentTimeMillis() - start) / 1000);
	}

	// My attempt
	public static void second()
	{
		final int wantedPrimeNb = 150000;
		int count = 0;

		int currentNumber = 1;
		int increment = 4;
		int lastPrime = 0;

		long start = System.currentTimeMillis();

NEXT_TESTING_NUMBER:
		while (count < wantedPrimeNb)
		{
			currentNumber += increment;
			increment = 6 - increment;
			if (currentNumber % 2 == 0) // Even number
				continue;
			if (currentNumber % 3 == 0) // Multiple of three
				continue;

			int top = (int) Math.sqrt(currentNumber) + 1;
			int testingNumber = 5;
			int testIncrement = 2;
			do
			{
				if (currentNumber % testingNumber == 0)
				{
					continue NEXT_TESTING_NUMBER;
				}
				testingNumber += testIncrement;
				testIncrement = 6 - testIncrement;
			} while (testingNumber < top);
			// If we got there, we have a prime
			count++;
			lastPrime = currentNumber;
//			System.out.print(lastPrime + " "); // Checking algo is correct...
		}

		System.out.println("\n-- Second");
		System.out.println("Last prime = " + lastPrime);
		System.out.println("Total time = " + (double) (System.currentTimeMillis() - start) / 1000);
	}

	public static void main(String[] args)
	{
		first();
		second();
	}
}
