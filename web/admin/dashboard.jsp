<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<%@ page import="com.caxerx.bean.User" %>
<%@ page import="com.caxerx.db.LogDb" %>
<%@ page import="com.caxerx.db.DatabaseConnectionPool" %>
<%@ page import="com.google.gson.Gson" %>
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

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
<script src="https://unpkg.com/vue-chartjs/dist/vue-chartjs.min.js"></script>
</body>
</html>


<script>
    Vue.use(VuetifyUploadButton);

    let m_key = []
    let m_value = []

    let d_key = []
    let d_value = []

    let k_key = []
    let k_value = []


    let mc = Vue.component('month-chart', {
        extends: VueChartJs.Line,
        mounted() {
            setTimeout(() => {
                this.renderChart({
                    labels: m_key,
                    datasets: [
                        {
                            label: 'Average Visitor',
                            backgroundColor: '#f87979',
                            data: m_value
                        }
                    ]
                }, {responsive: true, maintainAspectRatio: false})
            }, 200)
        },
    })

    let dc = Vue.component('district-chart', {
        extends: VueChartJs.Bar,
        mounted() {
            setTimeout(() => {
                this.renderChart({
                    labels: d_key,
                    datasets: [
                        {
                            label: 'Average Visitor',
                            backgroundColor: '#f87979',
                            data: d_value
                        }
                    ]
                }, {responsive: true, maintainAspectRatio: false})
            }, 200)
        },
    })


    let kc = Vue.component('kind-chart', {
        extends: VueChartJs.Bar,
        mounted() {
            setTimeout(() => {
                this.renderChart({
                    labels: k_key,
                    datasets: [
                        {
                            label: 'Average Visitor',
                            backgroundColor: '#f87979',
                            data: k_value
                        }
                    ]
                }, {responsive: true, maintainAspectRatio: false})
            }, 200)
        },
    })


    new Vue({
        el: '#app',
        data: () => ({
            userList: [],
            roleList: [],
            disableCreateButton: false,
            dobMenu: false,
            staticBy: 0,
            euid: -1,
            addUser: {
                id: 0,
                username: "",
                firstName: "",
                lastName: "",
                password: "",
                email: "",
                role: -1,
                dob: ""
            },
            breadcrumb: [
                {
                    text: "Admin Dashboard"
                },
                {
                    text: "<%=request.getAttribute("actionName")%>",
                    disabled: true
                }
            ],
            headers: [
                {
                    text: 'Time',
                    align: 'left',
                    value: 'timestamp'
                },
                {text: 'Keyword', value: 'keyword'}
            ],
            pag: {
                descending: true,
                page: 1,
                rowsPerPage: 25,
                sortBy: "timestamp",
                totalItems: history.length
            },
            history: []
        }),
        created() {
            axios.get("/api/stat/admin/month").then(r => {
                for (let i = 0; i <= 11; i++) {
                    m_key.push(moment().month(i).format("MMMM"))
                    m_value.push(r.data[i + 1])
                }
            })

            axios.get("/api/stat/admin/district").then(r => {
                let jd = r.data;
                Object.keys(jd).forEach(k => {
                    d_key.push(k);
                    d_value.push(jd[k])
                })
            })

            axios.get("/api/stat/admin/kind").then(r => {
                let jd = r.data;
                Object.keys(jd).forEach(k => {
                    k_key.push(k);
                    k_value.push(jd[k])
                })
            })

            this.$vuetify.theme.primary = '#f87979';
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
            <%
                Integer editUser = (Integer) request.getAttribute("editUser");
                if(editUser!=null){
                    out.print("this.euid = "+editUser);
                }
            %>

            <j:if condition="<%=editUser!=null%>">
            let u = this.userList.find(u => u.id == this.euid)
            this.addUser.id = u.id
            this.addUser.username = u.username;
            this.addUser.firstName = u.firstName;
            this.addUser.lastName = u.lastName;
            this.addUser.email = u.email;
            this.addUser.dob = moment(u.dateOfBirth, "MMM D, YYYY").format("YYYY-MM-DD");
            this.addUser.role = u.role.id;
            </j:if>

            let hist = {};
            <%
            LogDb logDb = new LogDb(DatabaseConnectionPool.contextInit(pageContext.getServletContext()));
            Gson gson= new Gson();
            out.println("hist = '"+gson.toJson(logDb.getHistory())+"';");
            %>
            if (hist != {}) {
                hist = JSON.parse(hist);
            }

            Object.keys(hist).forEach(k => {
                this.history.push({
                    timestamp: moment(k - 0).format("YYYY-MM-DD HH:mm"),
                    keyword: hist[k]
                })
            })

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
            },
            editUser() {
                if (this.$refs.addUserForm.validate()) {
                    this.disableCreateButton = true;
                    let request = {
                        id: this.addUser.id,
                        username: this.addUser.username,
                        firstName: this.addUser.firstName,
                        lastName: this.addUser.lastName,
                        password: this.addUser.password,
                        email: this.addUser.email,
                        dob: +moment(this.addUser.dob),
                        role: this.addUser.role
                    }
                    axios.put("/api/user", request).then(resp => {
                        if (resp.data.success) {
                            this.href("/admin/dashboard?action=userlist")
                        }
                    })
                }
            },
            deleteUser(id) {
                axios.delete("/api/user?userId=" + id).then(() => {
                    location.reload();
                })
            }
        },
        computed: {}
    })
</script>
