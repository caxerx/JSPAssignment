<%@ taglib prefix="j" uri="/WEB-INF/if.tld" %>
<%@ page import="com.caxerx.bean.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String includePage = (String) request.getAttribute("action");

    String navPage = (String) request.getAttribute("nav");
    navPage = "nav-drawer-" + navPage + ".jsp";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <link href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons' rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <title><%=request.getAttribute("actionName")%>
        - <%=request.getServletContext().getInitParameter("application-name")%>
    </title>
</head>

<body>
<div id="app">
    <v-app>
        <v-content>
            <jsp:include page="navbar.jsp"/>
            <jsp:include page="<%=navPage%>"/>
            <v-toolbar color="grey lighten-3" flat fixed style="padding-left:300px;margin-top:64px;z-index: 100">
                <v-breadcrumbs :items="breadcrumb"></v-breadcrumbs>
            </v-toolbar>
            <v-container style="margin-top:64px">
                <jsp:include page="<%=includePage%>"/>
            </v-container>
        </v-content>
    </v-app>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js "></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify@1.3.9/dist/vuetify.js "></script>
<script src="https://cdn.jsdelivr.net/npm/axios@0.18.0/dist/axios.js"></script>

<script src="https://cdn.jsdelivr.net/npm/vuetify-upload-button@1.2.1/dist/vuetify-upload-button.min.js"></script>
</body>
</html>


<script>
    Vue.use(VuetifyUploadButton);
    new Vue({
        el: '#app',
        data: () => ({
            selectedTags: [],
            tagSearch: "",
            tags: [],
            districts: [],
            restaurantList: [],
            breadcrumb: [
                {
                    text: "Restaurant Management"
                },
                {
                    text: "<%=request.getAttribute("actionName")%>",
                    disabled: true
                }
            ],
            imageUpload: {
                logo: {
                    image: "/static/image/noimage.png",
                    progress: -1,
                    fileName: "",
                    tempFileName: "",
                    imageId: -1
                },
                background: {
                    image: "/static/image/noimage.png",
                    progress: -1,
                    fileName: "",
                    tempFileName: "",
                    imageId: -1
                }
            },
            addForm: {
                restaurantName: "",
            },
            addMenuForm: {
                menuName: "",
                startDate: "",
                endDate: "",
                showMenu: true
            },
            addBranchForm: {
                branchName: "",
                district: "",
                openTime: "",
                closeTime: ""
            },
            disableCreateButton: false,
            selectedRestaurant: null,
            startDateMenu: false,
            endDateMenu: false,
            openTimeMenu: false,
            closeTimeMenu: false,
            menuImages: []
        }),
        created() {
            this.$vuetify.theme.primary = '#800080';


            <%--load tags from request --%>
            <%
                Object tags = request.getAttribute("tags");
                if(tags!=null){
                 out.println("let tag = '"+tags+"'");
                 out.println("this.tags = JSON.parse(tag);");
                }
            %>


            <%--load district from request --%>
            <%
                Object district = request.getAttribute("districts");
                if(district!=null){
                 out.println("let district = '"+district+"'");
                 out.println("this.districts = JSON.parse(district);");
                }
            %>

            <%--load restaurant from request --%>
            <%
                Object restaurant = request.getAttribute("restaurant");
                 out.println("let restaurant = '"+restaurant+"'");
                 out.println("this.restaurantList = JSON.parse(restaurant);");
            %>

            <%
                String rid = request.getParameter("rid");
                if(rid!=null){
                    out.print("let rid = "+rid);
                }
            %>

            this.restaurantList.map(rest => {
                rest.idName = rest.id + " - " + rest.name;
                return rest
            });

            if (rid) {
                let idx = this.restaurantList.findIndex(r => r.id == rid);
                if (idx > 0) {
                    this.selectedRestaurant = this.restaurantList[idx].idName;
                }
            }
        },
        methods: {
            href(loc) {
                window.location.href = loc
            },
            uploadFile(file, prog) {
                console.log(file);
                let formData = new FormData();
                formData.append("file", file);
                return axios.post("/api/upload", formData, {
                    onUploadProgress: prog
                })
            },
            uploadBackground(file) {
                console.log("uploadBackground")
                this.imageUpload.background.tempFileName = file.name;
                this.uploadFile(file, this.onBackgroundUploadProgress).then(r => {
                    if (r.data.success) {
                        this.imageUpload.background.fileName = this.imageUpload.background.tempFileName;
                        this.imageUpload.background.image = "/api/image?id=" + r.data.content.imageId
                        this.imageUpload.background.imageId = r.data.content.imageId;
                    }
                })
            },
            uploadLogo(file) {
                console.log("uploadLogo")
                this.imageUpload.logo.tempFileName = file.name;
                this.uploadFile(file, this.onLogoUploadProgress).then(r => {
                    if (r.data.success) {
                        this.imageUpload.logo.fileName = this.imageUpload.logo.tempFileName;
                        this.imageUpload.logo.image = "/api/image?id=" + r.data.content.imageId
                        this.imageUpload.logo.imageId = r.data.content.imageId;
                    }
                })
            },
            onBackgroundUploadProgress(e) {
                let prog = e.loaded / e.total;
                if (prog == 1) {
                    this.imageUpload.background.progress = -1;
                }
                this.imageUpload.background.progress = prog * 100;
            },
            onLogoUploadProgress(e) {
                let prog = e.loaded / e.total;
                if (prog == 1) {
                    this.imageUpload.logo.progress = -1;
                }
                this.imageUpload.logo.progress = prog * 100;
            },
            addTag(tag) {
                this.selectedTags.push(tag);
            },
            removeTag(tag) {
                this.selectedTags.splice(this.selectedTags.indexOf(tag), 1);
            },
            resetTag() {
                this.selectedTags.splice(0, this.selectedTags.length)
            },
            createRestaurant() {
                if (this.$refs.addForm.validate()) {
                    let request = {
                        restaurantName: this.addForm.restaurantName,
                        logo: this.imageUpload.logo.imageId,
                        background: this.imageUpload.background.imageId,
                        tags: this.selectedTags.map(p => p.id)
                    };
                    this.disableCreateButton = true;
                    axios.post("/api/restaurant", request).then(r => {
                        if (r.data.success) {
                            this.href("/restaurant/dashboard?action=list")
                        }
                    })
                }
            },
            uploadMenuImg(file) {
                this.uploadFile(file, () => {
                }).then(uFile => {
                    console.log(uFile)
                    if (uFile.data.success) {
                        this.menuImages.push(uFile.data.content.imageId)
                    }
                })
            },
            removeMenuImg(imgId) {
                this.menuImages.splice(this.menuImages.indexOf(imgId), 1)
            }
        },
        computed: {
            filteredTags() {
                return this.tags.filter(tag => tag.name.includes(this.tagSearch) && !this.selectedTags.includes(tag));
            },
            filteredDistricts() {
                return this.districts.filter(tag => tag.name.includes(this.tagSearch) && !this.selectedTags.includes(tag));
            }
        }
    })
</script>


<style>
    .menuimg {
        opacity: 0.5;
        transition: opacity 0.5s;
    }

    .menuimg:hover {
        opacity: 1;
    }
</style>
