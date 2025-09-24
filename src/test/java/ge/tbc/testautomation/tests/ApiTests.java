package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class ApiTests {


    @Test
    public void retrieveBooks(){
      RestAssured.baseURI ="https://bookstore.toolsqa.com";
      RestAssured.basePath = "/BookStore/v1";

     var res = given()
              .when()
              .get("/Books")
              .then()
             .statusCode(200)
             .body("books", notNullValue())
             .body("books.size()", greaterThanOrEqualTo(2))
             .extract();

        String isbnFirst = res.path("books[0].isbn");
        String authorFirst = res.path("books[0].author");

        String isbnSecond = res.path("books[1].isbn");
        String authorSecond= res.path("books[1].author");


    }
   @DataProvider(name = "isbnData")
    public Object[][] isbnData() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        RestAssured.basePath = "/BookStore/v1";

        var jp = given()
                .when()
                .get("/Books")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

       var isbns   = jp.getList("books.isbn", String.class);
       var authors = jp.getList("books.author", String.class);

       int n = isbns.size();

       Object[][] rows = new Object[n][3];

       for (int i = 0; i < n; i++) {
           rows[i][0] = i;
           rows[i][1] = isbns.get(i);
           rows[i][2] = authors.get(i);
       }
       return rows;
    }

    @Test(dataProvider = "isbnData")
    public void getBookByIsbn_basicChecks(int index, String isbn, String expectedAuthor) {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        RestAssured.basePath = "/BookStore/v1";

        given()
                .queryParam("ISBN", isbn)
                .when()
                .get("/Book")
                .then()
                .statusCode(200)
                .body("author", equalTo(expectedAuthor))
                .body("title", not(isEmptyOrNullString()))
                .body("isbn", equalTo(isbn))
                .body("publish_date", not(isEmptyOrNullString()))
                .body("pages", greaterThan(0));
    }
}
