package com.caxerx.response;

import com.caxerx.bean.User;

import java.io.Serializable;

public class LoginSuccessResponseContent implements Serializable {
    private String username;
    private String type;

    public LoginSuccessResponseContent(User user) {
        this.username = user.getUsername();
        this.type = user.getType();
    }
}
