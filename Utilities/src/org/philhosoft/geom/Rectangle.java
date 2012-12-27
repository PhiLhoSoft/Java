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
public class Rectangle extends BasePath implements ClosedShape
{
	private static final long serialVersionUID = 1L;

	// The top-left corner of the rectangle is the origin of this path

	/** The width of the rectangle. */
	private float width;
	/** The height of the rectangle. */
	private float height;


	/** Empty constructor. Corner and dimensions are set to 0, creating a simple point. */
	public Rectangle() {}
	/** Copy constructor. */
	public Rectangle(Rectangle r) { m_origin.set(r.m_origin); width = r.width; height = r.height; }
	/** Good old constructor. */
	public Rectangle(float cx, float cy, float w, float h) { m_origin.set(cx, cy); width = w; height = h; }
	/** Constructor from a point and two dimensions. */
	public Rectangle(PLSVector topLeftCorner, float w, float h) { m_origin.set(topLeftCorner); width = w; height = h; }
	/** Constructor from two opposite corners. */
	public Rectangle(PLSVector corner1, PLSVector corner2)
	{
		this(createFromPoints(corner1.getX(), corner1.getY(), corner2.getX(), corner2.getY()));
	}

	// Getters and setters

	public PLSVector getTopLeft() { return m_origin.copy(); }
	public PLSVector getBottomRight() { return m_origin.copy().add(width, height); }
	public float getLeft() { return m_origin.getX(); }
	public float getTop() { return m_origin.getY(); }
	public float getRight() { return m_origin.getX() + width; }
	public float getBottom() { return m_origin.getY() + height; }
	public float getWidth() { return width; }
	public void setWidth(float w) { width = w; }
	public float getHeight() { return height; }
	public void setHeight(float h) { height = h; }
	/**
	 * Sets the top-left corner to the given position, keeping the bottom-right corner in place.
	 * If the position is beyond the B-R corner, the dimension becomes zero.
	 * @param corner
	 */
	public void setTopLeft(float px, float py)
	{
		float x = m_origin.getX();
		float y = m_origin.getY();
		// Bottom-right corner
		float brcX = x + width;
		float brcY = y + height;
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
		m_origin.set(x, y);
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
		width = px - m_origin.getX();
		height = py - m_origin.getY();
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
		float left = Math.min(x1, x2);
		float right = Math.max(x1, x2);
		float top = Math.min(y1, y2);
		float bottom = Math.max(y1, y2);
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
		if (px < getLeft() || py < getTop())
			return false;
		if (px > getRight() || py > getBottom())
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
		return getLeft() <= cx && getRight() >= cx + w && getTop() <= cy && getBottom() >= cy + h;
	}
	/**
	 * Tells if this rectangle contains the given one.
	 */
	@Override
	public final boolean contains(Rectangle r)
	{
		return contains(r.getLeft(), r.getTop(), r.width, r.height);
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
		float r1left = getLeft();
		float r1right = getRight();
		float r1top = getTop();
		float r1bottom = getBottom();
		float r2left = cx;
		float r2right = cx + w;
		float r2top = cy;
		float r2bottom = cy + h;
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
		return intersects(r.getLeft(), r.getRight(), r.width, r.height);
	}

	/**
	 * Merges this rectangle with the given one, making a bigger (if this one isn't enclosing the other one)
	 * rectangle corresponding to the bounding box of both rectangles.
	 */
	public final Rectangle mergeWith(Rectangle r)
	{
		float maxRight = Math.max(getRight(), r.getRight());
		float x = Math.min(getLeft(), r.getLeft());
		float maxBottom = Math.max(getBottom(), r.getBottom());
		float y = Math.min(getTop(), r.getTop());
		m_origin.set(x, y);
		width = maxRight - x;
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
		float maxLeft = Math.max(getLeft(), r.getLeft());
		float minRight = Math.min(getRight(), r.getRight());
		float maxTop = Math.max(getTop(), r.getTop());
		float minBottom = Math.min(getBottom(), r.getBottom());
		m_origin.set(maxLeft, maxTop);
		width = minRight - maxLeft;
		height = minBottom - maxTop;
		return this;
	}


	// Base methods

	@Override
	public String toString()
	{
		return "Rectangle((" + getLeft() + ", " + getTop() + "), width=" + width + ")";
	}
	/** Compacter alternative, with only two decimal digits. */
	public String toShortString()
	{
		return String.format("R((%.2f, %.2f), w=%.2f, h=%.2f)", getLeft(), getTop(), width, height);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_origin == null) ? 0 : m_origin.hashCode());
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
		if (m_origin == null)
		{
			if (other.m_origin != null) return false;
		}
		else if (!m_origin.equals(other.m_origin)) return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width)) return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height)) return false;
		return true;
	}
}
