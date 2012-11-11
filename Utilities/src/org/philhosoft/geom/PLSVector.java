/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * A 2D or 3D vector, holding a pair or triplet of coordinates.
 *
 * I could make a 2D version, but even with millions of vectors I am not sure the gain of memory is worth the trouble...
 * (of maintaining two almost identical classes).
 * To compensate, I provide versions of the methods with two parameters (coordinates)
 * instead of three, allowing more natural 2D calls.
 *
 * Prefixing a class name to make it unique might seem old fashioned, but given the Java SE's Vector,
 * not to mention mixing with other geometric libraries, it seems better than using long package
 * prefixes in code...
 *
 * Reference: http://en.wikipedia.org/wiki/Euclidean_vector
 * Took ideas from various vector implementations, including Processing's PVector and toxiclibs' Vec3D.
 */
/* File history:
 *  0.03.000 -- 2012/11/06 (PL) -- Finally make the fields private...
 *  0.02.000 -- 2012/11/05 (PL) -- Extending the set of methods.
 *  0.01.000 -- 2009/09/29 (PL) -- Creation.
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009-2012 Philippe Lhoste / PhiLhoSoft
*/
// Sometime, I break with my rule of starting method names with a verb...
// Mostly for historical reasons, like length() in Java tradition or dot() in vector library tradition...

package org.philhosoft.geom;



/**
 * A 2D or 3D vector, holding a pair or triplet of coordinates.
 * <p>
 * This class can be seen as a vector with one point at the origin and the other end at
 * the given coordinates. Or just a convenient class to hold a set of coordinates (a point).
 * Or some vectorial quantity, like speed, acceleration, etc.
 * <p>
 * It provides methods with two coordinates instead of three to allow a more natural usage in 2D,
 * you can assume the z coordinate is 0 in these cases.
 * 2D methods, on the X-Y plane have no suffix (kind of default).
 * 3D methods have the 3D suffix (unless the Z parameter makes it unambiguous)
 * or a suffix indicating the implied plane (XZ or YZ).
 * <p>
 * Note that unlike the traditional mathematical definition, the Y axis goes toward the bottom,
 * for consistency with the traditional display system of coordinates.<br>
 * Thus, the angles first go down (toward North at PI / 2), in clockwise manner.<br>
 * The Z axis goes toward the viewer, ie. positive values are closer of the point-of-view.
 * That's the left-handed coordinate system.
 * <p>
 * Most operations return the vector itself, allowing to chain the calls.
 * <p>
 * It uses float instead of double: we probably don't need the extra precision of double in most graphics applications,
 * and it can save quite a bit of memory with lot of points!
 * Most computations are done using doubles internally, though, to reduce the risk of rounding errors and overflows.
 * <p>
 * Nearly all methods are final, as there is little need to override the math methods,
 * except for some specific entry points, like the random number generator.
 *
 * @author PhiLho
 */
