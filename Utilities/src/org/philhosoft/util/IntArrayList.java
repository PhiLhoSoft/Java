/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
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


import java.util.*;


/**
 * An array list of primitive integers with add, insert and remove service support.
 * This is used to avoid big memory consumption when using ArrayList&lt;Integer>()
 *
 * @author PhiLho
 */
public class IntArrayList
{
	/** Memory contents of the array. */
	private int[] m_integers;
	/** Size of the used capacity. */
	private int m_size;

	/**
	 * Constructor with a default size (128).
	 */
	public IntArrayList()
	{
		m_integers = new int[128];
	}

	/**
	 * Constructor with the given initial size.
	 */
	public IntArrayList(int initialCapacity)
	{
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		m_integers = new int[initialCapacity];
	}

	/**
	 * Constructor with an initial array.<br>
	 * To improve the speed, the class just takes control of the array, so if you need to keep your array
	 * independent, pass a copy of it (Arrays.copyOf()) or use {@link #IntArrayList(int[],int)}.
	 *
	 * @param initArray  the initial data to populate the class
	 */
	public IntArrayList(int[] initArray)
	{
		m_integers = initArray;
		m_size = initArray.length;
	}

	/**
	 * Constructor with an initial array of given length.<br>
	 * Unlike {@link #IntArrayList(int[])}, this constructor copies the values from the given array,
	 * up to the given size.
	 *
	 * @param initArray  the initial data to populate the class
	 * @param size  the initial data to populate the class
	 */
	public IntArrayList(int[] initArray, int size)
	{
		m_integers = Arrays.copyOf(initArray, size);
		// If size exceeds the length of the initial array, copyOf() padded the values, so we can have extra room to grow,
		// but then we limit the visible size to the original length.
		m_size = size > initArray.length ? initArray.length : size;
	}

	/** Gets the value of the given index (within the current size). */
	public int get(int index)
	{
		if (index < 0 || index >= m_size) throw new ArrayIndexOutOfBoundsException(index);
		return m_integers[index];
	}

	/** Sets the given value of the given index (within the current size). */
	public void set(int index, int value)
	{
		if (index < 0 || index >= m_size) throw new ArrayIndexOutOfBoundsException(index);
		m_integers[index] = value;
	}

	public void clear()
	{
		m_size = 0;
	}

	public int getSize()
	{
		return m_size;
	}

	/** Inserts the given value at the given index. Same as AddAt(). */
	public void insertAt(int index, int nValue)
	{
		addAt(index, nValue);
	}

	/** Adds the given value at the given index. */
	public void addAt(int index, int nValue)
	{
		ensureCapacity(m_size + 1);
		System.arraycopy(m_integers, index, m_integers, index + 1, m_size - index);
		m_integers[index] = nValue;
		m_size++;
	}

	/** Adds the given value at the end. */
	public void add(int nValue)
	{
		ensureCapacity(m_size + 1);
		m_integers[m_size++] = nValue;
	}

	/** Removes one value at the given index. */
	public void removeAt(int index)
	{
		if (index < 0 || index >= m_size) throw new ArrayIndexOutOfBoundsException(index);

		if (index != m_size - 1) // If equals, just decrement the size
		{
			System.arraycopy(m_integers, index + 1, m_integers, index, m_size - (index + 1));
		}

		m_size--;
	}

	/**
	 * @return a copy of the data in an integer array.
	 */
	public int[] getArray()
	{
		return Arrays.copyOf(m_integers, m_size);
	}

	public void sort(IntComparator icComparator)
	{
		// QuickSort( m_naData, 0, m_nSize - 1, icComparator );
		IntroSort.sort(m_integers, 0, m_size - 1, icComparator);
	}

	public void sort(boolean bAscending)
	{
		Arrays.sort(m_integers, 0, m_size);
		if (!bAscending)
		{
			// Invert the content of the array
			reverse();
		}
	}

	/** Reverses the order of the data. */
	public void reverse()
	{
		for (int idx = 0; idx < m_size / 2; idx++)
		{
			int nRevIdx = m_size - idx - 1;
			int nTmp = m_integers[idx];
			m_integers[idx] = m_integers[nRevIdx];
			m_integers[nRevIdx] = nTmp;
		}
	}

	// Works only if the array is sorted (@see Sort())
	public int binarySearch(int nValue, IntComparator icComparator)
	{
		return binarySearch(m_integers, 0, m_size - 1, nValue, icComparator);
	}

	// Works only if m_theContents is sorted (@see Sort())
	public int binarySearch(int nValue)
	{
		return Arrays.binarySearch(m_integers, 0, m_size, nValue);
	}

