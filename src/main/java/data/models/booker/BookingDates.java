package data.models.booker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDates {
    public String checkin;
    public String checkout;

    public BookingDates() {}
    public BookingDates(String checkin, String checkout) {
        this.checkin = checkin; this.checkout = checkout;
    }
}
