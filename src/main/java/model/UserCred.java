package model;
import com.github.javafaker.Faker;

public class UserCred {

    static Faker faker = new Faker();


    private String email;
    private String password;

    public UserCred(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public static UserCred returnUserCreds(User user) {
        return new UserCred(user.getEmail(), user.getPassword());
    }

    public static UserCred returnUserCredsWrongLogin(User user) {
        return new UserCred(faker.internet().safeEmailAddress(), user.getPassword());
    }

    public static UserCred returnUserCredsWrongPassword(User user) {
        return new UserCred(user.getEmail(), faker.internet().password());
    }

}
