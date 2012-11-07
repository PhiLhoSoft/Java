/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Geometric entities.
 */
/* File history:
 *  0.01.000 -- 2012/11/05 (PL) -- Creation.
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
 * A line, defined by two points.
 *
 * @author PhiLho
 */
public class Line implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	/** One end of the line. */
	private PLSVector point1;
	/** The other end of the line. */
	private PLSVector point2;


	/** Empty constructor. Coordinates and radius are set to 0, creating a simple point. */
	public Line() {}
	/** Copy constructor. */
	public Line(Line line) { point1 = line.point1.copy(); point2 = line.point2.copy(); }
	/** Good old constructor. The given points are now owned by the created object. */
	public Line(PLSVector p1, PLSVector p2) { point1 = p1; point2 = p2; }
	/** 2D constructor. */
	public Line(float x1, float y1, float x2, float y2) { setPoint1(x1, y1); setPoint2(x2, y2); };
	/** 3D constructor. */
	public Line(float x1, float y1, float z1, float x2, float y2, float z2) { setPoint1(x1, y1, z1); setPoint2(x2, y2, z2); };


	// Getters and setters

	public final PLSVector getPoint1() { return point1; }
	public final void setPoint1(PLSVector p1) { point1 = p1; }
	public final void setPoint1(float x, float y) { point1 = new PLSVector(x, y); }
	public final void setPoint1(float x, float y, float z) { point1 = new PLSVector(x, y, z); }
	public final PLSVector getPoint2() { return point2; }
	public final void setPoint2(PLSVector p2) { point2 = p2; }
	public final void setPoint2(float x, float y) { point2 = new PLSVector(x, y); }
	public final void setPoint2(float x, float y, float z) { point2 = new PLSVector(x, y, z); }


	/** Returns a copy of this line. */
	public final Line copy()
	{
		return new Line(this);
	}

	/** Creates a new normalized line (length = 1), from the origin toward the positive X axis. */
	public static Line create()
	{
		return new Line(PLSVector.create(), PLSVector.X_AXIS.copy());
	}
	/**
	 * Creates a line from an array of coordinates.
	 *
	 * @param coordinates  the array of coordinates
	 * @return the created line
	 * @see #set(float[])
	 */
	public static Line create(float[] coordinates)
	{
		return create().set(coordinates);
	}

	/**
	 * Sets the coordinates of the points from the given array of coordinates.
	 * <p>
	 * If null or empty, or not the right number of values, changes nothing.<br>
	 * If 2 coordinates, changes x and y of the first point.
	 * If 3 coordinates, changes x, y and z of the first point.
	 * If 4 coordinates, changes x and y of the two points.
	 * If 6 coordinates, changes x, y and z of the two points.
	 */
	public Line set(float[] coordinates)
	{
		if (coordinates == null) // Accepts bad input with a default result, here changes nothing!
			return this;
		switch (coordinates.length)
		{
		case 2:
			point1.set(coordinates[0], coordinates[1]);
			break;
		case 3:
			point1.set(coordinates[0], coordinates[1], coordinates[2]);
			break;
		case 4:
			point1.set(coordinates[0], coordinates[1]);
			point2.set(coordinates[2], coordinates[3]);
			break;
		case 6:
			point1.set(coordinates[0], coordinates[1], coordinates[2]);
			point2.set(coordinates[3], coordinates[4], coordinates[5]);
			break;
		default:
			// Ignore other values, if any
			break;
		}
		return this;
	}


	/** Checks if this line intersects the given one in the 2D plane. */
	public final boolean intersects(Line other)
	{
		return GeomUtil.areLinesIntersecting(
				point1.getX(), point1.getY(),
				point2.getX(), point2.getY(),
				other.point1.getX(), other.point1.getY(),
				other.point2.getX(), other.point2.getY()
		);
	}

	// Base methods

	@Override
	public String toString()
	{
		if (point1.getZ() == 0 && point2.getZ() == 0)
			return "Line((" + point1.getX() + ", " + point1.getY() + "), (" + point2.getX() + ", " + point2.getY() + "))";
		return "Line((" + point1.getX() + ", " + point1.getY() + ", " + point1.getZ() + "), (" +
			point2.getX() + ", " + point2.getY() + ", " + point2.getZ() + "))";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		if (point1.getZ() == 0 && point2.getZ() == 0)
			return String.format("L[(%.2f, %.2f), (%.2f, %2f))", point1.getX(), point1.getY(), point2.getX(), point2.getY());
		return String.format("L((%.2f, %.2f, %.2f), (%.2f, %2f, %.2f))",
				point1.getX(), point1.getY(), point1.getZ(),
				point2.getX(), point2.getY(), point2.getZ());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point1 == null) ? 0 : point1.hashCode());
		result = prime * result + ((point2 == null) ? 0 : point2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof Line)) return false;
		Line other = (Line) obj;
		if (point1 == null)
		{
			if (other.point1 != null) return false;
		}
		else if (!point1.equals(other.point1)) return false;
		if (point2 == null)
		{
			if (other.point2 != null) return false;
		}
		else if (!point2.equals(other.point2)) return false;
		return true;
	}
}
