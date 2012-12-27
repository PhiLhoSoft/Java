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
public class GeomUtil
{
	// To move to a MathUtil class...
	public static final float FEPSILON = 1.192093e-07f;
	public static final double DEPSILON = 2.220446e-16;

	private GeomUtil() {} // Only functions, no instance

	public static boolean areAlmostEqual(float f1, float f2)
	{
		return Math.abs(f1 - f2) < FEPSILON;
	}
	public static boolean areAlmostEqual(double d1, double d2)
	{
		return Math.abs(d1 - d2) < DEPSILON;
	}


	/** Rotates of the given angle, for coordinates a and b. Returns the rotated a and b in an array. */
	public static double[] rotate(double angle, double a, double b)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		double newX = a * cos - b * sin;
		double y = a * sin + b * cos;
		double x = newX;
		return new double[] { x, y };
	}
	/** Computes the squared distance between the two 2D points defined by their coordinates. */
	public static double squaredDistance(double x1, double y1, double x2, double y2)
	{
		double dx = x1 - x2;
		double dy = y1 - y2;
		return dx * dx + dy * dy;
	}
	/** Computes the squared distance between the two 3D points defined by their coordinates. */
	public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		double dz = z1 - z2;
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
		double dsx = x1 * x2;
		double dsy = y1 * y2;
		return dsx + dsy;
	}
	/** Computes the dot product of the given 3D vector defined by its coordinates. */
	public static double dot(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		double dsz = z1 * z2;
		return dot(x1, y1, x2, y2) + dsz;
	}

	/**
	 * Checks if two lines defined by their end points intersect.
	 *
	 * @param s1x1  X coordinate of the first point of the first line segment
	 * @param s1y1  Y coordinate of the first point of the first line segment
	 * @param s1x2  X coordinate of the second point of the first line segment
	 * @param s1y2  Y coordinate of the second point of the first line segment
	 * @param s2x1  X coordinate of the first point of the second line segment
	 * @param s2y1  Y coordinate of the first point of the second line segment
	 * @param s2x2  X coordinate of the second point of the second line segment
	 * @param s2y2  Y coordinate of the second point of the second line segment
	 * @return true if they intersect
	 */
	public static boolean areLinesIntersecting(
			// For readability sake, I use s1 / s2 (line segment!) instead of l1 / l2...
			double s1x1, double s1y1, double s1x2, double s1y2,
			double s2x1, double s2y1, double s2x2, double s2y2)
	{
		// Paul Bourke's formulae... I won't try to understand it for now...
		double denominator = (s2y2 - s2y1) * (s1x2 - s1x1) - (s2x2 - s2x1) * (s1y2 - s1y1);
		double numeratorA  = (s2x2 - s2x1) * (s1y1 - s2y1) - (s2y2 - s2y1) * (s1x1 - s2x1);
		double numeratorB  = (s1x2 - s1x1) * (s1y1 - s2y1) - (s1y2 - s1y1) * (s1x1 - s2x1);

		// If the lines are coincident, ie. parallel and overlapping, they intersect
		if (Math.abs(denominator) < DEPSILON && // Parallel
				Math.abs(numeratorA) < DEPSILON &&
				Math.abs(numeratorB) < DEPSILON)
			return true;
		// If they are just parallel (not overlapping), they cannot intersect
		if (Math.abs(denominator) < DEPSILON)
			return false;

		double muA = numeratorA / denominator;
		double muB = numeratorB / denominator;
		return muA >= 0 && muA <= 1 && muB >= 0 && muB <= 1;
	}

	/**
	 * Checks if a line segment defined by its end points and a circle defined by its center and radius intersect.
	 *
	 * @param sx1  X coordinate of the first point of the line
	 * @param sy1  Y coordinate of the first point of the line
	 * @param sx2  X coordinate of the second point of the line
	 * @param sy2  Y coordinate of the second point of the line
	 * @param cx  X coordinate of the center of the circle
	 * @param cy  Y coordinate of the center of the circle
	 * @param radius  radius of the circle
	 * @return true if they intersect
	 */
	public static boolean isLineIntersectingCircle(
			// See above for s vs. l
			double sx1, double sy1, double sx2, double sy2,
			double cx, double cy, double radius)
	{
		return getLineCircleIntersectionPoints(sx1, sy1, sx2, sy2, cx, cy, radius, null) > 0;
	}
	/**
	 * Checks if the given line (defined by its two end points) intersects the given circle
	 * (defined by its center and its radius).
	 * If so, the intersection points can be put in the {@code results} array which must have a size of 4.
	 *
	 * @param sx1  X coordinate of the first point of the line
	 * @param sy1  Y coordinate of the first point of the line
	 * @param sx2  X coordinate of the second point of the line
	 * @param sy2  Y coordinate of the second point of the line
	 * @param cx  X coordinate of the center of the circle
	 * @param cy  Y coordinate of the center of the circle
	 * @param radius  radius of the circle
	 * @param results  an array of four floats, or null. If null, the parameter is ignored.
	 *        Otherwise, if there is only one intersection point, its coordinates are in the first two entries,
	 *        the two others are set to NaN. If there are two points, their coordinates (in X, Y order) are put
	 *        in the array.
	 * @return the number of intersection points: 0 if no intersection, 1 if tangent, 2 otherwise
	 */
	public static int getLineCircleIntersectionPoints(
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
		double vDirSegX = sx2 - sx1;
		double vDirSegY = sy2 - sy1;
		// Vector from center of circle to the first end of the segment
		double vCirSegX = cx - sx1;
		double vCirSegY = cy - sy1;

		// Dot products
		double a = dot(vDirSegX, vDirSegY, vDirSegX, vDirSegY); // dir.dot(dir)
		double b = dot(vDirSegX, vDirSegY, vCirSegX, vCirSegY); // dir.dot(circ)
		double c = dot(vCirSegX, vCirSegY, vCirSegX, vCirSegY) - radius * radius; // circ.dot(circ)

		double discriminant = b * b - a * c;
		if (discriminant < 0)
			return 0; // No intersection

		double rootDisc = Math.sqrt(discriminant);
		// These factors tells "how far on the segment" the intersection points are:
		// must be between 0 and 1 to be on the segment.
		// If discriminant is zero, both factors are the same (only one point of intersection)
		double factor1 = (-b + rootDisc) / a;
		double factor2 = (-b - rootDisc) / a;

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
		return discriminant > 0 ? 2 : 1;
	}
}
