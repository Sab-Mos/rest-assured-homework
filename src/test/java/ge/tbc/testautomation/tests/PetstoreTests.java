package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetstoreTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI  = "https://petstore.swagger.io";
        RestAssured.basePath = "/v2";
    }


    @Test
    public void createOrder_andValidateFields() {
        String body = """
      {
        "id": 987654321,
        "petId": 123456789,
        "quantity": 2,
        "shipDate": "2025-01-01T00:00:00.000Z",
        "status": "placed",
        "complete": true
      }
    """;

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/store/order")
                .then()
                .statusCode(anyOf(is(200), is(201))) // petstore usually 200
                .contentType(containsString("application/json"))
                .body("$", allOf(
                        hasKey("id"), hasKey("petId"), hasKey("quantity"),
                        hasKey("shipDate"), hasKey("status"), hasKey("complete")
                ))
                .body("id", equalTo(987654321))
                .body("petId", equalTo(123456789))
                .body("quantity", equalTo(2))
                .body("status", equalTo("placed"))
                .body("complete", equalTo(true));
    }


    @Test
    public void updateExistingPetWithForm_andValidateApiResponse() {

        List<Map<String, Object>> pets = given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(200)
                .extract().path("$");

        Assert.assertTrue(pets != null && !pets.isEmpty(), "No available pets returned");
        Object idObj = pets.get(0).get("id");
        String petId = String.valueOf(idObj);


        given()
                .contentType("application/x-www-form-urlencoded")
                .pathParam("petId", petId)
                .formParam("name", "MittensUpdated")
                .formParam("status", "sold")
                .when()
                .post("/pet/{petId}")
                .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("$", allOf(hasKey("code"), hasKey("type"), hasKey("message")));
    }


    @Test
    public void getNonexistentOrder_returns404WithCode() {
        long badOrderId = 999999999999L;

        given()
                .when()
                .get("/store/order/{orderId}", badOrderId)
                .then()
                .statusCode(404)
                .contentType(containsString("application/json"))
                .body("code", anyOf(equalTo(404), equalTo(1)))  // petstore often uses 1
                .body("message", anyOf(containsStringIgnoringCase("not found"), not(isEmptyOrNullString())));
    }


    @Test
    public void userLogin_extractTenDigitSessionId() {
        String message = given()
                .queryParam("username", "user1")
                .queryParam("password", "pass1")
                .when()
                .get("/user/login")
                .then()
                .statusCode(200)
                .contentType(anyOf(containsString("application/json"), containsString("text/plain")))
                .body("message", containsString("logged in user session"))
                .extract()
                .path("message");


        Matcher m = Pattern.compile("(\\d{10,})").matcher(message);
        Assert.assertTrue(m.find(), "Expected at least 10 digits in: " + message);
        String digits = m.group(1);
        String first10 = digits.substring(0, 10);
        System.out.println("Extracted 10-digit session id: " + first10);
        Assert.assertEquals(first10.length(), 10);
    }
}
