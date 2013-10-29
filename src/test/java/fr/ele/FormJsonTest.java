package fr.ele;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import fr.ele.core.search.ui.SearchToUi;
import fr.ele.model.search.BookmakerSearch;

public class FormJsonTest {

    @Test
    public void testSearch() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out,
                SearchToUi.transform(null, BookmakerSearch.class));
    }

    @Test
    public void testReadSearch() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        BookmakerSearch value = mapper
                .readValue(
                        "{\"code\":{\"operator\":\"EQ\",\"criteriaValue\":\"hghjgkj\"},\"id\":{\"operator\":\"EQ\"},\"url\":{\"operator\":\"EQ\"},\"zizicoptere\":{\"operator\":\"EQ\"}}",
                        BookmakerSearch.class);
        value.toString();
    }
}
