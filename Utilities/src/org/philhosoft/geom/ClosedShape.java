/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Geometric entities.
 */
/* File history:
 *  1.00.000 -- 2012/11/06 (PL) -- Creation.
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
 * Defines methods common to all 2D closed shapes, ie. shapes defining an area in the 2D plane.
 *
 * @author PhiLho
 */
public interface ClosedShape
{
	/**
	 * Tells if the shape is empty, ie. if its area is zero.
	 * An empty shape contains nothing, cannot be contained by another shape, and intersects nothing.
	 */
	boolean isEmpty();
	/**
	 * Returns a rectangle enclosing entirely the shape.
	 * There is no guarantee of being the smallest possible bounding box.
	 */
	Rectangle getBounds();
	/**
	 * Tells if the shape contains the given point defined by its coordinates.
	 *
	 * @param px  X coordinate of the point
	 * @param py  Y coordinate of the point
	 * @return true if the shape contains the point
	 */
	boolean contains(float px, float py);
	/**
	 * Tells if the shape contains the given point.
	 *
	 * @param p  point to check against this shape
	 * @return true if the shape contains the point
	 */
	boolean contains(PLSVector p);
	/**
	 * Tells if the shape contains the given rectangle defined by its coordinates and dimensions.
	 *
	 * @param cx  X coordinate of the top-left corner of the rectangle
	 * @param cy  Y coordinate of the top-left corner of the rectangle
	 * @param w  width of the rectangle
	 * @param h  height of the rectangle
	 * @return true if the rectangle is entirely contained in the shape
	 */
	boolean contains(float cx, float cy, float w, float h);
	/**
	 * Tells if the shape contains the given rectangle.
	 *
	 * @param r  rectangle to check against this shape
	 * @return true if the rectangle is entirely contained in the shape
	 */
	boolean contains(Rectangle r);
	/**
	 * Tells if the shape intersects the given rectangle defined by its coordinates and dimensions.
	 *
	 * @param cx  X coordinate of the top-left corner of the rectangle
	 * @param cy  Y coordinate of the top-left corner of the rectangle
	 * @param w  width of the rectangle
	 * @param h  height of the rectangle
	 * @return true if the rectangle intersects the shape
	 */
	boolean intersects(float cx, float cy, float w, float h);
	/**
	 * Tells if the shape intersects the given rectangle.
	 *
	 * @param r  rectangle to check against this shape
	 * @return true if the rectangle intersects the shape
	 */
	boolean intersects(Rectangle r);
}
