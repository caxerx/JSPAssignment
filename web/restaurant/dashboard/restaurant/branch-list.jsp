<v-card>
    <v-toolbar flat>
        <v-toolbar-title>All Branch</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn icon @click="href('/restaurant/dashboard?action=addbranch&rid='+rid)">
                <v-icon>add</v-icon>
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>

    <div v-for="branch in branchList">
        <v-layout row class="pa-3">
            <v-flex>
                <v-list three-line>
                    <v-list-tile>
                        <v-list-tile-content>
                            <v-list-tile-title>
                                <a>{{branch.name}} ({{districtName(branch.districtId)}})</a>
                            </v-list-tile-title>
                            <v-list-tile-sub-title>
                                <v-icon>phone</v-icon>
                                {{branch.telephone}}
                            </v-list-tile-sub-title>
                            <v-list-tile-sub-title>
                                <v-icon>home</v-icon>
                                {{branch.address}}
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