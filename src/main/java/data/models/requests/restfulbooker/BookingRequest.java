package data.models.requests.restfulbooker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRequest {
    private String firstname;
    private String lastname;
    private Integer totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    @JsonIgnore
    private Double saleprice;

    private String passportNo;

    @Data @Builder
    public static class BookingDates {
        private String checkin;
        private String checkout;
    }
}
