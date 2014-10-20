package org.philhosoft.experiments.libraries.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadPages
{
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		Document document = Jsoup.connect("http://www.sans-abris-sdf-exclus.com/pages/poubelles-7527411.html")
				  .userAgent("Mozilla")
				  .timeout(3000)
				  .get();
		System.out.println(document.title());
		Element page = document.select("div.page").first();
		Element socialShare = page.select("div.socialShare").first();
		socialShare.remove();
		Element socialPopup = page.select("div.socialPopup").first();
		socialPopup.remove();
		Elements scripts = document.select("script");
		scripts.remove();
		System.out.println(page);
	}
}
