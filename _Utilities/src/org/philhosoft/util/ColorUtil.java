package org.philhosoft.util;

import java.awt.Color;

/**
 *
 * @author PhiLho
 */
public class ColorUtil
{
	protected static int MIN_COLOR_DIFF = 400;
	protected static int MIN_COLOR_DIFF_STRICT = 500;
	protected static float MIN_BRIGHT_DIFF = 125.0F;

	protected static float MIN_LUM_RATIO_LARGE_AA = 3.0F;
	protected static float MIN_LUM_RATIO_NORMAL_AA = 4.5F;
	protected static float MIN_LUM_RATIO_LARGE_AAA = 4.5F;
	protected static float MIN_LUM_RATIO_NORMAL_AAA = 7.0F;

	/**
	 * Check if two given colors have enough contrast in a slightly lenient way.
	 */
	public static boolean IsColorConstrastOK(Color c1, Color c2, boolean bWithLargeFont)
	{
		return IsColorConstrastOKByLuminance(c1, c2, bWithLargeFont, false);
	}
	/**
	 * Check if two given colors have enough luminance contrast.
	 * See {@link http://www.paciellogroup.com/resources/contrast-analyser.html for details on algorithm}.
	 * See also {@link http://www.wat-c.org/tools/CCA/1.1/}
	 */
	public static boolean IsColorConstrastOKByLuminance(Color c1, Color c2, boolean bWithLargeFont, boolean bStrict)
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
		boolean bIsLuminanceContrastOK = GetLuminanceContrastRatio(c1, c2) > minLumRatio;
		return bIsLuminanceContrastOK;
	}

	/**
	 * Check if two given colors have enough color contrast (brightness and difference).
	 * See {@link http://www.paciellogroup.com/resources/contrast-analyser.html for details on algorithm}.
	 * See also {@link http://www.wat-c.org/tools/CCA/1.1/}
	 */
	public static boolean IsColorConstrastOKByColor(Color c1, Color c2, boolean bStrict)
	{
		int minColorDiff = MIN_COLOR_DIFF;
		if (bStrict)
		{
			minColorDiff = MIN_COLOR_DIFF_STRICT;
		}
		float b1 = GetColorBrightness(c1);
		float b2 = GetColorBrightness(c2);
		boolean bIsBrightnessOK = Math.abs(b1 - b2) > MIN_BRIGHT_DIFF;
		boolean bIsColorDiffOK = GetColorDifference(c1, c2) > minColorDiff;
		return bIsBrightnessOK && bIsColorDiffOK;
	}

	public static float GetLuminanceContrastRatio(Color c1, Color c2)
	{
		float l1 = GetColorLuminance(c1);
		float l2 = GetColorLuminance(c2);
		float ll = Math.min(l1, l2);
		float hl = Math.max(l1, l2);
		return (hl + 0.05F) / (ll + 0.05F);
	}

	public static int GetColorDifference(Color c1, Color c2)
	{
		return
				Math.abs(c1.getRed() - c2.getRed()) +
				Math.abs(c1.getGreen() - c2.getGreen()) +
				Math.abs(c1.getBlue() - c2.getBlue());
	}

	public static float GetColorLuminance(Color c)
	{
		float[] facc = c.getRGBColorComponents(null);
		return
				0.2126F * GetLinearisedColorComponent(facc[0]) +
				0.7152F * GetLinearisedColorComponent(facc[1]) +
				0.0722F * GetLinearisedColorComponent(facc[2]);
	}

	/**
	 * Compute brightness of a color using RGB to YIQ conversion.
	 * YIQ: Y=Luminance. I=Red-Y, Q=Blue-Y
	 */
	public static float GetColorBrightness(Color c)
	{
		return (
				299.0F * c.getRed() +
				587.0F * c.getGreen() +
				114.0F * c.getBlue()
			) / 1000.0F;
	}

	public static float GetLinearisedColorComponent(float fcc)
	{
		if (fcc <= 0.03928F)
			return fcc / 12.92F;
		return (float) Math.pow((fcc + 0.055F) / 1.055F, 2.4F);
	}
}
