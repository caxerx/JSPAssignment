<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<jsp:useBean id="loggedInAs" class="com.caxerx.bean.User" scope="session"/>
<%
    boolean loggedIn = false;

    if (loggedInAs != null && loggedInAs.getId() != 0) {
        loggedIn = true;
    }

    if (!loggedIn) {
        response.sendRedirect("/login.jsp");
    }
%>

<v-toolbar dark color="primary" app prominent clipped-left>
    <a class="title font-weight-light mr-2" style="color:white" @click="href('/')">Takeaway King
        <v-icon>fastfood</v-icon>
    </a>
    <v-spacer></v-spacer>

    <j:if condition="<%=loggedIn%>">
    <v-toolbar-items>
        <v-menu bottom
                offset-y
                left
                z-index="9999"
        >
            <v-btn icon slot="activator">
                <v-icon>account_circle</v-icon>
            </v-btn>
            </j:if>

            <j:if condition="<%=loggedIn && loggedInAs.getPermission().contains(1)%>">
                <v-list>
                    <v-list-tile @click="href('/admin/dashboard?action=dashboard')">
                        <v-list-tile-title>Admin Dashboard</v-list-tile-title>
                    </v-list-tile>
                </v-list>
            </j:if>


            <j:if condition="<%=loggedIn && loggedInAs.getPermission().contains(2)%>">
                <v-list>
                    <v-list-tile @click="href('/restaurant/dashboard?action=dashboard')">
                        <v-list-tile-title>Manage Restaurant</v-list-tile-title>
                    </v-list-tile>
                </v-list>
            </j:if>

            <j:if condition="<%=loggedIn%>">
            <v-list>
                <v-list-tile @click="href('/favourite.jsp')">
                    <v-list-tile-title>Favourite</v-list-tile-title>
                </v-list-tile>
            </v-list>
            <v-list>
                <v-list-tile @click="href('/edit-user-info.jsp')">
                    <v-list-tile-title>Edit User Info</v-list-tile-title>
                </v-list-tile>
            </v-list>
            <v-list>
                <v-list-tile @click="href('/api/logout')">
                    <v-list-tile-title>Logout</v-list-tile-title>
                </v-list-tile>
            </v-list>
        </v-menu>
    </v-toolbar-items>
    </j:if>

    <j:if condition="<%=!loggedIn%>">
        <v-toolbar-items>

            <v-menu bottom
                    offset-y
                    left
                    attach>
                <v-btn icon slot="activator">
                    <v-icon>lock_open</v-icon>
                </v-btn>
                <v-list>
                    <v-list-tile @click="href('/login.jsp')">
                        <v-list-tile-title>Login</v-list-tile-title>
                    </v-list-tile>
                </v-list>
            </v-menu>
        </v-toolbar-items>
    </j:if>

</v-toolbar>

