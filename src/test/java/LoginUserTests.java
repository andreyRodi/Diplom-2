import clients.ApiClient;
import clients.ResponsesCatalogue;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.UnsuccessfulResponse;
import model.User;
import model.UserRegisteredResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static generators.UserGenerator.*;
import static model.UserCred.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AllureJunit5.class})
public class LoginUserTests {

    private User user;
    private ApiClient apiClient;
    private String accessToken;

    @BeforeEach
    public void setup() {
        apiClient = new ApiClient();
        user = randomUser();
        apiClient.createUser(user);

    }

    //успешный логин
    @Test
    public void loginUserSuccess() {

        Response response = apiClient.loginUser(returnUserCreds(user));
        assertEquals(SC_OK, response.statusCode(), "Response code does not match");
        assertTrue(response.as(UserRegisteredResponse.class).getSuccess(), "В теле ответа отсутствует success = true");

    }

    //логин с неверным логином
    @Test
    public void loginUserWrongLoginFail() {

        Response response = apiClient.loginUser(returnUserCredsWrongLogin(user));

        assertEquals(SC_UNAUTHORIZED, response.statusCode(), "Response code does not match");
        assertEquals(ResponsesCatalogue.getResponseWrongCredentials(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");
    }

    //логин с неверным паролем
    @Test
    public void loginUserWrongPasswordFail() {

        Response response = apiClient.loginUser(returnUserCredsWrongPassword(user));

        assertEquals(SC_UNAUTHORIZED, response.statusCode(), "Response code does not match");
        assertEquals(ResponsesCatalogue.getResponseWrongCredentials(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");
    }


    @AfterEach
    public void deleteData() {

        Response response = apiClient.loginUser(returnUserCreds(user));

        accessToken = response.as(UserRegisteredResponse.class).getAccessToken();
        apiClient.delete(accessToken);
    }

}
