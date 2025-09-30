package ge.tbc.testautomation.tests;

import data.models.booker.*;
import data.models.bookstore.*;
import data.models.escuela.*;
import org.testng.annotations.Test;
import services.EscuelaApi;
import services.RestfulBookerApi;
import services.BookstoreApi;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthAndMappingTests {

    @Test
    public void escuela_createLoginProfile_and_validate() {
        EscuelaApi api = new EscuelaApi();

        String email = "nika_" + System.currentTimeMillis() + "@mail.com";
        data.model.request.UserCreateRequest newUser = new data.model.request.UserCreateRequest(
                "Nika", email, "test1234", "https://api.lorem.space/image/face?w=200&h=200"
        );

        User created = api.createUser(newUser);

        AuthLoginResponse auth = api.login(new AuthLoginRequest(email, "test1234"));
        assertThat(auth.access_token, not(isEmptyOrNullString()));
        assertThat(auth.refresh_token, not(isEmptyOrNullString()));

        Profile profile = api.profile(auth.access_token);
        api.validateProfileMatches(profile, created);
    }

    @Test
    public void restfulBooker_full_flow_create_patch_delete_and_validate() {
        RestfulBookerApi api = new RestfulBookerApi();

        String token = api.token("admin", "password123");
        assertThat(token, not(isEmptyOrNullString()));

        BookingRequest create = new BookingRequest();
        create.firstname = "Nika";
        create.lastname  = "Z";
        create.totalprice = 111;
        create.depositpaid = true;
        create.bookingdates = new BookingDates("2025-10-01", "2025-10-05");
        create.additionalneeds = "Breakfast";

        int id = api.createBooking(create);
        BookingRequest got = api.getBooking(id);
        assertThat(got.firstname, equalTo("Nika"));
        assertThat(got.lastname,  equalTo("Z"));

        BookingRequest patch = new BookingRequest();
        patch.firstname = "NikaUpdated";
        patch.lastname  = "ZUpdated";
        patch.bookingdates = new BookingDates("2025-11-01", "2025-11-10");

        BookingRequest afterPatch = api.partialUpdate(id, token, patch);
        assertThat(afterPatch.firstname, equalTo("NikaUpdated"));
        assertThat(afterPatch.lastname,  equalTo("ZUpdated"));

        BookingRequest gotAfter = api.getBooking(id);
        assertThat(gotAfter.firstname, equalTo("NikaUpdated"));
        assertThat(gotAfter.lastname,  equalTo("ZUpdated"));

        api.deleteBooking(id, token);

    }

    @Test
    public void bookstore_deserialize_and_validate_pages_and_authors() {
        BookstoreApi api = new BookstoreApi();
        BooksResponse resp = api.books();

        List<Book> list = resp.books;
        assertThat(list, is(not(empty())));


        for (Book b : list) {
            assertThat("Book " + b.title + " has too many pages", b.pages, lessThan(1000));
        }


        int n = list.size();
        String expectedLastAuthor     = "Nicholas C. Zakas";
        String expectedSecondLastAuth = "Marijn Haverbeke";

        assertThat(list.get(n - 1).author, equalTo(expectedLastAuthor));
        assertThat(list.get(n - 2).author, equalTo(expectedSecondLastAuth));
    }
}
