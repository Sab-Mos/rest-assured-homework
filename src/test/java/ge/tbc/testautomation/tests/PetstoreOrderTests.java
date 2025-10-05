package ge.tbc.testautomation.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetstoreOrderTests {

    @BeforeClass
    public void setUp() {
        io.restassured.RestAssured.baseURI = "https://petstore3.swagger.io/api/v3";
        io.restassured.RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void createOrder_minimal_and_validate() {
        var body = Map.of(
                "id", System.currentTimeMillis(),
                "petId", 1,
                "quantity", 2,
                "shipDate", OffsetDateTime.now().toString(),
                "status", "placed",
                "complete", true
        );

        var res =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(body)
                        .when()
                        .post("/store/order")
                        .then()
                        .statusCode(200)
                        .extract().as(Map.class);

        assertThat(res.get("status"), is("placed"));
        assertThat((Boolean) res.get("complete"), is(true));
        assertThat(((Number) res.get("quantity")).intValue(), equalTo(2));
    }
}
