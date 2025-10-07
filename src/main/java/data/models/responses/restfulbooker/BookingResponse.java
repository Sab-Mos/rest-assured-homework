package data.models.responses.restfulbooker;

import lombok.Data;

@Data
public class BookingResponse {
    private String firstname;
    private String lastname;
    private Integer totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    @Data
    public static class BookingDates {
        private String checkin;
        private String checkout;
    }
}
