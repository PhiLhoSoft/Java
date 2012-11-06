/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Geometry utility.
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

package org.philhosoft.geom;

/**
 * Generic geometry utility functions.
 * <p>
 * Mostly for internal usage, computing values internally as double to avoid bad rounding and potential overflows.
 * But can also be used if one wants to avoid creating objects for doing these computations.
 * <p>
 * Provides some squared values because it can be faster for some computing.
 * Doesn't provide the square root of these values, as it is trivial via {@link Math#sqrt(double)}, to avoid
 * making a tool large API.
 *
 * @author PhiLho
 */
class GeomUtil
{
	private GeomUtil() {} // Only functions, no instance

	/** Computes the squared distance between the two 2D points given by their coordinates. */
	public static double squaredDistance(float x1, float y1, float x2, float y2)
	{
		final double dx = (double) x1 - x2;
		final double dy = (double) y1 - y2;
		return dx * dx + dy * dy;
	}
	/** Computes the squared distance between the two 3D points given by their coordinates. */
	public static double squaredDistance(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		final double dz = (double) z1 - z2;
		return squaredDistance(x1, y1, x2, y2) + dz * dz;
	}
	/** Computes the squared length of the given 2D vector given by its coordinates. */
	public static double squaredLength(float x, float y)
	{
		final double dx = x, dy = y;
		return dx * dx + dy * dy;
	}
	/** Computes the squared length of the given 3D vector given by its coordinates. */
	public static double squaredLength(float x, float y, float z)
	{
		final double dz = z;
		return squaredLength(x, y) + dz * dz;
	}
	/** Computes the dot product of the given 2D vector given by its coordinates. */
	public static double dot(float x1, float y1, float x2, float y2)
	{
		final double dsx = (double) x1 * x2;
		final double dsy = (double) y1 * y2;
		return dsx + dsy;
	}
	/** Computes the dot product of the given 3D vector given by its coordinates. */
	public static double dot(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		final double dsz = (double) z1 * z2;
		return dot(x1, y1, x2, y2) + dsz;
	}
}
