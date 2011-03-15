// http://www.javaworld.com/javatips/jw-javatip130.html
// http://devblog.streamy.com/2009/07/24/determine-size-of-java-object-class/
// http://www.javaworld.com/javaqa/2003-12/02-qa-1226-sizeof.html

public class Sizeof
{
	long heap1;

	public void init()
	{
		runGC();
		heap1 = usedMemory();
	}
	public long measureMemory()
	{
		long heap2 = usedMemory(); // Take an after heap snapshot

		return size = heap2 - heap1;
	}
	public void showMemory(Object obj)
	{
		final long size = measureMemory();
		System.out.println ("Used memory of " + obj.getClass() + ": " + size + " bytes");
	}

	private void runGC()
	{
		// It helps to call Runtime.gc()
		// using several method calls
		for (int r = 0; r < 4; r++) _runGC();
	}
	private void _runGC()
	{
		long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
		for (int i = 0; usedMem1 < usedMem2 && i < 500; i++)
		{
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.yield();

			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}
	private long usedMemory()
	{
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}

	private static final Runtime s_runtime = Runtime.getRuntime ();
}

