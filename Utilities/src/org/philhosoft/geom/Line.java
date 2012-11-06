/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Geometric entities.
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
 * A line, defined by two points.
 *
 * @author PhiLho
 */
public class Line implements Cloneable, java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	/** One end of the line. */
	public PLSVector point1;
	/** The other end of the line. */
	public PLSVector point2;


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


	public final PLSVector getPoint1() { return point1; }
	public final void setPoint1(PLSVector p1) { point1 = p1; }
	public final void setPoint1(float x, float y) { point1 = new PLSVector(x, y); }
	public final void setPoint1(float x, float y, float z) { point1 = new PLSVector(x, y, z); }
	public final PLSVector getPoint2() { return point2; }
	public final void setPoint2(PLSVector p2) { point2 = p2; }
	public final void setPoint2(float x, float y) { point2 = new PLSVector(x, y); }
	public final void setPoint2(float x, float y, float z) { point2 = new PLSVector(x, y, z); }


	/** Creates a new normalized line (length = 1), from the origin toward the positive X axis. */
	public static Line create()
	{
		return new Line(PLSVector.create(), PLSVector.X_AXIS.copy());
	}

	/** Returns a copy of this line. */
	public final Line copy()
	{
		return new Line(this);
	}


	// Base methods

	@Override
	public String toString()
	{
		if (point1.z == 0 && point2.z == 0)
			return "Line[(" + point1.x + ", " + point1.y + "), (" + point2.x + ", " + point2.y + ")]";
		return "Line[(" + point1.x + ", " + point1.y + ", " + point1.z + "), (" + point2.x + ", " + point2.y + ", " + point2.z + ")]";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		if (point1.z == 0 && point2.z == 0)
			return String.format("L[(%.2f, %.2f), (%.2f, %2f)]", point1.x, point1.y, point2.x, point2.y);
		return String.format("L[(%.2f, %.2f, %.2f), (%.2f, %2f, %.2f)]", point1.x, point1.y, point1.z, point2.x, point2.y, point2.z);
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
