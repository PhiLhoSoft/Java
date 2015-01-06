package org.philhosoft.ui;

import java.util.Arrays;

import org.assertj.core.util.VisibleForTesting;

/**
 * Generates HSL colors out of a user name (or whatever string), in a semi-random, but reproducible way:
 * for a given string, we always return the same colors.<br>
 * The version with background and foreground can be used to color these parts for a user icon,
 * taking care of having good contrast between the two colors.<br>
 * The version with one color uses a wider range of saturation and lightness.
 */
public class UserColors
{
	// The idea is to spread the bits of the characters of the name over (at least) 46 bits, in a kind of hash code.
	// We don't use hashCode() because we want a large spreading, even for short names (assumes a minimum of 3 chars).
	// 46 bits = 9 bits (0 to 512) * 2 (hues) + 7 bits (0 to 128) * 4 (saturation and lightness).

	private static final long PRIME = 35_0891; // 19-bit prime number

	// Component bits: number of bits used for making a HSL component
	private static final float MAX_BIT_HUE_VALUE = 512;
	private static final long MASK_HUE_COMPONENT_BITS_SIZE = 9;
	private static final long MASK_HUE_COMPONENT_BITS = 0x1FF;

	private static final float MAX_BIT_PERCENT_VALUE = 128;
	private static final long MASK_PERCENT_COMPONENT_BITS_SIZE = 7;
	private static final long MASK_PERCENT_COMPONENT_BITS = 0x7F;

	private int fgHue, bgHue;
	private int[] percentages = new int[4];
	private int[] sortedPercentages = new int[4];

	public UserColors(String userName)
	{
		long hash = computeHash(userName);
		computeHues(hash);
		computePercentageComponents(hash);
		sortedPercentages = Arrays.copyOf(percentages, percentages.length);
		Arrays.sort(sortedPercentages);
	}

	/**
	 * Returns a HSL color from the given user name.<br>
	 * Avoids too desaturated colors, and keep lightness from being too dark or too light.
	 */
	public String computeHslColor()
	{
		return createHslColor(getColorComponents());
	}

	/**
	 * Returns a HSL color for foreground, from the given user name.<br>
	 * Aims at high saturation, low lightness.
	 */
	public String computeHslForegroundColor()
	{
		return createHslColor(getForegroundColorComponents());
	}

	/**
	 * Returns a HSL color for background, from the given user name.<br>
	 * Aims at low saturation, high lightness.
	 */
	public String computeHslBackgroundColor()
	{
		return createHslColor(getBackgroundColorComponents());
	}

	@VisibleForTesting
	int[] getColorComponents()
	{
		// Hue, Saturation, Lightness
		int[] c =
			{
				scale(fgHue, 0, 360, MAX_BIT_HUE_VALUE),
				scale(percentages[0], 10, 100, MAX_BIT_PERCENT_VALUE),
				scale(percentages[1], 25, 90, MAX_BIT_PERCENT_VALUE)
			};
		return c;
	}

	@VisibleForTesting
	int[] getForegroundColorComponents()
	{
		// Hue, Saturation, Lightness
		// High saturation, low lightness for foreground
		int[] c =
			{
				scale(fgHue, 0, 360, MAX_BIT_HUE_VALUE),
				scale(sortedPercentages[3], 20, 100, MAX_BIT_PERCENT_VALUE),
				scale(sortedPercentages[1], 25, 45, MAX_BIT_PERCENT_VALUE)
			};
		return c;
	}

	@VisibleForTesting
	int[] getBackgroundColorComponents()
	{
		// Hue, Saturation, Lightness
		// Low saturation, high lightness for foreground
		int[] c =
			{
				scale(bgHue, 0, 360, MAX_BIT_HUE_VALUE),
				scale(sortedPercentages[0], 20, 100, MAX_BIT_PERCENT_VALUE),
				scale(sortedPercentages[2], 45, 100, MAX_BIT_PERCENT_VALUE)
			};
		return c;
	}

	private long computeHash(String userName)
	{
		long hash = 0;
		for (int i = 0; i < userName.length(); i++)
		{
			// Overflow is OK
			hash = hash * PRIME + userName.charAt(i);
		}
		return hash;
	}

	private void computeHues(long hash)
	{
		fgHue = (int) (hash & MASK_HUE_COMPONENT_BITS);
		bgHue = (int) ((hash >> MASK_HUE_COMPONENT_BITS_SIZE) & MASK_HUE_COMPONENT_BITS);
	}

	private void computePercentageComponents(long hash)
	{
		hash >>= 2 * MASK_HUE_COMPONENT_BITS_SIZE;
		for (int i = 0; i < percentages.length; i++)
		{
			percentages[i] = (int) ((hash >> (i * MASK_PERCENT_COMPONENT_BITS_SIZE)) & MASK_PERCENT_COMPONENT_BITS);
		}
	}

	private String createHslColor(int[] components)
	{
		return "hsl(" + components[0] + "," + components[1] + "%," + components[2] + "%)";
	}

	private int scale(int value, float wantedMin, float wantedMax, float realMax)
	{
		return (int) (wantedMin +  (wantedMax - wantedMin) * value / realMax);
	}
}
