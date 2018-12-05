<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Add Branch</v-toolbar-title>
    </v-toolbar>

    <v-layout class="pa-3">
        <v-flex>
            <span>Basic Information</span>
        </v-flex>
    </v-layout>
    <v-divider></v-divider>

    <v-form ref="addBranch" lazy-validation>
        <v-layout column class="pa-3">

            <v-flex>
                <v-text-field prepend-icon="restaurant" label="Branch Name"
                              :rules="[v => !!v || 'You must enter the menu name']"
                              v-model="addBranchForm.branchName"></v-text-field>
            </v-flex>

            <v-flex>
                <v-select prepend-icon="restaurant" label="District"
                          :rules="[v => !!v || 'You must enter the district']"
                          v-model="addBranchForm.district"
                          :items="districts"
                          item-text="name"
                          item-value="id"
                ></v-select>
            </v-flex>

            <v-flex>
                <v-menu
                        ref="openTimeMenu"
                        :close-on-content-click="false"
                        v-model="openTimeMenu"
                        :nudge-right="40"
                        :return-value.sync="addBranchForm.openTime"
                        lazy
                        transition="scale-transition"
                        offset-y
                        full-width
                        max-width="290px"
                        min-width="290px"
                >
                    <v-text-field
                            slot="activator"
                            v-model="addBranchForm.openTime"
                            label="Open Time"
                            prepend-icon="access_time"
                            readonly
                            :rules="[v => !!v || 'You must select the open time']"
                    ></v-text-field>
                    <v-time-picker
                            v-if="openTimeMenu"
                            v-model="addBranchForm.openTime"
                            full-width
                            @change="$refs.openTimeMenu.save(addBranchForm.openTime)"
                    ></v-time-picker>
                </v-menu>
            </v-flex>

            <v-flex>
                <v-menu
                        ref="closeTimeMenu"
                        :close-on-content-click="false"
                        v-model="closeTimeMenu"
                        :nudge-right="40"
                        :return-value.sync="addBranchForm.closeTime"
                        lazy
                        transition="scale-transition"
                        offset-y
                        full-width
                        max-width="290px"
                        min-width="290px"
                >
                    <v-text-field
                            slot="activator"
                            v-model="addBranchForm.closeTime"
                            label="Close Time"
                            prepend-icon="access_time"
                            readonly
                            :rules="[v => !!v || 'You must select the close time']"
                    ></v-text-field>
                    <v-time-picker
                            v-if="closeTimeMenu"
                            v-model="addBranchForm.closeTime"
                            full-width
                            @change="$refs.closeTimeMenu.save(addBranchForm.closeTime)"
                    ></v-time-picker>
                </v-menu>
            </v-flex>

            <v-flex>
                <v-textarea prepend-icon="home" label="Address"
                            :rules="[v => !!v || 'You must enter the address']"
                            v-model="addBranchForm.address"></v-textarea>
            </v-flex>
        </v-layout>


        <v-divider></v-divider>
        <v-layout pa-3>
            Delivery District
        </v-layout>
        <v-divider></v-divider>
        <v-layout style="height:400px;">
            <v-flex xs3>
                <v-layout style="height:400px;" column>
                    <v-text-field prepend-inner-icon="search" solo v-model="tagSearch" hide-details label="Search"
                                  class="pb-2"></v-text-field>
                    <v-navigation-drawer width="xs3">
                        <v-list>
                            <v-list-tile v-for="tag in filteredDistricts" @click="addTag(tag)" :key="tag.id">
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
                    <v-subheader>Selected District
                        <v-spacer></v-spacer>
                        <a @click="resetTag">Reset Districts</a></v-subheader>
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
        <v-btn flat @click="createRestaurant" :disabled="disableCreateButton">Create</v-btn>
    </v-layout>
</v-card>