public class PLSVector implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	/** The coordinate on the X axis. */
	private float x;
	/** The coordinate on the Y axis. */
	private float y;
	/** The coordinate on the Z axis, zero if we are in 2D. */
	private float z;

	// Beware! These vectors are NOT immutable... Avoid to alter them! Use them with copy(), for example.
	/** The canonical X axis, from the origin to the right / East. */
	public static final PLSVector X_AXIS = new PLSVector(1, 0, 0);
	/** The canonical Y axis, from the origin to the bottom / South. */
	public static final PLSVector Y_AXIS = new PLSVector(0, 1, 0);
	/** The canonical Z axis, from the origin to the viewer / point-of-view. */
	public static final PLSVector Z_AXIS = new PLSVector(0, 0, 1);
	// Extreme vectors useful for bounding box computations
	public static final PLSVector MINIMUM = new PLSVector(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
	public static final PLSVector MAXIMUM = new PLSVector(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

	/** Empty constructor. Coordinates are set to 0, creating a null vector. */
	public PLSVector() {}
	/** Copy constructor. */
	public PLSVector(PLSVector v) { set(v); }
	/** Good old constructor. */
	public PLSVector(float px, float py, float pz) { set(px, py, pz); }
	/** 2D constructor. */
	public PLSVector(float px, float py) { set(px, py); }
	/** Convenience constructor from an array of points (easier to define literally). */
	public PLSVector(float[] coordinates) { set(coordinates); }

	// Getters and setters

	public final float getX() { return x; }
	public final void setX(float px) { x = px; }
	public final float getY() { return y; }
	public final void setY(float py) { y = py; }
	public final float getZ() { return z; }
	public final void setZ(float pz) { z = pz; }

	/** Returns a copy of this vector. */
	public final PLSVector copy()
	{
		return new PLSVector(this);
	}

	// Some static PLSVector generators

	/** Creates normalized vector (length = 1) toward the X axis. */
	public static PLSVector create()
	{
		return new PLSVector(1, 0, 0);
	}
	/** Creates a normalized 2D vector toward the given angle. */
	public static PLSVector create(float angle)
	{
		return create().setAngle(angle);
	}
	/** Creates a normalized 2D vector toward a random angle. */
	public static PLSVector createRandom()
	{
		return create().setRandom();
	}
	/** Creates a normalized 3D vector toward a random angle. */
	public static PLSVector createRandom3D()
	{
		return create().setRandom3D();
	}
	/** Creates a normalized 2D vector toward a random angle in the given range. */
	public static PLSVector createRandomAngle(float angle1, float angle2)
	{
		return create().setRandomAngle(angle1, angle2);
	}
	/** Creates a random 2D point in the given area. */
	public static PLSVector createRandomPoint(float maxX, float maxY)
	{
		return create().setRandomPoint(maxX, maxY);
	}
	/** Creates a random 3D point in the given area. */
	public static PLSVector createRandomPoint(float maxX, float maxY, float maxZ)
	{
		return create().setRandomPoint(maxX, maxY, maxZ);
	}
	/** Creates a random 2D point in the given area. */
	public static PLSVector createRandomPoint(float minX, float maxX, float minY, float maxY)
	{
		return create().setRandomPoint(maxX, maxY);
	}
	/** Creates a random 3D point in the given area. */
	public static PLSVector createRandomPoint(float minX, float maxX, float minY, float maxY, float minZ, float maxZ)
	{
		return create().setRandomPoint(minX, maxX, minY, maxY, minZ, maxZ);
	}

	/** Sets the coordinates of the end point in 3 dimensions. */
	public final PLSVector set(float px, float py, float pz)
	{
		x = px;
		y = py;
		z = pz;
		return this;
	}
	/** Sets the coordinates of the end point in 2 dimensions. */
	public final PLSVector set(float px, float py)
	{
		x = px;
		y = py;
		return this;
	}
	/** Sets the coordinates of the end point to be the same of the given vector. */
	public final PLSVector set(PLSVector v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}
	/**
	 * Sets the coordinates of the end point from the given array of coordinates.
	 * <p>
	 * If null or empty, changes nothing.<br>
	 * If one coordinates, changes only x. If two, changes x and y. If three, changes all values.
	 * Extra values are ignored.
	 */
	public final PLSVector set(float[] coordinates)
	{
		if (coordinates == null) // Accepts bad input with a default result, here changes nothing!
			return this;
		switch (coordinates.length)
		{
		case 0:
			break;
		case 1:
			x = coordinates[0];
			break;
		case 2:
			x = coordinates[0];
			y = coordinates[1];
			break;
		default:
			x = coordinates[0];
			y = coordinates[1];
			z = coordinates[2];
			// Ignore other values, if any
			break;
		}
		return this;
	}

	/** Rotates the vector of the given angle in 2D (X-Y plane), in radians. */
	public final PLSVector rotate(float angle)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		final float newX = (float) (x * cos - y * sin);
		y = (float) (x * sin + y * cos);
		x = newX;
		return this;
	}
	/** Rotates the vector of the given angle in the Y-Z plane, in radians. */
	public final PLSVector rotateYZ(float angle)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		final float newY = (float) (y * cos - z * sin);
		z = (float) (y * sin + z * cos);
		y = newY;
		return this;
	}
	/** Rotates the vector of the given angle in the X-Z plane, in radians. */
	public final PLSVector rotateXZ(float angle)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		final float newX = (float) (x * cos - z * sin);
		z = (float) (x * sin + z * cos);
		x = newX;
		return this;
	}

	/** Sets the vector toward the given angle (in radians) in the X-Y plane, keeping the same length. */
	public final PLSVector setAngle(float angle)
	{
		final float len = length();
		return set((float) (len * Math.cos(angle)), (float) (len * Math.sin(angle)));
	}
	/** Sets the vector toward the given angle (in radians) in the Y-Z plane, keeping the same length. */
	public final PLSVector setAngleYZ(float angle)
	{
		final float len = length();
		return set(x, (float) (len * Math.cos(angle)), (float) (len * Math.sin(angle)));
	}
	/** Sets the vector toward the given angle (in radians) in the X-Z plane, keeping the same length. */
	public final PLSVector setAngleXZ(float angle)
	{
		final float len = length();
		return set((float) (len * Math.cos(angle)), y, (float) (len * Math.sin(angle)));
	}

	/** Sets the vector toward a random angle in the X-Y plane, keeping the same length. */
	public final PLSVector setRandom()
	{
		return setRandomAngle(0, (float) (2 * Math.PI));
	}
	/** Sets the vector toward a random angle in the 3D space, keeping the same length. */
	public final PLSVector setRandom3D()
	{
		final float len = length();
		final float xyAngle = getRandom(0, (float) (2 * Math.PI));
		final float yzAngle = getRandom(0, (float) (2 * Math.PI));
		// TODO check the math!
		return set((float) (len * Math.cos(xyAngle)), (float) (len * Math.sin(xyAngle) * Math.cos(yzAngle)), (float) (len * Math.sin(xyAngle)));
	}
	/** Sets the vector toward a random angle in the given range (in radians) in the X-Y plane, keeping the same length. */
	public final PLSVector setRandomAngle(float angle1, float angle2)
	{
		return setAngle(getRandom(angle1, angle2));
	}
	/** Sets the vector to a random end point the given 2D area. */
	public final PLSVector setRandomPoint(float minX, float maxX, float minY, float maxY)
	{
		return set(getRandom(minX, maxX), getRandom(minY, maxY));
	}
	/** Sets the vector to a random end point in the given 3D area. */
	public final PLSVector setRandomPoint(float minX, float maxX, float minY, float maxY, float minZ, float maxZ)
	{
		return set(getRandom(minX, maxX), getRandom(minY, maxY), getRandom(minZ, maxZ));
	}
	/** Sets the vector to a random end point in the given 2D area. */
	public final PLSVector setRandomPoint(float maxX, float maxY)
	{
		return setRandomPoint(0, maxX, 0, maxY);
	}
	/** Sets the vector to a random end point the given 3D area. */
	public final PLSVector setRandomPoint(float maxX, float maxY, float maxZ)
	{
		return setRandomPoint(0, maxX, 0, maxY, 0, maxZ);
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
		return x == 0 && y == 0 && z == 0;
	}
	/** Each coordinate of this vector becomes the minimum between this coordinate and the same coordinate in the given vector. */
	public final PLSVector min(PLSVector v)
	{
		x = Math.min(x, v.x);
		y = Math.min(y, v.y);
		z = Math.min(z, v.z);
		return this;
	}
	/** Each coordinate of this vector becomes the maximum between this coordinate and the same coordinate in the given vector. */
	public final PLSVector max(PLSVector v)
	{
		x = Math.max(x, v.x);
		y = Math.max(y, v.y);
		z = Math.max(z, v.z);
		return this;
	}

	/** Magnitude of the vector (same as {@link #length()}). */
	public final float magnitude() { return length(); }
	/** Length of the vector. */
	public final float length()
	{
		return (float) dblLen();
	}
	/** Squared length of the vector. Can be used to compare vectors as it is faster than {@link #length()}. */
	public final float sqLength()
	{
		// Since we aim at speed, we don't use dblSqLen
		return x * x + y * y + z * z;
	}

	// Do internal computing with doubles to reduce the risk of bad rounding or overflow
	private double dblLen()
	{
		return Math.sqrt(GeomUtil.squaredLength(x, y, z));
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
	// I don't provide the same for scalar parameters... Not orthogonal but feels a bit pointless, as we can negate them.

	/** Scalar multiplication. */
	public final PLSVector multiply(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	/** Scalar division. */
	// A bit more elegant than multiply(1.0/scalar)...
	public final PLSVector divide(float scalar)
	{
		x /= scalar;
		y /= scalar;
		z /= scalar;
		return this;
	}

	private PLSVector multiply(double scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}


	/** Normalizes the vector to length 1 (unit vector). */
	public final PLSVector normalize()
	{
		final double len = dblLen();
		if (len > 0)
		{
			multiply(1.0 / len);
		}
		return this;
	}
	/** Limits the length of the vector to the given maximum length. */
	public final PLSVector limit(float maxLen)
	{
		final double len = dblLen();
		if (len > maxLen)
		{
			multiply(maxLen / len);
		}
		return this;
	}
	/** Sets the length of the vector to the given length. */
	public final PLSVector setLength(float newLen)
	{
		final double len = dblLen();
		return multiply(newLen / len);
	}

	/**
	 * Returns the dot product with another vector.
	 * Also known as inner product or scalar product.
	 * @see #angleWith(PLSVector)
	 */
	public final float dot(PLSVector v)
	{
		return (float) GeomUtil.dot(x, y, z, v.x, v.y, v.z);
	}
	/**
	 * Returns a new vector composed of the cross product between this vector and the given vector.
	 * Also known as outer product or vector product.
	 * Produces a vector perpendicular to both vectors (pointing up or down with regard
	 * to the plane defined by the two vectors).
	 */
	public final PLSVector cross(PLSVector v)
	{
		return new PLSVector(
				y * v.z - z * v.y,
				z * v.x - x * v.z,
				x * v.y - y * v.x
		);
	}
	/**
	 * Inverts this vector, ie. inverts the sign of its coordinates.
	 */
	public final PLSVector invert()
	{
		return multiply(-1);
	}
	/**
	 * Transforms this vector to one of its perpendiculars in 2D.
	 * For the other, use {@link #invert()}.
	 */
	public final PLSVector perpendicular()
	{
		float t = x;
		x = -y;
		y = t;
		return this;
	}

	/** Computes the distance between this point and the given one. */
	public final float distance(PLSVector v)
	{
		return (float) Math.sqrt(GeomUtil.squaredDistance(x, y, z, v.x, v.y, v.z));
	}
	/**
	 * Computes the squared distance between this point and the given one.
	 * Useful for quick comparisons of distances without the costly square root.
	 */
	public final float sqDistance(PLSVector v)
	{
		// Since we aim at speed, we don't use dblSqDist
		final float dx = x - v.x;
		final float dy = y - v.y;
		final float dz = z - v.z;
		return dx * dx + dy * dy + dz * dz;
	}

	/**
	 * Returns the angle between this vector and the given one.
	 * Always between 0 and 180 degrees (PI radians).
	 */
	public final float angleWith(PLSVector v)
	{
		if (isNull() || v.isNull())
			return 0;
		// One usage of dot product...
		final double dot = GeomUtil.dot(x, y, z, v.x, v.y, v.z);
		final double val = dot / dblLen() / v.dblLen();
		// Avoid NaN if we have some rounding error...
		if (val <= -1)
			return (float) Math.PI;
		if (val >= 1)
			return 0;
		return (float) Math.acos(val);
	}

	private final float getAngle(float a, float b)
	{
		double angle = Math.atan2(a, b);
		if (angle < 0)
		{
			angle += Math.PI * 2;
		}
		return (float) angle;
	}

	/**
	 * Returns the vector's direction (angle in radians) in 2D, ie. in the X-Y plane.
	 * The positive X axis equals 0 degrees / radians,
	 * the positive Y axis (toward the South) equals 90 degrees / half-pi radians,
	 * the negative X axis (toward the West) equals 180 degrees, pi radians,
	 * the negative Y axis (toward the North) equals 270 degrees, pi * 3/2 radians.
	 */
	public final float heading()
	{
		return getAngle(y, x);
	}

	/**
	 * Returns the vector's direction (angle in radians) in 3D, in the X-Z plane.
	 * The positive X axis equals 0 degrees / radians.
	 */
	public final float headingXZ()
	{
		return getAngle(z, x);
	}

	/**
	 * Returns the vector's direction (angle in radians) in 3D, in the Y-Z plane.
	 * The positive Z axis equals 0 degrees / radians.
	 */
	public final float headingYZ()
	{
		return getAngle(y, z);
	}


	/**
	 * Tells if this point is within the given circle, defined by its center and its radius.
	 * <p>
	 * If lot of points must be checked against the same circle, {@link Circle#contains(PLSVector)} can be faster.
	 *
	 * @param center  the center of the circle
	 * @param radius  the radius of the circle
	 * @return true if the point is inside the circle
	 */
	public final boolean isInCircle(PLSVector center, float radius)
	{
		final double sqR = GeomUtil.squaredDistance(x, y, center.x, center.y);
		return sqR <= radius * radius;
	}
	public final boolean isInSphere(PLSVector center, float radius)
	{
		final double sqR = GeomUtil.squaredDistance(x, y, z, center.x, center.y, center.z);
		return sqR <= radius * radius;
	}


	/**
	 * Linear interpolation.
	 * <p>
	 * Returns a new vector resulting of the interpolation between the current vector, the given one, and an amount.
	 * See {@link #lerp(float, float, float)} for an explanation of amount.
	 */
	public final PLSVector lerp(PLSVector v, float amount)
	{
		PLSVector nv = new PLSVector();
		return lerp(v, amount, nv);
	}
	/**
	 * Linear interpolation.
	 * <p>
	 * Puts in the target vector the interpolation between the current vector, the given one, and an amount.
	 * Allows to get intermediary values without generating lot of temporary objects.
	 * See {@link #lerp(float, float, float)} for an explanation of amount.
	 */
	public final PLSVector lerp(PLSVector v, float amount, PLSVector target)
	{
		final float lx = lerp(x, v.x, amount);
		final float ly = lerp(y, v.y, amount);
		final float lz = lerp(z, v.z, amount);
		return target.set(lx, ly, lz);
	}

	// Base methods

	@Override
	public String toString()
	{
		if (z == 0)
			return "PLSVector(" + x + ", " + y + ")";
		return "PLSVector(" + x + ", " + y + ", " + z + ")";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		if (z == 0)
			return String.format("V(%.2f, %.2f)", x, y);
		return String.format("V(%.2f, %.2f, %.2f)", x, y, z);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof PLSVector)) return false;
		PLSVector other = (PLSVector) obj;
		return x == other.x && y == other.y && z == other.z;
	}
	public boolean almostEquals(PLSVector other)
	{
		if (this == other) return true;
		return GeomUtil.areAlmostEqual(x, other.x) &&
				GeomUtil.areAlmostEqual(y, other.y) &&
				GeomUtil.areAlmostEqual(z, other.z);
	}


	/**
	 * Returns a random number in the given range.
	 * <p>
	 * Public and static as it has a general usefulness...<br>
	 * One might want to override it, to plug in another random number generator.
	 */
	public static float getRandom(float min, float max)
	{
		return lerp(min, max, (float) Math.random());
	}

	/**
	 * Returns the linear interpolation between the first value and the second one of the given amount.
	 * <p>
	 * An amount close of 0.0 is close of this vector, an amount close of 1.0 is close of the given vector,
	 * 0.5 is just in the middle. This method doesn't control if amount is in this range...
	 * <p>
	 * Public and static as it has a general usefulness...
	 */
	public static float lerp(float from, float to, float amount)
	{
		return from + (to - from) * amount;
	}
}
