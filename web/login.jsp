<%@ page import="com.caxerx.bean.User" %><%--
  Created by IntelliJ IDEA.
  User: webma
  Date: 2/12/2018
  Time: 5:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="loggedInAs" scope="session" class="com.caxerx.bean.User"/>
<%
    if (loggedInAs != null && loggedInAs.getId() != 0) {
        response.sendRedirect("./index.jsp");
    }
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <title>Login - <%=request.getServletContext().getInitParameter("application-name")%>
    </title>
</head>

<body>
<div id="app">
    <v-app>
        <v-content class="primary">
            <v-container fluid fill-height>
                <v-layout align-center justify-center>
                    <v-flex xs12 sm8 md4>
                        <v-container style="max-width: 500px;">
                            <v-layout column justify-center align-center>
                                <p class=" font-weight-thin display-3 white--text text-xs-center">Takeaway King</p>
                            </v-layout>
                            <v-card class="elevation-12">
                                <v-container class="px-5">
                                    <v-form @submit.prevent="login" ref="loginForm" v-model="formValid" lazy-validation>
                                        <p class="headline text-xs-center primary--text">Login</p>
                                        <v-text-field prepend-icon="person" name="login" label="User ID" type="text"
                                                      v-model="loginInfo.username" required :rules="userIdRules"
                                                      validate-on-blur></v-text-field>
                                        <v-text-field id="password" prepend-icon="lock" name="password"
                                                      label="Password" type="password" v-model="loginInfo.password"
                                                      autocomplete="current-password" required
                                                      :rules="passwordRules" validate-on-blur></v-text-field>
                                        <v-flex class="mt-3">
                                            <v-expansion-panel v-model="loginFailed" class="elevation-0">
                                                <v-expansion-panel-content>
                                                    <v-card flat color="red lighten-2" class="white--text">
                                                        <v-card-text>
                                                            {{failedMessage}}
                                                        </v-card-text>
                                                    </v-card>
                                                </v-expansion-panel-content>
                                            </v-expansion-panel>
                                        </v-flex>
                                        <v-layout class="align-center justify-center mt-3">
                                            <v-btn depressed color="primary" type="submit">Login
                                            </v-btn>
                                        </v-layout>
                                    </v-form>
                                </v-container>
                            </v-card>

                        </v-container>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
    </v-app>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js "></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify@1.3.9/dist/vuetify.js "></script>
<script src="https://cdn.jsdelivr.net/npm/axios@0.18.0/dist/axios.js"></script>
</body>
</html>


<script>
    new Vue({
        el: '#app',
        data: () => ({
            formValid: true,
            loginFailed: -1,
            failedMessage: "Login Failed",
            passwordRules: [
                v => !!v || 'Password is required'
            ],
            userIdRules: [v => !!v || "User ID is required"],
            loginInfo: {
                username: "",
                password: ""
            }
        }),
        methods: {
            href(loc){
                window.location.href = loc
            },
            login() {
                axios.post("./api/login", this.loginInfo).then(resp => {
                    if (resp.data.success) {
                        window.location.href = "./index.jsp"
                    } else {
                        this.failedMessage = resp.data.errorMessage;
                        this.loginFailed = 0;
                    }
                }).catch(err => {
                    this.failedMessage = err.response.data.errorMessage;
                    this.loginFailed = 0;
                })
            }
        }
    })
</script>
