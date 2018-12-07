<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Edit Menu</v-toolbar-title>
    </v-toolbar>

    <v-layout class="pa-3">
        <v-flex>
            <span>Basic Information</span>
        </v-flex>
    </v-layout>
    <v-divider></v-divider>

    <v-form ref="addMenu" lazy-validation>
        <v-layout column class="pa-3">

            <v-flex>
                <v-text-field prepend-icon="restaurant" label="Menu Name"
                              :rules="[v => !!v || 'You must enter the menu name']"
                              v-model="addMenuForm.menuName"></v-text-field>
            </v-flex>

            <v-flex>

                <v-menu
                        :close-on-content-click="false"
                        v-model="startDateMenu"
                        :nudge-right="40"
                        lazy
                        transition="scale-transition"
                        offset-y
                        full-width
                        min-width="290px"
                >
                    <v-text-field
                            slot="activator"
                            v-model="addMenuForm.startDate"
                            label="Start Show Date"
                            prepend-icon="event"
                            readonly
                    ></v-text-field>
                    <v-date-picker v-model="addMenuForm.startDate" @input="startDateMenu = false"></v-date-picker>
                </v-menu>

            </v-flex>

            <v-flex>

                <v-menu
                        :close-on-content-click="false"
                        v-model="endDateMenu"
                        :nudge-right="40"
                        lazy
                        transition="scale-transition"
                        offset-y
                        full-width
                        min-width="290px"
                >
                    <v-text-field
                            slot="activator"
                            v-model="addMenuForm.endDate"
                            label="End Show Date"
                            prepend-icon="event"
                            readonly
                            :rules="[v => (v == '' || v >= this.addMenuForm.startDate) || 'End date must later than start date']"
                    ></v-text-field>
                    <v-date-picker v-model="addMenuForm.endDate" @input="endDateMenu = false"></v-date-picker>
                </v-menu>
            </v-flex>
            <v-flex>
                <v-checkbox label="Show Menu" v-model="addMenuForm.showMenu"></v-checkbox>
            </v-flex>
        </v-layout>

        <v-divider></v-divider>
        <v-layout pa-3 align-center>
            Images

            <v-spacer></v-spacer>
            <upload-btn flat small color="white" class="primary--text" block
                        :file-changed-callback="uploadMenuImg" unique-id
                        accept="image/jpeg,image/png"
                        style="padding:0"
            ></upload-btn>
            </v-subheader>
        </v-layout>
        <v-divider></v-divider>


        <v-layout style="height:400px;" align-center justify-center v-if="menuImages.length==0">
            <span class="grey--text"> No Image Uploaded</span>

        </v-layout>

        <v-layout style="height:400px;" v-else>
            <v-navigation-drawer style="width:100%">
                <v-container grid-list-xl fluid>
                    <v-layout row wrap>
                        <v-flex
                                v-for="pic in menuImages"
                                xs4
                        >
                            <v-hover>
                                <v-card tile slot-scope="{ hover }"
                                >
                                    <v-icon style="position:absolute; z-index: 10; right: 5px; top:5px"
                                            class="menuimg"
                                            @click="removeMenuImg(pic)">cancel
                                    </v-icon>
                                    <v-img
                                            :src="'/api/image?id='+pic"
                                            height="150"
                                    ></v-img>
                                </v-card>
                            </v-hover>
                        </v-flex>
                    </v-layout>
                </v-container>
            </v-navigation-drawer>
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
                        <v-item v-for="tag in selectedTags" :key="tag.id">
                            <v-chip close slot-scope="{ active, toggle }" @input="removeTag(tag)">
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
        <v-btn flat @click="editMenu" :disabled="disableCreateButton">Edit</v-btn>
    </v-layout>
</v-card>
