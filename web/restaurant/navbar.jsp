<%--
  Created by IntelliJ IDEA.
  User: webma
  Date: 2/12/2018
  Time: 6:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="j" uri="/WEB-INF/if.tld" %>

<jsp:useBean id="loggedInAs" class="com.caxerx.bean.User" scope="session"/>

<%
    boolean loggedIn = false;

    if (loggedInAs != null && loggedInAs.getId() != 0) {
        loggedIn = true;
    }

%>
<v-toolbar app dark class="primary" clipped-left style="z-index: 100">
    <v-toolbar-title>
        <%=request.getServletContext().getInitParameter("application-name")%>
    </v-toolbar-title>

    <div style="width:250px; margin-top:10px" class="ml-2">
        <v-overflow-btn
                :items="restaurantList"
                label="Manage Restaurant"
                v-model="selectedRestaurant"
                item-text="idName"
        ></v-overflow-btn>
    </div>

    <v-spacer></v-spacer>

    <j:if condition="<%=loggedIn%>">
    <v-toolbar-items>
        <v-menu bottom
                offset-y
                left
                attach>
            <v-btn icon slot="activator">
                <v-icon>account_circle</v-icon>
            </v-btn>
            </j:if>

            <j:if condition="<%=loggedIn && loggedInAs.getType()==1%>">
                <v-list>
                    <v-list-tile @click="href('/dashboard.jsp')">
                        <v-list-tile-title>Dashboard</v-list-tile-title>
                    </v-list-tile>
                </v-list>
            </j:if>

            <j:if condition="<%=loggedIn%>">
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