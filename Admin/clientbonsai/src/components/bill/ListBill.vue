<template>
    <div>
        <Header></Header>
        <div class="box" style="justify-content: flex-start; align-items: center;">
            <div style="margin-right: 20px;">Danh mục hóa đơn</div>
            <div>
                <el-select v-model="billId" placeholder="Select" @change="getBillById">
                    <el-option
                    v-for="item in bills"
                    :key="item.id"
                    :label="item.code"
                    :value="item.id">
                    </el-option>
                </el-select>
            </div>
        </div>
        <div v-if="billId == null"></div>
        <div v-else class="box">
            <div class="bill">
                <div class="title">Chi tiết đơn hàng</div>
                <div class="row-title">
                    <div>Sản phẩm</div>
                    <div>Tổng</div>
                </div>
                <div class="row-item" v-for="item in bill.billDetail">
                    <div><span style="color: #75c321;">{{ item.treeName }}</span> <b>x{{ item.count }}</b></div>
                    <div><b>{{ getStringForMoney(item.amount) }}</b></div>
                </div>
                <div class="row-item" >
                    <div><b>Giao nhận hàng</b></div>
                    <div>{{ getStringTypePay(bill.typePay, getStringForMoney(bill.feeShip)) }}</div>
                </div>
                <div class="row-item" >
                    <div><b>Tổng cộng</b></div>
                    <div><b>{{ getStringForMoney(bill.amount) }}</b></div>
                </div>
                <div class="row-item">
                    <div><b>Địa chỉ giao</b></div>
                    <div style="max-width: 500px;">{{ bill.address }}</div>
                </div>
                <div class="row-item" >
                    <div><b>Lưu ý</b></div>
                    <div style="max-width: 500px;">{{ bill.note }}</div>
                </div>
            </div>
            <div class="contact">
                <div class="box-bill">
                    <div class="title2">{{ getStringStatus(bill.status) }}</div>
                    <div class="text-info">Mã đơn hàng: <b>{{ bill.code }}</b></div>
                    <div class="text-info">Ngày: <b>{{ getStringDateByLong(bill.created) }}</b></div>
                    <div class="text-info">Tổng cộng: <b>{{ getStringForMoney(bill.amount) }}</b></div>
                    <div class="text-info">Phương thức thanh toán: <b>Trả tiền mặt khi nhận hàng</b></div>
                </div>

                <div class="box-user">
                    <div class="title">Thông tin liên hệ khách hàng</div>
                    <div class="text-info">Họ tên: <b>{{ user.fullname }}</b></div>
                    <div class="text-info">Số điện thoại: <b>{{ user.phone }}</b></div>
                    <div class="text-info">Email: <b>{{ user.email }}</b></div>
                    <div class="text-info">Địa chỉ: <b>{{ user.address }}</b></div>
                </div>

                <div v-if="bill.status == 0" style="margin-bottom: 20px;text-align: center;">
                    <el-button type="success" @click="confirmDelete(billId)">Hủy</el-button>
                </div>
            </div>
        </div>
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
        width: 39%;
        text-align: left;
    }

    .bill{
        width: calc(60% - 42px);
        padding: 20px;
        border: 3px solid #75c321;
    }

    .title, .title2{
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

    .title2{
        padding: 8px 12px;
        background-color: #75c321;
        color: white;
        width: fit-content;
    }

    .text-info{
        margin-bottom: 12px;
    }

    .box-bill, .box-user{
        padding: 20px;
        width: calc(100% - 42px);
        background-color: rgb(217, 229, 233);
        margin-bottom: 20px;
        border: 1px solid transparent;
        border-radius: 5px;
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
                    amount:0,
                    created: 0
                },
                user:{
                    id:0,
                    username:"",
                    fullname:"",
                    phone:"",
                    email:"",
                    address: ""
                },
                bills:[],
                totalAmount: 0,
                billId: null
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
            getUrlOfResource: function(images){
                if((images || []).length > 0){
                    return store.baseUrl+images[0];
                }
                return "";
            },
            getUserDetail: async function(){
                await this.checkSession();
                let res = await RestFul.getWithToken("/api/manage/user/getByIdForUser/"+store.user.id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.user = res.data;
                        return;
                    }else{
                        this.$message.error(res.message);
                    }
                }
                store.setUser({});
                this.$router.push("/");
            },

            getBillById: async function(){
                if(this.billId == null) return;
                let res = await RestFul.getWithToken("/api/manage/bill/getByIdForUser/"+this.billId);
                if(res != null){
                    if(res.statusCode == 200){
                        this.bill = res.data;
                        this.billId = this.bill.id;
                        return;
                    }else{
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            store.setUser({});
                            this.$router.push("/");
                        }
                    }
                }
            },

            getBillForUser: async function(){
                let res = await RestFul.getWithToken("/api/manage/bill/getByUser/"+this.user.id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.bills = res.data;
                        return;
                    }else{
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            store.setUser({});
                            this.$router.push("/");
                        }
                    }
                }
            },
            updateStatusBill: async function(billId){
                let res = await RestFul.putWithToken("/api/manage/bill/updateForUser",{id:billId, status: 4});
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.getBillById();
                    }else{
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$router.push('/login');
                        }
                    }
                }else this.$message.error("Error!");
            },
            confirmDelete: function(billId){
                let me = this;
                this.$confirm.confirm('Bạn có muốn hủy đơn này?', 'Xác nhận hủy đơn', {
                    confirmButtonText: 'Ok',
                    cancelButtonText: 'Hủy',
                    type: 'warning'
                }).then(() => {
                    me.updateStatusBill(billId);
                }).catch(() => {
                    return;    
                });
            },
            getStringDateByLong: function(value){
                let date = new Date(value);
                let ngay = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
                let month = (date.getMonth() + 1) < 10 ? `0${date.getMonth() + 1}` : date.getMonth();
                let hour = (date.getHours() + 1) < 10 ? `0${date.getHours()}` : date.getHours();
                let min = (date.getMinutes() + 1) < 10 ? `0${date.getMinutes()}` : date.getMinutes();
                let second = (date.getSeconds() + 1) < 10 ? `0${date.getSeconds()}` : date.getSeconds();
                let strDate = `${ngay}/${month}/${date.getFullYear()} ${hour}:${min}:${second}`;
                return strDate;
            },
            getStringStatus: function(status){
                if(status == 0){
                    return "Đơn hàng của bạn đặt thành công";
                } else if(status == 1){
                    return "Đơn hàng của bạn đã được xác nhận"
                }else if(status == 2){
                    return "Đơn hàng của bạn đã được vận chuyển"
                }else if(status == 3){
                    return "Đơn hàng của bạn đã hoàn thành";
                }else if(status == 4){
                    return "Đơn hàng của bạn đã hủy";
                }else return "";
            },
            getStringTypePay: function(type, feeShip){
                if(type == 0){
                    return "Nhận tại cửa hàng (Miễn phí)"
                }else{
                    return `Giao hàng tận nơi ${feeShip}`;
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
            await this.getBillForUser();
            let params = this.$route.query;
            if(params.billId != null && params.billId != undefined){
                this.billId = params.billId;
            }
            await this.getBillById();
        },
    }
</script>