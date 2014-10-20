package org.philhosoft.experiments.i18n;

import javax.swing.*;
import java.util.Date;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The class.
 */
public class LocalizedDateFormatTest
{
	/* The entry point */
	public static void main(String[] args)
	{
		// turn bold fonts off in metal
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		Date today = new Date();
		Locale lc_es_ES = new Locale("es", "ES");
		Locale lc_en_US = new Locale("en", "US");
		Locale lc_zh_TW = new Locale("zh", "TW");


		DateFormat df_s = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat df_l = DateFormat.getDateInstance(DateFormat.LONG);
		DateFormat df_f = DateFormat.getDateInstance(DateFormat.FULL);

		DateFormat df_s_es_ES = DateFormat.getDateInstance(DateFormat.SHORT, lc_es_ES);
		DateFormat df_l_es_ES = DateFormat.getDateInstance(DateFormat.LONG, lc_es_ES);
		DateFormat df_f_es_ES = DateFormat.getDateInstance(DateFormat.FULL, lc_es_ES);
/* US */
		DateFormat df_s_en_US = DateFormat.getDateInstance(DateFormat.SHORT, lc_en_US);
		DateFormat df_l_en_US = DateFormat.getDateInstance(DateFormat.LONG, lc_en_US);
		DateFormat df_f_en_US = DateFormat.getDateInstance(DateFormat.FULL, lc_en_US);
/* TW */
		DateFormat df_s_zh_TW = DateFormat.getDateInstance(DateFormat.SHORT, lc_zh_TW);
		DateFormat df_l_zh_TW = DateFormat.getDateInstance(DateFormat.LONG, lc_zh_TW);
		DateFormat df_f_zh_TW = DateFormat.getDateInstance(DateFormat.FULL, lc_zh_TW);


		String dfp_s = GetDateFormatPattern(df_s);
		String dfp_l = GetDateFormatPattern(df_l);
		String dfp_f = GetDateFormatPattern(df_f);

		String dfp_s_es_ES = GetDateFormatPattern(df_s_es_ES);
		String dfp_l_es_ES = GetDateFormatPattern(df_l_es_ES);
		String dfp_f_es_ES = GetDateFormatPattern(df_f_es_ES);

		String dfp_s_en_US = GetDateFormatPattern(df_s_en_US);
		String dfp_l_en_US = GetDateFormatPattern(df_l_en_US);
		String dfp_f_en_US = GetDateFormatPattern(df_f_en_US);

		String dfp_s_zh_TW = GetDateFormatPattern(df_s_zh_TW);
		String dfp_l_zh_TW = GetDateFormatPattern(df_l_zh_TW);
		String dfp_f_zh_TW = GetDateFormatPattern(df_f_zh_TW);


		String date_s = df_s.format(today);
		String date_l = df_l.format(today);
		String date_f = df_f.format(today);

		String date_s_es_ES = df_s_es_ES.format(today);
		String date_l_es_ES = df_l_es_ES.format(today);
		String date_f_es_ES = df_f_es_ES.format(today);

		String date_s_en_US = df_s_en_US.format(today);
		String date_l_en_US = df_l_en_US.format(today);
		String date_f_en_US = df_f_en_US.format(today);

		String date_s_zh_TW = df_s_zh_TW.format(today);
		String date_l_zh_TW = df_l_zh_TW.format(today);
		String date_f_zh_TW = df_f_zh_TW.format(today);


		JLabel label_s = new JLabel("Locale short: (" + dfp_s + ") " + date_s);
		JLabel label_l = new JLabel("Locale long: (" + dfp_l + ") " + date_l);
		JLabel label_f = new JLabel("Locale full: (" + dfp_f + ") " + date_f);

		JLabel label_s_es_ES = new JLabel("es_ES short: (" + dfp_s_es_ES + ") " + date_s_es_ES);
		JLabel label_l_es_ES = new JLabel("es_ES long: (" + dfp_l_es_ES + ") " + date_l_es_ES);
		JLabel label_f_es_ES = new JLabel("es_ES full: (" + dfp_f_es_ES + ") " + date_f_es_ES);

		JLabel label_s_en_US = new JLabel("en_US short: (" + dfp_s_en_US + ") " + date_s_en_US);
		JLabel label_l_en_US = new JLabel("en_US long: (" + dfp_l_en_US + ") " + date_l_en_US);
		JLabel label_f_en_US = new JLabel("en_US full: (" + dfp_f_en_US + ") " + date_f_en_US);

		JLabel label_s_zh_TW = new JLabel("zh_TW short: (" + dfp_s_zh_TW + ") " + date_s_zh_TW);
		JLabel label_l_zh_TW = new JLabel("zh_TW long: (" + dfp_l_zh_TW + ") " + date_l_zh_TW);
		JLabel label_f_zh_TW = new JLabel("zh_TW full: (" + dfp_f_zh_TW + ") " + date_f_zh_TW);


		// Display panel
		JPanel p = new JPanel(new GridLayout(14, 4));
/*
		p.add(label_s);
		p.add(label_l);
		p.add(label_f);

		p.add(label_s_es_ES);
		p.add(label_l_es_ES);
		p.add(label_f_es_ES);

		p.add(label_s_en_US);
		p.add(label_l_en_US);
		p.add(label_f_en_US);

		p.add(label_s_zh_TW);
		p.add(label_l_zh_TW);
		p.add(label_f_zh_TW);
*/

		p.add(label_s);
		p.add(label_s_es_ES);
		p.add(label_s_en_US);
		p.add(label_s_zh_TW);

		p.add(new JLabel());

		p.add(label_l);
		p.add(label_l_es_ES);
		p.add(label_l_en_US);
		p.add(label_l_zh_TW);

		p.add(new JLabel());

		p.add(label_f);
		p.add(label_f_es_ES);
		p.add(label_f_en_US);
		p.add(label_f_zh_TW);

		JOptionPane pane = new JOptionPane(p);
		pane.setOptions(new Object[] { "OK" });

		JDialog dialog = pane.createDialog(null, "Test Dialog");
		dialog.setVisible(true);
		System.exit(0);
	}

	static String GetDateFormatPattern(DateFormat df)
	{
		SimpleDateFormat sdf = (SimpleDateFormat) df;
		return sdf.toPattern();
	}
}
