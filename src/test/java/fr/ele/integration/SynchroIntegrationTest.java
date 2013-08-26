package fr.ele.integration;

import java.io.BufferedInputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import fr.ele.feeds.betclick.dto.SportsBcDto;
import fr.ele.feeds.expekt.ExpektUnmarshallingTest;
import fr.ele.feeds.expekt.dto.PunterOdds;
import fr.ele.model.Bet;
import fr.ele.model.ref.BookMaker;
import fr.ele.services.mapping.BetclickSynchronizer;
import fr.ele.services.mapping.ExpektSynchronizer;
import fr.ele.services.repositories.BetRepository;
import fr.ele.services.repositories.MatchRepository;

public class SynchroIntegrationTest extends AbstractSuperbetIntegrationTest {

    @Autowired
    private BetclickSynchronizer betclickSynchronizer;

    @Autowired
    private ExpektSynchronizer expektSynchronizer;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private BetRepository betRepository;

    @Override
    @Before
    public void initializeDatas() {
        super.initializeDatas();
    }

    @Test
    public void test() throws Throwable {
        JAXBContext jaxbContext = JAXBContext.newInstance(SportsBcDto.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        BufferedInputStream inputStream = new BufferedInputStream(
                BetclickIntegrationTest.class
                        .getResourceAsStream("/fr/ele/feeds/betclick/odds_en.xml"));
        betclickSynchronizer.synchronize((SportsBcDto) unmarshaller
                .unmarshal(inputStream));

        jaxbContext = JAXBContext.newInstance(PunterOdds.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        inputStream = new BufferedInputStream(
                ExpektUnmarshallingTest.class
                        .getResourceAsStream("/fr/ele/feeds/expekt/exportServlet.xml"));
        expektSynchronizer.synchronize((PunterOdds) unmarshaller
                .unmarshal(inputStream));

        // Assert.assertNotNull(matchRepository.findByCode(code));
        List<Bet> bets = betRepository.findAll();
        System.err.println(bets.size());
        Multimap<String, BookMaker> map = HashMultimap.create();
        for (Bet bet : bets) {
            map.put(bet.getRefKey().getMatch().getCode(), bet.getBookMaker());
        }
        for (String code : map.keySet()) {
            if (map.get(code).size() > 1) {
                // TODO try to find common match to validate synchro algos
                System.err.println(code);
            }
        }
    }
}