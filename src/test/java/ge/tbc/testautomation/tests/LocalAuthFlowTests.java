package ge.tbc.testautomation.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import lombok.Getter;
import lombok.Setter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LocalAuthFlowTests {

    private String BASE = "http://localhost:8086";
    private String email;
    private String password = "Admin_12345";
    private String accessToken;
    private String refreshToken;

    @Getter @Setter
    public static class Tokens {
        private String accessToken;
        private String refreshToken;
    }

    @BeforeClass
    public void setUp() {
        io.restassured.RestAssured.filters(new AllureRestAssured());
        email = "admin_" + System.currentTimeMillis() + "@test.com";
    }

    @Test
    public void register_auth_access_refresh() {
        given()
                .baseUri(BASE)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(Map.of(
                        "firstname", "QA",
                        "lastname", "Admin",
                        "email", email,
                        "password", password,
                        "role", "ADMIN"
                ))
                .when()
                .post("/api/v1/auth/register")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        var tokens =
                given()
                        .baseUri(BASE)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(Map.of(
                                "email", email,
                                "password", password
                        ))
                        .when()
                        .post("/api/v1/auth/authenticate")
                        .then()
                        .statusCode(200)
                        .extract().as(Tokens.class);

        accessToken = tokens.getAccessToken();
        refreshToken = tokens.getRefreshToken();
        assertThat(accessToken, not(isEmptyOrNullString()));
        assertThat(refreshToken, not(isEmptyOrNullString()));

        var msg =
                given()
                        .baseUri(BASE)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(ContentType.JSON)
                        .when()
                        .get("/api/v1/admin/hello")
                        .then()
                        .statusCode(200)
                        .extract().asString();

        assertThat(
                msg,
                containsString("Hello, you have access to a protected resource")
        );

        var refreshed =
                given()
                        .baseUri(BASE)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(Map.of("refreshToken", refreshToken))
                        .when()
                        .post("/api/v1/auth/refresh-token")
                        .then()
                        .statusCode(200)
                        .extract().as(Tokens.class);

        assertThat(refreshed.getAccessToken(), not(isEmptyOrNullString()));
    }
}
