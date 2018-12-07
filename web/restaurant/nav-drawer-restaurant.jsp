<v-navigation-drawer
        app
        clipped
        stateless
        value="true"
        style="z-index: 101"
>
    <v-list>


        <v-list-tile @click="href('/restaurant/dashboard?action=dashboard')">
            <v-list-tile-action>
                <v-icon>dashboard</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Back to dashboard</v-list-tile-title>
        </v-list-tile>


        <v-list-tile @click="href('/restaurant/dashboard?action=info&rid='+rid)">
            <v-list-tile-action>
                <v-icon>info</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Information</v-list-tile-title>
        </v-list-tile>


        <v-list-tile @click="href('/restaurant/dashboard?action=listbranch&rid='+rid)">
            <v-list-tile-action>
                <v-icon>place</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Branch</v-list-tile-title>
        </v-list-tile>


        <v-list-tile @click="href('/restaurant/dashboard?action=listmenu&rid='+rid)">
            <v-list-tile-action>
                <v-icon>menu</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Menu</v-list-tile-title>
        </v-list-tile>

    </v-list>
</v-navigation-drawer>