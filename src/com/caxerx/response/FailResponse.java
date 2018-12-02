package com.caxerx.response;

public class FailResponse extends Response {
    private String errorMessage;

    public FailResponse(String errorMessage) {
        super(false);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
