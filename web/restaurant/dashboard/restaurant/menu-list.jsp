<v-card>
    <v-toolbar flat>
        <v-toolbar-title>All Menu</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn icon @click="href('/restaurant/dashboard?action=addmenu&rid='+rid)">
                <v-icon>add</v-icon>
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>

    <div v-for="menu in menuList">
        <v-layout row class="pa-3">
            <v-flex xs2>
                <v-img src="/static/image/noimage.png"
                       max-width="200" max-height="100" contain
                       v-if="menu.image.length==0"
                ></v-img>
                <v-img :src="'/api/image?id='+menu.image[0]"
                       max-width="200" max-height="100" contain
                       v-else
                ></v-img>
            </v-flex>
            <v-flex>
                <v-list three-line>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title>
                                <v-icon v-if="menu.showMenu">visibility</v-icon>
                                <v-icon v-else>visibility_off</v-icon>
                                <a>{{menu.title}}</a>
                            </v-list-tile-title>
                            <v-list-tile-sub-title v-if="checkDate(menu.startTime) && checkDate(menu.endTime)">
                                <v-icon>date_range</v-icon>
                                {{menu.startTime}}
                                <span> - </span>
                                {{menu.endTime}}
                            </v-list-tile-sub-title>
                            <v-list-tile-sub-title v-else-if="checkDate(menu.startTime)">
                                <v-icon>date_range</v-icon>
                                Start From
                                {{menu.startTime}}
                            </v-list-tile-sub-title>

                            <v-list-tile-sub-title v-else-if="checkDate(menu.endTime)">
                                <v-icon>date_range</v-icon>
                                Until
                                {{menu.endTime}}
                            </v-list-tile-sub-title>

                            <v-list-tile-sub-title v-else>
                                <v-icon>date_range</v-icon>
                                Always
                            </v-list-tile-sub-title>

                            <v-list-tile-sub-title>
                                {{menu.image.length}} Images
                            </v-list-tile-sub-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </v-list>
            </v-flex>
            <v-flex>
                <v-layout align-center justify-end fill-height>
                    <v-btn icon>
                        <v-icon v-if="menu.showMenu">visibility_off</v-icon>
                        <v-icon v-else>visibility</v-icon>
                    </v-btn>
                    <v-btn flat>
                        Edit
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