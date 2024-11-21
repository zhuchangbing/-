<script setup>
import { ref } from 'vue';
const emit = defineEmits('searchLoad')
const searchUserVo = defineModel('searchUserVo')

function handleSearch() {
    emit('searchLoad')
}

</script>

<template>
    <el-form :model="searchUserVo" :inline="true" label-width="auto">  
        <el-form-item label="用户名">
            <el-input v-model="searchUserVo.nickName" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
            <el-input v-model="searchUserVo.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="电话">
            <el-input v-model="searchUserVo.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="地址">
            <el-input v-model="searchUserVo.address" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="success" @click="handleSearch">搜索</el-button>
        </el-form-item>
    </el-form>

</template>

<style scoped> 

</style>