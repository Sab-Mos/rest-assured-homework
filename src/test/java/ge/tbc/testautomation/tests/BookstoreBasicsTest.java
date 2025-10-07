package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookstoreBasicsTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI  = "https://bookstore.toolsqa.com";
        RestAssured.basePath = "/BookStore/v1";
    }

    @Test
    public void validateBooks_pagesAndAuthors() {
        given()
                .when()
                .get("/Books")
                .then()
                .statusCode(200)
                .body("books.pages", everyItem(lessThan(1000)))
                .body("books[0].author", equalTo("Richard E. Silverman"))
                .body("books[1].author", equalTo("Addy Osmani"));
    }
}
