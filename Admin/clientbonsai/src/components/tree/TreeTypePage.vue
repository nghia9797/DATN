<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Danh mục cây/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddTreeType">Thêm</el-button>
                    <el-button icon="el-icon-close" :disabled="idSelecteds.length == 0" @click="openConfirmDeletes">Xóa</el-button>
                </div>
            </div>
            <div class="filter">
                <div class="item-filter">
                    <div class="item-filter-title">Tên danh mục</div>
                    <el-input v-model="params.name" placeholder="-- Tìm theo tên danh mục"></el-input>
                </div>
                <div class="item-filter">
                    <el-button icon="el-icon-search" circle @click="searchTreeType"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listTreeType"
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
                        label="Tên danh mục"
                        sortable
                        width="750">
                        <template slot-scope="scope">{{ scope.row.name }}</template>
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
                    <el-form :model="treeType" ref="treeTypeDetail" label-width="150px" class="">
                        <el-form-item
                            prop="name"
                            label="Tên danh mục"
                            :rules="[
                                { required: true, message: 'Tên danh mục không được để trống', trigger: ['blur', 'change'] },
                                { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="treeType.name"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('treeTypeDetail')">Lưu</el-button>
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

                listTreeType: [],
                total: 0,
                idSelecteds: [],

                treeTypeDefault:{
                    id: 0,
                    name:"",
                },
                treeType:{
                    id: 0,
                    name:"",
                },
                isOpenModal: false,
                typeModal: 0
            }
        },

        methods:{
            searchTreeType: async function(){
                let res = await RestFul.getWithToken("/api/manage/treetype/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listTreeType = res.data.data;
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
                await this.getTreeTypeById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/treetype/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchTreeType();
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
                let res = await RestFul.delWithToken("/api/manage/treetype/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchTreeType();
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
                this.$confirm.confirm("Bạn có muốn xóa danh mục này này?","Xóa danh mục",{
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
                this.$confirm.confirm("Bạn có muốn xóa những danh mục này?","Xóa danh mục",{
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
                this.searchTreeType();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchTreeType();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchTreeType();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Đang hoạt động";
                }else return "Đã khóa"
            },
            getTreeTypeById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/treetype/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.treeType = res.data;
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addTreeType: async function(){
                let res = await RestFul.postWithToken("/api/manage/treetype/create", this.treeType);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.treeType = res.data;
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
            updateTreeType: async function(){
                let res = await RestFul.putWithToken("/api/manage/treetype/update", this.treeType);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.treeType = res.data;
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
                this.searchTreeType();
                this.isOpenModal = false;
            },
            openAddTreeType: function(){
                this.treeType = JSON.parse(JSON.stringify(this.treeTypeDefault));
                this.isOpenModal = true;
                this.$refs['treeTypeDetail'].resetFields();
                this.typeModal = 0;
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addTreeType();
                        }else{
                            me.updateTreeType();
                        }
                    }
                });
            },
        },

        created: function(){
            this.searchTreeType();
        }
    }

</script>