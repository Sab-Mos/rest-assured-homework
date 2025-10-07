// src/main/java/data/models/escuela/Profile.java
package data.models.escuela;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
    public int id;
    public String name;
    public String email;
    public String role;
    public String avatar;
}
