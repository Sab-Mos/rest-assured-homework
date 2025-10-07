package ge.tbc.testautomation.tests;

import com.example.springboot.soap.interfaces.AddEmployeeRequest;
import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.GetEmployeeByIdRequest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.xml.XmlPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.Marshall;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SoapEmployeesTests {

    private static final String SOAP_ENDPOINT = "http://localhost:8087/ws";

    @BeforeClass
    public void cfg() {
        io.restassured.RestAssured.filters(new AllureRestAssured());
    }

    private static String addEmployeeBody(long id, String name) {
        try {
            var info = new EmployeeInfo();
            info.setEmployeeId(id);
            info.setName(name);
            info.setDepartment("QA");
            info.setPhone("555-000");
            info.setAddress("Main St 1");
            info.setSalary(new java.math.BigDecimal("1234.56"));
            info.setEmail("qa@example.com");
            var date = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendarDate(1999, 10, 7, DatatypeConstants.FIELD_UNDEFINED);
            info.setBirthDate(date);

            var req = new AddEmployeeRequest();
            req.setEmployeeInfo(info);
            return Marshall.marshallSoapRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getByIdBody(long id) {
        var req = new GetEmployeeByIdRequest();
        req.setEmployeeId(id);
        return Marshall.marshallSoapRequest(req);
    }

    @Test
    public void addEmployee_shouldSucceed() {
        long id = System.currentTimeMillis() % 1_000_000;

        String resp =
                given()
                        .contentType("text/xml; charset=utf-8")
                        .body(addEmployeeBody(id, "John QA"))
                        .when()
                        .post(SOAP_ENDPOINT)
                        .then()
                        .statusCode(200)
                        .extract().asString();

        XmlPath xp = new XmlPath(resp);
        String status = xp.getString("Envelope.Body.addEmployeeResponse.serviceStatus.status");
        String message = xp.getString("Envelope.Body.addEmployeeResponse.serviceStatus.message");

        assertThat(status, equalTo("SUCCESS"));
        assertThat(message, anyOf(equalTo("Content Added Successfully"), containsString("Added Successfully")));
        assertThat(xp.getLong("Envelope.Body.addEmployeeResponse.employeeInfo.employeeId"), equalTo(id));
        assertThat(xp.getString("Envelope.Body.addEmployeeResponse.employeeInfo.name"), equalTo("John QA"));
    }

    @Test
    public void getEmployeeById_shouldMatch() {
        long id = System.currentTimeMillis() % 1_000_000;

        given()
                .contentType("text/xml; charset=utf-8")
                .body(addEmployeeBody(id, "Jane QA"))
                .when()
                .post(SOAP_ENDPOINT)
                .then()
                .statusCode(200);

        String resp =
                given()
                        .contentType("text/xml; charset=utf-8")
                        .body(getByIdBody(id))
                        .when()
                        .post(SOAP_ENDPOINT)
                        .then()
                        .statusCode(200)
                        .extract().asString();

        XmlPath xp = new XmlPath(resp);
        assertThat(xp.getLong("Envelope.Body.getEmployeeByIdResponse.employeeInfo.employeeId"), equalTo(id));
        assertThat(xp.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.name"), equalTo("Jane QA"));
        assertThat(xp.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.department"), equalTo("QA"));
    }
}
