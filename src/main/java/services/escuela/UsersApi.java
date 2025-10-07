// src/main/java/services/escuela/UsersApi.java
package services.escuela;

import base.BaseApi;
import data.models.escuela.UserCreateRequest;
import data.models.requests.escuela.LoginRequest;
import data.models.responses.escuela.ProfileResponse;
import data.models.responses.escuela.TokensResponse;

import static io.restassured.RestAssured.given;

public class UsersApi extends BaseApi {

    private static final String BASE = "https://api.escuelajs.co/api";

    public Integer createUser(UserCreateRequest req) {
        return given()
                .spec(spec(BASE))
                .body(req)
                .when()
                .post("/v1/users")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    public TokensResponse login(LoginRequest req) {
        return given()
                .spec(spec(BASE))
                .body(req)
                .when()
                .post("/v1/auth/login")
                .then()
                .statusCode(201) // Escuelajs returns 201
                .extract().as(TokensResponse.class);
    }

    public ProfileResponse profile(String accessToken) {
        return given()
                .spec(spec(BASE))
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/v1/auth/profile")
                .then()
                .statusCode(200)
                .extract().as(ProfileResponse.class);
    }
}
