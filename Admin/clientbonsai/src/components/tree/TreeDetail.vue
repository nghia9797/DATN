<template>
    <div>
        <Header></Header>
        <div style="display: flex;justify-content: flex-start; width: 70%;margin: auto;">
            <el-button style="margin-top: 20px;margin-bottom: 20px;" type="default" @click="$router.push('/')">Trở về xem</el-button>
        </div>
        <div class="body">
            <div class="imgs">
                <el-carousel height="700px" indicator-position="outside">
                    <el-carousel-item v-for="item in (tree.images || [])" :key="item+(Math.random())">
                        <img class="image" :src="getUrlOfResource(item)">
                    </el-carousel-item>
                </el-carousel>
            </div>
            <div class="content">
                <div class="text">Danh mục: {{ tree.treeType.name }}</div>
                <div class="text">Mã SP: {{ tree.code }}</div>
                <div class="text" style="font-size: 24px; font-weight: bold;">{{ tree.name }}</div>
                <div v-if="tree.discount > 0" style="display: flex; justify-content: space-between; align-items: center;">
                    <div class="giamoi text">{{ getStringForMoney(getNewCost(tree)) }}</div>
                    <div class="text">Giá cũ: <span class="giacu">{{ getStringForMoney(tree.cost) }}</span></div>
                </div>
                <div v-else class="giamoi text">{{ getStringForMoney(tree.cost) }}</div>
                <div v-if="tree.discount > 0" class="text">
                    Tiết kiệm: <span>{{ getStringForMoney(getSaving(tree)) }}</span>
                    <span style="padding: 10px 20px; background-color: red; color: white; font-weight: bold;border: 1px solid transparent; border-radius: 5px;">{{ tree.discount }}%</span>
                </div>
                <div class="text"><b>Mô tả</b></div>
                <div style="text-align: justify;margin-bottom: 12px;" v-html="tree.description"></div>
                <div v-if="tree.count > 0" style="display: flex; justify-content: space-between; align-items: center;">
                    <el-input-number v-model="numValue" :min="1" :max="tree.count" style="margin-right: 12px;"></el-input-number>
                    <el-button type="success" @click="addToCart(tree.id)">Thêm vào giỏ hàng</el-button>
                </div>
                <div v-else>Hết hàng</div>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>

<style scoped>

    .body{
        width: 80%;
        margin: auto;
        display: flex;
        justify-content: space-between;
        margin-bottom: 40px;
    }

    .imgs{
        width: 70%;
        min-height: 500px;
        max-height: 1000px;
    }

    .imgs img{
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    .el-carousel__item h3 {
        color: #475669;
        font-size: 18px;
        opacity: 0.75;
        line-height: 300px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }

    .content{
        width: 29%;
    }
    .text{
        text-align: left;
        margin-bottom: 12px;
    }

    .giamoi{
        color: #75c321;
        padding: 4px 8px;
        font-weight: bold;
        font-size: 24px;
    }
    .giacu{
        color: gray;
        padding: 4px 8px;
        text-decoration: line-through;
    }
</style>

<script>
    import { store } from '../../store';
    import { RestFul } from '../../restful';
    import Header from '../common/Header.vue';
    import Footer from '../common/Footer.vue';
    export default {
        data: function(){
            return {
                tree:{
                    id:0,
                    name:"",
                    code:"",
                    description: "",
                    cost: 0,
                    discount: 0,
                    images:[],
                    treeType:{
                        name: ""
                    }
                },
                id: 0,
                numValue: 1
            }
        },
        methods: {
            getDetailTree: async function(){
                let path = this.$route.path;
                this.id = path.substring(path.lastIndexOf("/")+1);
                let res = await RestFul.get("/api/manage/tree/getByIdFree/"+this.id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.tree = res.data;
                    }
                }
            },
            getUrlOfResource: function(image){
                if((image || "").length > 0){
                    return store.baseUrl+image;
                }
                return "";
            },
            getNewCost: function(product){
                return Math.round(product.cost * (100 - product.discount) / 100);
            },
            getSaving: function(product){
                return Math.round(product.cost * (product.discount) / 100);
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
                }else{
                    carts.forEach(el => {
                        if(el.id == idTree){
                            el.count = this.numValue;
                        }
                    })
                }
                store.setCarts(carts);
                localStorage.setItem("carts", JSON.stringify(carts));
                this.$message.success("Đã thêm vào giỏ hàng");
            },
            getStringForMoney: function(money){
                return new Intl.NumberFormat('vn-VN').format(money) + "đ";
            }
        },

        computed: {

        },
        created: function(){
            this.getDetailTree();
        },
        components: {Header, Footer}
    }
</script>