<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Vai trò hệ thống/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddRole">Thêm</el-button>
                    <el-button icon="el-icon-close" :disabled="idSelecteds.length == 0" @click="openConfirmDeletes">Xóa</el-button>
                </div>
            </div>
            <div class="filter">
                <div class="item-filter">
                    <div class="item-filter-title">Tên vai trò</div>
                    <el-input v-model="params.name" placeholder="-- Tìm theo tên"></el-input>
                </div>
                <div class="item-filter">
                    <el-button icon="el-icon-search" circle @click="searchRole"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listRole"
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
                    @sort-change="handleSortChange"
                >
                    <el-table-column
                        type="selection"
                        width="55">
                    </el-table-column>
                    <el-table-column
                        prop="name"
                        label="Tên"
                        sortable
                        width="200">
                        <template slot-scope="scope">{{ scope.row.name }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="description"
                        label="Mô tả"
                        width="700">
                        <template slot-scope="scope">{{ scope.row.description }}</template>
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
                title="Vai trò hệ thống"
                :visible.sync="isOpenModal"
                direction="rtl"
                :before-close="handleCloseModal">
                <div style="padding: 20px; width: calc(100% - 40px);">
                    <el-form :model="role" ref="roleDetail" label-width="150px" class="">
                        <el-form-item
                            prop="name"
                            label="Tên đầy đủ"
                            :rules="[
                                { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                                { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="role.name"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="description"
                            label="Mô tả"
                            :rules="[
                                { max: 200, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input type="textarea" v-model="role.description"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="operationIds"
                            label="Hành động"
                        >
                            <div style="width: 100%; max-height: 200px; overflow-y: scroll;">
                                <el-tree v-if="isOpenModal" :data="operations" style="width: 100%;" :props="defaultProps" node-key="id" :default-checked-keys="getOperationIds"
                                    show-checkbox
                                    @check-change="handleCheckChange"
                                ></el-tree>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('roleDetail')">Lưu</el-button>
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
                    name: "",
                    page: 0,
                    limit: 10,
                    fieldSort: "name",
                    typeSort: "acs",
                    status: -1
                },

                listRole: [],
                total: 0,
                idSelecteds: [],

                roleDefault:{
                    id: 0,
                    name:"",
                    description: "",
                    operationIds: [],
                    operations: [],
                },
                role:{
                    id: 0,
                    name:"",
                    description: "",
                    operationIds: [],
                    operations: [],
                },
                isOpenModal: false,
                typeModal: 0,
                operations: [],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                dataTest: []
            }
        },

        methods:{
            searchRole: async function(){
                let res = await RestFul.getWithToken("/api/manage/role/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listRole = res.data.data;
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
                await this.getRoleById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/role/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchRole();
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
                let res = await RestFul.delWithToken("/api/manage/role/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchRole();
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
                this.$confirm.confirm("Bạn có muốn xóa vai trò này?","Xóa vai trò",{
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
                this.$confirm.confirm("Bạn có muốn xóa những vai trò này?","Xóa vai trò",{
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
                this.searchRole();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchRole();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchRole();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Đang hoạt động";
                }else return "Đã khóa"
            },
            getRoleById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/role/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.role = res.data;
                        this.role.operationIds = res.data.operationIds || [];
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addRole: async function(){
                let res = await RestFul.postWithToken("/api/manage/role/create", this.role);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.role = res.data;
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
            updateRole: async function(){
                let res = await RestFul.putWithToken("/api/manage/role/update", this.role);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.role = res.data;
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
                this.searchRole();
                this.isOpenModal = false;
            },
            openAddRole: function(){
                this.role = JSON.parse(JSON.stringify(this.roleDefault));
                this.isOpenModal = true;
                this.$refs['roleDetail'].resetFields();
                this.typeModal = 0;
                console.log("data: ", this.role);
                
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addRole();
                        }else{
                            me.updateRole();
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
                            if(!me.role.operationIds.includes(el)){
                                me.role.operationIds.push(el);
                            }
                        });
                    }else if(subNodeHasSelected == false){
                        me.role.operationIds = me.role.operationIds.filter(el => !keyIds.includes(el));
                    }
                }else{
                    if(isSelected == true){
                        me.role.operationIds.push(node.id);
                    }else{
                        me.role.operationIds = me.role.operationIds.filter(el => el != node.id);
                    }
                }
                // console.log("selectHandle: ", this.dataTest.filter(el => me.role.operationIds.includes(el.id)).map(el => el.alias));
            }
        },

        computed:{
            getOperationIds: function(){
                return this.role.operationIds;
            }
        },

        created: function(){
            this.searchRole();
            this.getAllOperation();
        }
    }

</script>