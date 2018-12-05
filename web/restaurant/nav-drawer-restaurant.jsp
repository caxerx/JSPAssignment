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
                <v-icon>info</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Information</v-list-tile-title>
        </v-list-tile>

        <v-list-group
                prepend-icon="place"
        >
            <v-list-tile slot="activator">
                <v-list-tile-title>Branch</v-list-tile-title>
            </v-list-tile>

            <v-list-tile @click="">
                <v-list-tile-title>Branch List</v-list-tile-title>
                <v-list-tile-action>
                    <v-icon></v-icon>
                </v-list-tile-action>
            </v-list-tile>

        </v-list-group>


        <v-list-group
                prepend-icon="menu"
        >
            <v-list-tile slot="activator">
                <v-list-tile-title>Menu</v-list-tile-title>
            </v-list-tile>

            <v-list-tile @click="">
                <v-list-tile-title>Menu List</v-list-tile-title>
                <v-list-tile-action>
                    <v-icon></v-icon>
                </v-list-tile-action>
            </v-list-tile>

        </v-list-group>


    </v-list>
</v-navigation-drawer>