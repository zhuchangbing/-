<script setup>
import api from '@/axios';
import { ref } from 'vue';
import { Lock, User } from '@element-plus/icons-vue'
import userApi from '@/axios/userApi';
import { ElMessage } from 'element-plus';
import router from '@/router';
import { emailApi } from '@/axios/emailApi';



const user = ref({header:''})
const captcha = ref('')

function loadCaptcha() {
    api.get(
        `${api.userURL}captcha`
    ).then(result=>{
        user.value.jwt = result.message
        captcha.value = result.data
    })
}
loadCaptcha()

const UserRegForm = ref(null)

function reg(formEl) {
    formEl.validate((valid) => {
        if (valid) {
            userApi.reg(user.value).then(result => {
                if(result.code == 200 ) {
                    ElMessage.success('注册成功')
                    router.push('/login')
                }
                else {
                    ElMessage.error(result.message)
                }
            })
        }
        else {
            ElMessage.error('输入格式不正确')
        }
    })

}


const rules = {
    nickName:[
        { required: true, message:'请输入昵称',trigger:'blur'},
    ],
    email:[
        { required: true, message:'请输入邮箱',trigger:'blur'},
        { pattern:/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,message:'邮箱格式不正确',trigger:'blur'}
    ],
    password:[
        { required: true, message:'请输入密码',trigger:'blur'}
    ],
    repassword: [
        { required: true, message: '请输入确认密码', trigger: 'blur',},
        {
            type:'string',
            validator: (rule, value) => {
                return new Promise((resolve, reject) => {
                    if (value != user.value.password) {
                        reject('两次密码不一致');  // reject with error message
                    } else {
                        resolve();
                    }
                });
            },
        },

    ],
    captcha:[
        { required: true, message:'请输入验证码',trigger:'blur'}
    ]
}


function send() {
    userApi.send(user.value.email).then(result => {
        if(result.code == 200 ) {
            ElMessage.success('验证码发送成功')
        }
        else {
            ElMessage.error(result.message)
        }

    })
}


</script>

<template>
    <div class="whole-bg">
        <div class="login-form">
            <div style="width: 500px;margin: 0 auto;margin-top: 20px;font-size: 24px;border-bottom: 5px solid ;">
                <el-row class="row-style">
                    <el-col :span="12"><el-image src="/src/assets/logo.svg" fit="contain"
                            style="height: 30px;width: 100px;"></el-image></el-col>
                    <el-col :span="12">用户注册</el-col>
                </el-row>
            </div>

            <div class="real-form">
                <el-form :model="user" label-width="80px" ref="UserRegForm" :rules="rules">

                    <el-form-item label="昵称" prop="nickName">
                        <div class="icon-wrapper"><el-icon><User /></el-icon></div>
                        <el-input v-model="user.nickName" placeholder="请输入昵称" class="input-style"></el-input>
                    </el-form-item>

                    <el-form-item label="邮箱" prop="email">
                        <div class="icon-wrapper"><el-icon><User /></el-icon></div>
                        <el-input v-model="user.email" placeholder="请输入邮箱" class="input-style"></el-input>
                    </el-form-item>

                    <el-form-item label="密码" prop="password">
                        <div class="icon-wrapper"><el-icon><Lock /></el-icon></div>
                        <el-input v-model="user.password" placeholder="请输入密码" show-password
                            class="input-style"></el-input>
                    </el-form-item>

                    <el-form-item label="确认密码" prop="repassword">
                        <div class="icon-wrapper"><el-icon><Lock /></el-icon></div>
                        <el-input v-model="user.repassword" placeholder="请再次输入密码" show-password
                            class="input-style"></el-input>
                    </el-form-item>

                    <el-form-item label="邮箱验证" prop="vercode" style="width: 300px;">
                        <el-row>
                            <el-col :span="18">
                                <el-input v-model="user.vercode" placeholder="请输入邮箱验证码"></el-input>
                            </el-col>
                            <el-col :span="6">
                                <el-button type="primary" @click="send">发送</el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item label="验证码" style="margin: 0px;" prop="captcha">
                        <div class="icon-wrapper"><el-icon><Lock /></el-icon></div>
                        <el-row style="width: 300px;height: 30px;">
                            <el-col :span="16">
                                <el-input v-model="user.captcha" placeholder="请输入验证码"
                                    class="input-style-captcha"/>
                                </el-col>
                            <el-col :span="8"><el-image :src="captcha" :fit="contain"
                                    @click="loadCaptcha"
                                    class="captcha"></el-image></el-col>
                        </el-row>
                    </el-form-item>
                    <div style="height: 20px;margin-top: 20px;text-align: right;">
                        已有账号？去<a href="/login">登录</a>
                    </div>
                    <div style="width: 50px;margin: 0 auto;margin-top: 10px;">
                        <el-button type="primary" @click="reg(UserRegForm)">注册</el-button>
                    </div>
                </el-form>
            </div>
        </div>
    </div>

</template>

<style scoped>
.icon-wrapper{
    position: absolute;
    left: 10px; /* 图标离输入框的左侧距离 */
    top: 50%; /* 图标在输入框中垂直居中 */
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10;
}
.remember {
    width: 320px;
    margin-left: 40px;
}
.whole-bg {
    background-image: url('/src/assets/img/home_bg.webp');
    background-repeat: no-repeat;
    background-size: cover;
    height: 700px;
    overflow: hidden; /* 添加这行 */
}
.login-form {
    width: 800px;
    height: 550px;
    margin: 0 auto;
    background-color: rgb(255, 255, 255);
    box-shadow: 0px 0px 19px -5px rgba(0, 0, 249, 1.5);
    margin-top: 50px;
    overflow: hidden; /* 添加这行 */
}
.row-style{
    height: 50px;
    margin-top: 30px;
}

.real-form {
    width: 400px;
    height:auto;
    margin: 0 auto;
    margin-top: 50px;
}
.input-style {
    width: 300px;
}

.input-style-captcha {
    width: 200px;
    padding: 0px;
}
.captcha{
    padding: 0px;
    margin: 0;
    width: 100px;
    height: 32px;
}

</style>