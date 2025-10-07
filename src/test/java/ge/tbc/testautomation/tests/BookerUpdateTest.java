package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookerUpdateTest {

    @Test
    public void updateBooking_plainFlow() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        JSONObject createDates = new JSONObject()
                .put("checkin", "2025-10-01")
                .put("checkout", "2025-10-07");

        JSONObject createBody = new JSONObject()
                .put("firstname", "Nika")
                .put("lastname", "Z")
                .put("totalprice", 123)
                .put("depositpaid", true)
                .put("bookingdates", createDates)
                .put("additionalneeds", "Breakfast");

        int bookingId = given()
                .contentType("application/json")
                .body(createBody.toString())
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        JSONObject authBody = new JSONObject()
                .put("username", "admin")
                .put("password", "password123");

        String token = given()
                .contentType("application/json")
                .body(authBody.toString())
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        JSONObject newDates = new JSONObject()
                .put("checkin", "2025-11-01")
                .put("checkout", "2025-11-10");

        JSONObject updateBody = new JSONObject()
                .put("firstname", "NikaUpdated")
                .put("lastname", "ZUpdated")
                .put("totalprice", 222)
                .put("depositpaid", false)
                .put("bookingdates", newDates)
                .put("additionalneeds", "Late Checkout");

        Response resp = given()
                .contentType("application/json")
                .cookie("token", token)
                .body(updateBody.toString())
                .when()
                .put("/booking/{id}", bookingId)
                .then()
                .extract().response();

        int status = resp.getStatusCode();
        Assert.assertTrue(status == 200 || status == 201, "Unexpected status: " + status);
        if (status == 201) {
            System.out.println("Response (201): " + resp.asString());
        }

        resp.then()
                .statusCode(status)
                .body("firstname", equalTo("NikaUpdated"))
                .body("lastname",  equalTo("ZUpdated"))
                .body("totalprice", equalTo(222));
    }
}
