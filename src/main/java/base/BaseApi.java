
package base;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
    public static RequestSpecification escuelaSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.escuelajs.co")
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
