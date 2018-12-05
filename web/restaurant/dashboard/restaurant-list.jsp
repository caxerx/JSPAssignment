<v-card>
    <v-toolbar flat>
        <v-toolbar-title>All Restaurant</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn icon @click="href('/restaurant/dashboard?action=add')">
                <v-icon>add</v-icon>
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>

    <div v-for="restaurant in restaurantList">
        <v-layout row class="pa-3">
            <v-flex xs2>
                <v-img :src="'/api/image?id='+restaurant.logo"
                       max-width="200" max-height="100" contain

                ></v-img>
            </v-flex>
            <v-flex>
                <v-list>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title>
                                <a>{{restaurant.name}}</a>
                            </v-list-tile-title>
                            <v-list-tile-sub-title>
                                {{restaurant.tags.length}} Branch
                            </v-list-tile-sub-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </v-list>
            </v-flex>
            <v-flex>
                <v-layout align-center justify-end fill-height>
                    <v-btn flat>
                        Manage
                    </v-btn>
                    <v-menu offset-y nudge-left="105">
                        <v-icon slot="activator">arrow_drop_down</v-icon>
                        <v-list dense>
                            <v-list-tile @click="">
                                <v-list-tile-action>
                                    <v-icon color="error">delete</v-icon>
                                </v-list-tile-action>
                                <v-list-tile-content>
                                    <v-list-tile-title class="error--text">Remove</v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>
                        </v-list>
                    </v-menu>
                </v-layout>
            </v-flex>
        </v-layout>
        <v-divider></v-divider>
    </div>
</v-card>