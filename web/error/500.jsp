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
    <title>404 - Takeaway King</title>
</head>

<body>
<div id="app">
    <v-app>
        <!-- Nav bar -->
        <v-toolbar dark color="primary" app prominent clipped-left>
            <a class="title font-weight-light mr-2" style="color:white" @click="href('/')">Takeaway King
                <v-icon>fastfood</v-icon>
            </a>
            <v-spacer></v-spacer>

        </v-toolbar>
        <!-- Show top search keyword / popular restaurant -->
        <v-content>
            <v-container>
                <div style="position: absolute; right: 20px; bottom: 20px;">
                    <p class="display-4 text-xs-right">500</p>
                    <p class="display-4 text-xs-right">Internal Server Error</p>
                </div>
            </v-container>
        </v-content>
    </v-app>
</div>
<script>
    new Vue({
        el: '#app',
        created() {

        },
        data: {},
        methods: {
            href(h){
                location.href = h;
            }
        }
    })
</script>
</body>

</html>