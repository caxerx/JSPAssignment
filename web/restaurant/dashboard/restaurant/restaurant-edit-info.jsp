<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Edit Restaurant</v-toolbar-title>
    </v-toolbar>

    <v-layout class="pa-3">
        <v-flex>
            <span>Basic Information</span>
        </v-flex>
    </v-layout>
    <v-divider></v-divider>

    <v-form ref="addForm" lazy-validation>
        <v-layout column class="pa-3">

            <v-flex>
                <v-text-field
                        prepend-icon="restaurant"
                        label="Restaurant Name"
                        :rules="[v => !!v || 'You must enter the restaurant name']"
                        v-model="addForm.restaurantName"
                ></v-text-field>
            </v-flex>

            <v-flex>
                <v-layout row align-center>
                    <v-flex>
                        <v-layout row>
                            <v-flex>
                                <v-text-field
                                        prepend-icon="photo"
                                        label="Logo"
                                        readonly
                                        :value="imageUpload.logo.fileName"
                                        :rules="[v => !!v || 'You must upload the logo']"
                                ></v-text-field>
                            </v-flex>
                            <v-flex xs2>
                                <upload-btn :file-changed-callback="uploadLogo" unique-id
                                            accept="image/jpeg,image/png"></upload-btn>
                            </v-flex>
                        </v-layout>
                    </v-flex>
                    <v-flex xs2>
                        <v-layout justify-center align-center>
                            <v-img :src="imageUpload.logo.image"
                                   max-width="165" max-height="165" contain

                            ></v-img>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>

            <v-flex>
                <v-layout row pt-3 align-center>
                    <v-flex>
                        <v-layout row>
                            <v-flex>
                                <v-text-field
                                        prepend-icon="photo"
                                        label="Background Image"
                                        :value="imageUpload.background.fileName"
                                        :rules="[v => !!v || 'You must upload a background picture']"
                                        readonly
                                ></v-text-field>
                            </v-flex>
                            <v-flex xs2>
                                <v-layout>
                                    <upload-btn :file-changed-callback="uploadBackground" unique-id
                                                accept="image/jpeg,image/png"></upload-btn>
                                </v-layout>
                            </v-flex>
                        </v-layout>
                    </v-flex>
                    <v-flex xs2>
                        <v-layout justify-center align-center>
                            <v-img :src="imageUpload.background.image"
                                   max-width="165" max-height="165" contain

                            ></v-img>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>


        <v-divider></v-divider>

        <v-layout pa-3>
            Tags
        </v-layout>
        <v-divider></v-divider>
        <v-layout style="height:400px;">
            <v-flex xs3>
                <v-layout style="height:400px;" column>
                    <v-text-field prepend-inner-icon="search" solo v-model="tagSearch" hide-details label="Search"
                                  class="pb-2"></v-text-field>
                    <v-navigation-drawer width="xs3">
                        <v-list>
                            <v-list-tile v-for="tag in filteredTags" @click="addTag(tag)" :key="tag.id">
                                <v-list-tile-content>
                                    <v-list-tile-title>{{tag.name}}</v-list-tile-title>
                                </v-list-tile-content>
                            </v-list-tile>
                        </v-list>
                    </v-navigation-drawer>
                </v-layout>
            </v-flex>

            <v-flex xs9>
                <v-flex>
                    <v-subheader>Selected Tags
                        <v-spacer></v-spacer>
                        <a @click="resetTag">Reset Tags</a></v-subheader>
                </v-flex>
                <v-divider></v-divider>
                <v-navigation-drawer width="xs9" style="height:350px;">
                    <v-item-group multiple class="pl-2">
                        <v-item
                                v-for="tag in selectedTags"
                                :key="tag.id"
                        >
                            <v-chip close
                                    slot-scope="{ active, toggle }"
                                    @input="removeTag(tag)"
                            >
                                {{tag.name}}
                            </v-chip>
                        </v-item>
                    </v-item-group>
                </v-navigation-drawer>
                <v-divider></v-divider>
                </v-card-text>
            </v-flex>

        </v-layout>
        <v-divider></v-divider>
    </v-form>
    <v-layout justify-end fill-width>
        <v-btn flat @click="editRestaurant" :disabled="disableCreateButton">Edit</v-btn>
    </v-layout>
</v-card>