	/**
	 * Adds all the given values to the data. If there is no initial data, the class just uses the given array as
	 * internal storage, taking control of it, so if you need to keep your array independent, pass a copy of it
	 * (Array.copyOf()). If there is initial data, the additional data is appended to it.
	 *
	 * @param naValues the data to add
	 */
	public void addAll(int[] naValues)
	{
		if (m_size == 0)
		{
			m_integers = naValues;
		}
		else if (m_size + naValues.length <= m_integers.length)
		{
			System.arraycopy(naValues, 0, m_integers, m_size, naValues.length);
		}
		else
		{
			int[] naNew = new int[m_size + naValues.length];
			System.arraycopy(m_integers, 0, naNew, 0, m_size);
			System.arraycopy(naValues, 0, naNew, m_size, naValues.length);
			m_integers = naNew;
		}
		m_size += naValues.length;
	}

	private void ensureCapacity(int nRequestedSize)
	{
		if (nRequestedSize >= m_integers.length)
		{
			// One time and half the current capacity
			int nNewCapacity = m_integers.length + (m_integers.length >> 1);
			// If still below requested size, just take the requested size
			if (nNewCapacity < nRequestedSize)
			{
				nNewCapacity = nRequestedSize;
			}
			// Copy the old data and add extra slots up to the requested new capacity
			m_integers = Arrays.copyOf(m_integers, nNewCapacity);
		}
	}

	public static void quickSort(int[] naData, int nFirst, int nLast, IntComparator comparator)
	{
		int nPivotIndex = 0;
		if (nFirst < nLast)
		{
			nPivotIndex = partition(naData, nFirst, nLast, comparator);
			quickSort(naData, nFirst, nPivotIndex - 1, comparator);
			quickSort(naData, nPivotIndex + 1, nLast, comparator);
		}
	}

	private static int partition(int[] naData, int nFirst, int nLast, IntComparator comparator)
	{
		int nUp, nDown;
		int nPivot = naData[nFirst];
		nUp = nFirst;
		nDown = nLast;
		do
		{
			while (comparator.compare(naData[nUp], nPivot) <= 0 && nUp < nLast)
			{
				nUp++;
			}
			while (comparator.compare(naData[nDown], nPivot) > 0 && nDown > nFirst)
			{
				nDown--;
			}
			if (nUp < nDown)
			{
				int n = naData[nUp];
				naData[nUp] = naData[nDown];
				naData[nDown] = n;
			}
		} while (nDown > nUp);
		naData[nFirst] = naData[nDown];
		naData[nDown] = nPivot;
		return nDown;
	}

	public static int binarySearch(int[] naData, int nFirst, int nLast, int nKey, IntComparator comparator)
	{
		int nLow = nFirst;
		int nHigh = nLast - 1;

		while (nLow <= nHigh)
		{
			int nMid = (nLow + nHigh) >>> 1;
			int nMidVal = naData[nMid];

			int nComparison = comparator.compare(nMidVal, nKey);
			if (nComparison < 0)
			{
				nLow = nMid + 1;
			}
			else if (nComparison > 0)
			{
				nHigh = nMid - 1;
			}
			else return nMid; // key found
		}
		return -(nLow + 1); // key not found.
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + m_size;
		result = prime * result + Arrays.hashCode(m_integers);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		IntArrayList other = (IntArrayList) obj;
		if (m_size != other.m_size) return false;
		if (!Arrays.equals(m_integers, other.m_integers)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "IntArrayList: " + m_size + " elements";
	}

	public Iterator getIterator()
	{
		return new Iterator();
	}

	public class Iterator
	{
		int m_cursor;
		int m_lastRet = -1; // index of last element returned; -1 if no such

		public boolean hasNext()
		{
			return m_cursor != m_size;
		}

		public boolean hasPrevious()
		{
			return m_cursor != 0;
		}

		public int getNextIndex()
		{
			return m_cursor;
		}

		public int getPreviousIndex()
		{
			return m_cursor - 1;
		}

		public int getNext()
		{
			int i = m_cursor;
			if (i >= m_size) throw new NoSuchElementException();
			if (i >= m_integers.length) throw new ConcurrentModificationException();
			m_cursor = i + 1;
			m_lastRet = i;
			return m_integers[i];
		}

		public void remove()
		{
			if (m_lastRet < 0) throw new IllegalStateException();

			IntArrayList.this.removeAt(m_lastRet);
			m_cursor = m_lastRet;
			m_lastRet = -1;
		}

		public int getPrevious()
		{
			int i = m_cursor - 1;
			if (i < 0) throw new NoSuchElementException();
			if (i >= m_integers.length) throw new ConcurrentModificationException();
			m_cursor = i;
			m_lastRet = i;
			return m_integers[i];
		}

		public void set(int nValue)
		{
			if (m_lastRet < 0) throw new IllegalStateException();

			IntArrayList.this.set(m_lastRet, nValue);
		}

		public void add(int nValue)
		{
			int i = m_cursor;
			IntArrayList.this.addAt(i, nValue);
			m_cursor = i + 1;
			m_lastRet = -1;
		}
	}
}
