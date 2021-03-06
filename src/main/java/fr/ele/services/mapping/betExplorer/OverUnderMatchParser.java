package fr.ele.services.mapping.betExplorer;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.ele.feeds.HtmlBetDto;

public class OverUnderMatchParser extends MatchParser {

    private Element elementTmp;

    @Override
    protected List<HtmlBetDto> doParse(Elements elements, Element t,
            String match, String sport, Date date) {
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

        List<HtmlBetDto> bets = new LinkedList<HtmlBetDto>();
        String betType = null;
        Iterator<Element> it = elements.iterator();
        if (extractActiveOdd(elements.toString()) == false) {
            if (it.hasNext()) {
                it.next();
                String str = it.next().text().replaceAll("/", "")
                        .replaceAll("td", "").replaceAll("<", "")
                        .replaceAll(">", "").replace(" \\", "")
                        .replace("\\", "");
                str = "Over/Under " + str;
                betType = str;
                if (betType != null) {
                    if (it.hasNext()) {
                        elementTmp = it.next();
                        String odd = extractOdd(elementTmp);

                        createOdd(match, bookie, bets, betType, odd, "Over",
                                sport, date);

                        if (it.hasNext()) {
                            elementTmp = it.next();
                            odd = extractOdd(elementTmp);

                            createOdd(match, bookie, bets, betType, odd,
                                    "Under", sport, date);

                        }
                    }
                }
            }
        }
        return bets;
    }

    @Override
    protected String getUrlExtension() {
        return "&b=ou";
    }

}
