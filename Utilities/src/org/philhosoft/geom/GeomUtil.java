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

	public static boolean areAlmostEqual(float f1, float f2)
	{
		return Math.abs(f1 - f2) < FEPSILON;
	}
	public static boolean isAlmostEqual(double d1, double d2)
	{
		return Math.abs(d1 - d2) < DEPSILON;
	}


	/** Rotates of the given angle, for coordinates a and b. Returns the rotated a and b in an array. */
	public static double[] rotate(double angle, double a, double b)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		final double newX = a * cos - b * sin;
		final double y = a * sin + b * cos;
		final double x = newX;
		return new double[] { x, y };
	}
	/** Computes the squared distance between the two 2D points defined by their coordinates. */
	public static double squaredDistance(double x1, double y1, double x2, double y2)
	{
		final double dx = x1 - x2;
		final double dy = y1 - y2;
		return dx * dx + dy * dy;
	}
	/** Computes the squared distance between the two 3D points defined by their coordinates. */
	public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		final double dz = z1 - z2;
		return squaredDistance(x1, y1, x2, y2) + dz * dz;
	}
	/** Computes the squared length of the given 2D vector defined by its coordinates. */
	public static double squaredLength(double x, double y)
	{
		return x * x + y * y;
	}
	/** Computes the squared length of the given 3D vector defined by its coordinates. */
	public static double squaredLength(double x, double y, double z)
	{
		return squaredLength(x, y) + z * z;
	}
	/** Computes the dot product of the given 2D vector defined by its coordinates. */
	public static double dot(double x1, double y1, double x2, double y2)
	{
		final double dsx = x1 * x2;
		final double dsy = y1 * y2;
		return dsx + dsy;
	}
	/** Computes the dot product of the given 3D vector defined by its coordinates. */
	public static double dot(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		final double dsz = z1 * z2;
		return dot(x1, y1, x2, y2) + dsz;
	}
	/** Checks if two lines defined by their end points intersect. */
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
	/** Checks if a line segment defined by its end points and a circle defined by its center and radius intersect. */
	public static boolean isLineIntersectingCircle(
			// See above for s vs. l
			double sx1, double sy1, double sx2, double sy2,
			double cx, double cy, double radius)
	{
		return isLineIntersectingCircle(sx1, sy1, sx2, sy2, cx, cy, radius, null);
	}
	public static boolean isLineIntersectingCircle(
			// See above for s vs. l
			double sx1, double sy1, double sx2, double sy2,
			double cx, double cy, double radius,
			double[] results)
	{
		if (results != null)
		{
			if (results.length != 4)
				throw new IllegalArgumentException("isLineIntersectingCircle must be called with an array of size 4");
			results[0] = results[1] = results[2] = results[3] = Double.NaN;
		}
		// http://stackoverflow.com/questions/1073336/circle-line-collision-detection
		// http://stackoverflow.com/questions/13053061/circle-line-intersection-points
		// Direction vector of the segment, ie. segment taken to the origin
		final double vDirSegX = sx2 - sx1;
		final double vDirSegY = sy2 - sy1;
		// Vector from center of circle to the first end of the segment
		final double vCirSegX = cx - sx1;
		final double vCirSegY = cy - sy1;

		// Dot products
		final double a = dot(vDirSegX, vDirSegY, vDirSegX, vDirSegY); // dir.dot(dir)
		final double b = dot(vDirSegX, vDirSegY, vCirSegX, vCirSegY); // dir.dot(circ)
		final double c = dot(vCirSegX, vCirSegY, vCirSegX, vCirSegY) - radius * radius; // circ.dot(circ)

		final double discriminant = b * b - a * c;
		if (discriminant < 0)
			return false;

		final double rootDisc = Math.sqrt(discriminant);
		// These factors tells "how far on the segment" the intersection points are:
		// must be between 0 and 1 to be on the segment.
		// If discriminant is zero, both factors are the same (only one point of intersection)
		final double factor1 = (-b + rootDisc) / a;
		final double factor2 = (-b - rootDisc) / a;

		if (results != null)
		{
			results[0] = sx1 - vDirSegX * factor1;
			results[1] = sy1 - vDirSegY * factor1;
			if (discriminant > 0)
			{
				results[2] = sx1 - vDirSegX * factor2;
				results[3] = sy1 - vDirSegY * factor2;
			}
		}
		return true;
	}
}
