package org.philhosoft.geom;


import static org.testng.Assert.*;

import java.util.Arrays;

import org.testng.annotations.*;



public class PLSVectorTest
{
	static final float EPSILON = 1e-6f; // We need a higher tolerance than FEPSILON

	@Test
	public void testConstructors()
	{
		PLSVector v = new PLSVector();
		checkResult(v, 0, 0, 0);
		assertTrue(v.isNull());
		assertEquals(v.length(), 0.0f);

		v = new PLSVector(1, 2);
		checkResult(v, 1, 2, 0);
		assertTrue(GeomUtil.isAlmostEqual(v.length(), 2.236068f));

		v = new PLSVector(3, 4, 5);
		checkResult(v, 3, 4, 5);
		float len = v.length();
		assertTrue(GeomUtil.isAlmostEqual(len, 7.071068f));
		v.invert();
		checkResult(v, -3, -4, -5);
		assertEquals(v.length(), len);

		float[][] vectors =
		{
			{ 0, 1 },
			{ 2, 3 },
			{ 4, 5 },
			{ 6, 7, 8 },
			{ 0.9f, 0.10f, 0.11f },
			{ 12 },
			{ 0.12f },
			{ 101, 102, 103, 104 },
			{ 201, 202, 203, 204, 205 },
		};
		for (float[] vector : vectors)
		{
			PLSVector vect = new PLSVector(vector);
			if (vector.length == 1)
			{
				checkResult(vect, vector[0], 0, 0, " for " + Arrays.toString(vector));
			}
			else if (vector.length == 2)
			{
				checkResult(vect, vector[0], vector[1], 0);
			}
			else if (vector.length == 3)
			{
				checkResult(vect, vector[0], vector[1], vector[2]);
			}
		}

		v = new PLSVector((float[]) null);
		checkResult(v, 0, 0, 0);
		v = new PLSVector(new float[] {});
		checkResult(v, 0, 0, 0);
	}

	@Test
	public void testBuilders()
	{
		PLSVector v = PLSVector.create();
		checkResult(v, 1, 0, 0);
		assertEquals(v, PLSVector.X_AXIS);
		assertEquals(v.length(), 1.0f);

		v = PLSVector.create((float) Math.PI / 2);
		checkResult(v, 0, 1, 0);
		assertTrue(v.almostEquals(PLSVector.Y_AXIS));
		assertEquals(v.length(), 1.0f);

		v = PLSVector.create().set(0, 0, 1);
		checkResult(v, 0, 0, 1);
		assertTrue(v.almostEquals(PLSVector.Z_AXIS));
		assertEquals(v.length(), 1.0f);

		PLSVector rv = PLSVector.createRandom();
		assertNotEquals(v, rv);
		assertEquals(v.length(), 1.0f);
	}

	@Test
	public void testAngles()
	{
		PLSVector vPX = PLSVector.create(0);
		assertEquals(Math.abs(vPX.heading()), 0f);
		assertEquals(Math.abs(vPX.heading()), vPX.angleWith(PLSVector.X_AXIS));

		PLSVector vPY = PLSVector.create((float) Math.PI / 2);
		assertEquals(vPY.heading(), (float) Math.PI / 2);
		assertEquals(vPY.heading(), vPY.angleWith(PLSVector.X_AXIS));

		PLSVector vNX = PLSVector.create((float) Math.PI);
		assertEquals(vNX.heading(), (float) Math.PI);
		checkAlmostEqualFloats(vNX.heading(), vNX.angleWith(PLSVector.X_AXIS), "NX");

		PLSVector vNY = PLSVector.create((float) Math.PI * 3 / 2);
		assertEquals(vNY.heading(), (float) Math.PI * 3 / 2);
		checkAlmostEqualFloats(vNY.heading(), (float) Math.PI + vNY.angleWith(PLSVector.X_AXIS), "NY");
	}

	void checkResult(PLSVector v, float x, float y, float z)
	{
		checkResult(v, x, y, z, null);
	}
	void checkResult(PLSVector v, float x, float y, float z, String m)
	{
		checkAlmostEqualFloats(v.getX(), x, "X" + m);
		checkAlmostEqualFloats(v.getY(), y, "Y" + m);
		checkAlmostEqualFloats(v.getZ(), z, "Z" + m);
	}

	void checkAlmostEqualFloats(float f1, float f2, String m)
	{
		assertTrue(Math.abs(f1 - f2) < EPSILON, "Diff on " + m + ": " + Math.abs(f1 - f2));
	}
}
