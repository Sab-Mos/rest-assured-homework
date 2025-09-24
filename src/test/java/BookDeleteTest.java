package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookDeleteTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI  = "https://bookstore.toolsqa.com";
        RestAssured.basePath = "/BookStore/v1";
    }

    @Test
    public void deleteBook() {
        String payload = """
      { "isbn": "9781449325862", "userId": "dummy-user" }
    """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .delete("/Book")
                .then()
                .statusCode(401)
                .contentType(containsString("application/json"))
                .body("message", equalTo("User not authorized!"));
    }
}
