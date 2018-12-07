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

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js"></script>
<script src="https://unpkg.com/vue-chartjs/dist/vue-chartjs.min.js"></script>

</body>
</html>


<script>
    let m_key = []
    let m_value = []

    let d_key = []
    let d_value = []

    let k_key = []
    let k_value = []


    let mc = Vue.component('month-chart', {
        extends: VueChartJs.Line,
        mounted() {
            setTimeout(() => {
                this.renderChart({
                    labels: m_key,
                    datasets: [
                        {
                            label: 'Total Visitor',
                            backgroundColor: '#800080',
                            data: m_value
                        }
                    ]
                }, {responsive: true, maintainAspectRatio: false})
            }, 200)
        },
    })

    let dc = Vue.component('district-chart', {
        extends: VueChartJs.Bar,
        mounted() {
            setTimeout(() => {
                this.renderChart({
                    labels: d_key,
                    datasets: [
                        {
                            label: 'Total Visitor',
                            backgroundColor: '#800080',
                            data: d_value
                        }
                    ]
                }, {responsive: true, maintainAspectRatio: false})
            }, 200)
        },
    })


    let kc = Vue.component('kind-chart', {
        extends: VueChartJs.Bar,
        mounted() {
            setTimeout(() => {
                this.renderChart({
                    labels: k_key,
                    datasets: [
                        {
                            label: 'Total Visitor',
                            backgroundColor: '#800080',
                            data: k_value
                        }
                    ]
                }, {responsive: true, maintainAspectRatio: false})
            }, 200)
        },
    })

    Vue.use(VuetifyUploadButton);
    new Vue({
        el: '#app',
        data: () => ({
            staticBy: 0,
            rid: -1,
            selectedTags: [],
            tagSearch: "",
            tags: [],
            districts: [],
            restaurantList: [],
            branchList: [],
            menuList: [],
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
            menuImages: [],
            bid: -1,
            mid: -1
        }),
        created() {
            this.$vuetify.theme.primary = '#800080';

            axios.get("/api/stat/owner/month").then(r => {
                for (let i = 0; i <= 11; i++) {
                    m_key.push(moment().month(i).format("MMMM"))
                    m_value.push(r.data[i + 1])
                }
            })

            axios.get("/api/stat/owner/district").then(r => {
                let jd = r.data;
                Object.keys(jd).forEach(k => {
                    d_key.push(k);
                    d_value.push(jd[k])
                })
            })

            axios.get("/api/stat/owner/kind").then(r => {
                let jd = r.data;
                Object.keys(jd).forEach(k => {
                    k_key.push(k);
                    k_value.push(jd[k])
                })
            })


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

            <%--load restaurant from request --%>
            <%
                Object branch = request.getAttribute("branch");
                if(branch!=null){
                 out.println("let branch = '"+branch+"'");
                 out.println("this.branchList = JSON.parse(branch);");
                 }
            %>

            <%--load restaurant from request --%>
            <%
                Object menu = request.getAttribute("menu");
                if(menu!=null){
                 out.println("let menu = '"+menu+"'");
                 out.println("this.menuList = JSON.parse(menu);");
                 }
            %>

            <%
                String rid = request.getParameter("rid");
                if(rid!=null){
                    out.println("let rid = "+rid);
                    out.println("this.rid = rid;");
                }else{
                    out.println("let rid = null");
                }
            %>


            this.restaurantList.map(rest => {
                rest.idName = rest.id + " - " + rest.name;
                return rest
            });

            if (rid) {
                this.selectedRestaurant = this.restaurantList.find(r => r.id == rid);
            }


            <j:if condition="<%=request.getAttribute("editInfo")!=null%>">
            this.addForm.restaurantName = this.selectedRestaurant.name

            this.imageUpload.logo.imageId = this.selectedRestaurant.logo
            this.imageUpload.background.imageId = this.selectedRestaurant.background

            this.selectedTags = this.selectedRestaurant.tags

            this.imageUpload.logo.image = "/api/image?id=" + this.selectedRestaurant.logo
            this.imageUpload.logo.fileName = this.selectedRestaurant.logo

            this.imageUpload.background.image = "/api/image?id=" + this.selectedRestaurant.background
            this.imageUpload.background.fileName = this.selectedRestaurant.background

            </j:if>

            let bId = <%=request.getAttribute("editBranch")%>;
            this.bid = bId;

            <j:if condition="<%=request.getAttribute("editBranch")!=null%>">
            let _b = this.branchList.find(b => b.id == bId);
            this.addBranchForm.branchName = _b.name
            this.addBranchForm.district = _b.districtId
            let t = _b.openTime.split(" - ");
            this.addBranchForm.openTime = t[0]
            this.addBranchForm.closeTime = t[1]
            this.addBranchForm.address = _b.address
            this.addBranchForm.telephone = _b.telephone
            this.selectedTags = _b.deliveryDistrict
            </j:if>

            let mId = <%=request.getAttribute("editMenu")%>;
            this.mid = mId;

            <j:if condition="<%=request.getAttribute("editMenu")!=null%>">
            let _m = this.menuList.find(m => m.id == mId);
            console.log(_m)
            this.addMenuForm.menuName = _m.title;
            let sde = +moment(_m.startTime, "MMM D, YYYY") <= 0
            let ede = +moment(_m.endTime, "MMM D, YYYY") <= 0
            this.addMenuForm.startDate = sde ? "" : moment(_m.startTime, "MMM D, YYYY").format("YYYY-MM-DD")
            this.addMenuForm.endDate = ede ? "" : moment(_m.endTime, "MMM D, YYYY").format("YYYY-MM-DD")
            this.addMenuForm.showMenu = _m.showMenu
            this.selectedTags = _m.tags
            this.menuImages = _m.image
            </j:if>

            /*
            "restaurantId": this.rid,
                        "menuId": this.mid,
                        "menuName": this.addMenuForm.menuName,
                        "startDate": +moment(this.addMenuForm.startDate),
                        "endDate": +moment(this.addMenuForm.endDate),
                        "showMenu": this.addMenuForm.showMenu,
                        "tags": this.selectedTags.map(t => t.id),
                        "images": this.menuImages
             */


            <%--load tags from request --%>
            <%
                Object tags = request.getAttribute("tags");
                if(tags!=null){
                 out.println("let tag = '"+tags+"'");
                 out.println("this.tags = JSON.parse(tag);");
                }
            %>

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
            editRestaurant() {
                if (this.$refs.addForm.validate()) {
                    let request = {
                        restaurantName: this.addForm.restaurantName,
                        logo: this.imageUpload.logo.imageId,
                        background: this.imageUpload.background.imageId,
                        tags: this.selectedTags.map(p => p.id)
                    };
                    this.disableCreateButton = true;
                    axios.put("/api/restaurant?rid=" + this.rid, request).then(r => {
                        if (r.data.success) {
                            this.href("/restaurant/dashboard?action=info&rid=" + this.rid)
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
            },
            districtName(distId) {
                return this.districts.find(dist => dist.id == distId).name
            },
            createBranch() {
                if (this.$refs.addBranch.validate()) {
                    let request = {
                        "restaurantId": this.rid,
                        "branchName": this.addBranchForm.branchName,
                        "district": this.addBranchForm.district,
                        "openTime": this.addBranchForm.openTime,
                        "closeTime": this.addBranchForm.closeTime,
                        "address": this.addBranchForm.address,
                        "phoneNumber": this.addBranchForm.telephone,
                        "deliveryDistrict": this.selectedTags.map(r => r.id)
                    }
                    this.disableCreateButton = true;
                    axios.post("/api/branch", request).then(r => {
                        if (r.data.success) {
                            this.href("/restaurant/dashboard?action=listbranch&rid=" + this.rid)
                        }
                    })
                }
            },
            editBranch() {
                if (this.$refs.addBranch.validate()) {
                    let request = {
                        "branchId": this.bid,
                        "restaurantId": this.rid,
                        "branchName": this.addBranchForm.branchName,
                        "district": this.addBranchForm.district,
                        "openTime": this.addBranchForm.openTime,
                        "closeTime": this.addBranchForm.closeTime,
                        "address": this.addBranchForm.address,
                        "phoneNumber": this.addBranchForm.telephone,
                        "deliveryDistrict": this.selectedTags.map(r => r.id)
                    }
                    this.disableCreateButton = true;
                    axios.put("/api/branch", request).then(r => {
                        if (r.data.success) {
                            this.href("/restaurant/dashboard?action=listbranch&rid=" + this.rid)
                        }
                    })
                }
            },
            createMenu() {
                if (this.$refs.addMenu.validate()) {
                    let request = {
                        "restaurantId": this.rid,
                        "menuName": this.addMenuForm.menuName,
                        "startDate": +moment(this.addMenuForm.startDate),
                        "endDate": +moment(this.addMenuForm.endDate),
                        "showMenu": this.addMenuForm.showMenu,
                        "tags": this.selectedTags.map(t => t.id),
                        "images": this.menuImages

                    }
                    this.disableCreateButton = true;
                    axios.post("/api/menu", request).then(r => {
                        if (r.data.success) {
                            this.href("/restaurant/dashboard?action=listmenu&rid=" + this.rid)
                        }
                    })
                }
            },
            editMenu() {
                if (this.$refs.addMenu.validate()) {
                    let request = {
                        "restaurantId": this.rid,
                        "menuId": this.mid,
                        "menuName": this.addMenuForm.menuName,
                        "startDate": +moment(this.addMenuForm.startDate),
                        "endDate": +moment(this.addMenuForm.endDate),
                        "showMenu": this.addMenuForm.showMenu,
                        "tags": this.selectedTags.map(t => t.id),
                        "images": this.menuImages
                    }
                    this.disableCreateButton = true;
                    axios.put("/api/menu", request).then(r => {
                        if (r.data.success) {
                            this.href("/restaurant/dashboard?action=listmenu&rid=" + this.rid)
                        }
                    })
                }
            },
            checkDate(date) {
                if (date == "Jan 1, 1970") {
                    return false;
                }
                return true;
            },
            toggleVisibility(id) {
                axios.get("/api/menuvisibility?menuId=" + id).then(r => {
                    let menu = this.menuList.find(m => m.id == id)
                    menu.showMenu = !menu.showMenu
                })
            },
            deleteRestaurant(id) {
                axios.delete("/api/restaurant?restaurantId=" + id).then(r => {
                    location.reload()
                })
            },
            deleteBranch(id) {
                axios.delete("/api/branch?branchId=" + id).then(r => {
                    location.reload()
                })
            },
            deleteMenu(id) {
                axios.delete("/api/menu?menuId=" + id).then(r => {
                    location.reload()
                })
            }
        },
        computed: {
            filteredTags() {
                return this.tags.filter(tag => tag.name.includes(this.tagSearch) && this.selectedTags.findIndex(t => t.id == tag.id) < 0);
            },
            filteredDistricts() {
                return this.districts.filter(tag => tag.name.includes(this.tagSearch) && this.selectedTags.findIndex(t => t.id == tag.id) < 0);
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
