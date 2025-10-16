package model;

public class UserRegisteredResponse {


    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public boolean getSuccess() {
        return success;
    }
}

