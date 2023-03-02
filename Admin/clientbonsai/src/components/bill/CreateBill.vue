<template>
    <div>
        <Header></Header>
        <el-form :model="bill" ref="createBill" label-width="150px" class="" style="width: 100%;">
            <div class="box">
                <div class="contact">
                    <div class="title">Thông tin liên lạc khách hàng</div>
                    <el-form-item
                        prop="receiverName"
                        label="Người nhận"
                        :rules="[
                            { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                            { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="bill.receiverName" disabled></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="phone"
                        label="Số điện thoại"
                        :rules="[
                            { required: true, message: 'Số điện thoại không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^(0)+[1-9]+[0-9]{8}$', message: 'Định dạng số điện thoại không đúng', trigger: ['blur', 'change'] }
                        ]"
                    >
                        <el-input v-model="bill.phone" disabled></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="email"
                        label="Email"
                        :rules="[
                            { required: true, message: 'Email không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^[a-zA-Z0-9]+([\._]?[a-zA-Z0-9]+)*@[a-z]+(\.[a-z]+)*$', message: 'Email không đúng định dạng', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="bill.email" disabled></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="address"
                        label="Địa chỉ"
                        :rules="[
                            { required: true, message: 'Địa chỉ không được để trống', trigger: ['blur', 'change'] },
                            { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="bill.address"></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="note"
                        label="Ghi chú đơn hàng"
                        :rules="[
                            { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input type="textarea" v-model="bill.note"></el-input>
                    </el-form-item>
                </div>
                <div class="bill">
                    <div class="title">Thông tin đơn hàng</div>
                    <div class="row-title">
                        <div class="proName">Sản phẩm</div>
                        <div class="amount">Tạm tính</div>
                    </div>
                    <div v-for="item in bill.billDetail" class="row-item">
                        <div class="proName">{{ item.treeName }} <b>x{{ item.count }}</b></div>
                        <div class="amount">{{ getStringForMoney(item.amount) }}</div>
                    </div>
                    <div class="row-item">
                        <div>Giao hàng</div>
                        <div>
                            <el-form-item label="">
                                <el-radio-group v-model="bill.typePay" @change="typePayChange()">
                                    <div style="margin-bottom: 12px;"><el-radio :label="0">Nhận tại cửa hàng (<b>Miễn phí</b>)</el-radio></div>
                                    <div><el-radio :label="1">Giao hàng tận nơi (<b>{{ getStringForMoney(100000) }}</b>)</el-radio></div>
                                </el-radio-group>
                            </el-form-item>
                        </div>
                    </div>
                    <div class="row-item">
                        <div class="proName"><b>Tổng</b></div>
                        <div class="amount">{{ getStringForMoney(bill.amount) }}</div>
                    </div>
                    <div class="title">Trả tiền mặt khi nhận hàng</div>
                    <div style="margin-left: -145px;display: flex;justify-content: flex-start;">
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('createBill')">Đặt hàng</el-button>
                        </el-form-item>
                    </div>
                </div>
            </div>
        </el-form>
        <Footer></Footer>
    </div>
</template>

<style scoped>
    .box{
        width: 80%;
        margin-left: auto;
        margin-right: auto;
        margin-top: 20px;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
    }

    .contact{
        padding-top: 20px;
        width: 60%;
    }

    .bill{
        width: calc(39% - 42px);
        padding: 20px;
        border: 3px solid #75c321;
    }

    .title{
        font-size: 16px;
        font-weight: bold;
        text-align: left;
        margin-bottom: 20px;
    }

    .row-title{
        font-weight: bold;
        display: flex;
        justify-content: space-between;
        padding-bottom: 10px;
        border-bottom: 2px solid black;
    }

    .row-item{
        display: flex;
        justify-content: space-between;
        padding-bottom: 10px;
        padding-top: 7px;
        padding-bottom: 7px;
        border-bottom: 1px solid gray;
        margin-bottom: 10px;
    }
    .amount{
        font-weight: bold;
    }
</style>

<script>
    import { store } from '../../store';
    import { RestFul } from '../../restful';
    import Header from '../common/Header.vue';
    import Footer from '../common/Footer.vue';

    export default {
        components: {Header, Footer},

        data: function(){
            return {
                bill: {
                    code:"",
                    status: 0,
                    receiver: 0,
                    address: "",
                    note: "",
                    typePay: 0,
                    feeShip: 100000,
                    receiverName: "",
                    phone:"",
                    email:"",
                    billDetail:[],
                    amount:0
                },
                user:{
                    id:0,
                    username:"",
                    fullname:"",
                    phone:"",
                    email:"",
                    address: ""
                },
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
                        this.$router.push("/login");
                    }
                }
            },
            createBill: async function(){
                await this.checkSession();
                if(store.user.id != null && store.user.id != undefined){
                    
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
                this.bill.billDetail = [];
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
                                        this.bill.billDetail.push({
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
                }else{
                    this.$router.push("/");
                }
            },
            saveCart: function(){
                this.bill.amount = 0;
                let me = this;
                let cs = this.bill.billDetail.map(el => {
                    el.amountUnit2 = Math.round(el.amountUnit * (100 - el.discount) / 100);
                    el.amount = el.amountUnit2 * el.count;
                    me.bill.amount += el.amount;
                    return {
                        id: el.treeId,
                        count: el.count
                    }
                });
            },
            getUserDetail: async function(){
                await this.checkSession();
                let res = await RestFul.getWithToken("/api/manage/user/getByIdForUser/"+store.user.id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.user = res.data;
                        this.bill.receiver = this.user.id;
                        this.bill.receiverName = this.user.fullname;
                        this.bill.address = this.user.address;
                        this.bill.phone = this.user.phone;
                        this.bill.email = this.user.email;
                        return;
                    }else{
                        this.$message.error(res.message);
                    }
                }
                store.setUser({});
                this.$router.push("/");
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        let res = await RestFul.postWithToken("/api/manage/bill/createBillForUser", me.bill);
                        if(res != null){
                            if(res.statusCode == 200){
                                me.$message.success(res.message);
                                localStorage.setItem("carts",JSON.stringify([]));
                                store.setCarts([]);
                                me.$router.push("/listBill?billId="+res.data.id);
                            }else{
                                me.$message.error(res.message);
                                if(res.statusCode == 402 || res.statusCode == 401){
                                    me.$router.push("/login");
                                }
                            }
                        }
                    }
                });
            },
            typePayChange: function(value){
                this.saveCart();
                if(this.bill.typePay == 1){
                    this.bill.amount += 100000;
                }
            },
            getStringForMoney: function(money){
                return new Intl.NumberFormat('vn-VN').format(money) + "đ";
            }
        },

        computed:{
            
        },
        created: async function(){
            await this.checkSession();
            await this.getUserDetail();
            await this.getCart();
        },
    }
</script>