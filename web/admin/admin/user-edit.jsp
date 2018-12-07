<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Edit User</v-toolbar-title>
    </v-toolbar>

    <v-layout class="pa-3">
        <v-flex>
            <span>Basic Information</span>
        </v-flex>
    </v-layout>
    <v-divider></v-divider>

    <v-form ref="addUserForm" lazy-validation>
        <v-layout column class="pa-3">

            <v-flex>
                <v-text-field prepend-icon="account_circle" label="Username"
                              :rules="[v => !!v || 'You must enter the username']"
                              v-model="addUser.username"></v-text-field>
            </v-flex>

            <v-flex>
                <v-text-field prepend-icon="lock" label="Password"
                              type="password"
                              v-model="addUser.password"></v-text-field>
            </v-flex>

            <v-layout>
                <v-flex xs6 class="pr-2">
                    <v-text-field prepend-icon="person" label="First Name"
                                  :rules="[v => !!v || 'You must enter the first name']"
                                  v-model="addUser.firstName"></v-text-field>
                </v-flex>

                <v-flex xs6 class="pl-2">
                    <v-text-field label="Last Name"
                                  :rules="[v => !!v || 'You must enter the last name']"
                                  v-model="addUser.lastName"></v-text-field>
                </v-flex>
            </v-layout>

            <v-flex>
                <v-text-field prepend-icon="email" label="Email"
                              :rules="[v => !!v || 'You must enter the email']"
                              v-model="addUser.email"></v-text-field>
            </v-flex>


            <v-flex>
                <v-menu
                        :close-on-content-click="false"
                        v-model="dobMenu"
                        :nudge-right="40"
                        lazy
                        transition="scale-transition"
                        offset-y
                        full-width
                        min-width="290px"
                >
                    <v-text-field
                            slot="activator"
                            v-model="addUser.dob"
                            label="Date of birth"
                            prepend-icon="cake"
                            readonly
                            :rules="[v => !!v || 'You must select date of birth']"
                    ></v-text-field>
                    <v-date-picker v-model="addUser.dob" @input="dobMenu = false"></v-date-picker>
                </v-menu>
            </v-flex>


            <v-flex>
                <v-select prepend-icon="assignment_ind" label="Role"
                          :rules="[v => !!v || 'You must select the user role']"
                          v-model="addUser.role"
                          :items="roleList"
                          item-text="name"
                          item-value="id"
                ></v-select>
            </v-flex>

        </v-layout>

        </v-layout>
        <v-divider></v-divider>
    </v-form>
    <v-layout justify-end fill-width>
        <v-btn flat @click="editUser" :disabled="disableCreateButton">Edir</v-btn>
    </v-layout>
</v-card>
