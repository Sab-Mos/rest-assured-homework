// src/main/java/services/restfulbooker/BookingApi.java
package services.restfulbooker;

import data.models.requests.restfulbooker.BookingRequest;
import data.models.responses.restfulbooker.BookingResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingApi {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("https://restful-booker.herokuapp.com")
            .setContentType(ContentType.JSON)   // IMPORTANT
            .setAccept(ContentType.JSON)        // IMPORTANT
            .build();

    public String token(String username, String password) {
        return given()
                .spec(spec)
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");
    }

    public int create(BookingRequest req) {
        return given()
                .spec(spec)
                .body(req)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().path("bookingid");
    }

    public BookingResponse get(int id) {
        return given()
                .spec(spec)
                .when()
                .get("/booking/{id}", id)
                .then()
                .statusCode(200)
                .extract().as(BookingResponse.class);
    }

    public BookingResponse partialUpdate(int id, String token, BookingRequest patch) {
        return given()
                .spec(spec)
                .cookie("token", token)           // restful-booker expects token as Cookie
                .body(patch)
                .when()
                .patch("/booking/{id}", id)
                .then()
                .statusCode(200)
                .extract().as(BookingResponse.class);
    }

    public void delete(int id, String token) {
        given()
                .spec(spec)
                .cookie("token", token)
                .when()
                .delete("/booking/{id}", id)
                .then()
                .statusCode(201); // delete returns 201
    }
}
