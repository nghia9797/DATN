<template>
    <div>
        <Header></Header>
        <div style="width: 100%;">
            <div class="body">
                <div class="box-filter">
                    <div style="min-width: 100px;">Sản phẩm/ </div>
                    <el-input style="margin-right: 8px;" placeholder="Hôm nay bạn tìm cây gì? ..." v-model="keySearch"></el-input>
                    <el-select v-model="chooseSort" placeholder="Sắp xếp" style="margin-right: 8px; min-width: 200px;">
                        <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                        </el-option>
                    </el-select>
                    <el-button @click="searchTree">Tìm kiếm</el-button>
                </div>
                <div class="display">
                    <div class="product">
                        <div v-if="listTree.length == 0">Hiện không có sản phẩm nào</div>
                        <div v-else class="box-product">
                            <div v-for="itemProduct in listTree" class="item-product">
                                <img class="image-product" v-bind:src="getUrlOfResource(itemProduct.images)"/>
                                <div class="code">{{ itemProduct.code }}</div>
                                <div class="name">{{ itemProduct.name }}</div>
                                <div class="discount" v-if="itemProduct.discount > 0">-{{ itemProduct.discount }}%</div>
                                <div class="cost">
                                    <div v-if="itemProduct.discount > 0">
                                        <span class="giamoi">{{ getStringForMoney(getNewCost(itemProduct)) }}</span> - 
                                        <span class="giacu">{{ getStringForMoney(itemProduct.cost) }}</span>
                                    </div>
                                    <div v-else><span class="giamoi">{{ getStringForMoney(itemProduct.cost) }}</span></div>
                                </div>
                                <div class="status">
                                    <span v-if="itemProduct.count > 0">Còn hàng</span>
                                    <span v-else>Hết hàng</span>
                                </div>
                                <div class="box-button">
                                    <div class="view" @click="gotoDetail(itemProduct.id)">Chi tiết</div>
                                    <div v-if="itemProduct.count > 0" class="addCart" @click="addToCart(itemProduct.id)">Thêm</div>
                                </div>
                            </div>
                        </div>
                        <div v-if="listTree.length > 0">
                            <el-pagination
                                background
                                layout="prev, pager, next"
                                :total="totalProduct"
                                :page-size="pageSize"
                                @current-change="pageChange"
                                :current-page="pageNumber + 1"
                                >
                                </el-pagination>
                        </div>
                    </div>
                    <div class="box-type">
                        <div style="font-size: 16px; color:#75c321; font-weight: bold; margin-bottom: 20px;">Danh mục sản phẩm</div>
                        <div class="item-type" @click="changeKeyType(-1)">Tất cả</div>
                        <div v-for="itemType in listType" class="item-type" @click="changeKeyType(itemType.id)">{{ itemType.name }}</div>
                    </div>
                </div>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>
<style scoped>
    .body{
        width: 80%;
        margin: auto;
        margin-top: 40px;
    }
    .box-filter{
        display: flex;
        justify-content: flex-start;
        align-items: center;
    }
    .display{
        display: flex;
        justify-content: space-between;
        margin-top: 40px;
    }
    .product{
        width: 80%;
    }
    .box-product{
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
    }
    .item-product{
        position: relative;
        width: 24%;
        height: fit-content;
        margin-right: 2px;
        margin-bottom: 12px;
    }
    .image-product{
        width: 100%;
        height: 100%;
        max-height: 300px;
        min-height: 300px;
        object-fit: cover;
    }
    .box-type{
        width: 19%;
    }
    .item-type{
        text-align: left;
        padding: 10px 20px;
        width: calc(100% - 40px);
        border: 1px solid lightgray;
        cursor: pointer;
    }

    .code, .name, .status, .cost{
        text-align: center;
        padding: 0px 10px;
        width: calc(100% - 20px);
        margin-top: 4px;
    }

    .discount{
        position: absolute;
        top: 20px;
        left: 20px;
        background-color: red;
        color: white;
        padding: 4px 8px;
        border: 1px solid transparent;
        border-radius: 2px;
    }
    .box-button{
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 4px;
    }

    .view, .addCart{
        padding: 10px 10px;
        border: 1px solid transparent;
        border-radius: 5px;
        cursor: pointer;
    }
    .view{
        background-color: #75c321;
        color: white;
    }
    .addCart{
        background-color: #FF9900;
        color: white;
        margin-left: 8px;
    }

    .giamoi{
        color: #75c321;
        padding: 4px 8px;
        font-weight: bold;
    }
    .giacu{
        color: gray;
        padding: 4px 8px;
        text-decoration: line-through;
    }
