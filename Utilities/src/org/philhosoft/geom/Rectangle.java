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
 * A 2D rectangle, defined by its top-left corner and its dimensions: width and height,
 * where the sides are parallel to the axes.
 * <p>
 * Reminder: the height goes down, as the Y axis follows the display system of coordinates.
 *
 * @author PhiLho
 */
public class Rectangle implements ClosedShape, java.io.Serializable
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
	/** Moves the top-left corner to the given position, keeping the dimensions unchanged. */
	public void moveTo(float px, float py) { x = px; y = py; }
	/** Moves the top-left corner to the given position, keeping the dimensions unchanged. */
	public void moveTo(PLSVector p) { x = p.getX(); y = p.getY(); }
	/**
	 * Sets the top-left corner to the given position, keeping the bottom-right corner in place.
	 * If the position is beyond the B-R corner, the dimension becomes zero.
	 * @param corner
	 */
	public void setTopLeft(float px, float py)
	{
		// Bottom-right corner
		final float brcX = x + width;
		final float brcY = y + height;
		if (px < brcX) // OK
		{
			x = px;
			width = brcX - x;
		}
		else
		{
			x = brcX;
			width = 0;
		}
		if (py < brcY) // OK
		{
			y = py;
			height = brcY - y;
		}
		else
		{
			y = brcY;
			height = 0;
		}
	}
	public void setTopLeft(PLSVector corner)
	{
		setTopLeft(corner.getX(), corner.getY());
	}
	public void setBottomRight(PLSVector corner)
	{
		setBottomRight(corner.getX(), corner.getY());
	}
	public void setBottomRight(float px, float py)
	{
		width = px - x;
		height = py - y;
	}


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
	/** Creates a rectangle of random dimensions in the given range, with the top-left corner on the origin. */
	public static Rectangle createRandomRectangle(float minW, float maxW, float minH, float maxH)
	{
		return new Rectangle(0, 0, PLSVector.getRandom(minW, maxW), PLSVector.getRandom(minH, maxH));
	}


	/**
	 * Tells if this rectangle is empty, ie. if it has a dimension of zero (or negative!).
	 */
	@Override
	public final boolean isEmpty()
	{
		if (width <= 0) return true;
		if (height <= 0) return true;
		return false;
	}

	@Override
	public Rectangle getBounds()
	{
		return copy();
	}

	/**
	 * Tells if this rectangle contains the given point defined by its coordinates.
	 */
	@Override
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
	 */
	@Override
	public final boolean contains(PLSVector point)
	{
		return contains(point.getX(), point.getY());
	}

	/**
	 * Tells if this rectangle contains the given one.
	 */
	@Override
	public final boolean contains(float cx, float cy, float w, float h)
	{
		if (isEmpty() || w <= 0 || h <= 0)
			return false;
		return x <= cx && x + width >= cx + w && y <= cy && y + height >= cy + h;
	}
	/**
	 * Tells if this rectangle contains the given one.
	 */
	@Override
	public final boolean contains(Rectangle r)
	{
		return contains(r.x, r.y, r.width, r.height);
	}

	/**
	 * Tells if this rectangle intersects the given one.
	 */
	@Override
	public final boolean intersects(float cx, float cy, float w, float h)
	{
		if (isEmpty() || w <= 0 || h <= 0)
			return false;
		// Defines variable for clarity sake
		final float r1left = x;
		final float r1right = x + width;
		final float r1top = y;
		final float r1bottom = y + height;
		final float r2left = cx;
		final float r2right = cx + w;
		final float r2top = cy;
		final float r2bottom = cy + h;
		/* Not intersecting if:
		r1right < r2left (on left of other rect) or
		r1left > r2right (on right of other rect) or
		r1bottom < r2top (over the other rect) or
		r1top > r2bottom (below the other rect)
		We just invert the above expression below, to avoid negating.
		See http://jsfiddle.net/fhXFC/3/ (JS test!)
		*/
		return
				r1right >= r2left &&
				r1left <= r2right &&
				r1bottom >= r2top &&
				r1top <= r2bottom;
	}
	/**
	 * Tells if this rectangle intersects the given one.
	 */
	@Override
	public final boolean intersects(Rectangle r)
	{
		return intersects(r.x, r.y, r.width, r.height);
	}

	/**
	 * Merges this rectangle with the given one, making a bigger (if this one isn't enclosing the other one)
	 * rectangle corresponding to the bounding box of both rectangles.
	 */
	public final Rectangle mergeWith(Rectangle r)
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
	public final Rectangle intersectWith(Rectangle r)
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
