<jsp:useBean id="loggedInAs" class="com.caxerx.bean.User" scope="session"/>
<%
    boolean loggedIn = false;

    if (loggedInAs != null && loggedInAs.getId() != 0) {
        loggedIn = true;
    }

    if (!loggedIn) {
        response.sendRedirect("/login.jsp");
    }
%>

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
        <!--
            Favourite Page
            Show favourited restaurants and menus
         -->
        <v-content>
            <v-container>
                <div class="display-2">Favourites</div>
                <!-- Restaurants -->
                <div class="display-1">Restaurants</div>
                <v-data-iterator content-tag="v-layout" row wrap :items="restaurants" :rows-per-page-items="[12,24,36]"
                                 pagination.sync="pagination">
                    <v-flex xs4 slot="item" slot-scope="props" pa-1>
                        <v-card>
                            <v-img aspect-ratio="1" :src="'/api/image?id='+props.item.logo"></v-img>
                            <v-toolbar flat color="transparent">
                                <div class="title">{{ props.item.name }}</div>
                                <v-spacer></v-spacer>
                                <v-btn icon @click="removeRestaurantFavorite(props.item)">
                                    <v-icon color="red">delete</v-icon>
                                </v-btn>
                            </v-toolbar>
                        </v-card>
                    </v-flex>
                </v-data-iterator>
                <!-- Takeaway menus -->
                <div class="display-1">Takeaway menus</div>
                <v-data-iterator content-tag="v-layout" row wrap :items="menus" :rows-per-page-items="[12,24,36]"
                                 pagination.sync="pagination">
                    <v-flex xs4 slot="item" slot-scope="props" pa-1>
                        <v-card>
                            <v-img aspect-ratio="1"
                                   :src="props.item.image.length?'/api/image?id='+props.item.image[0]:'/static/image/noimage.png'"></v-img>
                            <v-toolbar flat color="transparent">
                                <div class="title">{{ props.item.title }}</div>
                                <v-spacer></v-spacer>
                                <v-btn icon @click="removeMenuFavorite(props.item)">
                                    <v-icon color="red">delete</v-icon>
                                </v-btn>
                            </v-toolbar>
                            <v-divider></v-divider>
                            <v-layout row align-center pa-3>
                                <v-icon class="mr-2">store_mall_directory</v-icon>
                                <span>{{ props.item.restaurant.name }}</span>
                            </v-layout>
                        </v-card>
                    </v-flex>
                </v-data-iterator>
            </v-container>
        </v-content>
    </v-app>
</div>
<script>
    new Vue({
        el: '#app',
        async created() {
            await axios({
                url: '/api/login',
                method: 'post',
                data: {
                    username: 'c',
                    password: '1'
                }
            })
            this.loadData();
        },
        data: {
            restaurants: [],
            menus: []
        },
        methods: {
            href(url) {
                location.href = url;
            },
            async loadData() {
                let data = (await axios({
                    url: '/api/fav'
                })).data;
                console.log('Restaurant and Menu data:', data);
                this.restaurants = data.restaurant;
                this.menus = data.menu;
            },
            async removeRestaurantFavorite(item) {
                await axios({
                    url: '/api/favourite?restaurantId=' + item.id,
                });
                console.log('Unfavourite restaurant', item.id, '/api/favourite?restaurantId=' + item.id);
                this.restaurants.splice(this.restaurants.indexOf(item), 1);
            },
            async removeMenuFavorite(item) {
                await axios({
                    url: '/api/favourite?menuId=' + item.id,
                });
                console.log('Unfavourite menu', item.id, '/api/favourite?menuId=' + item.id);
                this.menus.splice(this.menus.indexOf(item), 1);
            }
        }
    })
</script>
</body>

</html>