package clients;

public class ResponsesCatalogue {
    private static String ResponseUserAlreadyExists = "User already exists";
    private static String ResponseNoNecessaryFields = "Email, password and name are required fields";
    private static String ResponseWrongCredentials = "email or password are incorrect";
    private static String ResponseNotAuthorized  = "You should be authorised";



    public static String getResponseWrongCredentials() {
        return ResponseWrongCredentials;
    }

    public static String getResponseUserAlreadyExists() {
        return ResponseUserAlreadyExists;
    }

    public static String getResponseNoNecessaryFields() {
        return ResponseNoNecessaryFields;
    }

    public static String getResponseNotAuthorized() {
        return ResponseNotAuthorized;
    }
}
