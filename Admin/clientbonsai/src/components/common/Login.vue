<template>
    <div class="page-login">
       <div class="box-login">
            <div class="title">Đăng nhập</div>
            <div class="box-form">
                <el-form :model="params" ref="loginForm" label-width="120px" class="">
                    <el-form-item
                        prop="username"
                        label="Tên đăng nhập"
                        :rules="[
                            { required: true, message: 'Tên đăng nhập không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^[a-zA-Z]+[a-zA-Z0-9_]*$', message: 'Username bắt đầu bằng chữ và chỉ chứa chữ và số', trigger: ['blur', 'change'] },
                            { max: 20, message: 'Số kí tự không vượt quá 20', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="params.username"></el-input>
                    </el-form-item>
                    <el-form-item
                        label="Mật khẩu"
                        prop="password"
                        :rules="[
                            {required: true, message: 'Mật khẩu không đươc đê trống', trigger: ['blur', 'change']},
                            {min: 6, message: 'Độ dài phải lớn hơn 6 kí tự', trigger: ['blur', 'change']},
                            {max: 20, message: 'Độ dài không vượt quá 20 kí tự', trigger: ['blur', 'change']}
                        ]"
                    >
                        <el-input type="password" v-model="params.password"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="warning" @click="$router.push('/signup')">Đăng ký</el-button>
                        <el-button type="primary" @click="submitForm('loginForm')">Đăng nhập</el-button>
                    </el-form-item>
                </el-form>
            </div>
       </div>
    </div>
</template>

<style scoped>
    .page-login{
        width: 100vw;
        height: 100vh;
        background-image: url('/static/images/background.jpg');
        background-size: 100vw 100vh;
        position: relative;
    }
    .box-login{
        position: absolute;
        width: 500px;
        height: fit-content;
        padding: 20px;
        background-color: white;
        top: 200px;
        left: 50vw;
        transform: translate(-50%, -50%);
    }

    .title{
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 20px;
    }

</style>

<script>
    import { store } from '../../store';
    import { RestFul } from '../../restful';
    export default {
        data: function(){
            return {
                params:{
                    username:"",
                    password:""
                }
            }
        },
        methods:{
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        let res = await RestFul.post("/api/auth/admin/login", me.params);
                        if(res != null){
                            if(res.statusCode == 200){
                                let token = res.data.token;
                                localStorage.setItem("token", token);
                                res = await RestFul.checkAuth("/api/auth/check",null, {type: 2})
                                if(res != null){
                                    if(res.statusCode == 200){
                                        store.setUser(res.data);
                                        me.$router.push("/");
                                    }
                                }
                            }else{
                                me.$message.error(res.message);
                            }
                        }else me.$message.error("Login failure!");

                    }
                });
            },
        }
    }
</script>