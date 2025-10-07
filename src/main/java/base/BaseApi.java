package base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApi {

    public static RequestSpecification spec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static RequestSpecification escuelaSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.escuelajs.co")
                .setBasePath("/api")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static RequestSpecification specWithBearer(String baseUri, String token) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .log(LogDetail.URI)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
