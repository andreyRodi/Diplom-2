package generators;

import model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {


    public static Order orderWithIngredients() {

        return new Order()
                .setIngredients(List.of(
                        "61c0c5a71d1f82001bdaaa70",
                        "61c0c5a71d1f82001bdaaa6f",
                        "61c0c5a71d1f82001bdaaa6c"
                ));
    }

    public static Order orderWithoutIngredients() {

        return new Order()
                .setIngredients(List.of());
    }

    public static Order orderWithWrongIngredients() {

        return new Order()
                .setIngredients(List.of(
                        "61c0c5a71d1f82001assaa70",
                        "61c0c5a71d1f82001asdaa6f",
                        "61c0c5a71d1f82001aswaa6c"
                ));
    }

}
