/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Geometric entities.
 */
/* File history:
 *  0.01.000 -- 2012/11/13 (PL) -- Creation.
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
 * Defines methods common to all geometric paths, from a simple straight line segment
 * to a complex closed shape, through various curves, etc.
 *
 * @author PhiLho
 */
public interface GeomPath
{
	/**
	 * Tells if the path is empty, ie. if its length is zero.
	 * An empty path intersects nothing.
	 */
	boolean isEmpty();
	/**
	 * Returns a rectangle enclosing entirely the path.
	 * There is no guarantee of being the smallest possible bounding box.
	 */
	Rectangle getBounds();

	/**
	 * Gets the X position of the path, ie. of its origin or an anchor point.
	 */
	float getPositionX();
	/**
	 * Gets the Y position of the path, ie. of its origin or an anchor point.
	 */
	float getPositionY();
	/**
	 * Gets the position of the path, ie. its origin or an anchor point.
	 *
	 * @return a copy of the current position; changing it will not change the position of the path
	 */
	PLSVector getPosition();
	/**
	 * Sets the position of the path, ie. where its origin or an anchor point will be.
	 *
	 * @param x  the X coordinate of the new position
	 * @param y  the Y coordinate of the new position
	 */
	void setPosition(float x, float y);
	/**
	 * Sets the position of the path, ie. where its origin or an anchor point will be.
	 *
	 * @param pos  the vector definition the new point / position
	 */
	void setPosition(PLSVector pos);
	/**
	 * Sets the anchor of the path relative to the previous / default origin or anchor point.
	 *
	 * @param dx  a delta on the X coordinate of the current anchor / origin
	 * @param dy  a delta on the Y coordinate of the current anchor / origin
	 */
	void setAnchor(float x, float y);
	/**
	 * Sets the anchor of the path relative to the previous / default origin or anchor point.
	 *
	 * @param delta  the vector defining the displacement of the origin / previous anchor
	 */
	void setAnchor(PLSVector delta);

	/**
	 * Tells if the path intersects the given rectangle defined by its coordinates and dimensions.
	 *
	 * @param cx  X coordinate of the top-left corner of the rectangle
	 * @param cy  Y coordinate of the top-left corner of the rectangle
	 * @param w  width of the rectangle
	 * @param h  height of the rectangle
	 * @return true if the path intersects the rectangle
	 */
	boolean intersects(float cx, float cy, float w, float h);
	/**
	 * Tells if the path intersects the given rectangle.
	 *
	 * @param r  rectangle to check against this path
	 * @return true if the path intersects the rectangle
	 */
	boolean intersects(Rectangle r);
	/**
	 * Tells if the path intersects the given other path.
	 *
	 * @param p  path to check against this path
	 * @return true if the path intersects the other path
	 */
// TODO: can be complex to implement with an arbitrary path!
//	boolean intersects(GeomPath p);

// TODO someday...
//    PathIterator getPathIterator(AffineTransform at);
//    PathIterator getPathIterator(AffineTransform at, double flatness);
}
