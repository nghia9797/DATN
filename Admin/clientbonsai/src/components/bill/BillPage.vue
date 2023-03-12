<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Hóa đơn/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddBill">Thêm</el-button>
                    <el-button icon="el-icon-close" :disabled="idSelecteds.length == 0" @click="openConfirmDeletes">Xóa</el-button>
                </div>
            </div>
            <div class="filter">
                <div class="item-filter">
                    <div class="item-filter-title">Mã hóa đơn</div>
                    <el-input v-model="params.code" placeholder="-- Tìm theo mã hóa đơn"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Trạng thái</div>
                    <el-select v-model="params.status" placeholder="-- Tìm theo trạng thái" style="width: 100%;">
                        <el-option key="all" label="Tất cả" :value="-1"></el-option>
                        <el-option key="init" label="Khởi tạo" :value="0"></el-option>
                        <el-option key="comfirm" label="Xác nhận" :value="1"></el-option>
                        <el-option key="trans" label="Vận chuyển" :value="2"></el-option>
                        <el-option key="complete" label="Hoàn thành" :value="3"></el-option>
                        <el-option key="cancel" label="Hủy" :value="4"></el-option>
                    </el-select>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Từ ngày</div>
                    <el-date-picker style="width: 100%;"
                        value-format="timestamp"
                        v-model="params.from"
                        type="date"
                        placeholder="-- Chọn ngày">
                    </el-date-picker>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Đến ngày</div>
                    <el-date-picker style="width: 100%;"
                        value-format="timestamp"
                        v-model="params.to"
                        type="date"
                        placeholder="-- Chọn ngày">
                    </el-date-picker>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Người nhận</div>
                    <el-input v-model="keySearchReceiver" @change="getCustomers" placeholder="-- Tìm theo người nhận"></el-input>
                </div>
                <div class="item-filter">
                    <el-select v-model="params.receiver" placeholder="-- Tìm theo người nhận" style="width: 100%;">
                        <el-option key="all" label="Tất cả" :value="-1"></el-option>
                        <el-option v-for="item in receivers" :key="item.id" :label="`${item.username} - Tên đầy đủ: ${item.fullname}`" :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Người gửi</div>
                    <el-input v-model="keySearchSender" placeholder="-- Tìm theo người gửi" @change="getEmployees"></el-input>
                </div>
                <div class="item-filter">
                    <el-select v-model="params.sender" placeholder="-- Tìm theo người gửi" style="width: 100%;">
                        <el-option key="all" label="Tất cả" :value="-1"></el-option>
                        <el-option v-for="item in senders" :key="item.id" :label="`${item.code} - Tên đầy đủ: ${item.name}`" :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Loại giao hàng</div>
                    <el-select v-model="params.typePay" placeholder="-- Tìm theo trạng thái" style="width: 100%;">
                        <el-option key="all" label="Tất cả" :value="-1"></el-option>
                        <el-option key="self" label="Nhận hàng tại cửa hàng" :value="0"></el-option>
                        <el-option key="ship" label="Giao hàng tận nơi" :value="1"></el-option>
                    </el-select>
                </div>
                <div class="item-filter">
                    <el-button icon="el-icon-search" circle @click="searchBill"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listBill"
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
                    @sort-change="handleSortChange"
                >
                    <el-table-column
                        type="selection"
                        width="55">
                    </el-table-column>
                    <el-table-column
                        prop="code"
                        label="Mã hóa đơn"
                        sortable
                        width="180">
                        <template slot-scope="scope">{{ scope.row.code }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="receiver"
                        label="Tên khách hàng"
                        sortable
                        width="200">
                        <template slot-scope="scope">{{ scope.row.receiverName }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="sender"
                        label="Nhân viên giao hàng"
                        sortable
                        width="200">
                        <template slot-scope="scope">{{ scope.row.senderName }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="typePay"
                        label="Loại vận chuyển"
                        width="250">
                        <template slot-scope="scope">{{ getStringTypePay(scope.row.typePay) }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="amount"
                        align="right"
                        label="Giá trị"
                        sortable
                        width="180">
                        <template slot-scope="scope">{{ getStringForMoney(scope.row.amount) }}</template>
                    </el-table-column>
                    <el-table-column
                        label="Địa chỉ"
                        width="300">
                        <template slot-scope="scope">{{ scope.row.address }}</template>
                    </el-table-column>
                    <el-table-column
                        label="Trạng thái"
                        width="150">
                        <template slot-scope="scope">{{ getNameStatus(scope.row.status) }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="created"
                        label="Ngày tạo"
                        sortable
                        width="150">
                        <template slot-scope="scope">{{ getStringOfDate(scope.row.created) }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="created"
                        label="Ngày cập nhật gần nhất"
                        sortable
                        width="240">
                        <template slot-scope="scope">{{ getStringOfDate(scope.row.updated) }}</template>
                    </el-table-column>
                    <el-table-column
                        label=""
                        width="120">
                        <template slot-scope="scope">
                            <el-button size="mini" type="primary" icon="el-icon-edit" circle @click="openUpdate(scope.row.id)"></el-button>
                            <el-button size="mini" type="danger" icon="el-icon-delete" circle @click="openConfirmDelete(scope.row.id)"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination style="margin-top: 12px;"
                    background
                    :current-page="params.page + 1"
                    :page-size="params.limit"
                    :page-sizes="[5,10,15,20]"
                    layout="prev, pager, next, sizes"
                    :total="total"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
                </el-pagination>
            </div>
            <el-drawer
                size="1200px"
                title="Hóa đơn"
                :visible.sync="isOpenModal"
                direction="rtl"
                :before-close="handleCloseModal">
                <div style="padding: 20px; width: calc(100% - 40px);">
                    <el-form :model="bill" ref="billDetail" label-width="150px" class="">
                        <div v-if="typeModal == 1" style="display: flex;justify-content: space-between;align-items: center;">
                            <el-form-item style="width: 49%;"
                                prop="code"
                                label="Mã hóa đơn"
                            >
                                <el-input v-model="bill.code" disabled></el-input>
                            </el-form-item>
                            <el-form-item v-if="typeModal == 1" style="width: 49%;"
                                prop="status"
                                label="Trạng thái"
                            >
                            <el-select v-model="bill.status" placeholder="-- Chọn trạng thái" style="width: 100%;" :disabled="isBillNotChange">
                                <el-option key="init" label="Khởi tạo" :value="0"></el-option>
                                <el-option key="comfirm" label="Xác nhận" :value="1"></el-option>
                                <el-option key="trans" label="Vận chuyển" :value="2"></el-option>
                                <el-option key="complete" label="Hoàn thành" :value="3"></el-option>
                                <el-option key="cancel" label="Hủy" :value="4"></el-option>
                            </el-select>
                            </el-form-item>
                        </div>
                        <el-form-item
                            v-if="typeModal==0"
                            prop="receiver"
                            label="Người nhận"
                            :rules="[
                                { required: true, message: 'Người nhận không được để trống', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <div style="display: flex;justify-content: space-between;align-items: center;">
                                <el-input v-model="keySearchReceiver2" @change="getCustomers2" style="width: 49%;" placeholder="-- Tìm theo tên"></el-input>
                                <el-select v-model="bill.receiver" placeholder="-- Chọn người nhận" style="width: 49%;" :disabled="typeModal==1" @change="changeReceiver">
                                    <el-option v-for="item in receivers2" :key="item.id" :label="`${item.username} - Tên đầy đủ: ${item.fullname}`" :value="item.id"></el-option>
                                </el-select>
                            </div>
                        </el-form-item>
                        <el-form-item
                            v-else
                            prop="receiverName"
                            label="Người nhận"
                        >
                            <el-input v-model="bill.receiverName" style="width: 100%;" disabled></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="phone"
                            label="Loại giao hàng"
                        >
                            <el-select v-model="bill.typePay" placeholder="-- Tìm theo trạng thái" style="width: 100%;" :disabled="typeModal==1">
                                <el-option key="self" label="Nhận hàng tại cửa hàng" :value="0"></el-option>
                                <el-option key="ship" label="Giao hàng tận nơi" :value="1"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item
                            v-if="bill.status==2 && bill.typePay == 1"
                            prop="sender"
                            label="Người gửi"
                            :rules="[
                                { required: true, message: 'Người gửi không được để trống', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <div style="display: flex;justify-content: space-between;align-items: center;">
                                <el-input v-model="keySearchSender2" @change="getEmployees2" style="width: 49%;" placeholder="-- Tìm theo tên"></el-input>
                                <el-select v-model="bill.sender" placeholder="-- Chọn nhân viên giao hàng" style="width: 49%;" @change="changeSender">
                                    <el-option v-for="item in senders2" :key="item.id" :label="`${item.code} - Tên đầy đủ: ${item.name}`" :value="item.id"></el-option>
                                </el-select>
                            </div>
                        </el-form-item>
                        <el-form-item
                            v-if="bill.status!=2 && bill.typePay == 1"
                            prop="senderName"
                            label="Người gửi"
                        >
                            <el-input v-model="bill.senderName" style="width: 100%;" disabled></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="address"
                            label="Địa chỉ"
                            :rules="[
                                { required: true, message: 'Địa chỉ không được để trống', trigger: ['blur', 'change'] },
                                { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input type="textarea" v-model="bill.address" :disabled="typeModal==1"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="note"
                            label="Ghi chú"
                            :rules="[
                                { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input type="textarea" v-model="bill.note" :disabled="typeModal==1"></el-input>
                        </el-form-item>
                        <el-form-item label="Tổng">
                            <span><b>{{ getStringForMoney(bill.amount) }}</b></span>
                        </el-form-item>
                        <el-form-item label="Danh sách cây" style="text-align: left;">
                            <el-button type="warning" v-if="typeModal == 0 && isOpenAddTree == false" @click="openAddTree">Thêm cây</el-button>
                        </el-form-item>
                        <el-table v-if="bill.billDetail.length > 0"
                            :data="bill.billDetail"
                            style="width: 100%" max-height="300px"
                        >
                            <el-table-column
                                prop="treeCode"
                                label="Mã cây"
                                width="180">
                                <template slot-scope="scope">{{ scope.row.treeCode }}</template>
                            </el-table-column>
                            <el-table-column
                                prop="treeName"
                                label="Tên cây"
                                width="200">
                                <template slot-scope="scope">{{ scope.row.treeName }}</template>
                            </el-table-column>
                            <el-table-column
                                prop="amountUnit"
                                label="Giá gốc"
                                align="right"
                                width="180">
                                <template slot-scope="scope">{{ getStringForMoney(scope.row.amountUnit) }}</template>
                            </el-table-column>
                            <el-table-column
                                prop="discount"
                                label="Chiết khấu"
                                align="right"
                                width="100">
                                <template slot-scope="scope">{{ getStringForNumber(scope.row.discount) }}</template>
                            </el-table-column>
                            <el-table-column
                                prop="count"
                                label="Số lượng"
                                align="right"
                                width="180">
                                <template slot-scope="scope">{{ getStringForNumber(scope.row.count) }}</template>
                            </el-table-column>
                            <el-table-column
                                prop="amount"
                                align="right"
                                label="Giá trị"
                                width="180">
                                <template slot-scope="scope">{{ getStringForMoney(scope.row.amount) }}</template>
                            </el-table-column>
                            <el-table-column
                                v-if="typeModal == 0"
                                label=""
                                width="120">
                                <template slot-scope="scope">
                                    <el-button size="mini" type="primary" icon="el-icon-edit" circle @click="updateTree(scope.row)"></el-button>
                                    <el-button size="mini" type="danger" icon="el-icon-delete" circle @click="deleteTree(scope.row.treeId)"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div style="display: flex;justify-content: space-between;align-items: center;" v-if="isOpenAddTree == true">
                            <el-input v-if="typeOpenTree == 0" v-model="keySearchTree" @change="getTrees" style="width: 24%;" placeholder="-- Tìm theo mã"></el-input>
                            <el-input v-if="typeOpenTree == 0" v-model="keySearchTreeName" @change="getTrees" style="width: 24%;" placeholder="-- Tìm theo tên"></el-input>
                            <el-select v-if="typeOpenTree == 0" v-model="billDetail.treeId" placeholder="-- Chọn cây" style="width: 24%;" @change="changeTree">
                                <el-option v-for="item in trees" :key="item.id" :label="`${item.code} - Tên cây: ${item.name}`" :value="item.id"></el-option>
                            </el-select>
                            <div v-if="typeOpenTree==1">{{ `Mã cây ${billDetail.treeCode} - Tên cây: ${billDetail.treeName}` }}</div>
                            <el-input-number v-model="billDetail.count" :min="1" :max="billDetail.maxCount"></el-input-number>
                            <el-button type="primary" @click="addTree">Thêm</el-button>
                        </div>
                        <el-form-item style="margin-top: 20px;" v-if="bill.statusOld<3">
                            <el-button type="primary" @click="submitForm('billDetail')">Lưu hóa đơn</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-drawer>
        </div>
    </div>
</template>
<style scoped>
    .filter{
        margin-top: 12px;
        padding: 10px;
        border: 1px solid lightgray;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        flex-wrap: wrap;
    }
    .item-filter{
        width: 24%;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        margin-bottom: 12px;
        margin-right: 8px;
    }

    .item-filter-title{
        min-width: 120px;
        text-align: left;
    }

    .content{
        padding: 10px;
        margin-top: 12px;
        border: 1px solid lightgray ;
    }
</style>
<script>
    import { RestFul } from '../../restful';

    export default {
        data: function(){
            return {
                params: {
                    typePay: -1,
                    code: "",
                    receiver: -1,
                    sender:-1,
                    from:new Date().getTime(),
                    to: new Date().getTime(),
                    page: 0,
                    limit: 10,
                    fieldSort: "created",
                    typeSort: "desc",
                    status: -1
                },

                listBill: [],
                total: 0,
                idSelecteds: [],

                billDefault:{
                    id: 0,
                    created: 0,
                    updated: 0,
                    code: "",
                    status: 0,
                    sender: null,
                    receiver: null,
                    address: "",
                    note: "",
                    typePay: 0,
                    feeShip: 0,
                    senderName: null,
                    receiverName: "",
                    amount: 0,
                    billDetail: [],
                    statusOld: 0
                },
                bill:{
                    id: 0,
                    created: 0,
                    updated: 0,
                    code: "",
                    status: 0,
                    sender: null,
                    receiver: null,
                    address: "",
                    note: "",
                    typePay: 0,
                    feeShip: 0,
                    senderName: null,
                    receiverName: "",
                    amount: 0,
                    billDetail: [],
                    statusOld: 0
                },
                billDetailDefault:{
                    id: 0,
                    created: null,
                    updated: null,
                    billId: 0,
                    treeId: null,
                    count: 0,
                    amountUnit: 0,
                    amount: 0,
                    discount: 0,
                    treeName: "",
                    treeCode: ""
                },
                billDetail:{
                    id: 0,
                    created: null,
                    updated: null,
                    billId: 0,
                    treeId: null,
                    count: 0,
                    amountUnit: 0,
                    amount: 0,
                    discount: 0,
                    treeName: "",
                    treeCode: "",
                    treeType: 0
                },
                isOpenModal: false,
                typeModal: 0,
                keySearchReceiver:"",
                keySearchSender:"",
                keySearchReceiver2:"",
                keySearchSender2:"",
                receivers: [],
                senders:[],
                receivers2: [],
                senders2:[],
                trees:[],
                keySearchTree: "",
                keySearchTreeName:"",
                isOpenAddTree: false,
                typeOpenTree: 0,
                isBillNotChange: false
            }
        },

        methods:{
            searchBill: async function(){
                let a = this.params.from;
                let b = this.params.to;
                this.params.from = this.params.from * 1 || 0;
                this.params.to = this.params.to * 1 || 0;
                let res = await RestFul.getWithToken("/api/manage/bill/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listBill = res.data.data;
                        this.total = res.data.count;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
                this.params.from = a;
                this.params.to = b;
            },
            handleSelectionChange: function(selected){
                this.idSelecteds = selected.map(el => el.id);
            },
            openUpdate: async function(id){
                this.keySearchReceiver2 = "";
                this.keySearchSender2 = "";
                await this.getCustomers2();
                await this.getEmployees2();
                await this.getBillById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/bill/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchBill();
                    }else {
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$router.push("/login");
                        }
                    }
                }else{
                    this.$message.error("Có lỗi xảy ra");
                }
            },
            deletes: async function(){
                let res = await RestFul.delWithToken("/api/manage/bill/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchBill();
                    }else {
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$router.push("/login");
                        }
                    }
                }else{
                    this.$message.error("Có lỗi xảy ra");
                }
            },
            openConfirmDelete: function(id){
                this.$confirm.confirm("Bạn có muốn xóa hóa đơn này?","Xóa hóa đơn",{
                    confirmButtonText: 'OK',
                    cancelButtonText: 'Hủy',
                    type: 'warning'
                }).then(() => {
                    this.deleteById(id);
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Delete canceled'
                    });          
                });
            },
            openConfirmDeletes: function(){
                this.$confirm.confirm("Bạn có muốn xóa những hóa đơn này?","Xóa hóa đơn",{
                    confirmButtonText: 'OK',
                    cancelButtonText: 'Hủy',
                    type: 'warning'
                }).then(() => {
                    this.deletes();
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Delete canceled'
                    });          
                });
            },
            handleSortChange: function(column, prop, order){
                // console.log("column:",column, " prop: ", prop, "order: ", order);
                this.params.fieldSort = column.prop;
                if(column.order == "ascending"){
                    this.params.typeSort = "asc";
                }else{
                    this.params.typeSort = "desc";
                }
                this.searchBill();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchBill();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchBill();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Khởi tạo";
                }else if(status == 1){
                    return "Xác nhận";
                }else if(status == 2){
                    return "Vận chuyển";
                }else if(status == 3){
                    return "Hoàn thành";
                }else if(status == 4){
                    return "Hủy";
                }
                return "";
            },
            getStringTypePay: function(typepay){
                if(typepay == 0){
                    return "Nhận hàng tại cửa hàng";
                }else return "Giao hàng tận nơi";
            },
            getBillById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/bill/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.bill = res.data;
                        this.bill.statusOld = this.bill.status;
                        if(this.bill.status >=3){
                            this.isBillNotChange = true;
                        }else{
                            this.isBillNotChange = false;
                        }
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addBill: async function(){
                let res = await RestFul.postWithToken("/api/manage/bill/create", this.bill);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.bill = res.data;
                        this.typeModal = 1;
                    }else {
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$router.push("/login");
                        }
                    }
                }else{
                    this.$message.error("Có lỗi xảy ra");
                }
            },
            updateBill: async function(){
                let res = await RestFul.putWithToken("/api/manage/bill/update", this.bill);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.bill = res.data;
                    }else{
                        this.$message.error(res.message);
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$router.push("/login");
                        }
                    }
                }else{
                    this.$message.error("Có lỗi xảy ra");
                }
            },
            handleCloseModal: function(){
                this.searchBill();
                this.isOpenModal = false;
                this.isOpenAddTree = false;
            },
            openAddBill: async function(){
                this.isBillNotChange = false;
                this.keySearchReceiver2 = "";
                this.keySearchSender2 = "";
                await this.getCustomers2();
                await this.getEmployees2();
                this.bill = JSON.parse(JSON.stringify(this.billDefault));
                this.isOpenModal = true;
                this.$refs['billDetail'].resetFields();
                this.typeModal = 0;
            },
            submitForm(formName) {
                if(this.bill.billDetail.length == 0) return;
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addBill();
                        }else{
                            me.updateBill();
                        }
                    }
                });
            },
            openAddTree: async function(){
                this.keySearchTree = "";
                this.keySearchTreeName = "";
                this.billDetail = JSON.parse(JSON.stringify(this.billDetailDefault));
                await this.getTrees();
                this.isOpenAddTree = true;
                this.typeOpenTree = 0;
            },
            getTrees: async function(){
                let res = await RestFul.getWithToken("/api/manage/tree/search", null, {name: this.keySearchTreeName,code:this.keySearchTree, typeSort:"asc", fieldSort:"name",count:1});
                if(res != null){
                    if(res.statusCode == 200){
                        this.trees = res.data.data;
                        this.billDetail.treeId = null;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
            },
            getCustomers: async function(){
                let res = await RestFul.getWithToken("/api/manage/user/search", null, {fullname:this.keySearchReceiver, typeSort:"asc", fieldSort:"fullname"});
                if(res != null){
                    if(res.statusCode == 200){
                        this.receivers = res.data.data;
                        this.params.sender = -1;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
            },
            getEmployees: async function(){
                let res = await RestFul.getWithToken("/api/manage/shipper/search", null, {name:this.keySearchSender, typeSort:"asc", fieldSort:"name"});
                if(res != null){
                    if(res.statusCode == 200){
                        this.senders = res.data.data;
                        this.params.sender = -1;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
            },
            getCustomers2: async function(){
                let res = await RestFul.getWithToken("/api/manage/user/search", null, {fullname:this.keySearchReceiver2, typeSort:"asc", fieldSort:"fullname"});
                if(res != null){
                    if(res.statusCode == 200){
                        this.receivers2 = res.data.data;
                        this.bill.receiver = null;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
            },
            getEmployees2: async function(){
                let res = await RestFul.getWithToken("/api/manage/shipper/search", null, {name:this.keySearchSender2, typeSort:"asc", fieldSort:"name",status:0});
                if(res != null){
                    if(res.statusCode == 200){
                        this.senders2 = res.data.data;
                        this.bill.sender = null;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
            },
            getStringOfDate: function(timestamp){
                let date = new Date(timestamp);
                let ngay = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
                let month = (date.getMonth() + 1) < 10 ? `0${date.getMonth() + 1}` : date.getMonth();
                let hour = (date.getHours() + 1) < 10 ? `0${date.getHours()}` : date.getHours();
                let min = (date.getMinutes() + 1) < 10 ? `0${date.getMinutes()}` : date.getMinutes();
                let second = (date.getSeconds() + 1) < 10 ? `0${date.getSeconds()}` : date.getSeconds();
                let strDate = `${ngay}/${month}/${date.getFullYear()} ${hour}:${min}:${second}`;
                // let strDate = `${ngay}/${month}/${date.getFullYear()}`;
                return strDate;
            },
            getStringForMoney: function(number){
                return new Intl.NumberFormat('vn-VN').format(number)+"đ";
            },
            getStringForNumber: function(number){
                return new Intl.NumberFormat('vn-VN').format(number);
            },
            changeTree: function(){
                this.trees.forEach(el => {
                    if(el.id == this.billDetail.treeId){
                        this.billDetail.amountUnit = el.cost;
                        this.billDetail.treeCode = el.code;
                        this.billDetail.treeName = el.name;
                        this.billDetail.discount = el.discount;
                        this.billDetail.maxCount = el.count;
                        this.billDetail.treeType = el.typeId;
                    }
                });
            },
            addTree: function(){
                this.bill.billDetail = this.bill.billDetail || [];
                if(this.typeOpenTree == 0){
                    let flag = false;
                    for(let i = 0;i<this.bill.billDetail.length;i++){
                        let el = this.bill.billDetail[i];
                        if(el.treeId == this.billDetail.treeId){
                            el.count += this.billDetail.count;
                            flag = true;
                        }
                    }
                    if(flag == false){
                        this.bill.billDetail.push(this.billDetail);
                    }
                }else{
                    for(let i = 0;i<this.bill.billDetail.length;i++){
                        let el = this.bill.billDetail[i];
                        if(el.treeId == this.billDetail.treeId){
                            el.count = this.billDetail.count;
                        }
                    }
                }
                this.isOpenAddTree = false;
                this.updateForBillDetail();
            },
            updateTree: function(tree){
                this.billDetail = JSON.parse(JSON.stringify(tree));
                this.isOpenAddTree = true;
                this.typeOpenTree = 1;
            },
            deleteTree:function(treeId){
                let index = -1;
                for(let i = 0;i<this.bill.billDetail.length;i++){
                    if(this.bill.billDetail[i].treeId == treeId){
                        index = i;
                    }
                }
                if(index >= 0){
                    this.bill.billDetail.splice(index, 1);
                }
                this.updateForBillDetail();
            },
            updateForBillDetail: function(){
                let totalAmount = 0;
                this.bill.billDetail.forEach(el => {
                    el.amountUnit2 = Math.round(el.amountUnit * (100 - el.discount) /100);
                    el.amount = el.amountUnit2 * el.count;
                    totalAmount += el.amount;
                });
                this.bill.amount = totalAmount;
            },
            changeReceiver:function(){
                for(let i = 0;i<this.receivers2.length;i++){
                    if(this.receivers2[i].id == this.bill.receiver){
                        this.bill.receiverName = this.receivers2[i].fullname;
                        break;
                    }
                }
            },
            changeSender: function(){
                for(let i = 0;i<this.senders2.length;i++){
                    if(this.senders2[i].id == this.bill.sender){
                        this.bill.senderName = this.senders2[i].name;
                        break;
                    }
                }
            }
        },

        created: function(){
            this.searchBill();
            this.getCustomers();
            this.getEmployees();
        }
    }

</script>