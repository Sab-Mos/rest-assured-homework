package ge.tbc.testautomation.tests;
import data.models.requests.restfulbooker.BookingRequest;
import data.models.requests.restfulbooker.BookingRequest.BookingDates;
import data.models.responses.restfulbooker.BookingResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import services.restfulbooker.BookingApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RestfulBookerTests {

    @DataProvider
    public Object[][] bookings() {
        var b1 = BookingRequest.builder()
                .firstname("Nika")
                .lastname("Z")
                .totalprice(120)
                .depositpaid(true)
                .bookingdates(BookingDates.builder().checkin("2025-10-01").checkout("2025-10-05").build())
                .additionalneeds("Breakfast")
                .saleprice(99.99)
                .passportNo(null)
                .build();

        var b2 = BookingRequest.builder()
                .firstname("Ana")
                .lastname("K")
                .totalprice(222)
                .depositpaid(false)
                .bookingdates(BookingDates.builder().checkin("2025-11-01").checkout("2025-11-07").build())
                .additionalneeds("Late checkout")
                .passportNo(null)
                .build();

        return new Object[][]{{b1}, {b2}};
    }

    @Test(dataProvider = "bookings")
    public void create_update_delete_validate(BookingRequest req) {
        var api = new BookingApi();
        var token = api.token("admin", "password123");

        int id = api.create(req);
        BookingResponse got = api.get(id);
        assertThat(got.getFirstname(), equalTo(req.getFirstname()));
        assertThat(got.getTotalprice(), equalTo(req.getTotalprice()));

        var partial = BookingRequest.builder()
                .firstname(req.getFirstname() + "_UPD")
                .totalprice(req.getTotalprice() + 10)
                .build();

        BookingResponse upd = api.partialUpdate(id, token, partial);
        assertThat(upd.getFirstname(), endsWith("_UPD"));
        assertThat(upd.getTotalprice(), equalTo(req.getTotalprice() + 10));

        api.delete(id, token);
    }
}
