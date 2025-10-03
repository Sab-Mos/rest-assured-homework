package services.bookstore;

import base.BaseApi;
import data.models.responses.bookstore.BooksResponse;

import static io.restassured.RestAssured.given;

public class BooksApi extends BaseApi {
    private static final String BASE = "https://bookstore.toolsqa.com";

    public BooksResponse all() {
        return given()
                .spec(spec(BASE))
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .statusCode(200)
                .extract().as(BooksResponse.class);
    }
}
