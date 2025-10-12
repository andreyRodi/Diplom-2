package generators;
import com.github.javafaker.Faker;
import model.User;

public class UserGenerator {
    static Faker faker = new Faker();

    public static User randomUser() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password())
                .setName(faker.name().firstName());
    }

    public static User randomUserWithoutName() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password())
                .setName(null);
    }

    public static User randomUserWithoutEmail() {
        return new User()
                .setEmail(null)
                .setPassword(faker.internet().password())
                .setName(faker.name().firstName());
    }

    public static User randomUserWithoutPassword() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(null)
                .setName(faker.name().firstName());
    }

    public static User randomUserWithEmailOnly() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress());
    }

    public static User randomUserWithPasswordOnly() {
        return new User()
                .setPassword(faker.internet().password());
    }

    public static User randomUserWithNameOnly() {
        return new User()
                .setName(faker.name().firstName());
    }


}

