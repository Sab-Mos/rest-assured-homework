package ge.tbc.testautomation.tests;

import data.models.responses.planets.PlanetDetail;
import data.models.responses.planets.PlanetsListResponse;
import org.testng.annotations.Test;
import services.planets.SwapiApi;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SwapiPlanetsTests {

    @Test
    public void mostRecentThree_and_validations_and_topRotation() {
        var api = new SwapiApi();
        PlanetsListResponse list = api.planetsPage();

        assertThat(list.getResults(), is(not(empty())));
        assertThat(list.getMessage(), containsString("ok"));


        List<PlanetDetail> details = list.getResults().stream()
                .map(r -> api.planetByUrl(r.getUrl()))
                .toList();


        details.forEach(d -> assertThat(d.getName(), not(isEmptyOrNullString())));

        details.forEach(d -> assertThat(d.getCreated(), notNullValue()));

        details.forEach(d -> assertThat(d.getClimate(), not(isEmptyOrNullString())));


        List<PlanetDetail> mostRecent = details.stream()
                .sorted(Comparator.comparing(PlanetDetail::getCreated).reversed())
                .limit(3)
                .toList();
        System.out.println("Most recent: " + mostRecent);


        PlanetDetail top = details.stream()
                .filter(d -> d.getRotation_period() != null && d.getRotation_period().matches("\\d+"))
                .max(Comparator.comparingInt(d -> Integer.parseInt(d.getRotation_period())))
                .orElseThrow();
        assertThat(top.getRotation_period(), matchesPattern("\\d+"));
        assertThat(Integer.parseInt(top.getRotation_period()), greaterThan(0));
    }
}
