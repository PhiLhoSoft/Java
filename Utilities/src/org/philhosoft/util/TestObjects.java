package org.philhosoft.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

/**
 * Class for testing objects (with JUnit),
 * namely their equals() and hashCode() methods,
 * which should follow the Object contract.
 *
 * <blockquote>
 The equals method implements an equivalence relation on non-null object references:
 <ul>
    <li>It is reflexive: for any non-null reference value x, x.equals(x) should return true.
    <li>It is symmetric: for any non-null reference values x and y,
        x.equals(y) should return true if and only if y.equals(x) returns true.
    <li>It is transitive: for any non-null reference values x, y, and z,
        if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
    <li>It is consistent: for any non-null reference values x and y,
        multiple invocations of x.equals(y) consistently return true or consistently return false,
        provided no information used in equals comparisons on the objects is modified.
    <li>For any non-null reference value x, x.equals(null) should return false.
 </ul>
 * </blockquote>
 * See also the <a href="http://www.artima.com/lejava/articles/equality.html">How to Write an Equality Method in Java</a> article.
 */
public class TestObjects
{
	private static final int CONSISTENCY_REPEATS = 11;

	private TestObjects() {}

	/**
	 * For any non-null reference value x, x.equals(null) should return false.
	 *
	 * @param x
	 */
	public static void assertEqualsReturnFalseOnNull(Object x)
	{
		assertFalse(x.equals(null));
	}

	/**
	 * For any non-null reference value x, x.equals(x) should return true.
	 *
	 * @param x
	 */
	public static void assertEqualsIsReflexive(Object x)
	{
		assertTrue(x.equals(x));
	}

	/**
	 * Test that x.equals(y) where x and y are the same instance works.
	 *
	 * @param x
	 * @param y
	 */
	public static void assertEqualsIsReflexive(Object x, Object y)
	{
		assumeTrue(x == y);
		assertTrue(x.equals(y));
	}

	/**
	 * For any non-null reference values x and y,
	 * x.equals(y) should return true if and only if y.equals(x) returns true.
	 *
	 * @param x
	 * @param y
	 */
	public static void assertEqualsIsSymmetric(Object x, Object y)
	{
		assumeTrue(y.equals(x));
		assertTrue(x.equals(y));
	}

	/**
	 * For any non-null reference values x, y, and z,
	 * if x.equals(y) returns true and y.equals(z) returns true,
	 * then x.equals(z) should return true.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void assertEqualsIsTransitive(Object x, Object y, Object z)
	{
		assumeTrue(x.equals(y) && y.equals(z));
		assertTrue(z.equals(x));
	}

	/**
	 * For any non-null reference values x and y,
	 * multiple invocations of x.equals(y) consistently return true or consistently return false,
	 * provided no information used in equals comparisons on the objects is modified.
	 *
	 * @param x
	 * @param y
	 */
	public static void assertEqualsIsConsistent(Object x, Object y)
	{
		boolean alwaysTheSame = x.equals(y);

		for (int i = 0; i < CONSISTENCY_REPEATS; i++)
		{
			assertEquals(alwaysTheSame, x.equals(y));
		}
	}

	/**
	 * Whenever it is invoked on the same object more than once
	 * the hashCode() method must consistently return the same integer.
	 *
	 * @param x
	 */
	public static void assertHashCodeIsSelfConsistent(Object x)
	{
		int alwaysTheSame = x.hashCode();

		for (int i = 0; i < CONSISTENCY_REPEATS; i++)
		{
			assertEquals(alwaysTheSame, x.hashCode());
		}
	}

	/**
	 * If two objects are equal according to the equals() method,
	 * then the hashCode() method on these objects must produce the same integer.
	 *
	 * @param x
	 * @param y
	 */
	public static void assertHashCodeIsConsistentWithEquals(Object x, Object y)
	{
		assumeTrue(x.equals(y));
		assertEquals(x.hashCode(), y.hashCode());
	}

	/**
	 * Test consistency of equals() and hashCode().
	 *
	 * @param a1  an object.
	 * @param a2  an object, equal to the first parameter but a different instance.
	 * @param a3  an object, equal to the first parameter but a different instance of the first two parameters.
	 * @param b   an object, different (meaning equals != true) from the first parameter.
	 */
	public static <T> void checkEqualsAndHashCode(T a1, T a2, T a3, T b)
	{
		assumeTrue(!(a1 instanceof String));

		// Base checks of the contract of the test
		assertFalse(a1.equals(b));
		assertEquals(a1, a2);
		assertEquals(a1, a3);

		// The full tests
		assertFalse(a1.equals("")); // An object is different from another object of different class.
		assertEqualsReturnFalseOnNull(a1);
		assertEqualsIsReflexive(a1);
		assertEqualsIsReflexive(a1, a1);
		assertEqualsIsSymmetric(a1, a2);
		assertEqualsIsTransitive(a1, a2, a3);
		assertEqualsIsConsistent(a1, a2);
		assertEqualsIsConsistent(a1, b);
		assertHashCodeIsSelfConsistent(a1);
		assertHashCodeIsConsistentWithEquals(a1, a2);
	}
}
