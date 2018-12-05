package com.caxerx.response;

public class SuccessResponse extends Response {
    Object content;

    public SuccessResponse(Object content) {
        super(true);
        this.content = content;
    }
}
