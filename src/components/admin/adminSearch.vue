<script setup>
const emit = defineEmits('load')
const searchA = defineModel('searchA')

function handleSearch() {
    emit('load')
}


</script>

<template>
    <el-form :model="searchA" :inline="true">
        <el-form-item label="昵称">
            <el-input v-model="searchA.nickName" placeholder="请输入昵称" clearable></el-input>
        </el-form-item>
        
        <el-form-item label="邮箱">
            <el-input v-model="searchA.email" placeholder="请输入邮箱" clearable></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
            <el-input v-model="searchA.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>

        <el-form-item label="电话">
            <el-input v-model="searchA.phone" placeholder="请输入电话号码" clearable></el-input>
        </el-form-item>
        <el-form-item label="是否超管">
            <el-select v-model="searchA.isSuper" placeholder="请选择" size="large" style="width: 140px" 
            clearable >
                <el-option label="普通管理员" :value=0></el-option>
                <el-option label="超级管理员" :value=1></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="是否封禁">
            <el-select v-model="searchA.isBanned" placeholder="请选择" size="large" style="width: 100px" clearable>
                <el-option label="正常" :value=0></el-option>
                <el-option label="封禁" :value=1></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="">
            <el-button type="success" @click="handleSearch">搜索</el-button>
        </el-form-item>
    </el-form>

</template>

<style scoped>

</style>