<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<jsp:useBean id="loggedInAs" scope="session" class="com.caxerx.bean.User"/>
<%
    boolean loggedIn = false;
    if (loggedInAs != null && loggedInAs.getId() != 0) {
        loggedIn = true;
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
        <v-content>
            <div style="width:100%;height: 400px;background:linear-gradient(to bottom, #444, #000)">
                <v-img :src="backgroundImg" max-height="400px">
                    <v-layout fill-height style="background:linear-gradient(to top, rgba(0,0,0,0.7),rgba(0,0,0,0))">
                    </v-layout>
                </v-img>
            </div>
            <v-container style="margin-top:-150px;">
                <v-card tile>
                    <v-layout row>
                        <!-- Restaurant Logo -->
                        <v-flex class="pa-3">
                            <v-img :src="restaurantImg" aspect-ratio="1" class="black"></v-img>
                        </v-flex>
                        <!-- Restaurant Description -->
                        <v-flex xs9 class="pl-2 pt-4">
                            <v-layout column>
                                <!-- Restaurant Name -->
                                <v-flex>
                                    <v-layout row align-center>
                                        <span class="display-3 mr-3">{{ restaurantName }}</span>
                                        <j:if condition="<%=loggedIn%>">
                                            <v-btn icon @click="favouriteRest()">
                                                <v-icon :color="favorited ? 'pink' : 'grey'">favorite</v-icon>
                                            </v-btn>
                                            <v-btn icon @click="likeRest()">
                                                <v-icon :color="liked ? 'blue' : 'grey'">thumb_up</v-icon>
                                            </v-btn>
                                        </j:if>
                                    </v-layout>
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
                                    <v-chip v-for="tag in tags">{{ tag.name }}</v-chip>
                                </v-flex>
                            </v-layout>
                        </v-flex>
                    </v-layout>
                </v-card>

                <!-- Takeaway Menu -->
                <h2 class="title my-2 font-weight-light">Takeaway Menus</h2>

                <v-layout row wrap class="py-3">
                    <v-flex xs3 v-for="menu in menus" class="pa-1">
                        <v-card dark>
                            <v-img v-if="menu.image.length>0" :src="menu.image[0]" aspect-ratio="1.6"
                                   @click="openMenu(menu)"></v-img>
                            <v-img v-else src="/static/image/noimage.png"
                                   aspect-ratio="1.6"></v-img>
                            <v-toolbar transparent flat>
                                {{ menu.title }}
                                <j:if condition="<%=loggedIn%>">
                                    <v-spacer></v-spacer>
                                    <v-btn icon @click="favMenu(menu.id)">
                                        <v-icon :color="menu.fav ? 'pink' : 'grey'">favorite</v-icon>
                                    </v-btn>
                                </j:if>
                            </v-toolbar>
                        </v-card>
                    </v-flex>
                </v-layout>
                <!-- Menu Detail -->
                <v-dialog v-model="menuDialog" max-width="1200px">
                    <v-card dark v-if="openedMenu">
                        <v-layout row align-center justify-center>
                            <v-flex xs1>
                                <v-layout row justify-center align-center>
                                    <v-btn icon @click="menuPage--" :disabled="menuPage==0">
                                        <v-icon>arrow_left</v-icon>
                                    </v-btn>
                                </v-layout>
                            </v-flex>
                            <v-flex xs10>
                                <v-img :src="openedMenu.image[menuPage]" max-height="80vh" contain></v-img>
                            </v-flex>
                            <v-flex xs1>
                                <v-layout row justify-center align-center>
                                    <v-btn icon @click="menuPage++" :disabled="menuPage==openedMenu.image.length-1">
                                        <v-icon>arrow_right</v-icon>
                                    </v-btn>
                                </v-layout>
                            </v-flex>
                        </v-layout>
                    </v-card>
                </v-dialog>

                <!-- Branches -->
                <h2 class="title my-2 font-weight-light">Branches</h2>
                <v-card>
                    <template v-for="branch in branches">
                        <v-layout column fill-height>
                            <v-flex>
                                <v-layout row class="pa-3" row align-center fill-height>
                                    <!-- Address -->
                                    <v-flex xs8>
                                        <span>
                                            <v-icon class="mr-2">store_mall_directory</v-icon>
                                            {{ branch.address }}, {{ branch.district.name }}</span>
                                    </v-flex>
                                    <!-- Tel -->
                                    <v-flex xs6>
                                        <v-layout column justify-center fill-height>
                                            <v-flex>
                                                <v-layout row align-center my-1>
                                                    <v-icon class="mr-4">schedule</v-icon>
                                                    <span>{{
                                                            branch.openTime
                                                            }}</span>
                                                </v-layout>
                                            </v-flex>
                                            <v-flex>
                                                <v-layout row align-center my-1>
                                                    <v-icon class="mr-4">phone</v-icon>
                                                    <span>{{ branch.telephone }}</span>
                                                </v-layout>
                                            </v-flex>
                                            <v-flex>
                                                <v-layout row align-center my-1>
                                                    <v-icon class="mr-4">location_on</v-icon>
                                                    <span>{{
                                                            branch.district.name
                                                            }}</span>
                                                </v-layout>
                                            </v-flex>
                                        </v-layout>
                                    </v-flex>
                                </v-layout>
                            </v-flex>
                            <v-flex pa-3>
                                <span>Delivers to</span>
                                <v-chip v-for="d in branch.deliveryDistrict.slice(0,3)">{{ d.name }}</v-chip>
                                <span v-if="branch.deliveryDistrict.length>3"> and more....</span>
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
                                            <v-icon>account_circle</v-icon>
                                        </v-avatar>
                                    </v-flex>
                                    <v-flex>
                                        {{ comment.user.username }}
                                    </v-flex>
                                    <v-flex>
                                        <v-rating small dense readonly v-model="comment.rating"></v-rating>
                                    </v-flex>
                                </v-layout>
                            </v-flex>
                            <v-flex xs10>
                                <v-layout column fill-height>
                                    <v-flex xs10>
                                        <div class="body-2">{{ comment.comment }}</div>
                                    </v-flex>
                                    <v-flex xs2>
                                            <span class="caption grey--text">Posted on {{
                                                showDate(comment.postTime) }}</span>
                                    </v-flex>
                                </v-layout>
                            </v-flex>
                        </v-layout>
                        <v-divider></v-divider>
                    </template>
                    <!-- New Comment -->
                    <j:if condition="<%=loggedIn%>">

                        <v-layout column pa-3>
                            <span class="subheading">Add a new comment</span>
                            <v-layout row align-center>
                                Rating:
                                <v-rating v-model="newCommentRating"></v-rating>
                            </v-layout>
                            <v-textarea v-model="newComment" hide-details></v-textarea>
                        </v-layout>
                        <v-layout row px-3 pb-3>
                            <v-spacer></v-spacer>
                            <v-btn color="orange" @click="postComment">Submit</v-btn>
                        </v-layout>
                    </j:if>
                </v-card>

            </v-container>
        </v-content>
    </v-app>
</div>
<%
    if (request.getParameter("rid") == null) {
        response.sendRedirect("/error/404.jsp");
    }
%>
<script>
    new Vue({
        el: '#app',
        created() {
            this.loadData();
        },

        data: {
            rid: <%=request.getParameter("rid")%>,
            newCommentRating: 5,
            newComment: '',
            liked: false,
            favorited: false,
            restaurantImg: '',
            backgroundImg: '',
            restaurantName: '',
            rating: 0,
            tags: [],
            // Menu Dialog
            menuDialog: false,
            openedMenu: undefined,
            menuPage: 0,
            menus: [],
            numVisited: 0,
            comments: [],
            branches: []
        },
        methods: {
            href(loc) {
                window.location.href = loc
            },
            async loadData() {
                try {
                    <j:if condition="<%=loggedIn%>">
                    let lk = await axios.get("/api/liked?restaurantId=" + this.rid);
                    this.liked = lk.data.content
                    let fv = await axios.get("/api/favourited?restaurantId=" + this.rid);
                    this.favorited = fv.data.content
                    </j:if>

                    let requests = [];
                    let restaurantResult = await axios({
                        url: '/api/restaurant?id=' + this.rid
                    });
                    let rData = restaurantResult.data;
                    console.log('Restaurant data:', rData);
                    this.menus = rData.menus.map(m => {
                        m.fav = false;
                        m.image = m.image.map(i => '/api/image?id=' + i)
                        return m;
                    });
                    <j:if condition="<%=loggedIn%>">
                    for (i in this.menus) {
                        let fv2 = await axios.get("/api/favourited?menuId=" + this.menus[i].id);
                        this.menus[i].fav = fv2.data.content
                    }
                    </j:if>
                    this.branches = rData.branchs;
                    this.tags = rData.tags;
                    this.restaurantName = rData.name;
                    this.restaurantImg = '/api/image?id=' + rData.logo;
                    this.backgroundImg = '/api/image?id=' + rData.background;
                    this.comments = rData.comments;
                    this.comments.forEach(r => {
                        this.rating += r.rating
                    })
                    if (this.comments.length > 0) {
                        this.rating = new Number(parseFloat(this.rating / this.comments.length).toFixed(2))
                    }
                    this.numVisited = rData.visitor;
                } catch (e) {
                    this.href("/index.jsp")
                }
            },
            showDate(date) {
                return moment(date).format('YYYY-MM-DD');
            },
            openMenu(menu) {
                this.menuPage = 0;
                this.openedMenu = menu;
                this.menuDialog = true;
            },
            async favouriteRest() {
                await axios({
                    url: '/api/favourite?restaurantId=' + this.rid
                }).then(() => {
                    location.reload()
                })
            },
            async favMenu(id) {
                await axios({
                    url: '/api/favourite?menuId=' + id
                }).then(() => {
                    location.reload()
                })
            },
            async likeRest() {
                await axios({
                    url: '/api/like?restaurantId=' + this.rid
                }).then(() => {
                    location.reload()
                })
            },
            async postComment() {
                await axios({
                    url: '/api/comment?restaurantId=' + this.rid + '&comment=' +
                        this.newComment + '&rating=' + this.newCommentRating
                })
                this.newComment = '';
                this.newCommentRating = 5;
                this.loadData();
            }
        }
    })
</script>
</body>

</html>