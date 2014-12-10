package org.philhosoft.geom;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;


public class TestPLSVector
{
	static final float EPSILON = 1e-6f; // We need a higher tolerance than FEPSILON

	@Test
	public void testConstructors()
	{
		PLSVector v = new PLSVector();
		checkResult(v, 0, 0, 0);
		assertThat(v.isNull()).isTrue();
		assertThat(v.length()).isEqualTo(0.0f);

		v = new PLSVector(1, 2);
		checkResult(v, 1, 2, 0);
		assertThat(GeomUtil.areAlmostEqual(v.length(), 2.236068f)).isTrue();

		v = new PLSVector(3, 4, 5);
		checkResult(v, 3, 4, 5);
		float len = v.length();
		assertThat(GeomUtil.areAlmostEqual(len, 7.071068f)).isTrue();
		v.invert();
		checkResult(v, -3, -4, -5);
		assertThat(v.length()).isEqualTo(len);

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
		checkResult(v, 1, 1, 1);
		assertThat(v.length()).isEqualTo((float) Math.sqrt(3));

		v = PLSVector.create((float) Math.PI / 2);
		checkResult(v, 0, 1, 0);
		assertThat(v.almostEquals(PLSVector.Y_AXIS)).isTrue();
		assertThat(v.length()).isEqualTo(1.0f);

		v = PLSVector.create().set(0, 0, 1);
		checkResult(v, 0, 0, 1);
		assertThat(v.almostEquals(PLSVector.Z_AXIS)).isTrue();
		assertThat(v.length()).isEqualTo(1.0f);

		PLSVector rv = PLSVector.createRandom();
		assertThat(v).isNotEqualTo(rv);
		assertThat(v.length()).isEqualTo(1.0f);
	}

	@Test
	public void testAngles()
	{
		PLSVector vPX = PLSVector.create(0);
		assertThat(Math.abs(vPX.heading())).isEqualTo(0f);
		assertThat(Math.abs(vPX.heading())).isEqualTo(vPX.angleWith(PLSVector.X_AXIS));

		PLSVector vPY = PLSVector.create((float) Math.PI / 2);
		assertThat(vPY.heading()).isEqualTo((float) Math.PI / 2);
		assertThat(vPY.heading()).isEqualTo(vPY.angleWith(PLSVector.X_AXIS));

		PLSVector vNX = PLSVector.create((float) Math.PI);
		assertThat(vNX.heading()).isEqualTo((float) Math.PI);
		checkAlmostEqualFloats(vNX.heading(), vNX.angleWith(PLSVector.X_AXIS), "NX");

		PLSVector vNY = PLSVector.create((float) Math.PI * 3 / 2);
		assertThat(vNY.heading()).isEqualTo((float) Math.PI * 3 / 2);
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
		assertThat(Math.abs(f1 - f2) < EPSILON).isTrue();
	}
}
