package services;

import data.models.bookstore.BooksResponse;

import static io.restassured.RestAssured.given;

public class BookstoreApi {
    public BooksResponse books() {
        return given()
                .baseUri("https://bookstore.toolsqa.com")
                .basePath("/BookStore/v1")
                .when()
                .get("/Books")
                .then()
                .statusCode(200)
                .extract()
                .as(BooksResponse.class);
    }
}
