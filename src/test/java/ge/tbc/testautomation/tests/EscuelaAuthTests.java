package ge.tbc.testautomation.tests;
import data.models.escuela.UserCreateRequest;
import data.models.requests.escuela.LoginRequest;
import data.models.responses.escuela.ProfileResponse;
import data.models.responses.escuela.TokensResponse;
import org.testng.annotations.Test;
import services.escuela.UsersApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EscuelaAuthTests {

    @Test
    public void createLoginProfile_flow() {
        var api = new UsersApi();

        String email = "qa_" + System.currentTimeMillis() + "@test.com";
        api.createUser(UserCreateRequest.builder()
                .name("QA Student")
                .email(email)
                .password("secret123")
                .avatar("https://picsum.photos/200")
                .build());

        TokensResponse tokens = api.login(LoginRequest.builder()
                .email(email)
                .password("secret123")
                .build());

        assertThat(tokens.getAccessToken(), not(isEmptyOrNullString()));
        assertThat(tokens.getRefreshToken(), not(isEmptyOrNullString()));

        ProfileResponse me = api.profile(tokens.getAccessToken());
        assertThat(me.getEmail(), equalTo(email));
        assertThat(me.getName(), equalTo("QA Student"));
    }
}
