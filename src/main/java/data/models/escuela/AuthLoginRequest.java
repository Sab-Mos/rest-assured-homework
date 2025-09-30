package data.models.escuela;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthLoginRequest {
    public String email;
    public String password;

    public AuthLoginRequest() {}
    public AuthLoginRequest(String email, String password) {
        this.email = email; this.password = password;
    }
}
