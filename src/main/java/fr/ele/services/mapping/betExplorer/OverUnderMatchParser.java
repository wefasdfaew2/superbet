package fr.ele.services.mapping.betExplorer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.ele.model.Bet;
import fr.ele.model.ref.BetType;
import fr.ele.model.ref.Match;
import fr.ele.services.mapping.SynchronizerContext;

public class OverUnderMatchParser extends MatchParser {

    @Override
    protected List<Bet> doParse(Elements elements, Element t, Match match,
            SynchronizerContext context) {
        // Over Under structure <tr> 4 elements : 1/nothing : 2/ref over under
        // 3/ odd Over 4/odd under
        String bookie = "";
        Element tnode = t.select("th").first();
        Elements link = tnode.select("a");
        if (link != null) {
            Element tnode2 = link.select("span").first();

            if (tnode2 != null) {
                Elements link2 = tnode2.select("span");

                if (link2 != null) {
                    bookie = link2.text().replaceAll(" ", "")
                            .replaceAll("/", "").replaceAll("th", "")
                            .replaceAll("span", "").replaceAll("<a", "")
                            .replaceAll("a>", "").replace("(www)", "")
                            .replaceAll("<", "").replaceAll(">", "")
                            .replace(" \\", "").replace("\\", "");
                }
            }
        }

        List<Bet> bets = new LinkedList<Bet>();
        BetType betType = null;
        Iterator<Element> it = elements.iterator();
        it.next();
        String str = it.next().text().replaceAll("/", "").replaceAll("td", "")
                .replaceAll("sets", "").replaceAll("<", "")
                .replaceAll("points", "").replaceAll(">", "")
                .replace(" \\", "").replace("\\", "");
        str = "Over/Under " + str;
        betType = context.findBetType(str);

        String odd = extractOdd(it.next());
        createOdd(match, context, bookie, bets, betType, odd, "Over");

        odd = extractOdd(it.next());
        createOdd(match, context, bookie, bets, betType, odd, "Under");
        return bets;
    }

    @Override
    protected String getUrlExtension() {
        return "&b=ou";
    }

}
