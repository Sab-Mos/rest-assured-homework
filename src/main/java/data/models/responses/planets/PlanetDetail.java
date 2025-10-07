package data.models.responses.planets;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanetDetail {
    private String name;
    private String rotation_period;
    private String orbital_period;
    private String diameter;
    private String climate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime created;
}
