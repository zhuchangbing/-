<script setup>
import { ref } from 'vue';
import adminApi from '@/axios/adminApi';
import { ElMessage } from 'element-plus';


const addadmin = ref({})
const addVisible = defineModel('addVisible')
const emit = defineEmits(['addLoad'])

function handleAdd() {
    adminApi.addadmin(addadmin.value).then(result => {
        console.log(addadmin.value)
        if(result.code == 200 ) {
            ElMessage('添加成功')
            emit('addLoad')
            addVisible.value = false
        }
        else {
            ElMessage('添加失败 请重试')
            addVisible.value = false
        }
    })
}


</script>

<template>
    <el-dialog title="添加管理员" v-model="addVisible">
        <el-form :model="addadmin" label-width="80px">
            <el-form-item label="邮箱">
                <el-input v-model="addadmin.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="电话">
                <el-input v-model="addadmin.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
                <el-input v-model="addadmin.nickName" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="真实姓名">
                <el-input v-model="addadmin.name" placeholder="请输入真实姓名"></el-input>
            </el-form-item>
            <el-form-item label="密码">
                <el-input v-model="addadmin.password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleAdd">确 定</el-button>
        </div>
    </el-dialog>

</template>

<style scoped>

</style>