<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Nhân viên giao hàng/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddEmployee">Thêm</el-button>
                    <el-button icon="el-icon-close" :disabled="idSelecteds.length == 0" @click="openConfirmDeletes">Xóa</el-button>
                </div>
            </div>
            <div class="filter">
                <div class="item-filter">
                    <div class="item-filter-title">Mã nhân viên</div>
                    <el-input v-model="params.code" placeholder="-- Tìm theo mã nhân viên"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Tên đầy đủ</div>
                    <el-input v-model="params.name" placeholder="-- Tìm theo tên đầy đủ"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Số điện thoại</div>
                    <el-input v-model="params.phone" placeholder="-- Tìm theo số điện thoại"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">CCCD</div>
                    <el-input v-model="params.indentifyId" placeholder="-- Tìm theo CCCD"></el-input>
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
                    <el-button icon="el-icon-search" circle @click="searchEmployee"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listEmployee"
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
                        label="Mã nhân viên"
                        sortable
                        width="180">
                        <template slot-scope="scope">{{ scope.row.code }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="name"
                        label="Tên đầy đủ"
                        sortable
                        width="200">
                        <template slot-scope="scope">{{ scope.row.name }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="phone"
                        label="Số điện thoại"
                        sortable
                        width="150">
                        <template slot-scope="scope">{{ scope.row.phone }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="indentify_id"
                        label="CCCD"
                        sortable
                        width="150">
                        <template slot-scope="scope">{{ scope.row.indentifyId }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="birth"
                        label="Ngày sinh"
                        sortable
                        width="180">
                        <template slot-scope="scope">{{ getStringOfDate(scope.row.birth) }}</template>
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
                title="Nhân viên giao hàng"
                :visible.sync="isOpenModal"
                direction="rtl"
                :before-close="handleCloseModal">
                <div style="padding: 20px; width: calc(100% - 40px);">
                    <el-form :model="employee" ref="employeeDetail" label-width="150px" class="">
                        <el-form-item v-if="typeModal == 1"
                            prop="code"
                            label="Mã nhân viên"
                        >
                            <el-input disabled v-model="employee.code"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="name"
                            label="Tên đầy đủ"
                            :rules="[
                                { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                                { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="employee.name"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="phone"
                            label="Số điện thoại"
                            :rules="[
                                { required: true, message: 'Số điện thoại không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^(0)+[1-9]+[0-9]{8}$', message: 'Định dạng số điện thoại không đúng', trigger: ['blur', 'change'] }
                            ]"
                        >
                            <el-input v-model="employee.phone"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="indentifyId"
                            label="CCCD"
                            :rules="[
                                { required: true, message: 'CCCD không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^[0-9]{12}$', message: 'CCCD không đúng định dạng', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="employee.indentifyId"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="address"
                            label="Địa chỉ"
                            :rules="[
                                { required: true, message: 'Địa chỉ không được để trống', trigger: ['blur', 'change'] },
                                { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="employee.address"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="birth"
                            label="Ngày sinh"
                            :rules="[
                                { required: true, message: 'Ngày sinh không được để trống', trigger: ['blur', 'change'] },
                            ]"
                        >
                        <el-date-picker style="width: 100%;"
                            value-format="timestamp"
                            v-model="employee.birth"
                            type="date"
                            placeholder="-- Chọn ngày sinh">
                        </el-date-picker>
                        </el-form-item>
                        <el-form-item label="Trạng thái">
                            <el-select v-model="employee.status" placeholder="Trạng thái" style="width: 100%;">
                                <el-option label="Hoạt động" :value="0"></el-option>
                                <el-option label="Khóa" :value="1"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('employeeDetail')">Lưu</el-button>
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
                    code:"",
                    indentifyId:"",
                    name: "",
                    phone:"",
                    email:"",
                    page: 0,
                    limit: 10,
                    fieldSort: "name",
                    typeSort: "acs",
                    status: -1
                },

                listEmployee: [],
                total: 0,
                idSelecteds: [],

                employeeDefault:{
                    id: 0,
                    code:"",
                    username:"",
                    fullname: "",
                    phone:"",
                    email: "",
                    address:"",
                    status: 0,
                    birth: new Date().getTime()
                },
                employee:{
                    id: 0,
                    code:"",
                    name:"",
                    phone:"",
                    email: "",
                    address:"",
                    indentifyId:"",
                    birth:0,
                    status: 0,
                },
                isOpenModal: false,
                typeModal: 0
            }
        },

        methods:{
            searchEmployee: async function(){
                let res = await RestFul.getWithToken("/api/manage/shipper/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listEmployee = res.data.data;
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
                await this.getEmployeeById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/shipper/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchEmployee();
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
                let res = await RestFul.delWithToken("/api/manage/shipper/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchEmployee();
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
                this.$confirm.confirm("Bạn có muốn xóa nhân viên giao hàng này?","Xóa nhân viên giao hàng",{
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
                this.$confirm.confirm("Bạn có muốn xóa những nhân viên giao hàng này?","Xóa nhân viên giao hàng",{
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
                this.searchEmployee();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchEmployee();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchEmployee();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Đang hoạt động";
                }else return "Đã khóa"
            },
            getEmployeeById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/shipper/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.employee = res.data;
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addEmployee: async function(){
                let res = await RestFul.postWithToken("/api/manage/shipper/create", this.employee);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.employee = res.data;
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
            updateEmployee: async function(){
                let res = await RestFul.putWithToken("/api/manage/shipper/update", this.employee);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.employee = res.data;
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
                this.searchEmployee();
                this.isOpenModal = false;
            },
            openAddEmployee: function(){
                this.employee = JSON.parse(JSON.stringify(this.employeeDefault));
                this.isOpenModal = true;
                this.$refs['employeeDetail'].resetFields();
                this.typeModal = 0;
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addEmployee();
                        }else{
                            me.updateEmployee();
                        }
                    }
                });
            },
            getStringOfDate: function(timestamp){
                let date = new Date(timestamp);
                let ngay = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
                let month = (date.getMonth() + 1) < 10 ? `0${date.getMonth() + 1}` : date.getMonth();
                let hour = (date.getHours() + 1) < 10 ? `0${date.getHours()}` : date.getHours();
                let min = (date.getMinutes() + 1) < 10 ? `0${date.getMinutes()}` : date.getMinutes();
                let second = (date.getSeconds() + 1) < 10 ? `0${date.getSeconds()}` : date.getSeconds();
                // let strDate = `${ngay}/${month}/${date.getFullYear()} ${hour}:${min}:${second}`;
                let strDate = `${ngay}/${month}/${date.getFullYear()}`;
                return strDate;
            }
        },

        created: function(){
            this.searchEmployee();
        }
    }

</script>