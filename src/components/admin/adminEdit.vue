<script setup>
import adminApi from '@/axios/adminApi';
import { ElMessage } from 'element-plus';

const editVisible = defineModel('editVisible')
const editadmin = defineModel('editadmin')
const emit = defineEmits('editLoad')

function handleEdit() {
    adminApi.editadmin(editadmin.value).then(result=>{
        if(result.code == 200) {
            ElMessage('修改成功')
            emit('editLoad')
            editVisible.value = false
        }
        else {
            ElMessage('修改失败 请重试')
            editVisible.value = false
        }
    })
}

</script>

<template>

    <el-dialog title="修改管理员信息" v-model ="editVisible">
        <el-form :model="editadmin" label-width="80px">
            <el-form-item label="昵称">
                <el-input v-model="editadmin.nickName" clearable></el-input>
            </el-form-item>
            <el-form-item label="电话">
                <el-input v-model="editadmin.phone" clearable></el-input>
            </el-form-item>
            <el-form-item label="真实姓名">
                <el-input v-model="editadmin.name" clearable></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
                <el-input v-model="editadmin.email" clearable></el-input>
            </el-form-item>
            <el-form-item label="是否封禁">
                <el-select v-model="editadmin.isBanned" clearable>
                    <el-option label="正常" :value=0></el-option>
                    <el-option label="封禁" :value=1></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleEdit">确 定</el-button>
        </div>
    </el-dialog>
</template>

<style scoped>

</style>