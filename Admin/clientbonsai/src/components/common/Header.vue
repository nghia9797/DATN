<template>
    <div style="width: 100%;">
        <div class="header">
            <div class="header-left">
                <div class="logo" @click="$router.push('/')"></div>
                <div class="shopname">CÂY CẢNH BONSAI</div>
            </div>
            <div class="header-right">
                <div class="hotline">
                    <div>Hot line</div>
                    <div>19002709</div>
                </div>
                <div class="username" @click="$router.push('/userDetail')">{{ getName }}</div>
                <!-- <div v-if="getName.length == 0" class="log" @click="$router.push('/login')">Login</div> -->
                <div class="log" @click="logout()">Logout</div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .header {
        width: 100%;
        background-color: #75c321;
        height: 75px;
        margin: auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .header-left{
        display: flex;
        justify-content: flex-start;
        align-items: center;
    }
    .logo{
        margin-left: 100px;
        background-image: url("/static/images/logo.png");
        width: 50px;
        height: 50px;
        background-size: 50px 50px;
        cursor: pointer;
    }
    .shopname{
        font-size: 20px;
        font-weight: bold;
        color: white;
        margin-left: 16px;
        font-family: cursive;
    }
    .header-right{
        font-size: 14px;
        font-weight: bold;
        color: white;
        display: flex;
        justify-content: flex-end;
        align-items: center;
    }
    .hotline{
        margin-right: 12px;
    }
    .cart{
        margin-right: 12px;
        padding: 8px 16px;
        border: 1px solid white;
        border-radius: 5px;
        cursor: pointer;
        position: relative;;
    }
    .count{
        padding: 4px;
        border: 1px solid transparent;
        border-radius: 2px;
        background-color: yellow;
        color: black;
        position: absolute;
        top: 0px;
        right: 0px;
        margin-top: -10px;
        margin-right: -5px;
    }
    .username{
        cursor: pointer;
        margin-right: 12px;
        padding-bottom: 2px;
        border-bottom: 1px solid white;
    }
    .log{
        margin-right: 100px;
        cursor: pointer;
    }
</style>

<script>
    import { RestFul } from '../../restful';
    import { store } from '../../store';
    export default {
        methods:{
            
            checkSession: async function(){
                let res = await RestFul.checkAuth("/api/auth/check",null, {type: 2})
                if(res != null){
                    if(res.statusCode == 200){
                        store.setUser(res.data);
                    }else{
                        store.setUser({});
                        localStorage.removeItem("token");
                        this.$router.push("/login");
                    }
                }
            },
            logout: function(){
                RestFul.logout("/api/auth/logout");
                store.setUser({});
                localStorage.removeItem("token");
                this.$router.push("/login");
            }
        },
        computed:{
            getName: function(){
                return store.user.username;
            },
        },
        created: function(){
            this.checkSession();
        }
    }
</script>