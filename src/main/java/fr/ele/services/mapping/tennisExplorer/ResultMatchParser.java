package fr.ele.services.mapping.tennisExplorer;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.ele.model.Bet;
import fr.ele.model.ref.BetType;
import fr.ele.model.ref.Match;
import fr.ele.services.mapping.SynchronizerContext;

public class ResultMatchParser extends MatchParser {

	private Element elementTmp;
	private String bookie;

	@Override
	protected List<Bet> doParse(Element t, Match match,
			SynchronizerContext context) {
		// Match Winner structure <tr> 3 elements : 1/nothing :
		// 2/ Home Winner 3/ Away Winner String bookie = "";
		Elements tnode = t.select("tr");
		List<Bet> bets = new LinkedList<Bet>();
		BetType betType = context.findBetType("1x2");
		

		for (Element tnodeelement : tnode) {
			Elements tnode3 = tnodeelement.select("td");
			if (tnodeelement.attr("class").equals("one")
					|| tnodeelement.attr("class").equals("two")) {
				for (Element tnode3element : tnode3) {
					if (tnode3element.attr("class").equals("first tl")) {
						bookie = tnode3element.text();
					}

					if (tnode3element.attr("class").equals("k1")) {
						String odd = tnode3element.text();
						createOdd(match, context, bookie, bets, betType, odd,
								"1");
					}
					if (tnode3element.attr("class").equals("k2")) {
						String odd = tnode3element.text();
						createOdd(match, context, bookie, bets, betType, odd,
								"2");
					}

				}
			}
		}

		return bets;

	}

}
