<template>
    <div>
        <Header></Header>
        <div class="body">
            <div class="list">
                <div v-if="carts.length > 0">
                    <div style="text-align: left;margin-bottom: 20px;">Danh mục sản phẩm</div>
                    <div>
                        <div class="title">
                            <div style="width: 30%;"><b>Sản phẩm</b></div>
                            <div style="width: 15%;"><b>Giá gốc</b></div>
                            <div style="width: 10%;"><b>Chiết khấu</b></div>
                            <div style="width: 10%;"><b>Giá mới</b></div>
                            <div style="width: 20%;"><b>Số lượng</b></div>
                            <div style="width: 15%;"><b>Thành tiền</b></div>
                        </div>
                        <div v-for="item in carts" class="product">
                            <div style="width: 30%; display: flex; justify-content: flex-start; align-items: center;">
                                <el-button style="margin-right: 8px;" icon="el-icon-close" circle @click="deleteItem(item.treeId)"></el-button>
                                <img style="width: 75px;height: 90px;margin-right: 8px;" :src="getUrlOfResource(item.images)"/>
                                {{ item.treeName }}
                            </div>
                            <div style="width: 15%;">{{ getStringForMoney(item.amountUnit) }}</div>
                            <div style="width: 10%;">{{ item.discount }}%</div>
                            <div style="width: 10%;">{{ getStringForMoney(item.amountUnit2) }}</div>
                            <div style="width: 20%;">
                                <el-input-number v-model="item.count" :min="1" :max="item.totalCount"></el-input-number>
                            </div>
                            <div style="width: 15%;">{{ getStringForMoney(item.amount) }}</div>
                        </div>
                    </div>
                </div>
                <div v-else>Hiện không có sản phẩm nào!</div>
                <div>
                    <el-button @click="$router.push('/')">Trở về xem tiếp</el-button>
                    <el-button type="success" @click="saveCart()">Cập nhật giỏ hàng</el-button>
                </div>
            </div>
            <div class="cal">
                <div style="margin-bottom: 20px;">
                    Tổng: <b>{{ getStringForMoney(totalAmount) }}</b>
                </div>
                <el-button type="warning" @click="createBill">Đặt hàng</el-button>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>
<style scoped>
    .body{
        width: 80%;
        margin-left: auto;
        margin-right: auto;
        margin-top: 20px;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
    }

    .list{
        width: 80%;
    }
    .cal{
        width: 19%;
        text-align: left;
    }
    .title, .product{
        width: 100%;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        margin-bottom: 12px;
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
                carts: [],
                totalAmount: 0
            }
        },
        methods:{
            checkSession: async function(){
                let res = await RestFul.checkAuth("/api/auth/check",null, {type: 1})
                if(res != null){
                    if(res.statusCode == 200){
                        store.setUser(res.data);
                    }else{
                        store.setUser({});
                    }
                }
            },
            createBill: async function(){
                await this.checkSession();
                if(store.user.id != null && store.user.id != undefined){
                    this.$router.push('/createBill');
                }else{
                    this.$router.push("/login");
                }
            },
            getUrlOfResource: function(images){
                if((images || []).length > 0){
                    return store.baseUrl+images[0];
                }
                return "";
            },
            getCart: async function(){
                let strcart = localStorage.getItem("carts");
                let cs = null;
                this.carts = [];
                if(strcart == null){
                    cs = [];
                }else{
                    cs = JSON.parse(strcart);
                }
                if(cs.length > 0){
                    let treeIds = cs.map(el => el.id);
                    let res = await RestFul.get("/api/manage/tree/getByIdsFree?ids="+treeIds.toString());
                    if(res != null){
                        if(res.statusCode == 200){
                            let trees = res.data;
                            for(let i = 0;i<cs.length;i++){
                                for(let j = 0;j<trees.length;j++){
                                    if(cs[i].id == trees[j].id){
                                        this.carts.push({
                                            treeId: trees[j].id,
                                            count: cs[i].count,
                                            totalCount: trees[j].count,
                                            images: trees[j].images,
                                            treeName: trees[j].name,
                                            treeCode: trees[j].code,
                                            amountUnit: trees[j].cost,
                                            discount: trees[j].discount,
                                            amountUnit2: 0,
                                            amount: 0
                                        })
                                    }
                                }
                            }
                            this.saveCart();
                        }
                    }
                }
            },
            saveCart: function(){
                this.totalAmount = 0;
                let me = this;
                let cs = this.carts.map(el => {
                    el.amountUnit2 = Math.round(el.amountUnit * (100 - el.discount) / 100);
                    el.amount = el.amountUnit2 * el.count;
                    me.totalAmount += el.amount;
                    return {
                        id: el.treeId,
                        count: el.count
                    }
                });
                localStorage.setItem("carts", JSON.stringify(cs));
                store.setCarts(cs);
            },
            deleteItem: function(treeId){
                let index = -1;
                for(let i = 0; i<this.carts.length;i++){
                    if(this.carts[i].treeId == treeId){
                        index = i;
                        break;
                    }
                }
                if(index >= 0){
                    this.carts.splice(index, 1);
                }
            },
            getStringForMoney: function(money){
                return new Intl.NumberFormat('vn-VN').format(money) + "đ";
            }
        },
        computed:{
            
        },
        created: function(){
            this.getCart();
        },
        components: {Header, Footer}
    }
</script>