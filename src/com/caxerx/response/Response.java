package com.caxerx.response;

import com.google.gson.Gson;

import java.io.Serializable;

public class Response implements Serializable {
    private boolean success;

    public Response(boolean success) {
        this.success = success;
    }

    public Response() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
