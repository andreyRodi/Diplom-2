import clients.ApiClient;
import clients.ResponsesCatalogue;
import generators.UserGenerator;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.UnsuccessfulResponse;
import model.User;
import model.UserRegisteredResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static generators.UserGenerator.*;
import static model.UserCred.returnUserCreds;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AllureJunit5.class})
public class ChangeDataUserTests {

    private User user;
    private ApiClient apiClient;
    private String accessToken;

    @BeforeEach
    public void setup(){
        apiClient = new ApiClient();
    }

    private static Stream<User> userDataProvider() {
        return Stream.of(
                UserGenerator.randomUser(),
                UserGenerator.randomUserWithNameOnly(),
                UserGenerator.randomUserWithPasswordOnly(),
                UserGenerator.randomUserWithEmailOnly()
        );
    }


    @Nested
    class AuthorizedTests {
        @BeforeEach
        public void setup() {
            user = randomUser();
            apiClient.createUser(user);
            Response response = apiClient.loginUser(returnUserCreds(user));
            accessToken = response.as(UserRegisteredResponse.class).getAccessToken();

        }

        //успешное изменение с авторизацией
        @ParameterizedTest
        @MethodSource("ChangeDataUserTests#userDataProvider")
        public void changeUserSuccess(User user) {

            Response response1 = apiClient.updateUser(accessToken, user);

            assertEquals(SC_OK, response1.statusCode());
            assertTrue(response1.as(UserRegisteredResponse.class).getSuccess(), "В теле ответа отсутствует success = true");


            apiClient.delete(accessToken);
        }
    }

    @Nested
    class NonAuthorizedTests {
        //неуспешное изменение без авторизации
        @ParameterizedTest
        @MethodSource("ChangeDataUserTests#userDataProvider")
        public void changeUserWithoutAuthorizationFail(User user) {

            accessToken = "invalid";
            Response response = apiClient.updateUser(accessToken, user);

            assertEquals(SC_UNAUTHORIZED, response.statusCode());
            assertEquals(ResponsesCatalogue.getResponseNotAuthorized(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");


        }
    }



}
