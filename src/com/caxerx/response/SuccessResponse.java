package com.caxerx.response;

import java.io.Serializable;

public class SuccessResponse extends Response {
    Serializable content;

    public SuccessResponse(Serializable content) {
        super(true);
        this.content = content;
    }
}
