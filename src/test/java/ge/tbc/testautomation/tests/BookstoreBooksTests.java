package ge.tbc.testautomation.tests;


import data.models.responses.bookstore.BooksResponse;
import org.testng.annotations.Test;
import services.bookstore.BooksApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookstoreBooksTests {

    @Test
    public void pagesUnder1000_and_lastTwoAuthorsMatch() {
        BooksResponse resp = new BooksApi().all();

        resp.getBooks().forEach(b -> assertThat(b.getPages(), lessThan(1000)));

        int n = resp.getBooks().size();
        String expected2 = "Nicholas C. Zakas";
        String expected1 = "Marijn Haverbeke";

        assertThat(resp.getBooks().get(n-1).getAuthor(), equalTo(expected2));
        assertThat(resp.getBooks().get(n-2).getAuthor(), equalTo(expected1));
    }
}
