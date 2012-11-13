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
 * Abstract path defines default behavior for a geometric path.
 *
 * @author PhiLho
 */
@SuppressWarnings("serial") // The sub-classes must define it
public abstract class BasePath implements GeomPath, java.io.Serializable
{
	/** The origin of the path. */
	protected PLSVector m_origin;
	/** The anchor, relative to the origin. */
	protected PLSVector m_anchor;

	@Override public float getPositionX() { return m_origin.getX() + m_anchor.getX(); }
	@Override public float getPositionY() { return m_origin.getY() + m_anchor.getY(); }
	@Override public PLSVector getPosition() { return m_origin.copy().add(m_anchor); }
	@Override public void setAnchor(float x, float y) { m_anchor.add(x, y); }
	@Override public void setAnchor(PLSVector pos) { m_anchor.add(pos); }
	@Override public void setPosition(float x, float y) { m_origin.set(x, y).subtract(m_anchor); }
	@Override public void setPosition(PLSVector pos) { m_origin.set(pos).subtract(m_anchor); }
}
