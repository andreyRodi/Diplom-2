package model;

public class GetOrderResponse {
    private boolean success;
    private Orders[] orders;
    private int totalToday;
    private int total;

    public boolean getSuccess() {
        return success;
    }
}
