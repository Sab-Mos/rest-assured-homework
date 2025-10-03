package data.models.responses.escuela;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponse {
    private Integer id;
    private String name;
    private String email;
    private String role;
    private String avatar;
}
