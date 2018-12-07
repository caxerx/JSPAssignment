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
            Search
            Can search by Restaurant or Menuf
            Restaurant: Can be searched by keyword, district or tag
            Menu: Can be searched by keyword or tag
         -->
        <v-content>
            <v-container>
                <!-- Search box -->
                <v-layout row align-center>
                    <v-flex>
                        <!-- Search type -->
                        <v-radio-group v-model="searchType" :column="false">
                            <v-radio label="Restaurant" value="restaurant"></v-radio>
                            <v-radio label="Menu" value="menu"></v-radio>
                        </v-radio-group>
                    </v-flex>
                    <v-flex xs8>
                        <!-- Search Keyword -->
                        <v-text-field @keyup.enter="search" class="mr-2" prepend-icon="search" v-model="searchWord"
                                      label="Search" clearable></v-text-field>
                    </v-flex>
                    <v-flex xs5>
                        <!-- District -->
                        <v-select class="mr-2" prepend-icon="place" :items="districts" item-text="name" item-value="id"
                                  v-model="searchDistrict" label="District" clearable></v-select>
                    </v-flex>
                    <v-flex xs4>
                        <!-- Tag -->
                        <v-select prepend-icon="local_offer" :items="tags" item-text="name" item-value="id"
                                  v-model="searchTag"
                                  label="Tag" clearable></v-select>
                    </v-flex>
                    <v-flex>
                        <v-btn color="primary" @click="search">Search</v-btn>
                    </v-flex>
                </v-layout>
                <!-- Filter -->

                <!-- Search Result -->
                <!-- Restaurant -->
                <v-data-iterator v-if="searchType=='restaurant'" row wrap content-tag="v-layout" :items="restaurants"
                                 :rows-per-page-items="[10,20,30]" pagination.sync="pagination">
                    <v-flex slot="item" slot-scope="props" xs12 class="mb-2">
                        <v-card @click="href('/restaurant.jsp?rid='+props.item.id)">
                            <v-layout row>
                                <!-- Image -->
                                <v-flex xs2 class="pa-1">
                                    <v-layout fill-height align-center>
                                        <v-img aspect-ratio="1" :src="'/api/image?id='+props.item.logo"
                                               max-height="150px"></v-img>
                                    </v-layout>
                                </v-flex>
                                <!-- Detail -->
                                <v-flex xs10>
                                    <v-layout column class="pa-2">
                                        <!-- Name -->
                                        <v-flex class="pa-3">
                                            <span class="title">{{ props.item.name }}</span>
                                        </v-flex>
                                        <v-divider></v-divider>
                                        <!-- Menus -->
                                        <v-flex class="py-3 pl-3">
                                            <div class="subheading pb-2 grey--text">Menus</div>
                                            <v-layout row justify-start>
                                                <div class="mr-2" style="height:140px;width:140px;"
                                                     v-for="menu in props.item.menus.slice(0,3)">
                                                    <v-img class="black"
                                                           :src="menu.image.length>0?'/api/image?id='+menu.image[0]:'/static/image/noimage.png'"
                                                           :aspect-ratio="4/3" height="140px" width="140px"
                                                           contain></v-img>
                                                </div>
                                                <div v-if="props.item.menus.length==0" class="body-2 pb-2 grey--text">No
                                                    menus
                                                </div>
                                            </v-layout>
                                        </v-flex>
                                        <!-- Tags -->
                                        <v-flex class="pl-2">
                                            <v-chip v-for="tag in props.item.tags">{{ tag.name }}</v-chip>
                                        </v-flex>
                                    </v-layout>
                                </v-flex>
                            </v-layout>
                        </v-card>
                    </v-flex>
                </v-data-iterator>
                <!-- Menu -->
                <v-data-iterator v-else content-tag="v-layout" row wrap :items="menus" :rows-per-page-items="[12,24,36]"
                                 :pagination.sync="pagination">
                    <v-flex slot="item" slot-scope="props" xs4 pa-1>
                        <v-card @click="href('/restaurant.jsp?rid='+props.item.restaurant.id)">
                            <v-layout column class="pa-2">
                                <!-- Menu Name -->
                                <v-flex class="px-2 pt-2">
                                    <span class="title">{{ props.item.title }}</span>
                                </v-flex>
                                <!-- Restaurant -->
                                <v-flex class="pa-2">
                                    <v-layout row align-center>
                                        <v-icon class="mr-2">store_mall_directory</v-icon>
                                        <span class="caption">{{ props.item.restaurant.name }}</span>
                                    </v-layout>
                                </v-flex>
                                <!-- Menu Image -->
                                <v-flex class="mb-2">
                                    <v-img :aspect-ratio="4/3"
                                           :src="props.item.image.length>0?'/api/image?id='+props.item.image[0]:'/static/image/noimage.png'"></v-img>
                                </v-flex>
                                <!-- Tags -->
                                <v-flex class="pl-2">
                                    <v-chip v-for="tag in props.item.tags">{{ tag.name }}</v-chip>
                                </v-flex>
                            </v-layout>
                        </v-card>
                    </v-flex>
                </v-data-iterator>
            </v-container>
        </v-content>
    </v-app>
