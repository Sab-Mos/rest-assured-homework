package ge.tbc.testautomation.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetStoreTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI  = "https://petstore.swagger.io";
        RestAssured.basePath = "/v2";
    }

    @Test
    public void addPet_thenFindByStatus_andValidate() {
        long id     = Math.abs(new Random().nextLong() % 1_000_000_000L);
        String name = "Buddy_" + id;
        String status = "available";

        JSONObject body = new JSONObject()
                .put("id", id)
                .put("category", new JSONObject().put("id", 10).put("name", "dogs"))
                .put("name", name)
                .put("photoUrls", List.of("http://img/one"))
                .put("tags", List.of(new JSONObject().put("id", 1).put("name", "cute")))
                .put("status", status);


        given()
                .contentType("application/json")
                .body(body.toString())
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo((int)id))
                .body("name", equalTo(name))
                .body("status", equalTo(status));


        JsonPath jp =
                given()
                        .queryParam("status", status)
                        .when()
                        .get("/pet/findByStatus")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();


        List<Integer> ids = jp.getList("id");
        assertThat(ids, hasItem((int)id));


        List<Map<String,Object>> pets = jp.getList("$");
        Map<String,Object> mine = pets.stream()
                .filter(p -> String.valueOf(p.get("id")).equals(String.valueOf(id)))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created pet not found in /findByStatus"));

        Assert.assertEquals(String.valueOf(mine.get("id")), String.valueOf(id));
        Assert.assertEquals(mine.get("name"), name);
        Assert.assertEquals(mine.get("status"), status);
    }
}
