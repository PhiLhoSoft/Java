/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2011/01/17 (PL) -- Normalize case of methods
 *  0.01.000 -- 2009/03/10 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.util;

import java.awt.Color;

/**
 * Static functions for color operations.
 */
public class ColorUtil
{
	// Constants can be overridden
	protected static float MIN_BRIGHT_DIFF = 125.0F;
	protected static int MIN_COLOR_DIFF = 400;
	protected static int MIN_COLOR_DIFF_STRICT = 500;

	protected static float MIN_LUM_RATIO_LARGE_AA = 3.0F;
	protected static float MIN_LUM_RATIO_NORMAL_AA = 4.5F;
	protected static float MIN_LUM_RATIO_LARGE_AAA = 4.5F;
	protected static float MIN_LUM_RATIO_NORMAL_AAA = 7.0F;

	// http://en.wikipedia.org/wiki/HSL_and_HSV
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int ALPHA = 3;
	public static final int HUE = 0;
	public static final int SATURATION = 1;
	public static final int BRIGHTNESS = 2;
	public static final int VALUE = 2;
	public static final int LUMINANCE = 2;

	public static float[] convertHSVtoHSL(float[] hsv, float[] hsl)
	{
		hsl[HUE] = hsv[HUE];
		hsl[LUMINANCE] = (2 - hsv[SATURATION]) * hsv[VALUE];
		hsl[SATURATION] = hsv[SATURATION] * hsv[VALUE];
		hsl[SATURATION] /= hsl[LUMINANCE] <= 1 ? hsl[LUMINANCE] : 2 - hsl[LUMINANCE];
		hsl[LUMINANCE] /= 2;
		return hsl;
	}
	public static float[] convertHSLtoHSV(float[] hsl, float[] hsv)
	{
		hsv[HUE] = hsl[HUE];
		hsl[LUMINANCE] *= 2;
		hsl[SATURATION] *= hsl[LUMINANCE] <= 1 ? hsl[LUMINANCE] : 2 - hsl[LUMINANCE];
		hsv[VALUE] = (hsl[LUMINANCE] + hsl[SATURATION]) / 2;
		hsv[SATURATION] = 2 * hsl[SATURATION] / (hsl[LUMINANCE] + hsl[SATURATION]);
		return hsv;
	}


	/**
	 * Checks if two given colors have enough contrast in a slightly lenient way.
	 */
	public static boolean isColorConstrastOK(Color c1, Color c2, boolean bWithLargeFont)
	{
		return isColorConstrastOKByLuminance(c1, c2, bWithLargeFont, false);
	}

	/**
	 * Checks if two given colors have enough luminance contrast.
	 * See {@link http://www.paciellogroup.com/resources/contrast-analyser.html for details on algorithm}.
	 * See also {@link http://www.wat-c.org/tools/CCA/1.1/}
	 *
	 * @param c1        first color to compare
	 * @param c1        second color to compare
	 * @param bWithLargeFont   if true, it is more tolerant because big fonts have better contrast
	 * @param bStrict   applies stricter rules (AAA instead of AA)
	 */
	public static boolean isColorConstrastOKByLuminance(Color c1, Color c2, boolean bWithLargeFont, boolean bStrict)
	{
		float minLumRatio = MIN_LUM_RATIO_NORMAL_AA;
		if (bWithLargeFont)
		{
			if (bStrict)
			{
				minLumRatio = MIN_LUM_RATIO_LARGE_AAA;
			}
			else
			{
				minLumRatio = MIN_LUM_RATIO_LARGE_AA;
			}
		}
		else if (bStrict)
		{
			minLumRatio = MIN_LUM_RATIO_NORMAL_AAA;
		}
		boolean bIsLuminanceContrastOK = getLuminanceContrastRatio(c1, c2) > minLumRatio;
		return bIsLuminanceContrastOK;
	}

	/**
	 * Checks if two given colors have enough color contrast (brightness and difference).
	 * See {@link http://www.paciellogroup.com/resources/contrast-analyser.html for details on algorithm}.
	 * See also {@link http://www.wat-c.org/tools/CCA/1.1/}
	 *
	 * @param c1        first color to compare
	 * @param c1        second color to compare
	 * @param bStrict   applies stricter rules (W3C instead of HP)
	 */
	public static boolean isColorConstrastOKByColor(Color c1, Color c2, boolean bStrict)
	{
		int minColorDiff = MIN_COLOR_DIFF;
		if (bStrict)
		{
			minColorDiff = MIN_COLOR_DIFF_STRICT;
		}
		float b1 = getColorBrightness(c1);
		float b2 = getColorBrightness(c2);
		boolean bIsBrightnessOK = Math.abs(b1 - b2) > MIN_BRIGHT_DIFF;
		boolean bIsColorDiffOK = getColorDifference(c1, c2) > minColorDiff;
		return bIsBrightnessOK && bIsColorDiffOK;
	}

	public static float getLuminanceContrastRatio(Color c1, Color c2)
	{
		float l1 = getColorLuminance(c1);
		float l2 = getColorLuminance(c2);
		float ll = Math.min(l1, l2);
		float hl = Math.max(l1, l2);
		return (hl + 0.05F) / (ll + 0.05F);
	}

	public static int getColorDifference(Color c1, Color c2)
	{
		return
				Math.abs(c1.getRed() - c2.getRed()) +
				Math.abs(c1.getGreen() - c2.getGreen()) +
				Math.abs(c1.getBlue() - c2.getBlue());
	}

	public static float getColorLuminance(Color c)
	{
		float[] facc = c.getRGBColorComponents(null);
		return
				0.2126F * getLinearisedColorComponent(facc[0]) +
				0.7152F * getLinearisedColorComponent(facc[1]) +
				0.0722F * getLinearisedColorComponent(facc[2]);
	}

	/**
	 * Computes brightness of a color using RGB to YIQ conversion.
	 * YIQ: Y=Luminance. I=Red-Y, Q=Blue-Y
	 */
	public static float getColorBrightness(Color c)
	{
		return (
				299.0F * c.getRed() +
				587.0F * c.getGreen() +
				114.0F * c.getBlue()
			) / 1000.0F;
	}

	public static float getLinearisedColorComponent(float fcc)
	{
		if (fcc <= 0.03928F)
			return fcc / 12.92F;
		return (float) Math.pow((fcc + 0.055F) / 1.055F, 2.4F);
	}
}
