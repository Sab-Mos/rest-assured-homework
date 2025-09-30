package data.models.escuela;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthLoginResponse {
    public String access_token;
    public String refresh_token;
}
