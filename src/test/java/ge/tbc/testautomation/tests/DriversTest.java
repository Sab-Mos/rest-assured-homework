package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DriversTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.jolpi.ca";
    }

    private List<Map<String, Object>> drivers() {
        JsonPath jp = given()
                .when().get("/ergast/f1/2025/drivers.json")
                .then().statusCode(200)
                .extract().jsonPath();
        return jp.getList("MRData.DriverTable.Drivers");
    }

    private String fullName(Map<String, Object> d) {
        return d.get("givenName") + " " + d.get("familyName");
    }

    @Test
    public void metaChecks_seriesAndSeason() {
        given()
                .when().get("/ergast/f1/2025/drivers.json")
                .then()
                .statusCode(200)
                .body("MRData.series", equalTo("f1"))
                .body("MRData.season", equalTo("2025"));
    }

    @Test
    public void totalMatchesDriversCount() {
        JsonPath jp = given().get("/ergast/f1/2025/drivers.json").then().extract().jsonPath();
        int total = Integer.parseInt(jp.getString("MRData.total"));
        int size  = jp.getList("MRData.DriverTable.Drivers").size();
        assertThat(size, equalTo(total));
    }

    @Test
    public void firstBornBefore1990_checkNamePresent() {
        Map<String, Object> first = drivers().stream()
                .filter(d -> String.valueOf(d.get("dateOfBirth")).compareTo("1990-01-01") < 0)
                .findFirst().orElseThrow();
        assertThat(fullName(first), allOf(notNullValue(), not(isEmptyOrNullString())));
    }

    @Test
    public void bornAfter2000_countAtLeast8() {
        List<String> names = drivers().stream()
                .filter(d -> String.valueOf(d.get("dateOfBirth")).compareTo("2000-01-01") > 0)
                .map(this::fullName)
                .collect(Collectors.toList());
        assertThat(names.size(), greaterThanOrEqualTo(8));
    }

    @Test
    public void frenchDrivers_exactly3() {
        long cnt = drivers().stream()
                .filter(d -> "French".equals(d.get("nationality")))
                .count();
        assertThat((int) cnt, equalTo(3));
    }

    private List<String> findDriversByNationality(String nat) {
        return drivers().stream()
                .filter(d -> nat.equals(d.get("nationality")))
                .map(this::fullName)
                .collect(Collectors.toList());
    }

    @Test
    public void nationalityValidations() {
        assertThat(findDriversByNationality("Dutch"),
                hasItem(containsStringIgnoringCase("Verstappen")));
        assertThat(findDriversByNationality("Brazilian").size(),
                greaterThanOrEqualTo(1));
        assertThat(findDriversByNationality("Canadian"),
                hasItem(equalTo("Lance Stroll")));
    }
}
