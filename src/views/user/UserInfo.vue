<script setup>
import api from '@/axios';
import userApi from '@/axios/userApi';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';


const user = useUserStore().user
console.log(user)




function handleAvatarSuccess(uploadFile) {
    console.log(uploadFile)
    console.log(user)
    user.header = uploadFile.data
}


function handleChange() {
    userApi.update(user).then(result => {
        if(result.code == 200) {
            ElMessage.success('修改成功')
            useUserStore().changeUser(user)
        }
        else {
            ElMessage.warning(result.message)
        }
    })
}

</script>

<template>
    <div class="title">修改个人信息</div>
    <el-form :model="user" label-width="80px" >

        <el-form-item label="用户头像">
            <el-upload class="avatar-uploader" :action="`${api.skuURL}upload`"
                :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                <img v-if="user.header" :src="`${api.skuURL}upload/${user.header}`" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus />
                </el-icon>
            </el-upload>

            <!-- <el-image style="width: 100px; height: 100px" :src="`${api.skuURL}upload/${user.header}`"
                :fit="fit"></el-image> -->
        </el-form-item>

        <el-form-item label="昵称">
            <el-input v-model="user.nickName" placeholder="请输入用户昵称"></el-input>
        </el-form-item>

        <el-form-item label="手机号">
            <el-input v-model="user.phone" placeholder="请输入用户电话"></el-input>
        </el-form-item>

        <el-form-item label="邮箱">
            <el-input v-model="user.email" placeholder="请输入用户邮箱"></el-input>
        </el-form-item>
        <el-form-item label="">
            <el-button type="primary" @click="handleChange">提交修改</el-button>
        </el-form-item>
    </el-form>

</template>

<style>

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style scoped>
.title {
    font-size: 20px;
    font-weight: bold;
    margin: 10px 0px;
}


.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>