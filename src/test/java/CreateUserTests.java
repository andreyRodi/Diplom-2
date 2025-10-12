import clients.ApiClient;
import clients.ResponsesCatalogue;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.UnsuccessfulResponse;
import model.User;
import model.UserRegisteredResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static generators.UserGenerator.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AllureJunit5.class})
public class CreateUserTests {

    private User user;
    private ApiClient apiClient;
    private String accessToken;

    @BeforeEach
    public void setup(){
        apiClient = new ApiClient();
    }

    //успешное создание
    @Test
    public void createUserSuccess(){
        user = randomUser();
        Response response = apiClient.createUser(user);

        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.as(UserRegisteredResponse.class).getSuccess(), "В теле ответа отсутствует success = true");

        accessToken = response.as(UserRegisteredResponse.class).getAccessToken();
        apiClient.delete(accessToken);

    }

    //создание уже существующего пользователя
    @Test
    public void createUserAlreadyExistingFail(){
        user = randomUser();
        apiClient.createUser(user);
        Response response = apiClient.createUser(user);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(ResponsesCatalogue.getResponseUserAlreadyExists(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");
    }

    //создание пользователя без имени
    @Test
    public void createUserWithoutNameFail(){
        user = randomUserWithoutName();
        Response response = apiClient.createUser(user);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(ResponsesCatalogue.getResponseNoNecessaryFields(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");

    }

    //создание пользователя без email
    @Test
    public void createUserWithoutEmailFail(){
        user = randomUserWithoutEmail();
        Response response = apiClient.createUser(user);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(ResponsesCatalogue.getResponseNoNecessaryFields(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");
    }

    //создание пользователя без пароля
    @Test
    public void createUserWithoutPasswordFail(){
        user = randomUserWithoutPassword();
        Response response = apiClient.createUser(user);

        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals(ResponsesCatalogue.getResponseNoNecessaryFields(), response.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");
    }


}
