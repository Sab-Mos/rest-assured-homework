package services.planets;

import base.BaseApi;
import data.models.responses.planets.PlanetDetail;
import data.models.responses.planets.PlanetsListResponse;

import static io.restassured.RestAssured.given;

public class SwapiApi extends BaseApi {
    private static final String BASE = "https://swapi.tech/api";

    public PlanetsListResponse planetsPage() {
        return given()
                .spec(spec(BASE))
                .when()
                .get("/planets/?format=json")
                .then()
                .statusCode(200)
                .extract().as(PlanetsListResponse.class);
    }

    public PlanetDetail planetByUrl(String absoluteUrl) {
        return given()
                .accept("application/json")
                .when()
                .get(absoluteUrl)
                .then()
                .statusCode(200)
                .extract().path("result.properties");
    }
}
