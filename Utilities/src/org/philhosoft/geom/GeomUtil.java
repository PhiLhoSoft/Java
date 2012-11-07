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
 * But can also be used if one wants to avoid creating objects for doing these commonly used computations.
 * <p>
 * Provides some squared values because it can be faster for some computing needs.
 * Doesn't provide the square root of these values, to avoid making a too large API,
 * and as it is trivial via {@link Math#sqrt(double)},.
 *
 * @author PhiLho
 */
class GeomUtil
{
	// To move to a MathUtil class...
	public static final float FEPSILON = 1.192093e-07f;
	public static final double DEPSILON = 2.220446e-16;

	private GeomUtil() {} // Only functions, no instance

	/** Computes the squared distance between the two 2D points defined by their coordinates. */
	public static double squaredDistance(float x1, float y1, float x2, float y2)
	{
		final double dx = (double) x1 - x2;
		final double dy = (double) y1 - y2;
		return dx * dx + dy * dy;
	}
	/** Computes the squared distance between the two 3D points defined by their coordinates. */
	public static double squaredDistance(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		final double dz = (double) z1 - z2;
		return squaredDistance(x1, y1, x2, y2) + dz * dz;
	}
	/** Computes the squared length of the given 2D vector defined by its coordinates. */
	public static double squaredLength(float x, float y)
	{
		final double dx = x, dy = y;
		return dx * dx + dy * dy;
	}
	/** Computes the squared length of the given 3D vector defined by its coordinates. */
	public static double squaredLength(float x, float y, float z)
	{
		final double dz = z;
		return squaredLength(x, y) + dz * dz;
	}
	/** Computes the dot product of the given 2D vector defined by its coordinates. */
	public static double dot(float x1, float y1, float x2, float y2)
	{
		final double dsx = (double) x1 * x2;
		final double dsy = (double) y1 * y2;
		return dsx + dsy;
	}
	/** Computes the dot product of the given 3D vector defined by its coordinates. */
	public static double dot(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		final double dsz = (double) z1 * z2;
		return dot(x1, y1, x2, y2) + dsz;
	}

	public static boolean areLinesIntersecting(
			// For readability sake, I use s1 / s2 (line segment!) instead of l1 / l2...
			double s1x1, double s1y1, double s1x2, double s1y2,
			double s2x1, double s2y1, double s2x2, double s2y2)
	{
		// Paul Bourke's formulae... I won't try to understand it for now...
		final double denominator = (s2y2 - s2y1) * (s1x2 - s1x1) - (s2x2 - s2x1) * (s1y2 - s1y1);
		final double numeratorA  = (s2x2 - s2x1) * (s1y1 - s2y1) - (s2y2 - s2y1) * (s1x1 - s2x1);
		final double numeratorB  = (s1x2 - s1x1) * (s1y1 - s2y1) - (s1y2 - s1y1) * (s1x1 - s2x1);

		// If the lines are coincident, ie. parallel and overlapping, they intersect
		if (Math.abs(denominator) < DEPSILON && // Parallel
				Math.abs(numeratorA) < DEPSILON &&
				Math.abs(numeratorB) < DEPSILON)
			return true;
		// If they are just parallel (not overlapping), they cannot intersect
		if (Math.abs(denominator) < DEPSILON)
			return false;

		final double muA = numeratorA / denominator;
		final double muB = numeratorB / denominator;
		return muA >= 0 && muA <= 1 && muB >= 0 && muB <= 1;
	}
}
