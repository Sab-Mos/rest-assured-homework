package services.petstore;

import base.BaseApi;
import data.models.requests.petstore.OrderRequest;
import data.models.responses.petstore.OrderResponse;

import static io.restassured.RestAssured.given;

public class StoreApi extends BaseApi {
    private static final String BASE = "https://petstore3.swagger.io/api/v3";

    public OrderResponse createOrder(OrderRequest req) {
        return given()
                .spec(spec(BASE))
                .body(req)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200)
                .extract().as(OrderResponse.class);
    }
}
