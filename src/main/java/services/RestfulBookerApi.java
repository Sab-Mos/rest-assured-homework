package services;

import data.models.booker.*;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RestfulBookerApi {
    private final String base = "https://restful-booker.herokuapp.com";

    public String token(String username, String password) {
        BookerTokenResponse tr =
                given()
                        .baseUri(base)
                        .contentType(ContentType.JSON)
                        .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                        .when()
                        .post("/auth")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(BookerTokenResponse.class);
        return tr.token;
    }

    public int createBooking(BookingRequest req) {
        BookingResponse br =
                given()
                        .baseUri(base)
                        .contentType(ContentType.JSON)
                        .body(req)
                        .when()
                        .post("/booking")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(BookingResponse.class);
        return br.bookingid;
    }

    public BookingRequest getBooking(int id) {
        return given()
                .baseUri(base)
                .when()
                .get("/booking/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingRequest.class);
    }

    public BookingRequest partialUpdate(int id, String token, BookingRequest patch) {
        return given()
                .baseUri(base)
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(patch)
                .when()
                .patch("/booking/{id}", id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingRequest.class);
    }

    public void deleteBooking(int id, String token) {
        given()
                .baseUri(base)
                .cookie("token", token)
                .when()
                .delete("/booking/{id}", id)
                .then()
                .statusCode(anyOf(is(201), is(200)));
    }
}
