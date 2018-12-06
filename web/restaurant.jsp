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
        <v-toolbar dark color="orange darken-1" app prominent>
            <span class="title font-weight-light mr-2">Takeaway King</span>
            <v-icon>fastfood</v-icon>
            <v-spacer></v-spacer>
            <v-avatar color="grey">
                <v-icon>person</v-icon>
            </v-avatar>
        </v-toolbar>
        <v-content>
            <div style="width:100%;height: 400px;background:linear-gradient(to bottom, #444, #000)"></div>
            <v-container style="margin-top:-150px;">
                <v-card tile>
                    <v-layout row wrap>
                        <!-- Restaurant Logo -->
                        <v-flex xs3 class="pa-3">
                            <v-img :src="restaurantImg" class="black"></v-img>
                        </v-flex>
                        <!-- Restaurant Description -->
                        <v-flex xs9 class="pl-2 pt-4">
                            <v-layout column>
                                <!-- Restaurant Name -->
                                <v-flex>
                                    <span class="display-3">{{ restaurantName }}</span>
                                </v-flex>
                                <!-- Rating -->
                                <v-flex class="ma-2">
                                    <v-layout row>
                                        <v-flex class="shrink mr-2">
                                            <v-rating v-model="rating" small dense half-increments readonly></v-rating>
                                        </v-flex>
                                        <v-flex class="shrink">
                                            <span>({{ rating }} out of 5)</span>
                                        </v-flex>
                                        <v-flex class="mx-3 shrink">
                                            <v-divider vertical></v-divider>
                                        </v-flex>
                                        <v-flex class="shrink mr-2">
                                            <v-icon>person</v-icon>
                                        </v-flex>
                                        <v-flex class="shrink">
                                            {{ numVisited }} people has visited this restaurant
                                        </v-flex>
                                    </v-layout>
                                </v-flex>
                                <v-flex class="my-3 pr-3">
                                    <v-divider></v-divider>
                                </v-flex>
                                <!-- Tags -->
                                <v-flex>
                                    <v-chip v-for="tag in tags">{{ tag }}</v-chip>
                                </v-flex>
                            </v-layout>
                        </v-flex>
                    </v-layout>
                </v-card>

                <!-- Takeaway Menu -->
                <h2 class="title my-2 font-weight-light">Takeaway Menus</h2>
                <v-card>
                    <v-layout row wrap class="pa-3">
                        <v-flex xs3 v-for="menu in menus" class="pa-2">
                            <v-img :src="menu" aspect-ratio="1.6"></v-img>
                        </v-flex>
                    </v-layout>
                </v-card>

                <!-- Branches -->
                <h2 class="title my-2 font-weight-light">Branches</h2>
                <v-card>
                    <template v-for="branch in branches">
                        <v-layout column>
                            <v-flex>
                                <v-layout row class="pa-3" row align-center>
                                    <!-- Address -->
                                    <v-flex xs8>
                                        <span>{{ branch.address }}, {{ branch.district }}</span>
                                    </v-flex>
                                    <!-- Open Hours -->
                                    <v-flex xs4>
                                        <template v-for="t in branch.openTime">
                                            <span>{{ t.day }}: {{ t.openHour }}</span><br>
                                        </template>
                                    </v-flex>
                                    <!-- Tel -->
                                    <v-flex xs2>
                                        <v-layout column justify-center>
                                            <v-flex>
                                                <v-layout row align-center my-1>
                                                    <v-icon class="mr-4">phone</v-icon>
                                                    <span>{{ branch.tel }}</span>
                                                </v-layout>
                                            </v-flex>
                                            <v-flex>
                                                <v-layout row align-center my-1>
                                                    <v-icon class="mr-4">location_on</v-icon>
                                                    <span>{{
                                                            branch.district
                                                            }}</span>
                                                </v-layout>
                                            </v-flex>

                                        </v-layout>

                                    </v-flex>
                                </v-layout>
                            </v-flex>
                            <v-flex pa-3>
                                <span>Delivers to</span>
                                <template v-if="branch.deliveryDistrict.length>3">
                                    <v-chip>{{ branch.deliveryDistrict[0] }}</v-chip>
                                    <v-chip>{{ branch.deliveryDistrict[1] }}</v-chip>
                                    <v-chip>{{ branch.deliveryDistrict[2] }}</v-chip>
                                    <span> and more....</span>
                                </template>
                                <template v-else>
                                    <v-chip v-for="d in branch.deliveryDistrict">{{ d }}</v-chip>
                                </template>
                            </v-flex>
                        </v-layout>
                        <v-divider></v-divider>
                    </template>


                </v-card>

                <!-- Comment -->
                <h2 class="title my-2 font-weight-light">Comments</h2>
                <v-card>

                    <template v-for="comment in comments">
                        <v-layout class="py-3">
                            <v-flex xs2>
                                <v-layout column justify-center align-center>
                                    <v-flex>
                                        <v-avatar color="orange" class="mb-2">
                                            <v-icon>{{ comment.avatar }}</v-icon>
                                        </v-avatar>
                                    </v-flex>
                                    <v-flex>
                                        {{ comment.author }}
                                    </v-flex>
                                    <v-flex>
                                        <v-rating small dense readonly v-model="comment.rating"></v-rating>
                                    </v-flex>
                                </v-layout>
                            </v-flex>
                            <v-flex xs10>
                                <v-layout column fill-height>
                                    <v-flex xs10>
                                        <div class="body-2">{{ comment.content }}</div>
                                    </v-flex>
                                    <v-flex xs2>
                                            <span class="caption grey--text">Posted on {{
                                                showDate(comment.postTime) }}</span>
                                    </v-flex>
                                </v-layout>
                            </v-flex>
                        </v-layout>
                        <!-- <div>
                                    <v-avatar color="grey" class="mr-4">
                                        <v-icon>{{ comment.avatar }}</v-icon>
                                    </v-avatar>
                                    <span class="subheading mr-4">{{ comment.author }}</span>
                                    <span class="caption grey--text">posted on {{ showDate(comment.postTime) }}</span>
                                </div> -->
                        <v-divider></v-divider>
                    </template>

                </v-card>

            </v-container>
        </v-content>
    </v-app>
