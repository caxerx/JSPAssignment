<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Restaurant Information</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon @click="href('/restaurant/dashboard?action=editinfo&rid='+rid)">
            <v-icon>edit</v-icon>
        </v-btn>
    </v-toolbar>

    <v-layout class="pa-3">
        <v-flex>
            <span>Basic Information</span>
        </v-flex>
    </v-layout>
    <v-divider></v-divider>

    <v-card tile>
        <v-layout row wrap>
            <!-- Restaurant Logo -->
            <v-flex xs3 class="pa-3">
                <v-img :src="'/api/image?id='+selectedRestaurant.logo" class="black" max-height="200"></v-img>
            </v-flex>
            <!-- Restaurant Description -->
            <v-flex xs9 class="pl-2 pt-4">
                <v-layout column>
                    <!-- Restaurant Name -->
                    <v-flex>
                        <span class="display-3">{{ selectedRestaurant.name }}</span>
                    </v-flex>
                    <!-- Rating -->
                    <v-flex class="ma-2">
                        <v-layout row>
                            <v-flex class="shrink">
                                <span>{{ selectedRestaurant.branchs.length }} Branches</span>
                            </v-flex>
                        </v-layout>
                    </v-flex>
                    <!-- Tags -->
                    <v-flex>
                        <v-chip v-for="tag in selectedRestaurant.tags">{{ tag.name }}</v-chip>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>
        <v-divider></v-divider>

        <v-layout class="pa-3">
            <v-flex>
                <span>Background Image</span>
            </v-flex>
        </v-layout>

        <v-divider></v-divider>

        <v-layout>
            <v-img :src="'/api/image?id='+selectedRestaurant.background" class="black" max-height="200"></v-img>
        </v-layout>
    </v-card>
</v-card>