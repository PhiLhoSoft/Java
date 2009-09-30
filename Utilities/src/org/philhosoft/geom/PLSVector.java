/*
 * A 2D or 3D vector, holding a pair or triplet of coordinates.
 *
 * I could make a 2D version, but even with million of vectors I am not sure the gain of memory
 * is worth the trouble... (of maintening two almost identical classes).
 * To compensate, I provide versions of the methods with two parameters (coordinates)
 * instead of threee, allowing more natural 2D calls.
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
// but for making a public API, I stick to more conventional stuff...
// I also broke with my rule of starting method names with a verb...
// Mostly for historical reasons, like length() in Java tradition or dot() in vector library tradition...

package org.philhosoft.geom;

/**
 * A 2D or 3D vector, holding a pair or triplet of coordinates.
 * Most operations return the vector itself, allowing to chain the calls.
 * This can be seen as a vector with one point at the origin and the other end at
 * the given coordinates. Or just a convenient class to hold a set of coordinates (a point).
 * The class provides methods with two coordinates instead of three to allow a more natural
 * usage in 2D, you can assume the z coordinate is 0 in these cases.
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
	/** 2D constructor. */
	public PLSVector(float px, float py) { set(px, py); }

	/** Returns a copy of this vector. */
	public final PLSVector copy()
	{
		return new PLSVector(this);
	}

	public final PLSVector set(float px, float py, float pz)
	{
		x = px;
		y = py;
		z = pz;
		return this;
	}
	public final PLSVector set(float px, float py)
	{
		x = px;
		y = py;
		return this;
	}
	public final PLSVector set(PLSVector v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}
	/** Sets the coordinates to zero (null vector). */
	public final PLSVector clear()
	{
		x = y = z = 0;
		return this;
	}
	/** Checks if this vector is a null vector. */
	public final boolean isNull()
	{
		return x == 0 && y == 0 && z = 0;
	}

	/** Magnitude of the vector (same as {@link #length()}). */
	public final float magnitude() { return length(); }
	/** Length of the vector. */
	public final float length()
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
	/** Squared length of the vector. Can be used to compare vectors as it is faster than {@link #length()}. */
	public final float sqLength()
	{
		return Math.sqrt(x * x + y * y + z * z);
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
	public final PLSVector add(float px, float py)
	{
		x += px;
		y += py;
		return this;
	}
	public final PLSVector subtract(PLSVector v)
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}
	// I don't provide the same for scalar parameters... Not orthogonal but feels a bit pointless

	/** Scalar multiplication. */
	public final PLSVector multiply(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	/** Scalar division. */
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
	/**
	 * Returns the dot product with another vector.
	 * Also known as inner product or scalar product.
	 */
	public final float dot(PLSVector v)
	{
		return x * v.x + y * v.y + z * v.z;
	}
	/**
	 * Returns a new vector composed of the cross product between this and given vector.
	 * Also known as outer product or vector product.
	 * Produces a vector perpendicular to both vectors (pointing up or down with regard
	 * to the plane defined by the two vectors).
	 */
	public final PLSVector cross(PLSVector v)
	{
		return new PLSVector(
				y * v.z - z * v.y,
				z * v.x - x * v.z,
				x * v.y - y * v.x,
		);
	}

	/** Returns the angle between this vector and the given one. */
	public final float angleWith(PLSVector v)
	{
		if (isNull() || v.isNull())
			return 0;
		// One usage of dot product... Perhaps the only one... :-)
		float dot = dot(v);
		return (float) Math.acos(dot / length() / v.length());
	}
	/**
	 * Computes the vector's direction in the XY plane (for example for 2D
	 * points). The positive X axis equals 0 degrees.
	 *
	 * @return rotation angle
	 */
	public final float headingXY() {
		return (float) Math.atan2(y, x);
	}

	/**
	 * Computes the vector's direction in the XZ plane. The positive X axis
	 * equals 0 degrees.
	 *
	 * @return rotation angle
	 */
	public final float headingXZ() {
		return (float) Math.atan2(z, x);
	}

	/**
	 * Computes the vector's direction in the YZ plane. The positive Z axis
	 * equals 0 degrees.
	 *
	 * @return rotation angle
	 */
	public final float headingYZ() {
		return (float) Math.atan2(y, z);
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
