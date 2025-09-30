package data.model.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateRequest {
    public String name;
    public String email;
    public String password;
    public String avatar;

    public UserCreateRequest() {}
    public UserCreateRequest(String name, String email, String password, String avatar) {
        this.name = name; this.email = email; this.password = password; this.avatar = avatar;
    }
}