</div>
<script>
    <%

    String tp = request.getParameter("type");
    if(tp==null){
        tp = "restaurant";
    }

    String kw = request.getParameter("keyword");
    if(kw==null){
        kw = "";
    }

    String tg = request.getParameter("tag");
    if(tg==null){
        tg = "''";
    }

    String dt = request.getParameter("district");
    if(dt==null){
        dt = "''";
    }
    %>
    new Vue({
        el: '#app',
        created() {
            this.rLd();
        },
        data: {
            searchWord: '', // User entered keywords
            searchDistrict: '',
            searchTag: '',
            searchType: 'restaurant',
            menus: [],
            // menus: [{
            //     id: 0,
            //     restaurant: 0,
            //     name: 'McDonald\'s Addon Menu',
            //     img: '/img/m1.jpg',
            //     tags: [0, 1]
            // }, {
            //     id: 1,
            //     restaurant: 0,
            //     name: 'McDonald\'s Shrimp Special Menu',
            //     img: '/img/m2.jpg',
            //     tags: [0, 1, 3]
            // }],
            restaurants: [],
            // restaurants: [{
            //     id: 0,
            //     name: 'McDonald\'s',
            //     img: '/img/mcdonalds_0.png',
            //     menus: [0, 1],
            //     tags: [0, 1]
            // }],
            districts: ['San Ka La', 'Mo Lui Gong Gum Yuen'],
            tags: [{
                id: 0,
                name: 'Fast food',
            }, {
                id: 1,
                name: 'Western',
            }, {
                id: 2,
                name: 'Eastern',
            }, {
                id: 3,
                name: 'Seafood'
            }], // Already available tags
            pagination: {
                descending: false,
                page: 1,
                rowsPerPage: 12,
            }
        },
        methods: {
            href(loc) {
                window.location.href = loc
            },
            async rLd() {
                await this.loadData();
                setTimeout(() => {
                    this.searchType = '<%=tp%>';
                    this.searchWord = '<%=kw%>';
                    this.searchDistrict = <%=dt%>;
                    this.searchTag = <%=tg%>;
                    this.search()
                }, 10);
            },
            async loadData() {
                let districtReq = axios({
                    url: '/api/district'
                });
                let tagReq = axios({
                    url: '/api/tag'
                });
                let reqs = [];
                reqs.push(districtReq, tagReq);
                let ress = await Promise.all(reqs);
                console.log('District & Tag:', ress)
                this.districts = ress[0].data.content;
                this.tags = ress[1].data.content;
            },
            async search() {
                let url = '/api/search?';
                url += 'keyword=' + (this.searchWord ? this.searchWord : '');
                if (this.searchType == 'menu') url += '&type=menu';
                if (this.searchDistrict) url += '&district=' + this.searchDistrict;
                if (this.searchTag) url += '&tag=' + this.searchTag;
                console.log('Search URL:', url);
                let result = (await axios({
                    url
                })).data;
                console.log('Search result:', result);
                if (this.searchType == 'restaurant') {
                    this.restaurants = result;
                } else {
                    this.menus = result;
                }
            }
            // },
            // getMenu(id) {
            //     return this.menus.find(menu => menu.id == id);
            // },
            // getRestaurant(id) {
            //     return this.restaurants.find(restaurant => restaurant.id == id);
            // },
            // getTag(id) {
            //     return this.tags.find(tag => tag.id == id);
            // }
        }
    })
</script>
</body>

</html>