</div>
<script>
    new Vue({
        el: '#app',
        created() {
            <%
                String rid = request.getParameter("rid");
                if(rid==null){
                    response.sendRedirect("/error/404.jsp");
                }
            %>

            let rid = <%=rid%>;
        },
        data: {
            restaurantImg: '/img/mcdonalds_0.png',
            restaurantName: 'McDonald\'s',
            rating: 4.5,
            tags: ['fast food', 'fried', 'western', 'hungary'],
            menus: ['/img/m1.jpg', '/img/m2.jpg', '/img/m3.jpg', '/img/m4.jpg', '/img/m5.jpg'],
            numVisited: 23240,
            comments: [{
                author: 'John Linux',
                avatar: 'account_circle',
                content: 'What a wonderful piece of shit',
                postTime: '2018-09-09T13:24:12',
                rating: 4
            }, {
                author: 'John Cena',
                avatar: 'school',
                content: 'Bitch please.',
                postTime: '2018-09-05T17:33:15',
                rating: 5
            }],
            branches: [{
                address: 'A113, fuck you road, DLLM, ONL99',
                district: 'Yau Ma Tei',
                deliveryDistrict: ['Tseung Kwan O', 'Mong Kwok', 'Chim Sha Tsui', 'Sheung Shui'],
                tel: '25890343',
                openTime: [{
                    day: 'Weekday',
                    openHour: '6:00AM - 12:00PM'
                }, {
                    day: 'Sat & Sun',
                    openHour: '9:00AM - 5:00PM'
                }, {
                    day: 'Public holiday',
                    openHour: 'closed'
                }],
            }, {
                address: 'Oh building, Shit load, Uninstall hope',
                district: 'San Ka La',
                deliveryDistrict: ['Lei Lo Mo go Sai', 'Si An'],
                tel: '20632422',
                openTime: [{
                    day: 'Everyday',
                    openHour: '24 hours'
                }],
            }]
        },
        methods: {
            showDate(date) {
                return moment(date).format('YYYY-MM-DD');
            }
        }
    })
</script>
</body>

</html>