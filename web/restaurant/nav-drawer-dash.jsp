<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<v-navigation-drawer
        app
        clipped
        stateless
        value="true"
        style="z-index: 101"
>
    <v-list>
        <%-- <j:if condition="">value="true"</j:if> --%>

        <v-list-tile @click="href('/restaurant/dashboard?action=dashboard')">
            <v-list-tile-action>
                <v-icon>dashboard</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Statistics</v-list-tile-title>
        </v-list-tile>

        <v-list-tile @click="href('/restaurant/dashboard?action=list')">
            <v-list-tile-action>
                <v-icon>restaurant</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Restaurant</v-list-tile-title>
        </v-list-tile>

    </v-list>
</v-navigation-drawer>