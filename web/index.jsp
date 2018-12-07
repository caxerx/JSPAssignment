<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js "></script>
    <script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vuetify@1.3.9/dist/vuetify.js "></script>
    <script src="https://cdn.jsdelivr.net/npm/axios@0.18.0/dist/axios.js"></script>
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">

</head>

<body>
<div id="app">
    <v-app>
        <!-- Nav bar -->
        <jsp:include page="navbar.jsp"></jsp:include>
        <!-- Show top search keyword / popular restaurant -->
        <v-content>
            <v-layout justify-center align-center style="height:480px;" class="black white--text">
                <v-img src="/static/image/home.jpg" max-height="480px">
                    <v-layout style="background: linear-gradient(to top, rgba(0,0,0,1),rgba(0,0,0,0))"
                              justify-center align-center fill-height>
                        <span class="display-4 font-weight-thin">Find your delicacy</span>
                    </v-layout>
                </v-img>
            </v-layout>
            <v-container style="margin-top:-100px;">
                <v-card class="px-4 pb-2">
                    <v-layout row>
                        <v-flex>
                            <!-- Search type -->
                            <v-radio-group v-model="searchType" :column="false">
                                <v-radio label="Restaurant" value="restaurant"></v-radio>
                                <v-radio label="Menu" value="menu"></v-radio>
                            </v-radio-group>
                        </v-flex>
                        <v-flex xs7>
                            <!-- Search Keyword -->
                            <v-text-field class="mr-2" prepend-icon="search" v-model="search" label="Search"
                                          clearable @keyup.native="s"></v-text-field>
                        </v-flex>
                        <v-flex xs3>
                            <!-- District -->
                            <v-select class="mr-2" prepend-icon="place" :items="districts" item-text="name"
                                      item-value="id" v-model="searchDistrict" label="District"></v-select>
                        </v-flex>
                        <v-flex xs3>
                            <!-- Tag -->
                            <v-select prepend-icon="local_offer" :items="tags" item-text="name" item-value="id"
                                      v-model="searchTag" label="Tag"></v-select>
                        </v-flex>
                    </v-layout>
                    <!-- Hot keywords -->
                    <v-layout row align-center>
                        <v-card class="subheading yellow darken-1 py-1 px-2 mr-2 font-weight-bold">
                            <v-icon>whatshot</v-icon>
                            What's hot
                        </v-card>
                        <v-chip v-for="word in hotWords" @click="setWord(word)">{{ word }}</v-chip>
                    </v-layout>
                </v-card>
                <!-- Popular Restaurants -->
                <div class="display-1 font-weight-light py-5">Popular Restaurants</div>
                <v-layout row wrap>
                    <v-flex xs3 v-for="restaurant in hotRestaurants">
                        <v-card @click="href('restaurant.jsp?rid='+restaurant.id)">
                            <div>
                                <v-img class="black" :src="'/api/image?id='+restaurant.logo"
                                       max-height="200"></v-img>
                            </div>
                            <v-toolbar flat color="orange lighten-3">
                                <span class="headline">{{ restaurant.name }}</span>
                            </v-toolbar>
                        </v-card>
                    </v-flex>
                </v-layout>

            </v-container>
        </v-content>
    </v-app>
</div>
<script>
    new Vue({
        el: '#app',
        created() {
            axios.get("/api/popular/keyword").then(r => {
                this.hotWords = r.data
            })
            axios.get("/api/popular/restaurant").then(r => {
                this.hotRestaurants = r.data
            })

            axios.get("/api/district").then(r => {
                this.districts = r.data.content
            })

            axios.get("/api/tag").then(r => {
                this.tags = r.data.content
            })
        },
        data: {
            hotWords: [],
            hotRestaurants: [],
            search: '', // User entered keywords
            searchDistrict: '',
            searchTag: '',
            searchType: 'restaurant',
            menus: [],
            restaurants: [],
            districts: [],
            tags: [] // Already available tags
        },
        methods: {
            s(e) {
                if (e.key == "Enter") {
                    let ur = "/search.jsp?type=" + this.searchType
                    if (this.search && this.search != '') {
                        ur = ur + '&keyword=' + this.search;
                    }
                    if (this.searchDistrict && this.searchDistrict != '') {
                        ur = ur + "&district=" + this.searchDistrict
                    }


                    if (this.searchTag && this.searchTag != '') {
                        ur = ur + "&tag=" + this.searchTag
                    }

                    this.href(ur)
                }
            },
            href(loc) {
                window.location.href = loc
            },
            getMenu(id) {
                return this.menus.find(menu => menu.id == id);
            },
            getRestaurant(id) {
                return this.restaurants.find(restaurant => restaurant.id == id);
            },
            getTag(id) {
                return this.tags.find(tag => tag.id == id);
            },
            setWord(w) {
                this.search = w;
            }
        }
    })
</script>
</body>

</html>