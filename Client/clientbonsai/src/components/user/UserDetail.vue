<template>
    <div>
        <Header></Header>
        <div class="panel">
            <el-button type="default" @click="$router.push('/')">Xem gian hàng</el-button>
            <el-button type="default" @click="$router.push('/listBill')">Xem hóa đơn</el-button>
        </div>
        <div class="panel">
            <div class="box-detail">
                <div class="title">Thông tin chi tiết</div>
                <el-form :model="params" ref="userDetail" label-width="150px" class="">
                    <el-form-item
                        prop="username"
                        label="Tên đăng nhập"
                        :rules="[
                            { required: true, message: 'Tên đăng nhập không được để trống', trigger: ['blur', 'change'] },
                            { pattern: '^[a-zA-Z]+[a-zA-Z0-9_]*$', message: 'Tên đăng nhập bắt đầu bằng chữ và chỉ chứa chữ và số', trigger: ['blur', 'change'] },
                            { max: 20, message: 'Số kí tự không vượt quá 20', trigger: ['blur', 'change'] },
                        ]"
                    >
                        <el-input v-model="params.username" disabled></el-input>
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
                        <el-button type="primary" @click="submitForm('userDetail')">Cập nhật</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="box-changepass">
                <div class="title">Mật khẩu</div>
                <el-form :model="paramChangePass" ref="changePass" label-width="150px" class="">
                    <el-form-item
                        label="Mật khẩu cũ"
                        prop="oldPassword"
                        :rules="[
                            {required: true, message: 'Mật khẩu không đươc đê trống', trigger: ['blur', 'change']},
                            {min: 6, message: 'Độ dài phải lớn hơn 6 kí tự', trigger: ['blur', 'change']},
                            {max: 20, message: 'Độ dài không vượt quá 20 kí tự', trigger: ['blur', 'change']}
                        ]"
                    >
                        <el-input type="password" v-model="paramChangePass.oldPassword"></el-input>
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
                        <el-input type="password" v-model="paramChangePass.password"></el-input>
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
                        <el-input type="password" v-model="paramChangePass.repassword"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitFormChangePass('changePass')">Đổi mật khẩu</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>

<style scoped>
    .panel{
        width: 80%;
        margin-left: auto;
        margin-right: auto;
        margin-top: 20px;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
    }
    .box-detail{
        width: 60%;
    }
    .box-changepass{
        width: 39%;
    }
    .title{
        width: 100%;
        text-align: left;
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 20px;
    }
</style>


<script>
    import { store } from '../../store';
    import { RestFul } from '../../restful';
    import Header from '../common/Header.vue';
    import Footer from '../common/Footer.vue';
    export default {
        data: function(){
            return {
                params:{
                    username:"",
                    fullname:"",
                    phone:"",
                    email:"",
                    address:"",
                    status: 0
                },
                paramChangePass:{
                    oldPassword:"",
                    password: "",
                    repassword: ""
                }
            }
        },

        methods:{
            checkSession: async function(){
                let res = await RestFul.checkAuth("/api/auth/check",null, {type: 1})
                if(res != null){
                    if(res.statusCode == 200){
                        store.setUser(res.data);
                    }else{
                        store.setUser({});
                    }
                }
            },
            getUserDetail: async function(){
                await this.checkSession();
                let res = await RestFul.getWithToken("/api/manage/user/getByIdForUser/"+store.user.id);
                if(res != null){
                    if(res.statusCode == 200){
                        this.params = res.data;
                        return;
                    }else{
                        this.$message.error(res.message);
                    }
                }
                store.setUser({});
                this.$router.push("/");
            },

            submitForm(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        let res = await RestFul.putWithToken("/api/manage/user/updateForUser", me.params);
                        if(res != null){
                            if(res.statusCode == 200){
                                me.$message.success(res.message);
                            }else{
                                me.$message.error(res.message);
                                if(res.statusCode == 402 || res.statusCode == 401){
                                    me.$router.push("/login");
                                }
                            }
                        }
                    }
                });
            },

            submitFormChangePass(formName) {
                let me = this;
                this.$refs[formName].validate(async function(valid){
                    if (valid) {
                        if(me.paramChangePass.oldPassword == store.user.password){
                            if(me.paramChangePass.oldPassword != me.paramChangePass.password){
                                if(me.paramChangePass.password == me.paramChangePass.repassword){
                                    let user = {
                                        id: store.user.id,
                                        password: me.paramChangePass.password
                                    }
                                    let res = await RestFul.putWithToken("/api/manage/user/changePassword", user);
                                    if(res != null){
                                        if(res.statusCode == 200){
                                            me.$message.success(res.message);
                                            me.$router.push("/login");
                                        }else{
                                            me.$message.error(res.message);
                                        }
                                    }
                                }else{
                                    me.$message.error("Mật khẩu nhập lại không đúng");
                                }
                            }else{
                                me.$message.error("Mật khẩu mới không được trùng mật khẩu cũ");
                            }
                        }else{
                            me.$message.error("Mật khẩu cũ không đúng");
                        }
                    }
                });
            },
        },

        created: function(){
            this.getUserDetail();
        },

        components: {Header, Footer}
    }
</script>