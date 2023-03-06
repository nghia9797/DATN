<template>
    <div class="div-scroll-height">
        <div class="filter">
            <div class="item-filter">
                <div class="item-filter-title">Loại cây</div>
                <el-select v-model="params.treeType" placeholder="-- Tìm theo loại cây" style="width: 100%;">
                    <el-option key="all" label="Tất cả" :value="-1"></el-option>
                    <el-option v-for="item in treeTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
                <el-button icon="el-icon-search" circle @click="getDataReport"></el-button>
                <!-- <el-button icon="el-icon-download" circle @click="downloadReport"></el-button> -->
            </div>
        </div>
        <div v-if="(dataReport.items || []).length > 0" class="header-report content">
            <div style="margin-bottom: 8px;"><b>BÁO CÁO DOANH SỐ BÁN HÀNG</b></div>
            <div style="margin-bottom: 8px;">Loại cây: <b>{{ params.treeTypeName }}</b></div>
            <div><span>Từ ngày: <b>{{ getStringOfDate(params.from) }}</b></span> <span>Đến ngày: <b>{{ getStringOfDate(params.to) }}</b></span></div>
        </div>
        <div>
            <div class="content" v-if="(dataReport.items || []).length > 0">
                <div v-for="itemType in dataReport.types">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <div style="display: flex; justify-content: flex-start; align-items: center;">
                            <div class="btn-show-detail" @click="addTabActive(itemType.name)" v-if="!listActive.includes(itemType.name)"><i class="el-icon-caret-bottom"></i></div>
                            <div class="btn-show-detail" @click="removeTabActive(itemType.name)" v-else><i class="el-icon-caret-top"></i></div>
                            <div>{{ itemType.name }}</div>
                        </div>
                        <div><b>{{ getStringForMoney(itemType.total_amount) }}</b></div>
                    </div>
                    <div v-if="listActive.includes(itemType.name)">
                        <el-table
                            :data="dataReport.items.filter(el=> el.treeTypeName == itemType.name)"
                            style="width: 100%" fit="true"
                        >
                            <el-table-column
                                prop="code"
                                label="Mã cây"
                                :min-width="20">
                                <template slot-scope="scope">{{ scope.row.treeCode }}</template>
                            </el-table-column>
                            <el-table-column
                                prop="treeName"
                                label="Tên cây"
                                :min-width="20">
                                <template slot-scope="scope">{{ scope.row.treeName }}</template>
                            </el-table-column>
                            <el-table-column
                                align="right"
                                prop="totalCount"
                                label="Số lượng bán ra"
                                :min-width="20">
                                <template slot-scope="scope">{{ getStringForNumber(scope.row.totalCount) }}</template>
                            </el-table-column>
                            <el-table-column
                                align="right"
                                prop="totalAmount"
                                label="Doanh số"
                                :min-width="40">
                                <template slot-scope="scope"><b>{{ getStringForMoney(scope.row.totalAmount) }}</b></template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 12px;">
                    <div>Tổng doanh thu</div>
                    <div><b style="color: #75c321;">{{ getTotalRevenue }}</b></div>
                </div>
            </div>
            <div class="content" v-else>
                <div>Không có dữ liệu</div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .div-scroll-height{
        max-height: calc(100vh - 135px);
        overflow-y: scroll;
        position: relative;
    }
    .btn-show-detail{
        padding: 4px;
        cursor: pointer;
    }
    .filter{
        margin-top: 12px;
        padding: 10px;
        border: 1px solid lightgray;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        flex-wrap: wrap;
        position: sticky;
        top: 0px;
        z-index: 1px;
        background-color: white;
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
    import { store } from '../../store';
    export default {
        data: function(){
            return {
                params: {
                    treeType: -1,
                    treeTypeName: "",
                    from: new Date().getTime(),
                    to: new Date().getTime()
                },
                dataReport:{},
                treeTypes: [],
                listActive:[]
            }
        },
        methods: {
            getStringOfDate: function(timestamp){
                let strDate = "";
                if(timestamp * 1 || 0 > 0){
                    let date = new Date(timestamp);
                    let ngay = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
                    let month = (date.getMonth() + 1) < 10 ? `0${date.getMonth() + 1}` : date.getMonth();
                    let hour = (date.getHours() + 1) < 10 ? `0${date.getHours()}` : date.getHours();
                    let min = (date.getMinutes() + 1) < 10 ? `0${date.getMinutes()}` : date.getMinutes();
                    let second = (date.getSeconds() + 1) < 10 ? `0${date.getSeconds()}` : date.getSeconds();
                    // strDate = `${ngay}/${month}/${date.getFullYear()} ${hour}:${min}:${second}`;
                    strDate = `${ngay}/${month}/${date.getFullYear()}`;
                }
                return strDate;
            },
            getStringForMoney: function(number){
                return new Intl.NumberFormat('vn-VN').format(number)+"đ";
            },
            getStringForNumber: function(number){
                return new Intl.NumberFormat('vn-VN').format(number);
            },
            getDataReport: async function(){
                let me = this;
                let a = this.params.from;
                let b = this.params.to;
                this.params.from = this.params.from * 1 || 0;
                this.params.to = this.params.to * 1 || 0;
                if(this.params.treeType == -1){
                    this.params.treeTypeName = "Tất cả";
                }else{
                    this.params.treeTypeName = this.treeTypes.filter(el => el.id == me.params.treeType)[0].name;
                }
                let res = await RestFul.getWithToken("/api/manage/report/revenue", null, this.params);
                if(res != null){
                    if(res.statusCode == 200){
                        this.dataReport = res.data;
                        this.listActive = [];
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }else{
                            this.$messsage.error(res.message);
                        }
                    }
                }
                this.params.from = a;
                this.params.to = b;
            },

            getListTreeType: async function(){
                let res = await RestFul.getWithToken("/api/manage/treetype", null, {});
                if(res != null){
                    if(res.statusCode == 200){
                        this.treeTypes = res.data;
                        this.params.treeType = -1;
                    }else{
                        if(res.statusCode == 401 || res.statusCode == 402){
                            this.$messsage.error(res.message);
                            this.$router.push("/login");
                        }else{
                            this.$messsage.error(res.message);
                        }
                    }
                }
            },
            addTabActive: function(name){
                this.listActive.push(name);
            },
            removeTabActive: function(name){
                let index = -1;
                for(let i = 0;i<this.listActive.length;i++){
                    if(this.listActive[i] == name){
                        index = i;
                        break;
                    }
                }
                if(index >= 0){
                    this.listActive.splice(index, 1);
                }
            },
            downloadReport: function(){
                let pas = {
                    treeType: this.params.treeType,
                    from: this.params.from * 1 || 0,
                    to: this.params.to * 1 || 0
                }
                let query = "/api/manage/report/revenue/download?";
                Object.keys(pas).forEach(el =>{
                    query += `${el}=${pas[el]}&`;
                });
                query = query.substring(0, query.length - 1);
                window.open(store.baseUrl+query,"_blank", null);
                // RestFul.downloadWithToken("/api/manage/report/revenue/download", null, pas);
            }
        },

        created: function(){
            this.getDataReport();
            this.getListTreeType();
        },

        computed: {
            getTotalRevenue: function(){
                let total = 0;
                this.dataReport.types.forEach(el => total+=el.total_amount);
                return this.getStringForMoney(total);
            }
        }
    }

</script>