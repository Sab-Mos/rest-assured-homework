package services.escuela;

import base.BaseApi;
import data.models.escuela.*;                      // User, UserCreateRequest, AuthLogin*, Profile
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EscuelaApi {
    private final RequestSpecification spec = BaseApi.escuelaSpec();

    public User createUser(UserCreateRequest req) { // ✅ correct type
        return given().spec(spec)
                .body(req)
                .when()
                .post("/v1/users")
                .then()
                .statusCode(anyOf(is(201), is(200)))
                .extract()
                .as(User.class);
    }

    public AuthLoginResponse login(AuthLoginRequest req) {
        return given().spec(spec)
                .body(req)
                .when()
                .post("/v1/auth/login")
                .then()
                .statusCode(anyOf(is(200), is(201))) // be tolerant
                .extract()
                .as(AuthLoginResponse.class);
    }

    public Profile profile(String accessToken) {
        return given().spec(spec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/v1/auth/profile")
                .then()
                .statusCode(200)
                .extract()
                .as(Profile.class);
    }

    public void validateProfileMatches(Profile p, User created) {
        assertThat(p.email, equalTo(created.email));
        assertThat(p.name, equalTo(created.name));
        assertThat(p.id, greaterThan(0));
    }
}
