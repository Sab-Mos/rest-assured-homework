package data.models.booker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponse {
    public int bookingid;
    public BookingRequest booking;
}
