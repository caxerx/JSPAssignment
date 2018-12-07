<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<v-navigation-drawer
        app
        clipped
        stateless
        value="true"
        style="z-index: 101"
>
    <v-list>

        <v-list-tile @click="href('/admin/dashboard?action=dashboard')">
            <v-list-tile-action>
                <v-icon>dashboard</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Dashboard</v-list-tile-title>
        </v-list-tile>


        <v-list-tile @click="href('/admin/dashboard?action=history')">
            <v-list-tile-action>
                <v-icon>history</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Search History</v-list-tile-title>
        </v-list-tile>


        <v-list-tile @click="href('/admin/dashboard?action=userlist')">
            <v-list-tile-action>
                <v-icon>
                    perm_identity
                </v-icon>
            </v-list-tile-action>
            <v-list-tile-title>User List</v-list-tile-title>
        </v-list-tile>


    </v-list>
</v-navigation-drawer>