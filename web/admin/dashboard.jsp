<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<%@ page import="com.caxerx.bean.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String includePage = (String) request.getAttribute("action");

    String navPage = (String) request.getAttribute("nav");
    navPage = "nav-drawer-" + navPage + ".jsp";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <title><%=request.getAttribute("actionName")%>
        - <%=request.getServletContext().getInitParameter("application-name")%>
    </title>
</head>

<body>
<div id="app">
    <v-app>
        <v-content>
            <jsp:include page="navbar.jsp"/>
            <jsp:include page="<%=navPage%>"/>
            <v-toolbar color="grey lighten-3" flat fixed style="padding-left:300px;margin-top:64px;z-index: 100">
                <v-breadcrumbs :items="breadcrumb"></v-breadcrumbs>
            </v-toolbar>
            <v-container style="margin-top:64px">
                <jsp:include page="<%=includePage%>"/>
            </v-container>
        </v-content>
    </v-app>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js "></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify@1.3.9/dist/vuetify.js "></script>
<script src="https://cdn.jsdelivr.net/npm/axios@0.18.0/dist/axios.js"></script>

<script src="https://cdn.jsdelivr.net/npm/vuetify-upload-button@1.2.1/dist/vuetify-upload-button.min.js"></script>
</body>
</html>


<script>
    Vue.use(VuetifyUploadButton);
    new Vue({
        el: '#app',
        data: () => ({
            userList: [],
            roleList: [],
            disableCreateButton: false,
            dobMenu: false,
            addUser: {
                username: "",
                firstName: "",
                lastName: "",
                password: "",
                email: "",
                role: -1,
                dob: ""
            }
        }),
        created() {
            this.$vuetify.theme.primary = '#A00000';
            <%
            Object users = request.getAttribute("user");
            if(users!=null){
               out.println("let user = '"+ users+"'");
               out.println("this.userList = JSON.parse(user)");
            }
            %>

            <%
         Object roles = request.getAttribute("role");
         if(roles!=null){
            out.println("let role = '"+ roles+"'");
            out.println("this.roleList = JSON.parse(role)");
         }
         %>

        },
        methods: {
            href(loc) {
                window.location.href = loc
            },
            createUser() {
                if (this.$refs.addUserForm.validate()) {
                    this.disableCreateButton = true;
                    let request = {
                        username: this.addUser.username,
                        firstName: this.addUser.firstName,
                        lastName: this.addUser.lastName,
                        password: this.addUser.password,
                        email: this.addUser.email,
                        dob: +moment(this.addUser.dob),
                        role: this.addUser.role
                    }
                    axios.post("/api/user", request).then(resp => {
                        if (resp.data.success) {
                            this.href("/admin/dashboard?action=userlist")
                        }
                    })
                }
            }
        },
        computed: {}
    })
</script>
