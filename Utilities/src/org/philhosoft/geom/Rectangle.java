/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Geometric entities.
 */
/* File history:
 *  0.01.000 -- 2012/11/06 (PL) -- Creation.
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
 * A 2D rectangle, defined by its top-left corner and its dimensions: width and height.
 * <p>
 * Reminder: the height goes down, as the Y axis follows the display system of coordinates.
 *
 * @author PhiLho
 */
public class Rectangle implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	/** The top-left corner of the rectangle: X coordinate. */
	private float x;
	/** The top-left corner of the rectangle: Y coordinate. */
	private float y;
	/** The width of the rectangle. */
	private float width;
	/** The height of the rectangle. */
	private float height;


	/** Empty constructor. Corner and dimensions are set to 0, creating a simple point. */
	public Rectangle() {}
	/** Copy constructor. */
	public Rectangle(Rectangle r) { x = r.x; y = r.y; width = r.width; height = r.height; }
	/** Good old constructor. */
	public Rectangle(float cx, float cy, float w, float h) { x = cx; y = cy; width = w; height = h; }
	/** Constructor from a point and two dimensions. */
	public Rectangle(PLSVector topLeftCorner, float w, float h) { x = topLeftCorner.getX(); y = topLeftCorner.getY(); width = w; height = h; }
	/** Constructor from two opposite corners. */
	public Rectangle(PLSVector corner1, PLSVector corner2)
	{
		this(createFromPoints(corner1.getX(), corner1.getY(), corner2.getX(), corner2.getY()));
	}

	// Getters and setters

	public PLSVector getTopLeft() { return new PLSVector(x, y); }
	public PLSVector getBottomRight() { return new PLSVector(x + width, y + height); }
	public float getLeft() { return x; }
	public float getTop() { return y; }
	public float getRight() { return x + width; }
	public float getBottom() { return y + height; }
	public float getWidth() { return width; }
	public void setWidth(float w) { width = w; }
	public float getHeight() { return height; }
	public void setHeight(float h) { height = h; }
	// TODO do it correctly! Setting one corner should change the dimensions, we must manage relative positions, etc.
	public void setTopLeft(PLSVector corner) { x = corner.getX(); y = corner.getY(); }
	public void setBottomRight(PLSVector corner) { width = corner.getX(); y = corner.getY(); }
	public void setTopLeft(float px, float py) { x = px; y = py; }
	public void setBottomRight(float px, float py) { x = px; y = py; }


	// Creation of instances

	/** Returns a copy of this rectangle. */
	public final Rectangle copy()
	{
		return new Rectangle(this);
	}

	/** Creates a new normalized rectangle (dimensions = 1), with the top-left corner on the origin. */
	public static Rectangle create()
	{
		return new Rectangle(0, 0, 1, 1);
	}
	/** Creates a new rectangle from two points. */
	public static Rectangle createFromPoints(float x1, float y1, float x2, float y2)
	{
		final float left = Math.min(x1, x2);
		final float right = Math.max(x1, x2);
		final float top = Math.min(y1, y2);
		final float bottom = Math.max(y1, y2);
		return new Rectangle(new PLSVector(left, top), right - left, bottom - top);
	}


	/**
	 * Tells if this rectangle is empty, ie. if it has a dimension of zero (or negative!).
	 */
	public final boolean isEmpty()
	{
		if (width <= 0) return true;
		if (height <= 0) return true;
		return false;
	}

	/**
	 * Tells if this rectangle contains the given point defined by its coordinates.
	 * An empty rectangle contains nothing.
	 *
	 * @param px  the X coordinate of the point to check
	 * @param py  the Y coordinate of the point to check
	 * @return true if the point is inside the rectangle
	 */
	public final boolean contains(float px, float py)
	{
		if (isEmpty())
			return false;
		if (px < x || py < y)
			return false;
		if (px > x + width || py > y + width)
			return false;
		return true;
	}
	/**
	 * Tells if this rectangle contains the given point.
	 * An empty rectangle contains nothing.
	 *
	 * @param point  the point to check
	 * @return true if the point is inside the rectangle
	 */
	public final boolean contains(PLSVector point)
	{
		return contains(point.getX(), point.getY());
	}

	/**
	 * Tells if this rectangle intersects the given one.
	 * An empty rectangle intersects nothing.
	 *
	 * @param rectangle  the rectangle to check
	 * @return true if the rectangles intersects
	 */
	public final boolean intersects(Rectangle rectangle)
	{
		if (isEmpty() || rectangle.isEmpty())
			return false;
		// TODO
		return true;
	}

	/**
	 * Merges this rectangle with the given one, making a bigger (if this one isn't enclosing the other one)
	 * rectangle corresponding to the bounding box of both rectangles.
	 */
	public final Rectangle merge(Rectangle r)
	{
		float maxRight = Math.max(x + width, r.x + r.width);
		x = Math.min(x, r.x);
		width = maxRight - x;
		float maxBottom = Math.max(y + height, r.y + r.height);
		y = Math.min(y, r.y);
		height = maxBottom - y;
		return this;
	}

	/**
	 * Intersects this rectangle with the given one, making it smaller.
	 * If these rectangles are not intersecting, the result is an empty rectangle.
	 */
	public final Rectangle intersect(Rectangle r)
	{
		if (!intersects(r))
		{
			width = height = 0;
			return this;
		}
		float maxLeft = Math.max(x, r.x);
		float minRight = Math.min(x + width, r.x + r.width);
		float maxTop = Math.max(y, r.y);
		float minBottom = Math.min(y + height, r.y + r.height);
		x = maxLeft;
		y = maxTop;
		width = minRight - x;
		height = minBottom - y;
		return this;
	}


	// Base methods

	@Override
	public String toString()
	{
		return "Rectangle((" + x + ", " + y + "), width=" + width + ")";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		return String.format("R((%.2f, %.2f), w=%.2f, h=%.2f)", x, y, width, height);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(width);
		result = prime * result + Float.floatToIntBits(height);
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!(obj instanceof Rectangle)) return false;
		Rectangle other = (Rectangle) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width)) return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height)) return false;
		return true;
	}
}
