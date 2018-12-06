<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<v-navigation-drawer
        app
        clipped
        stateless
        value="true"
        style="z-index: 101"
>
    <v-list>

        <v-list-tile>
            <v-list-tile-action>
                <v-icon>dashboard</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Dashboard</v-list-tile-title>
        </v-list-tile>


        <v-list-tile>
            <v-list-tile-action>
                <v-icon>assignment_ind</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Roles</v-list-tile-title>
        </v-list-tile>


        <v-list-tile @click="">
            <v-list-tile-action>
                <v-icon>
                    perm_identity
                </v-icon>
            </v-list-tile-action>
            <v-list-tile-title>User List</v-list-tile-title>
        </v-list-tile>


    </v-list>
</v-navigation-drawer>