</style>
<script>
    import { store } from '../store';
    import { RestFul } from '../restful';
    import Header from './common/Header.vue';
    import Footer from './common/Footer.vue';
    export default {
    data: function () {
        return {
            listTree:[
                {
                    id: 1,
                    code:"Code",
                    name:"Name",
                    cost:100000,
                    discount: 50,
                    description: "Description",
                    count: 100,
                    images:["/resource/images/daobonsai.jpg"]
                },
                {
                    id: 1,
                    code:"Code",
                    name:"Name",
                    cost:100000,
                    discount: 50,
                    description: "Description",
                    count: 0,
                    images:["/resource/images/daobonsai.jpg"]
                },
                {
                    id: 1,
                    code:"Code",
                    name:"Name",
                    cost:100000,
                    discount: 50,
                    description: "Description",
                    count: 100,
                    images:["/resource/images/daobonsai.jpg"]
                },
                {
                    id: 1,
                    code:"Code",
                    name:"Name",
                    cost:100000,
                    discount: 50,
                    description: "Description",
                    count: 100,
                    images:["/resource/images/daobonsai.jpg"]
                },
                {
                    id: 1,
                    code:"Code",
                    name:"Name",
                    cost:100000,
                    discount: 50,
                    description: "Description",
                    count: 100,
                    images:["/resource/images/daobonsai.jpg"]
                },
            ],
            listType:[
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
                {
                    id: 1,
                    name: "type"
                },
            ],
            keySearch:"",
            chooseSort:"",
            options:[
                {
                    label:"Mới nhất",
                    value:"updated#desc"
                },
                {
                    label:"Giá từ thấp đến cao",
                    value:"cost#asc"
                },
                {
                    label:"Giá từ cao đến thấp",
                    value:"cost#desc"
                }
            ],
            keyType: -1,
            pageSize: 8,
            pageNumber: 0,
            totalProduct: 0
        };
    },
    methods: {
        changeName: function () {
            store.setUsername(this.name);
        },
        getUrlOfResource: function(images){
            if((images || []).length > 0){
                return store.baseUrl+images[0];
            }
            return "";
        },
        changeKeyType: function(typeId){
            this.keyType = typeId;
            this.keySearch = "";
            this.searchTree();
            this.pageNumber = 0;
        },
        searchTree: async function(){
            let params = {
                name: this.keySearch,
                typeId: this.keyType,
                page: this.pageNumber,
                limit: this.pageSize
            }
            if((this.chooseSort || "").length > 0){
                params.fieldSort = this.chooseSort.split("#")[0];
                params.typeSort = this.chooseSort.split("#")[1]
            }
            let res = await RestFul.get("/api/manage/tree/searchfree", null, params);
            if(res != null){
                if(res.statusCode == 200){
                    this.listTree = res.data.data;
                    this.totalProduct = res.data.count;
                }
            }
        },
        getAllTreeType: async function(){
            let res = await RestFul.get("/api/manage/treetype/getAllFree");
            if(res != null){
                if(res.statusCode == 200){
                    this.listType = res.data;
                }
            }
        },
        addToCart: function(idTree){
            let carts = localStorage.getItem("carts");
            if(carts == null){
                carts = [];
            }else{
                carts = JSON.parse(carts);
            }
            let idItems = carts.map(el => el.id);
            if(!idItems.includes(idTree)){
                carts.push({id: idTree, count: 1});
            }
            store.setCarts(carts);
            localStorage.setItem("carts", JSON.stringify(carts));
            this.$message.success("Đã thêm vào giỏ hàng")
        },
        getNewCost: function(product){
            return Math.round(product.cost * (100 - product.discount) / 100);
        },
        gotoDetail: function(id){
            this.$router.push('/detail/'+id);
        },
        pageChange: function(page){
            this.pageNumber = page - 1;
            this.searchTree();
        },
        nexpPage: function(page){
            
        },
        prevPage: function(page){
            
        },
        getStringForMoney: function(money){
            return new Intl.NumberFormat('vn-VN').format(money) + "đ";
        },
        checkKeyup(){
            let keyCode = this.$event.keyCode;
            alert(keyCode);
            if(keyCode == 13){
                this.searchTree();
            }
        }
    },
    computed:{
        
    },
    created: function(){
        this.searchTree();
        this.getAllTreeType();
    },
    components: { Header, Footer }
}
</script>