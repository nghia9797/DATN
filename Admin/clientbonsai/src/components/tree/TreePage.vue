<template>
    <div>
        <div class="panel">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div class="title">Danh sách cây/</div>
                <div class="control">
                    <el-button icon="el-icon-plus" @click="openAddTree">Thêm</el-button>
                    <el-button icon="el-icon-close" :disabled="idSelecteds.length == 0" @click="openConfirmDeletes">Xóa</el-button>
                </div>
            </div>
            <div class="filter">
                <div class="item-filter">
                    <div class="item-filter-title">Mã cây</div>
                    <el-input v-model="params.code" placeholder="-- Tìm theo mã cây"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Tên cây</div>
                    <el-input v-model="params.name" placeholder="-- Tìm theo tên cây"></el-input>
                </div>
                <div class="item-filter">
                    <div class="item-filter-title">Loại cây</div>
                    <el-select v-model="params.typeId" placeholder="-- Tìm theo loại cây" style="width: 100%;">
                        <el-option key="all" label="Tất cả" :value="-1"></el-option>
                        <el-option v-for="item in treeTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="item-filter">
                    <el-button icon="el-icon-search" circle @click="searchTree"></el-button>
                </div>
            </div>
            <div class="content">
                <el-table
                    ref="multipleTable"
                    :data="listTree"
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
                        label="Mã cây"
                        sortable
                        width="180">
                        <template slot-scope="scope">{{ scope.row.code }}</template>
                    </el-table-column>
                    <el-table-column
                        prop="name"
                        label="Tên cây"
                        sortable
                        width="200">
                        <template slot-scope="scope">{{ scope.row.name }}</template>
                    </el-table-column>
                    <el-table-column
                        align="right"
                        prop="cost"
                        label="Giá"
                        sortable
                        width="150">
                        <template slot-scope="scope">{{ getStringForNumber(scope.row.cost) }}</template>
                    </el-table-column>
                    <el-table-column
                        align="right"
                        prop="discount"
                        label="Chiết khấu"
                        sortable
                        width="150">
                        <template slot-scope="scope">{{ getStringForNumber(scope.row.discount) }}</template>
                    </el-table-column>
                    <el-table-column
                        label="Mô tả"
                        width="300">
                        <template slot-scope="scope">{{ scope.row.description }}</template>
                    </el-table-column>
                    <el-table-column
                        align="right"
                        label="Số lượng"
                        width="150">
                        <template slot-scope="scope">{{ getStringForNumber(scope.row.count) }}</template>
                    </el-table-column>
                    <el-table-column
                        label="Loại cây"
                        width="180">
                        <template slot-scope="scope">{{ getTreeTypeNameById(scope.row.typeId) }}</template>
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
                title="Cây"
                :visible.sync="isOpenModal"
                direction="rtl"
                :before-close="handleCloseModal">
                <div style="padding: 20px; width: calc(100% - 40px);">
                    <el-form :model="tree" ref="treeDetail" label-width="150px" class="">
                        <el-form-item label="Loại cây">
                            <el-select v-model="tree.typeId" placeholder="Loại cây" style="width: 100%;">
                                <el-option v-for="item in treeTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item v-if="typeModal == 1"
                            prop="code"
                            label="Mã cây"
                        >
                            <el-input v-model="tree.code" disabled></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="name"
                            label="Tên cây"
                            :rules="[
                                { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                                { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input v-model="tree.name"></el-input>
                        </el-form-item>
                        <el-form-item
                            prop="cost"
                            label="Giá"
                        >
                            <el-input-number style="width: 100%;" v-model="tree.cost" :min="0"></el-input-number>
                        </el-form-item>
                        <el-form-item
                            prop="discount"
                            label="Chiết khấu"
                        >
                            <el-input-number style="width: 100%;" v-model="tree.discount" :min="0" :max="100"></el-input-number>
                        </el-form-item>
                        <el-form-item
                            prop="count"
                            label="Số lượng"
                        >
                            <el-input-number style="width: 100%;" v-model="tree.count" :min="0"></el-input-number>
                        </el-form-item>
                        <el-form-item
                            prop="description"
                            label="Mô tả"
                            :rules="[
                                { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                            ]"
                        >
                            <el-input type="textarea" v-model="tree.description"></el-input>
                        </el-form-item>
                        <el-form-item
                            label="Hình ảnh"
                        >
                            <div class="box-image">
                                <div v-for="item in tree.images" class="item-image">
                                    <img :src="getUrlForImage(item)"/>
                                    <div class="btn-remove-image" @click="deleteImage(item)"><i class="el-icon-close"></i></div>
                                </div>
                            </div>
                            <div>
                                <el-upload
                                    class="uploadImage"
                                    action="http://localhost:8000/api/uploadImage"
                                    :show-file-list="false"
                                    :on-success="handleAvatarSuccess"
                                    :before-upload="beforeUpload"
                                >
                                    <el-button size="small" type="primary">Click to upload</el-button>
                                </el-upload>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="submitForm('treeDetail')">Lưu</el-button>
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

    .box-image{
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        max-height: 200px;
        overflow-y: scroll;
    }

    .item-image{
        position: relative;
        width: 24%;
        margin-right: 4px;
        margin-bottom: 4px;
        height: 90px;
    }

    .item-image img{
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .btn-remove-image{
        position: absolute;
        top: 0;
        right: 0;
        padding: 4px;
        line-height: 14px;
        background-color: white;
        border: 1px solid black;
        border-radius: 50%;
        cursor: pointer;
        color: red;
        font-weight: bold;
    }
</style>
<script>
import { store } from '../../../../../client/clientbonsai/src/store';
import { RestFul } from '../../restful';

    export default {
        data: function(){
            return {
                params: {
                    name: "",
                    code: "",
                    typeId:-1,
                    page: 0,
                    limit: 10,
                    fieldSort: "name",
                    typeSort: "acs",
                    status: -1
                },

                listTree: [],
                total: 0,
                idSelecteds: [],

                treeDefault:{
                    id: 0,
                    code:"",
                    name:"",
                    cost: 0,
                    discount: 0,
                    count: 0,
                    description:"",
                    typeId: 0,
                    images:[],
                },
                tree:{
                    id: 0,
                    code:"",
                    name:"",
                    cost: 0,
                    discount: 0,
                    count: 0,
                    description:"",
                    typeId: 0,
                    images:[],
                },
                isOpenModal: false,
                typeModal: 0,
                treeTypes: []
            }
        },

        methods:{
            deleteImage: function(link){
                let index = -1;
                for(let i = 0;i<this.tree.images.length;i++){
                    if(this.tree.images[i] == link){
                        index = i;
                        break;
                    }
                }
                if(index >= 0){
                    this.tree.images.splice(index, 1);
                }
            },
            handleAvatarSuccess:function(res, file){
                if(res != null){
                    this.tree.images = this.tree.images.concat(res);
                }
            },
            beforeUpload(file) {
                const isJPG = file.type === 'image/jpeg' || file.type === 'image/jpg' || file.type === 'image/png';
                const isLt5M = file.size / 1024 / 1024 < 5;

                if (!isJPG) {
                this.$message.error('Avatar picture must be JPG or PNG format!');
                }
                if (!isLt5M) {
                this.$message.error('Avatar picture size can not exceed 5MB!');
                }
                return isJPG && isLt5M;
            },

            searchTree: async function(){
                let res = await RestFul.getWithToken("/api/manage/tree/search", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.listTree = res.data.data;
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
                await this.getTreeById(id);
                this.isOpenModal = true;
                this.typeModal = 1;
            },
            deleteById: async function(id){
                let res = await RestFul.delWithToken("/api/manage/tree/delete/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchTree();
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
                let res = await RestFul.delWithToken("/api/manage/tree/deletes?ids="+this.idSelecteds.toString());
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.searchTree();
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
                this.$confirm.confirm("Bạn có muốn xóa cây này?","Xóa cây",{
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
                this.$confirm.confirm("Bạn có muốn xóa những cây này?","Xóa cây",{
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
                this.searchTree();
            },
            handleSizeChange: function(pagesize){
                this.params.limit = pagesize;
                this.searchTree();
            },
            handleCurrentChange: function(newPage){
                this.params.page = newPage - 1;
                this.searchTree();
            },
            getNameStatus: function(status){
                if(status == 0){
                    return "Đang hoạt động";
                }else return "Đã khóa"
            },
            getTreeById: async function(id){
                let res = await RestFul.getWithToken("/api/manage/tree/getById/"+id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.tree = res.data;
                        this.tree.images = res.data.images || [];
                    }else if(res.statusCode == 401 || res.statusCode == 402){
                        this.$router.push("/login");
                    }
                }
            },
            addTree: async function(){
                let res = await RestFul.postWithToken("/api/manage/tree/create", this.tree);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.tree = res.data;
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
            updateTree: async function(){
                let res = await RestFul.putWithToken("/api/manage/tree/update", this.tree);
                if(res != null){
                    if(res.statusCode == 200){
                        this.$message.success(res.message);
                        this.tree = res.data;
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
                this.searchTree();
                this.isOpenModal = false;
            },
            openAddTree: function(){
                this.tree = JSON.parse(JSON.stringify(this.treeDefault));
                this.tree.typeId = this.treeTypes[0].id;
                this.isOpenModal = true;
                this.$refs['treeDetail'].resetFields();
                this.typeModal = 0;
            },
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.typeModal == 0){
                            me.addTree();
                        }else{
                            me.updateTree();
                        }
                    }
                });
            },
            getStringForNumber: function(number){
                return new Intl.NumberFormat('vn-VN').format(number);
            },
            getAllTreeType: async function(){
                let res = await RestFul.getWithToken("/api/manage/treetype");
                if(res != null){
                    this.treeTypes = res.data;
                }
            },
            getTreeTypeNameById: function(id){
                for(let i = 0;i<this.treeTypes.length;i++){
                    if(this.treeTypes[i].id == id){
                        return this.treeTypes[i].name;
                    }
                }
                return "Unknow";
            },
            getUrlForImage: function(link){
                return store.baseUrl + link;
            }
        },

        created: function(){
            this.searchTree();
            this.getAllTreeType();
        }
    }

</script>