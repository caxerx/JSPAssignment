<%@ page import="com.caxerx.bean.User" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js "></script>
    <script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vuetify@1.3.9/dist/vuetify.js "></script>
    <script src="https://cdn.jsdelivr.net/npm/axios@0.18.0/dist/axios.js"></script>
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">

</head>

<body>
<div id="app">
    <v-app>
        <!-- Nav bar -->
        <jsp:include page="navbar.jsp"></jsp:include>
        <%
            User owner = (User) request.getSession().getAttribute("loggedInAs");
            if (owner == null || owner.getId() <= 0) {
                response.sendRedirect("/index.jsp");
                return;
            }
        %>
        <!-- Show top search keyword / popular restaurant -->
        <v-content>
            <v-container>
                <v-card>
                    <v-toolbar flat>
                        <v-toolbar-title>Edit User Information</v-toolbar-title>
                    </v-toolbar>

                    <v-layout class="pa-3">
                        <v-flex>
                            <span>Basic Information</span>
                        </v-flex>
                    </v-layout>
                    <v-divider></v-divider>

                    <v-form ref="addUserForm" lazy-validation>
                        <v-layout column class="pa-3">

                            <v-flex>
                                <v-text-field prepend-icon="lock" label="Password"
                                              type="password"
                                              v-model="addUser.password"></v-text-field>
                            </v-flex>

                            <v-layout>
                                <v-flex xs6 class="pr-2">
                                    <v-text-field prepend-icon="person" label="First Name"
                                                  :rules="[v => !!v || 'You must enter the first name']"
                                                  v-model="addUser.firstName"></v-text-field>
                                </v-flex>

                                <v-flex xs6 class="pl-2">
                                    <v-text-field label="Last Name"
                                                  :rules="[v => !!v || 'You must enter the last name']"
                                                  v-model="addUser.lastName"></v-text-field>
                                </v-flex>
                            </v-layout>

                            <v-flex>
                                <v-text-field prepend-icon="email" label="Email"
                                              :rules="[v => !!v || 'You must enter the email']"
                                              v-model="addUser.email"></v-text-field>
                            </v-flex>


                            <v-flex>
                                <v-menu
                                        :close-on-content-click="false"
                                        v-model="dobMenu"
                                        :nudge-right="40"
                                        lazy
                                        transition="scale-transition"
                                        offset-y
                                        full-width
                                        min-width="290px"
                                >
                                    <v-text-field
                                            slot="activator"
                                            v-model="addUser.dob"
                                            label="Date of birth"
                                            prepend-icon="cake"
                                            readonly
                                            :rules="[v => !!v || 'You must select date of birth']"
                                    ></v-text-field>
                                    <v-date-picker v-model="addUser.dob" @input="dobMenu = false"></v-date-picker>
                                </v-menu>
                            </v-flex>

                        </v-layout>

                        </v-layout>
                        <v-divider></v-divider>
                    </v-form>
                    <v-layout justify-end fill-width>
                        <v-btn flat @click="editUser" :disabled="disableCreateButton">Edit</v-btn>
                    </v-layout>
                </v-card>
            </v-container>

        </v-content>
    </v-app>
</div>
<script>
    new Vue({
        el: '#app',
        created() {

        },
        data: {
            dobMenu: false,
            disableCreateButton: false,
            addUser: {
                username: "<%=owner.getUsername()%>",
                firstName: "<%=owner.getFirstName()%>",
                lastName: "<%=owner.getLastName()%>",
                password: "",
                email: "<%=owner.getEmail()%>",
                dob: moment("<%=owner.getDateOfBirth()%>", "MMM D, YYYY").format("YYYY-MM-DD"),
            },
        },
        methods: {
            editUser() {
                if (this.$refs.addUserForm.validate()) {
                    this.disableCreateButton = true;
                    let request = {
                        id: "<%=owner.getId()%>",
                        username: "<%=owner.getUsername()%>",
                        firstName: this.addUser.firstName,
                        lastName: this.addUser.lastName,
                        password: this.addUser.password,
                        email: this.addUser.email,
                        dob: +moment(this.addUser.dob),
                        role: "<%=owner.getType()%>"
                    }
                    axios.put("/api/user", request).then(resp => {
                        if (resp.data.success) {
                            this.href("/index.jsp")
                        }
                    })
                }
            },
            href(loc) {
                window.location.href = loc
            },

        }
    })
</script>
</body>

</html>