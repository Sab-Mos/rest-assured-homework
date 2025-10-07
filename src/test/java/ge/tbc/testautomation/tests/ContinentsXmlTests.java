package ge.tbc.testautomation.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.xml.XmlPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ContinentsXmlTests {

    @BeforeClass
    public void cfg() {
        io.restassured.RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void validateContinentsXml() {
        String xml =
                given()
                        .contentType("text/xml; charset=utf-8")
                        .header("SOAPAction",
                                "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/ListOfContinentsByName")
                        .body("""
          <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
              <ListOfContinentsByName xmlns="http://www.oorsprong.org/websamples.countryinfo"/>
            </soap:Body>
          </soap:Envelope>
        """)
                        .when()
                        .post("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso")
                        .then()
                        .statusCode(200)
                        .extract().asString();

        XmlPath xp = new XmlPath(xml);

        List<String> names = xp.getList(
                "Envelope.Body.ListOfContinentsByNameResponse.ListOfContinentsByNameResult.tContinent.sName");
        List<String> codes = xp.getList(
                "Envelope.Body.ListOfContinentsByNameResponse.ListOfContinentsByNameResult.tContinent.sCode");

        assertThat(names, is(not(empty())));

        assertThat(names, hasItems(
                "Africa", "Antarctica", "Asia", "Europe", "North America", "Oceania", "South America"
        ));

        String lastName = xp.getString(
                "Envelope.Body.ListOfContinentsByNameResponse.ListOfContinentsByNameResult.tContinent[-1].sName");
        assertThat(lastName, not(isEmptyOrNullString()));

        assertThat(names, hasItems("Africa", "Antarctica", "Asia", "Europe", "North America", "Oceania", "South America"));

        assertThat(names, everyItem(not(matchesPattern(".*\\d.*"))));

        assertThat(new HashSet<>(names).size(), equalTo(names.size()));

        String oceaniaCode = xp.getString(
                "Envelope.Body.ListOfContinentsByNameResponse.ListOfContinentsByNameResult.tContinent.find { it.sName == 'Oceania' }.sCode");
        assertThat(oceaniaCode, startsWith("O"));

        List<String> a_ca = xp.getList(
                "Envelope.Body.ListOfContinentsByNameResponse.ListOfContinentsByNameResult.tContinent.findAll { it.sName =~ /^A.*ca$/ }.sName");
        assertThat(a_ca, hasItems("Africa", "Antarctica"));

        assertThat(codes, everyItem(matchesPattern("^[A-Z]{2}$")));
    }
}
