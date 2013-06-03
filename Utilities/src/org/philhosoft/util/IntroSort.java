/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
// original Copyright Ralph Unden,
// http://ralphunden.net/content/tutorials/a-guide-to-introsort/?q=a-guide-to-introsort
// (PL: dead link as of 2012-10-03)
//
// Modifications: Bernhard Pfahringer
// http://www.cs.waikato.ac.nz/~bernhard/317/source/IntroSort.java
// changes include: local insertion sort, no global array
//
// Modifications: Philippe Lhoste
// Used my format and conventions, fixed the InsertionSort, added the IntComparator

// http://en.wikipedia.org/wiki/Introsort
 */
/* File history:
 *  1.01.000 -- 2012/11/27 (PL) -- Add a simplified sort entry function and some other minor improvements.
 *  1.00.000 -- 2012/11/05 (PL) -- Creation.
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.util;



/**
 * A sort with IntComparator, designed to be fast / efficient for sorting arrays of integers with a given criterion.<br>
 * The IntComparator allows to avoid boxing / unboxing of int in Integer, so it reduces garbage and is faster.
 * <p>
 * Note: The point for having a comparator for integers is to use them as indices in an array: this allows to use the
 * int array as an indirection vector.
 * <p>
 * For natural int order, just use Arrays.sort()!
 *
 * @author PhiLho
 */
public class IntroSort
{
	/** Below this size, we use insertion sort instead of quicksort. */
	private static int SIZE_THRESHOLD = 32;

	private IntroSort() {} // Class with only static methods

	/**
	 * Sorts the given array of integer {@code data} with the provided {@code comparator}.
	 *
	 * @param data  the array of integers to sort
	 * @param comparator  the comparator of integers, which might use them as indices in another data structure
	 */
	public static void sort(int[] data, IntComparator comparator)
	{
		sort(data, 0, data.length, comparator);
	}

	/**
	 * Sorts the given array of integer {@code data} with the provided {@code comparator},
	 * between the bounds [{@code begin}, {@code end}[
	 * (ie. {@code begin} is inclusive while {@code end} is exclusive).
	 *
	 * @param data  the array of integers to sort
	 * @param begin  the starting position to sort in the array
	 * @param end  the ending position to sort in the array
	 * @param comparator  the comparator of integers, which might use them as indices in another data structure
	 */
	public static void sort(int[] data, int begin, int end, IntComparator comparator)
	{
		if (begin < end)
		{
			introsort(data, begin, end, 2 * floorLog(end - begin), comparator);
		}
	}

	/**
	 * Quicksort algorithm modified for Introsort.
	 */
	private static void introsort(int[] a, int lo, int hi, int depthLimit, IntComparator comparator)
	{
		while (hi - lo > SIZE_THRESHOLD)
		{
			// System.out.println("Introsort (" + nDepthLimit + ") " + lo + " to " + hi);
			if (depthLimit == 0) // Too much recursion, abandon QuickSort
			{
				heapSort(a, lo, hi, comparator);
				return;
			}
			depthLimit--;
			int p = partition(a, lo, hi, medianOfThree(a, lo, lo + ((hi - lo) / 2) + 1, hi - 1, comparator), comparator);
			introsort(a, p, hi, depthLimit, comparator);
			hi = p;
		}
		// Finishing touch
		insertionSort(a, lo, hi, comparator);
	}

	private static int partition(int[] a, int lo, int hi, int x, IntComparator comparator)
	{
		int i = lo, j = hi - 1;
		while (true)
		{
			// Find the first element above or equal to the median, before it
			// Note that this can fail wit an ArrayIndexOutOfBoundsException if the comparator is badly written:
			// seen with a comparator always returning -1 with a given set of data (ie. values where not even equal to themselves).
			while (comparator.compare(a[i], x) < 0)
			{
				i++;
			}
			// Find the last element below or equal to the median, after it
			while (comparator.compare(x, a[j]) < 0)
			{
				j--;
			}
			if (i >= j)
				return i;

			swap(a, i, j);
			i++;
			j--;
		}
	}

	private static int medianOfThree(int[] a, int lo, int mid, int hi, IntComparator comparator)
	{
		if (comparator.compare(a[mid], a[lo]) < 0)
		{
			if (comparator.compare(a[hi], a[mid]) < 0) return a[mid];
			if (comparator.compare(a[hi], a[lo]) < 0) return a[hi];
			return a[lo];
		}
		if (comparator.compare(a[hi], a[mid]) < 0)
		{
			if (comparator.compare(a[hi], a[lo]) < 0) return a[lo];
			return a[hi];
		}
		return a[mid];
	}

	/**
	 * Heapsort algorithm
	 */
	private static void heapSort(int[] a, int lo, int hi, IntComparator comparator)
	{
		// System.out.println("HeapSort " + lo + " to " + hi);
		int n = hi - lo;
		for (int i = n / 2; i >= 1; i = i - 1)
		{
			downHeap(a, i, n, lo, comparator);
		}
		for (int i = n; i > 1; i = i - 1)
		{
			swap(a, lo, lo + i - 1);
			downHeap(a, 1, i - 1, lo, comparator);
		}
	}

	private static void downHeap(int[] a, int i, int n, int lo, IntComparator comparator)
	{
		int d = a[lo + i - 1];
		int child;
		while (i <= n / 2)
		{
			child = 2 * i;
			if (child < n && comparator.compare(a[lo + child - 1], a[lo + child]) < 0)
			{
				child++;
			}
			if (comparator.compare(a[lo + child - 1], d) <= 0) break;
			a[lo + i - 1] = a[lo + child - 1];
			i = child;
		}
		a[lo + i - 1] = d;
	}

	/**
	 * Insertion sort algorithm
	 */
	private static void insertionSort(int[] a, int lo, int hi, IntComparator comparator)
	{
		// System.out.println("InsertionSort " + lo + " to " + hi);
		for (int i = lo + 1; i <= hi; i++)
		{
			// Create a hole at i
			int hole = i;
			// Save the value at the hole place
			int holeVal = a[i];
			// While the value is smaller than the previous (lower index) item
			while (hole != lo && comparator.compare(holeVal, a[hole - 1]) < 0)
			{
				// Move hole to next smaller index
				a[hole] = a[hole - 1];
				hole--;
			}
			// Put the value in the moved hole
			a[hole] = holeVal;
		}
	}

	/*
	 * Common methods for all algorithms
	 */

	private static void swap(int[] a, int i, int j)
	{
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	private static int floorLog(int a)
	{
		return (int) (Math.floor(Math.log(a) / Math.log(2)));
	}
}
