<template>
    <div class="page-login">
       <div class="box-login">
            <div class="title">Đăng ký</div>
            <div>
                <el-form :model="params" ref="signForm" label-width="150px" class="">
                    <el-form-item
                        prop="username"
                        label="Tên đăng nhập"
                        :rules="[
                            { required: true, message: 'Tên đăng nhập không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^[a-zA-Z]+[a-zA-Z0-9_]*$', message: 'Tên đăng nhập bắt đầu bằng chữ và chỉ chứa chữ và số', trigger: ['blur', 'change'] },
                            { max: 20, message: 'Số kí tự không vượt quá 20', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="params.username"></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="fullname"
                        label="Tên đầy đủ"
                        :rules="[
                            { required: true, message: 'Tên đầy đủ không được để trống', trigger: ['blur', 'change'] },
                            { max: 100, message: 'Số kí tự không vượt quá 100', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="params.fullname"></el-input>
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
                    <el-form-item
                        label="Nhập lại mật khẩu"
                        prop="repassword"
                        :rules="[
                            {required: true, message: 'Mật khẩu nhập lại không đươc đê trống', trigger: ['blur', 'change']},
                            {min: 6, message: 'Độ dài phải lớn hơn 6 kí tự', trigger: ['blur', 'change']},
                            {max: 20, message: 'Độ dài không vượt quá 20 kí tự', trigger: ['blur', 'change']}
                        ]"
                    >
                        <el-input type="password" v-model="params.repassword"></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="phone"
                        label="Số điện thoại"
                        :rules="[
                            { required: true, message: 'Số điện thoại không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^(0)+[1-9]+[0-9]{8}$', message: 'Định dạng số điện thoại không đúng', trigger: ['blur', 'change'] }
                        ]"
                    >
                        <el-input v-model="params.phone"></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="email"
                        label="Email"
                        :rules="[
                            { required: true, message: 'Email không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^[a-zA-Z0-9]+([\._]?[a-zA-Z0-9]+)*@[a-z]+(\.[a-z]+)*$', message: 'Email không đúng định dạng', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="params.email"></el-input>
                    </el-form-item>
                    <el-form-item
                        prop="address"
                        label="Địa chỉ"
                        :rules="[
                            { required: true, message: 'Địa chỉ không được để trống', trigger: ['blur', 'change'] },
                            { max: 250, message: 'Số kí tự không vượt quá 200', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="params.address"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="warning" @click="$router.push('/login')">Đăng nhập</el-button>
                        <el-button type="primary" @click="submitForm('signForm')">Đăng ký</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="default" @click="$router.push('/')">Về xem gian hàng</el-button>
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
        top: 50vh;
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
                    password:"",
                    repassword:"",
                    fullname:"",
                    phone:"",
                    email:"",
                    address:"",
                    status: 0
                }
            }
        },
        methods:{
            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.params.password == me.params.repassword){
                            let res = await RestFul.post("/api/auth/user/signin", me.params);
                            if(res != null){
                                if(res.statusCode == 200){
                                    me.$message.success(res.message);
                                    me.$router.push("/");
                                }else{
                                    me.$message.error(res.message);
                                }
                            }else me.$message.error("Login failure!");
                        }else{
                            me.$message.error("Mật khẩu nhập lại không đúng");
                        }
                    }
                });
            },
        }
    }
</script>