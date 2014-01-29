package fr.ele.services.mapping;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import fr.ele.feeds.nordicbet.dto.Odds;

@Service("BetExplorerSynchroniser")
public class BetExplorerSynchroniser extends AbstractSynchronizer<Odds> {

	private final String matchId = "matchid=";
	private int matchCount = 0;

	@Override
	protected long convert(SynchronizerContext context, Odds dto) {
		// TODO Auto-generated method stub
		long nb = 0L;

		try {
			parseNextMatch("http://www.betexplorer.com/next/soccer/"); // For
																		// basketball
			// parseNextMatch("http://www.betexplorer.com/next/volleyball/"); //
			// for
			// Volleyball
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nb;
	}

	private void parseNextMatch(String httpRef) throws Throwable {
		// TODO Auto-generated method stub
		URL website = new URL(httpRef);
		URLConnection urlConnetion = website.openConnection(getProxy(httpRef));
		Document doc = Jsoup
				.parse(urlConnetion.getInputStream(), null, httpRef);
		// Document doc = Jsoup.connect(httpRef).get();
		org.jsoup.select.Elements e = doc.select("tr");

		// Search URL for each match
		for (Element t : e) {
			String test = t.attr("data-dt").replace(",", ":");

			if (t.attr("class").equals("rtitle league-title") == false) {
				Element link = t.select("a").first();

				if (link != null) {
					String linkHref = link.attr("href");
					String Exist = t.select("span.Postponed").text();
					if (linkHref.isEmpty() == false) {

						DateFormat formatter = new SimpleDateFormat(
								"dd:MM:yyyy:hh:mm");
						Date date = formatter.parse(test);
						Date timeSQL = new Date();
						if (date.compareTo(timeSQL) > 0) {
							matchCount++;
							String extract = linkHref.substring(
									linkHref.length() - 9,
									linkHref.length() - 1);
							// Compute URL on gres/ajax-matchodds.php?t=n&b=1x2
							// or
							// b=ou
							// or
							// b=ha
							String linkOdd = "http://www.betexplorer.com/gres/ajax-matchodds.php?t=n&e="
									+ extract + "&b=ou";
							parseMatchId(linkOdd);
							System.out.println(matchCount + "," + linkOdd);
						}

					}
				}
			}
		}
	}

	private long parseMatchId(String httpRef) throws Throwable {
		long nb = 0L;
		URL website = new URL(httpRef);
		URLConnection urlConnetion = website.openConnection(getProxy(httpRef));
		Document docmatch = Jsoup.parse(urlConnetion.getInputStream(), null,
				httpRef);

		if (docmatch.select("tr").isEmpty() == false) {
			org.jsoup.select.Elements e = docmatch.select("tr");
			// One bet Type, one Bookmaker, one match

			for (Element t : e) {

				Element type = t.getElementById("td");
				System.out.println(type);
			}
		}

		return nb;
	}

	@Override
	protected Class<Odds> getDtoClass() {
		// TODO Auto-generated method stub
		return Odds.class;
	}

	private Proxy getProxy(String httpRef) throws Throwable {

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"gecd-proxy.equities.net.intra", 8080));

		return proxy;

	}
}
