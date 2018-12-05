package com.caxerx.response;

public class FailIdResponse extends FailResponse {
    private int errorId;

    public FailIdResponse(String errorMessage, int errorId) {
        super(errorMessage);
        this.errorId = errorId;
    }

    public FailIdResponse(String errorMessage) {
        super(errorMessage);
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }
}
