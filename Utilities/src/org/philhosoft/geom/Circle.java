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
public class Circle implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	/** The center of the circle. */
	private PLSVector center;
	/** The radius of the circle. */
	private float radius;

	/** The squared radius, useful for computations. */
	private transient double squaredRadius = Double.NaN;


	/** Empty constructor. Coordinates and radius are set to 0, creating a simple point. */
	public Circle() {}
	/** Copy constructor. */
	public Circle(Circle c) { center = c.center.copy(); radius = c.radius; }
	/** Good old constructor. The given center is now owned by the created object. */
	public Circle(PLSVector c, float r) { center = c; radius = r; }
	/** Other constructor. */
	public Circle(float px, float py, float r) { center = new PLSVector(px, py); radius = r; }


	// Getters and setters

	public PLSVector getCenter() { return center; }
	public void setCenter(PLSVector c) { center = c; }
	public void setCenter(float x, float y) { center = new PLSVector(x, y); }
	public float getRadius() { return radius; }
	public void setRadius(float r) { radius = r; squaredRadius = Double.NaN; }


	// Creation of instances

	/** Returns a copy of this circle. */
	public final Circle copy()
	{
		return new Circle(this);
	}

	/** Creates a new normalized circle (radius = 1), centered on the origin. */
	public static Circle create()
	{
		return new Circle(PLSVector.create(), 1);
	}


	/**
	 * Tells if this circle is empty, ie. if it has a radius of zero (or negative!).
	 */
	public final boolean isEmpty()
	{
		return radius <= 0;
	}

	/**
	 * Tells if this circle contains the given point defined by its coordinates.
	 * An empty circle contains nothing.
	 *
	 * @param px  the X coordinate of the point to check
	 * @param py  the Y coordinate of the point to check
	 * @return true if the point is inside the circle
	 */
	public final boolean contains(float px, float py)
	{
		if (isEmpty())
			return false;
		check();
		final double sqR = GeomUtil.squaredDistance(px, py, center.getX(), center.getY());
		return sqR <= squaredRadius;
	}
	/**
	 * Tells if this circle contains the given point.
	 * An empty circle contains nothing.
	 *
	 * @param point  the point to check
	 * @return true if the point is inside the circle
	 */
	public final boolean contains(PLSVector point)
	{
		return contains(point.getX(), point.getY());
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
		final double dist = Math.sqrt(GeomUtil.squaredDistance(center.getX(), center.getY(), circle.center.getX(), circle.center.getY()));
		return dist <= radius + circle.radius;
	}

	private void check()
	{
		if (Double.isNaN(squaredRadius))
		{
			// Compute it only once, and only if needed
			squaredRadius = (double) radius * radius;
		}
	}


	// Base methods

	@Override
	public String toString()
	{
		return "Circle((" + center.getX() + ", " + center.getY() + "), radius=" + radius + ")";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		return String.format("C((%.2f, %.2f), r=%.2f)", center.getX(), center.getY(), radius);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + Float.floatToIntBits(radius);
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof Circle)) return false;
		Circle other = (Circle) obj;
		if (center == null)
		{
			if (other.center != null) return false;
		}
		else if (!center.equals(other.center)) return false;
		if (Float.floatToIntBits(radius) != Float.floatToIntBits(other.radius)) return false;
		return true;
	}
}
