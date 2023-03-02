<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Quản trị viên/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddAdmin">Thêm</el-button>
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
                    <el-button icon="el-icon-search" circle @click="searchAdmin"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listAdmin"
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
                title="Quản trị viên"
                :visible.sync="isOpenModal"
                direction="rtl"
                :before-close="handleCloseModal">
                <div style="padding: 20px; width: calc(100% - 40px);">
                    <el-form :model="admin" ref="adminDetail" label-width="150px" class="">
                        <el-form-item
                            prop="username"
                            label="Tên đăng nhập"
                            :rules="[
                                { required: true, message: 'Tên đăng nhập không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^[a-zA-Z]+[a-zA-Z0-9_]*$', message: 'Tên đăng nhập bắt đầu bằng chữ và chỉ chứa chữ và số', trigger: ['blur', 'change'] },
                                { max: 20, message: 'Số kí tự không vượt quá 20', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="admin.username" :disabled="typeModal==1"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="fullname"
                            label="Tên đầy đủ"
                            :rules="[
                                { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                                { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="admin.fullname"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="phone"
                            label="Số điện thoại"
                            :rules="[
                                { required: true, message: 'Số điện thoại không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^(0)+[1-9]+[0-9]{8}$', message: 'Định dạng số điện thoại không đúng', trigger: ['blur', 'change'] }
                            ]"
                        >
                            <el-input v-model="admin.phone"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="email"
                            label="Email"
                            :rules="[
                                { required: true, message: 'Email không được để trống', trigger: ['blur', 'change'] },
                                { pattern: '^[a-zA-Z0-9]+([\._]?[a-zA-Z0-9]+)*@[a-z]+(\.[a-z]+)*$', message: 'Email không đúng định dạng', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="admin.email"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="address"
                            label="Địa chỉ"
                            :rules="[
                                { required: true, message: 'Địa chỉ không được để trống', trigger: ['blur', 'change'] },
                                { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="admin.address"></el-input>
                        </el-form-item>
                        <el-form-item label="Trạng thái">
                            <el-select v-model="admin.status" placeholder="Trạng thái" style="width: 100%;">
                                <el-option label="Hoạt động" :value="0"></el-option>
                                <el-option label="Khóa" :value="1"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item
                            prop="roleIds"
                            label="Vai trò"
                        >
                            <div style="width: 100%; max-height: 200px; min-height: 200px; overflow-y: scroll;">
                                <el-tree v-if="isOpenModal" :data="roles" style="width: 100%;" :props="defaultProps" node-key="id" :default-checked-keys="admin.roleIds"
                                    show-checkbox
                                    @check-change="handleRoleCheckChange"
                                ></el-tree>
                            </div>
                        </el-form-item>
                        <el-form-item
                            prop="operationIds"
                            label="Hành động"
                        >
                            <div style="width: 100%; max-height: 200px; overflow-y: scroll;">
                                <el-tree v-if="isOpenModal" :data="operations" style="width: 100%;" :props="defaultProps" node-key="id" :default-checked-keys="admin.operationIds"
                                    show-checkbox
                                    @check-change="handleCheckChange"
                                ></el-tree>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('adminDetail')">Lưu</el-button>
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
                    status: -1,
                },

                listAdmin: [],
                total: 0,
                idSelecteds: [],

                adminDefault:{
                    id: 0,
                    username:"",
                    fullname: "",
                    phone:"",
                    email: "",
                    address:"",
                    status: 0,
                    roleIds:[],
                    operationIds:[]
                },
                admin:{
                    id: 0,
                    username:"",
                    fullname: "",
                    phone:"",
                    email: "",
                    address:"",
                    status: 0,
                    roleIds:[],
                    operationIds:[]
                },
                isOpenModal: false,
                typeModal: 0,
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                roles:[],
                operations:[]
            }
        },

        methods:{
            searchAdmin: async function(){
                let res = await RestFul.getWithToken("/api/manage/admin/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listAdmin = res.data.data;
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
                await this.getAdminById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/admin/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchAdmin();
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
                let res = await RestFul.delWithToken("/api/manage/admin/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchAdmin();
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
                this.$confirm.confirm("Bạn có muốn xóa quản trị viên này?","Xóa quản trị viên",{
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
                this.$confirm.confirm("Bạn có muốn xóa những quản trị viên này?","Xóa quản trị viên",{
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
                this.searchAdmin();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchAdmin();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchAdmin();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Đang hoạt động";
                }else return "Đã khóa"
            },
            getAdminById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/admin/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.admin = res.data;
                        this.admin.roleIds = res.data.roleIds || [];
                        this.admin.operationIds = res.data.operationIds || [];
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addAdmin: async function(){
                let res = await RestFul.postWithToken("/api/manage/admin/create", this.admin);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.admin = res.data;
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
            updateAdmin: async function(){
                let res = await RestFul.putWithToken("/api/manage/admin/update", this.admin);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.admin = res.data;
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
                this.searchAdmin();
                this.isOpenModal = false;
            },
            openAddAdmin: function(){
                this.admin = JSON.parse(JSON.stringify(this.adminDefault));
                this.isOpenModal = true;
                this.$refs['adminDetail'].resetFields();
                this.typeModal = 0;
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addAdmin();
                        }else{
                            me.updateAdmin();
                        }
                    }
                });
            },
            parseDataOperation: function(data){
                let names = [];
                data.forEach(el => {
                    if(!names.includes(el.name)){
                        names.push(el.name);
                    }
                });
                this.operations = [];
                let indexGroup = -1;
                names.forEach(el => {
                    let item = {
                        id: indexGroup--,
                        label: el,
                        children: []
                    }
                    data.forEach(e => {
                        if(e.name == el){
                            e.label = e.action;
                            item.children.push(e);
                        }
                    });
                    this.operations.push(item);
                });
                this.dataTest = data;
            },
            getAllOperation: async function(){
                let res = await RestFul.getWithToken("/api/manage/operation/getAll");
                if(res != null){
                    if(res.statusCode == 200){
                        this.parseDataOperation(res.data);
                    }
                }
            },
            handleCheckChange: function(node,isSelected,subNodeHasSelected){
                let me = this;
                if(node.id < 0){
                    let keyIds = node.children.map(el => el.id);
                    if(isSelected == true){
                        keyIds.forEach(el =>{
                            if(!me.admin.operationIds.includes(el)){
                                me.admin.operationIds.push(el);
                            }
                        });
                    }else if(subNodeHasSelected == false){
                        me.admin.operationIds = me.admin.operationIds.filter(el => !keyIds.includes(el));
                    }
                }else{
                    if(isSelected == true){
                        me.admin.operationIds.push(node.id);
                    }else{
                        me.admin.operationIds = me.admin.operationIds.filter(el => el != node.id);
                    }
                }
                // console.log("selectHandle: ", this.dataTest.filter(el => me.role.operationIds.includes(el.id)).map(el => el.alias));
            },
            getAllRole: async function(){
                let me = this;
                let res = await RestFul.getWithToken("/api/manage/role/getAll");
                if(res != null){
                    if(res.statusCode == 200){
                        this.roles = [];
                        res.data.forEach(el => {
                            el.label = el.name;
                            me.roles.push(el);
                        })
                    }
                }
            },
            handleRoleCheckChange: function(node,isSelected,subNodeHasSelected){
                let me = this;
                if(isSelected == true){
                    me.admin.roleIds.push(node.id);
                }else{
                    me.admin.roleIds = me.admin.roleIds.filter(el => el != node.id);
                }
                // console.log("selectHandle: ", this.dataTest.filter(el => me.role.operationIds.includes(el.id)).map(el => el.alias));
            },
        },

        created: function(){
            this.searchAdmin();
            this.getAllOperation();
            this.getAllRole();
        }
    }

</script>