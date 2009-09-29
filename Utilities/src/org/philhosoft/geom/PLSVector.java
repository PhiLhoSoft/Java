/*
 * A 2D or 3D vector, holding a pair or triplet of coordinates.
 * I could make a 2D version, but even with million of vectors I am not sure the gain of memory
 * is worth the trouble... (of maintening two almost identical classes)
 *
 * Prefixing a class name to make it unique might seem old fashioned, but given the Java SE's Vector,
 * not to mention mixing with other geometric libraries, it seems better than using long package
 * prefixes in code...
 *
 * Reference: http://en.wikipedia.org/wiki/Euclidean_vector
 * Took ideas from various vector implementations, including Processing's PVector and toxic's Vec3D.
 */
/* File history:
 *  1.00.000 -- 2009/09/29 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009 Philippe Lhoste / PhiLhoSoft
*/
// I usually use slightly different code conventions
// (prefixing member variables with m_ (or _s if static), initial capital at method names)
// but for making a public API, I stick to more conventional stuff.

package org.philhosoft.geom;

/**
 * A 2D or 3D vector, holding a pair or triplet of coordinates.
 * Most operations return the vector itself, allowing to chain the calls.
 */
public class PLSVector
{
	// Yes, public, it is faster (at least to type!) and I see little point in adding getters/setters for these
	public float x;
	public float y;
	public float z;

	/** Default constructor. Coordinates are set to 0. */
	public PLSVector() {}
	/** Copy constructor. */
	public PLSVector(PLSVector v) { set(v); }
	/** Good old constructor. */
	public PLSVector(float px, float py, float pz) { set(px, py, pz); }

	public final PLSVector set(float px, float py, float pz)
	{
		x = px;
		y = py;
		z = pz;
		return this;
	}
	public final PLSVector set(PLSVector v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}
	/** Sets the coordinates to zero. */
	public final PLSVector clear()
	{
		x = y = z = 0;
		return this;
	}
	/** Returns a copy of this vector. */
	public final PLSVector copy()
	{
		return new PLSVector(this);
	}

	/** Magnitude of the vector (same as {@link #length()}). */
	public final float magnitude() { return length(); }
	/** Length of the vector. */
	public final float length()
	{
		return Math.sqrt(x*x + y*y + z*z);
	}
	/** Squared length of the vector. Can be used to compare vectors as it is faster than {@link #length()}. */
	public final float sqLength()
	{
		return Math.sqrt(x*x + y*y + z*z);
	}

	public final PLSVector add(float scalar)
	{
		x += scalar;
		y += scalar;
		z += scalar;
		return this;
	}
	public final PLSVector add(PLSVector v)
	{
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	public final PLSVector add(float px, float py, float pz)
	{
		x += px;
		y += py;
		z += pz;
		return this;
	}
	public final PLSVector subtract(PLSVector v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}
	// I don't provide the same for scalar parameters... Not orthogonal but a bit pointless

	public final PLSVector multiply(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	public final PLSVector divide(float scalar)
	{
		x /= scalar;
		y /= scalar;
		z /= scalar;
		return this;
	}

	/**
	 * Normalizes the vector to length 1 (unit vector).
	 */
	public PLSVector normalize()
	{
		float l = length();
		if (l > 0)
		{
			divide(l);
		}
		return this;
	}
	/** Calculates the dot product with another vector. */
	public final float dot(PLSVector v)
	{
		return x*v.x + y*v.y + z*v.z;
	}
	/**
	 * Returns a new vector composed of the cross product between this and given vector.
	 */
	public final PLSVector cross(PLSVector v)
	{
		return new PLSVector(
				x*v.y - y*v.x,
				x*v.z - z*v.x,
				y*v.z - z*v.y
		);
	}

	@Override
	public String toString()
	{
		return new String("V[" + x + ", " + y + ", " + z + "]" );
	}
	/** More compact alternative */
	public String toString2()
	{
		return String.format("V[%.2f, %.2f, %.2f]", x, y, z );
	}
}
