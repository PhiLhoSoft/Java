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
 * A 2D circle, defined by a center and a radius.
 *
 * @author PhiLho
 */
public class Circle extends BasePath implements ClosedShape
{
	private static final long serialVersionUID = 1L;

	// The center of the circle is the origin of this path

	/** The radius of the circle. */
	private float m_radius;

	/** The squared radius, useful for computations. */
	private transient double m_squaredRadius = Double.NaN;


	/** Empty constructor. Coordinates and radius are set to 0, creating a simple point. */
	public Circle() {}
	/** Copy constructor. */
	public Circle(Circle c) { m_origin = c.m_origin.copy(); m_radius = c.m_radius; }
	/** Good old constructor. The given center is now owned by the created object. */
	public Circle(PLSVector c, float r) { m_origin = c; m_radius = r; }
	/** Other constructor. */
	public Circle(float px, float py, float r) { m_origin = new PLSVector(px, py); m_radius = r; }


	// Getters and setters

	public PLSVector getCenter() { return m_origin; }
	public void setCenter(PLSVector c) { m_origin = c; }
	public void setCenter(float x, float y) { m_origin = new PLSVector(x, y); }
	public float getRadius() { return m_radius; }
	public void setRadius(float r) { m_radius = r; m_squaredRadius = Double.NaN; }


	// Creation of instances

	/** Returns a copy of this circle. */
	public final Circle copy()
	{
		return new Circle(this);
	}

	/** Creates a new normalized circle (radius = 1), centered on the origin. */
	public static Circle create()
	{
		return new Circle(0, 0, 1);
	}
	/** Creates a circle of random radius in the given range centered on the origin. */
	public static Circle createRandomCircle(float min, float max)
	{
		return new Circle(0, 0, PLSVector.getRandom(min, max));
	}


	/**
	 * Tells if this circle is empty, ie. if it has a radius of zero (or negative!).
	 */
	@Override
	public final boolean isEmpty()
	{
		return m_radius <= 0;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(m_origin.getX() - m_radius, m_origin.getY() - m_radius, 2 * m_radius, 2 * m_radius);
	}

	@Override
	public final boolean contains(float px, float py)
	{
		if (isEmpty())
			return false;
		check();
		double sqR = GeomUtil.squaredDistance(px, py, m_origin.getX(), m_origin.getY());
		return sqR <= m_squaredRadius;
	}
	@Override
	public final boolean contains(PLSVector point)
	{
		return contains(point.getX(), point.getY());
	}

	/**
	 * Tells if this circle contains the given rectangle.
	 */
	@Override
	public final boolean contains(float cx, float cy, float w, float h)
	{
		if (isEmpty() || w <= 0 || h <= 0)
			return false;
		return contains(cx, cy) && contains(cx + w, cy) && contains(cx, cy + h) && contains(cx + w, cy + h);
	}
	/**
	 * Tells if this circle contains the given rectangle.
	 */
	@Override
	public final boolean contains(Rectangle r)
	{
		return contains(r.getLeft(), r.getTop(), r.getWidth(), r.getHeight());
	}

	/**
	 * Tells if this circle intersects the given line.
	 */
	public final boolean intersectsLine(float x1, float y1, float x2, float y2)
	{
		if (isEmpty())
			return false;
		return GeomUtil.isLineIntersectingCircle(x1, y1, x2, y2, m_origin.getX(), m_origin.getY(), m_radius);
	}
	/**
	 * Tells if this circle intersects the given line.
	 */
	public final boolean intersectsLine(Line seg)
	{
		return intersects(seg.getPoint1().getX(), seg.getPoint1().getY(), seg.getPoint2().getX(), seg.getPoint2().getY());
	}
	/**
	 * Tells if this circle intersects the given line.
	 */
	public final int getLineIntersection(Line seg, PLSVector pt1, PLSVector pt2)
	{
		if (isEmpty())
			return 0;
		double[] result = new double[4];
		int ptNb = GeomUtil.getLineCircleIntersectionPoints(
				seg.getPoint1().getX(), seg.getPoint1().getY(), seg.getPoint2().getX(), seg.getPoint2().getY(),
				m_origin.getX(), m_origin.getY(), m_radius,
				result);
		if (ptNb > 1)
		{
			if (pt1 == null)
			{
				pt1 = new PLSVector();
			}
			pt1.set((float) result[0], (float) result[1]);
		}
		if (ptNb == 2)
		{
			if (pt2 == null)
			{
				pt2 = new PLSVector();
			}
			pt2.set((float) result[2], (float) result[3]);
		}
		return ptNb;
	}

	/**
	 * Tells if this circle intersects the given rectangle.
	 */
	@Override
	public final boolean intersects(float cx, float cy, float w, float h)
	{
		if (isEmpty() || w <= 0 || h <= 0)
			return false;
		// It if contains one corner, it is true
		if (contains(cx, cy) || contains(cx + w, cy) || contains(cx, cy + h) || contains(cx + w, cy + h))
			return true;
		// If it intersects one side, it is true
		// TODO
		return true;
	}
	/**
	 * Tells if this circle intersects the given rectangle.
	 */
	@Override
	public final boolean intersects(Rectangle r)
	{
		return intersects(r.getLeft(), r.getTop(), r.getWidth(), r.getHeight());
	}

	/**
	 * Tells if this circle intersects the given one.
	 * An empty circle intersects nothing.
	 *
	 * @param circle  the circle to check
	 * @return true if the circles intersects
	 */
	public final boolean intersects(Circle circle)
	{
		if (isEmpty() || circle.isEmpty())
			return false;
		double dist = Math.sqrt(GeomUtil.squaredDistance(m_origin.getX(), m_origin.getY(), circle.m_origin.getX(), circle.m_origin.getY()));
		return dist <= m_radius + circle.m_radius;
	}

	private void check()
	{
		if (Double.isNaN(m_squaredRadius))
		{
			// Compute it only once, and only if needed
			m_squaredRadius = (double) m_radius * m_radius;
		}
	}


	// Base methods

	@Override
	public String toString()
	{
		return "Circle((" + m_origin.getX() + ", " + m_origin.getY() + "), radius=" + m_radius + ")";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		return String.format("C((%.2f, %.2f), r=%.2f)", m_origin.getX(), m_origin.getY(), m_radius);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_origin == null) ? 0 : m_origin.hashCode());
		result = prime * result + Float.floatToIntBits(m_radius);
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof Circle)) return false;
		Circle other = (Circle) obj;
		if (m_origin == null)
		{
			if (other.m_origin != null) return false;
		}
		else if (!m_origin.equals(other.m_origin)) return false;
		if (Float.floatToIntBits(m_radius) != Float.floatToIntBits(other.m_radius)) return false;
		return true;
	}
}
