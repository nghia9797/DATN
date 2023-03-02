<template>
    <div>
        <Header></Header>
        <el-container>
            <el-aside width="200px">
                <el-menu :default-active="getMenuActive" background-color="#75c321" text-color="#fff" active-text-color="#000">
                    <el-menu-item index="1" @click="$router.push('/dashboard')">Dashboard</el-menu-item>
                    <el-menu-item index="2" @click="$router.push('/admin')">Quản trị viên</el-menu-item>
                    <el-menu-item index="3" @click="$router.push('/customer')">Khách hàng</el-menu-item>
                    <el-menu-item index="4" @click="$router.push('/employee')">Nhân viên giao hàng</el-menu-item>
                    <el-menu-item index="5" @click="$router.push('/role')">Vai trò hệ thống</el-menu-item>
                    <el-submenu index="6">
                        <template slot="title" style="font-size: 16px;">Cây</template>
                        <el-menu-item index="6-1" @click="$router.push('/treeType')">Danh mục cây</el-menu-item>
                        <el-menu-item index="6-2" @click="$router.push('/tree')">Danh sách cây</el-menu-item>
                    </el-submenu>
                    <el-menu-item index="7" @click="$router.push('/bill')">Hóa đơn</el-menu-item>
                    <el-menu-item index="8" @click="$router.push('/report')">Báo cáo</el-menu-item>
                </el-menu>
            </el-aside>
            <el-main>
                <div style="margin-top: 20px">
                    <router-view name="Dashboard"></router-view>
                    <router-view name="Admin"></router-view>
                    <router-view name="Customer"></router-view>
                    <router-view name="Employee"></router-view>
                    <router-view name="Role"></router-view>
                    <router-view name="TreeType"></router-view>
                    <router-view name="Tree"></router-view>
                    <router-view name="Bill"></router-view>
                    <router-view name="Report"></router-view>
                </div>
            </el-main>
        </el-container>
    </div>
</template>
<style scoped>
    .el-menu{
        text-align: left;
        height: calc(100vh - 75px);
        
    }

    .el-menu-item{
        font-size: 16px;
        font-weight: bold;
    }
    .el-submenu{
        font-size: 16px;
        font-weight: bold;
    }
</style>
<script>
    import { store } from '../store';
    import { RestFul } from '../restful';
    import Header from './common/Header.vue';
    export default {
    data: function () {
        return {

        };
    },
    methods:{

    },
    computed:{
        getMenuActive: function(){
            let path = this.$route.path;
            if(path.indexOf("/dashboard") >= 0){
                return "1";
            }else if(path.indexOf("/admin") >= 0){
                return "2";
            }else if(path.indexOf("/customer") >= 0){
                return "3";
            }else if(path.indexOf("/employee") >= 0){
                return "4";
            }else if(path.indexOf("/role") >= 0){
                return "5";
            }else if(path.indexOf("/treeType") >= 0){
                return "6-1";
            }else if(path.indexOf("/tree") >= 0){
                return "6-2";
            }else if(path.indexOf("/bill") >= 0){
                return "7";
            }else if(path.indexOf("/report") >= 0){
                return "8";
            }
        }
    },
    created: function(){
        
    },
    components: { Header }
}
</script>