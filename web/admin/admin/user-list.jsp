<v-card>
    <v-toolbar flat>
        <v-toolbar-title>All Users</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn icon @click="href('/admin/dashboard?action=adduser')">
                <v-icon>add</v-icon>
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>

    <div v-for="user in userList">
        <v-layout row class="pa-3">
            <v-flex>
                <v-list three-line>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title>
                                <a>{{user.firstName}} {{user.lastName}}</a>
                            </v-list-tile-title>
                            <v-list-tile-sub-title>
                                <v-icon>perm_identity</v-icon>
                                {{user.username}}
                            </v-list-tile-sub-title>
                            <v-list-tile-sub-title>
                                <v-icon>assignment_ind</v-icon>
                                {{user.role.name}}
                            </v-list-tile-sub-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </v-list>
            </v-flex>
            <v-flex>
                <v-layout align-center justify-end fill-height>
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