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
public class Line extends BasePath
{
	private static final long serialVersionUID = 1L;

	// One end of the line is the origin of this path

	/** The other end of the line. */
	private PLSVector m_end;


	/** Empty constructor. Coordinates and radius are set to 0, creating a simple point. */
	public Line() {}
	/** Copy constructor. */
	public Line(Line line) { m_origin = line.m_origin.copy(); m_end = line.m_end.copy(); }
	/** Good old constructor. The given points are now owned by the created object. */
	public Line(PLSVector p1, PLSVector p2) { m_origin = p1; m_end = p2; }
	/** 2D constructor. */
	public Line(float x1, float y1, float x2, float y2) { setPoint1(x1, y1); setPoint2(x2, y2); };
	/** 3D constructor. */
	public Line(float x1, float y1, float z1, float x2, float y2, float z2) { setPoint1(x1, y1, z1); setPoint2(x2, y2, z2); };


	// Getters and setters

	public final PLSVector getPoint1() { return m_origin; }
	public final void setPoint1(PLSVector p1) { m_origin = p1; }
	public final void setPoint1(float x, float y) { m_origin = new PLSVector(x, y); }
	public final void setPoint1(float x, float y, float z) { m_origin = new PLSVector(x, y, z); }
	public final PLSVector getPoint2() { return m_end; }
	public final void setPoint2(PLSVector p2) { m_end = p2; }
	public final void setPoint2(float x, float y) { m_end = new PLSVector(x, y); }
	public final void setPoint2(float x, float y, float z) { m_end = new PLSVector(x, y, z); }


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
			m_origin.set(coordinates[0], coordinates[1]);
			break;
		case 3:
			m_origin.set(coordinates[0], coordinates[1], coordinates[2]);
			break;
		case 4:
			m_origin.set(coordinates[0], coordinates[1]);
			m_end.set(coordinates[2], coordinates[3]);
			break;
		case 6:
			m_origin.set(coordinates[0], coordinates[1], coordinates[2]);
			m_end.set(coordinates[3], coordinates[4], coordinates[5]);
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
				m_origin.getX(), m_origin.getY(),
				m_end.getX(), m_end.getY(),
				other.m_origin.getX(), other.m_origin.getY(),
				other.m_end.getX(), other.m_end.getY()
		);
	}

	// GeomPath interface

	@Override
	public boolean isEmpty()
	{
		return m_origin.equals(m_end);
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(m_origin, m_end);
	}

	@Override
	public boolean intersects(float cx, float cy, float w, float h)
	{
		// TODO
		return false;
	}
	@Override
	public boolean intersects(Rectangle r)
	{
		return intersects(r.getLeft(), r.getTop(), r.getWidth(), r.getHeight());
	}


	// Base methods

	@Override
	public String toString()
	{
		if (m_origin.getZ() == 0 && m_end.getZ() == 0)
			return "Line((" + m_origin.getX() + ", " + m_origin.getY() + "), (" + m_end.getX() + ", " + m_end.getY() + "))";
		return "Line((" + m_origin.getX() + ", " + m_origin.getY() + ", " + m_origin.getZ() + "), (" +
			m_end.getX() + ", " + m_end.getY() + ", " + m_end.getZ() + "))";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		if (m_origin.getZ() == 0 && m_end.getZ() == 0)
			return String.format("L[(%.2f, %.2f), (%.2f, %2f))", m_origin.getX(), m_origin.getY(), m_end.getX(), m_end.getY());
		return String.format("L((%.2f, %.2f, %.2f), (%.2f, %2f, %.2f))",
				m_origin.getX(), m_origin.getY(), m_origin.getZ(),
				m_end.getX(), m_end.getY(), m_end.getZ());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_origin == null) ? 0 : m_origin.hashCode());
		result = prime * result + ((m_end == null) ? 0 : m_end.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof Line)) return false;
		Line other = (Line) obj;
		if (m_origin == null)
		{
			if (other.m_origin != null) return false;
		}
		else if (!m_origin.equals(other.m_origin)) return false;
		if (m_end == null)
		{
			if (other.m_end != null) return false;
		}
		else if (!m_end.equals(other.m_end)) return false;
		return true;
	}
}
