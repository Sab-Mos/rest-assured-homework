package ge.tbc.testautomation.tests;

import data.models.requests.petstore.OrderRequest;
import data.models.responses.petstore.OrderResponse;
import org.testng.annotations.Test;
import services.petstore.StoreApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PetStoreNewTests {

    @Test
    public void createOrder_validatePojo() {
        var api = new StoreApi();

        OrderRequest req = OrderRequest.builder()
                .id(123456L)
                .petId(999L)
                .quantity(2)
                .shipDate("2025-01-01T00:00:00.000Z")
                .status("placed")
                .complete(null)
                .build();

        OrderResponse resp = api.createOrder(req);

        assertThat(resp.getId(), equalTo(123456L));
        assertThat(resp.getPetId(), equalTo(999L));
        assertThat(resp.getQuantity(), equalTo(2));
        assertThat(resp.getStatus(), equalTo("placed"));
        assertThat(resp.getShipDate(), containsString("2025-01-01"));
    }
}
