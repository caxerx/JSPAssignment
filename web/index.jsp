<%@ page import="com.caxerx.bean.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <title>Home - <%=request.getServletContext().getInitParameter("application-name")%>
    </title>
</head>

<body>
<div id="app">
    <v-app>
        <v-content>
            <jsp:include page="navbar.jsp"/>
            <v-container>

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
        data: () => ({}),
        methods: {
            href(loc) {
                window.location.href = loc
            },
        }
    })
</script>
