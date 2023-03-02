<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Khách hàng/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddCustomer">Thêm</el-button>
                    <el-button icon="el-icon-close" :disabled="idSelecteds.length == 0" @click="openConfirmDeletes">Xóa</el-button>
                </div>
            </div>
            <div class="filter">
                <div class="item-filter">
                    <div class="item-filter-title">Tên đăng nhập</div>
                    <el-input v-model="params.username" placeholder="-- Tìm theo tên đăng nhập"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Tên đầy đủ</div>
                    <el-input v-model="params.fullname" placeholder="-- Tìm theo tên đầy đủ"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Số điện thoại</div>
                    <el-input v-model="params.phone" placeholder="-- Tìm theo số điện thoại"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Email</div>
                    <el-input v-model="params.email" placeholder="-- Tìm theo email"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Trạng thái</div>
                    <el-select v-model="params.status" placeholder="-- Tìm theo trạng thái" style="width: 100%;">
                        <el-option key="all" label="Tất cả" :value="-1"></el-option>
                        <el-option key="active" label="Hoạt động" :value="0"></el-option>
                        <el-option key="block" label="Khóa" :value="1"></el-option>
                    </el-select>
                </div>
                <div class="item-filter">
                    <el-button icon="el-icon-search" circle @click="searchUser"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listUser"
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
                    @sort-change="handleSortChange"
                >
                    <el-table-column
                        type="selection"
                        width="55">
                    </el-table-column>
                    <el-table-column
                        prop="username"
                        label="Tên đăng nhập"
                        sortable
                        width="180">
                        <template slot-scope="scope">{{ scope.row.username }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="fullname"
                        label="Tên đầy đủ"
                        sortable
                        width="200">
                        <template slot-scope="scope">{{ scope.row.fullname }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="phone"
                        label="Số điện thoại"
                        sortable
                        width="150">
                        <template slot-scope="scope">{{ scope.row.phone }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="email"
                        label="Email"
                        sortable
                        width="250">
                        <template slot-scope="scope">{{ scope.row.email }}</template>
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
                size="700px"
                title="Khách hàng"
                :visible.sync="isOpenModal"
                direction="rtl"
                :before-close="handleCloseModal">
                <div style="padding: 20px; width: calc(100% - 40px);">
                    <el-form :model="customer" ref="userDetail" label-width="150px" class="">
                        <el-form-item
                            prop="username"
                            label="Tên đăng nhập"
                            :rules="[
                                { required: true, message: 'Tên đăng nhập không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^[a-zA-Z]+[a-zA-Z0-9_]*$', message: 'Tên đăng nhập bắt đầu bằng chữ và chỉ chứa chữ và số', trigger: ['blur', 'change'] },
                                { max: 20, message: 'Số kí tự không vượt quá 20', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="customer.username" :disabled="typeModal==1"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="fullname"
                            label="Tên đầy đủ"
                            :rules="[
                                { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                                { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="customer.fullname"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="phone"
                            label="Số điện thoại"
                            :rules="[
                                { required: true, message: 'Số điện thoại không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^(0)+[1-9]+[0-9]{8}$', message: 'Định dạng số điện thoại không đúng', trigger: ['blur', 'change'] }
                            ]"
                        >
                            <el-input v-model="customer.phone"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="email"
                            label="Email"
                            :rules="[
                                { required: true, message: 'Email không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^[a-zA-Z0-9]+([\._]?[a-zA-Z0-9]+)*@[a-z]+(\.[a-z]+)*$', message: 'Email không đúng định dạng', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="customer.email"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="address"
                            label="Địa chỉ"
                            :rules="[
                                { required: true, message: 'Địa chỉ không được để trống', trigger: ['blur', 'change'] },
                                { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="customer.address"></el-input>
                        </el-form-item>
                        <el-form-item label="Trạng thái">
                            <el-select v-model="customer.status" placeholder="Trạng thái" style="width: 100%;">
                                <el-option label="Hoạt động" :value="0"></el-option>
                                <el-option label="Khóa" :value="1"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('userDetail')">Lưu</el-button>
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
                    username: "",
                    fullname: "",
                    phone:"",
                    email:"",
                    page: 0,
                    limit: 10,
                    fieldSort: "username",
                    typeSort: "acs",
                    status: -1
                },

                listUser: [],
                total: 0,
                idSelecteds: [],

                customerDefault:{
                    id: 0,
                    username:"",
                    fullname: "",
                    phone:"",
                    email: "",
                    address:"",
                    status: 0,
                },
                customer:{
                    id: 0,
                    username:"",
                    fullname: "",
                    phone:"",
                    email: "",
                    address:"",
                    status: 0,
                },
                isOpenModal: false,
                typeModal: 0
            }
        },

        methods:{
            searchUser: async function(){
                let res = await RestFul.getWithToken("/api/manage/user/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listUser = res.data.data;
                        this.total = res.data.count;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }
                    }
                }
            },
            handleSelectionChange: function(selected){
                this.idSelecteds = selected.map(el => el.id);
            },
            openUpdate: async function(id){
                await this.getCustomerById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/user/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchUser();
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
                let res = await RestFul.delWithToken("/api/manage/user/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchUser();
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
                this.$confirm.confirm("Bạn có muốn xóa khách hàng này?","Xóa khách hàng",{
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
                this.$confirm.confirm("Bạn có muốn xóa những khách hàng này?","Xóa khách hàng",{
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
                this.searchUser();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchUser();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchUser();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Đang hoạt động";
                }else return "Đã khóa"
            },
            getCustomerById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/user/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.customer = res.data;
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addCustomer: async function(){
                let res = await RestFul.postWithToken("/api/manage/user/create", this.customer);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.customer = res.data;
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
            updateCustomer: async function(){
                let res = await RestFul.putWithToken("/api/manage/user/update", this.customer);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.customer = res.data;
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
                this.searchUser();
                this.isOpenModal = false;
            },
            openAddCustomer: function(){
                this.customer = JSON.parse(JSON.stringify(this.customerDefault));
                this.isOpenModal = true;
                this.$refs['userDetail'].resetFields();
                this.typeModal = 0;
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addCustomer();
                        }else{
                            me.updateCustomer();
                        }
                    }
                });
            },
        },

        created: function(){
            this.searchUser();
        }
    }

</script>