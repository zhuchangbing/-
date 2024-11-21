<script setup>
import { ref } from 'vue';


const emit = defineEmits('pageLoad','edit')
const alist = defineModel('alist')
const pageInfo = defineModel('pageInfo')
const editadmin = defineModel('editadmin')
const editVisible = defineModel('editVisible')

function handlePageChange() {
    emit('pageLoad')
}


function handleEdit(admin) {
    console.log(editadmin.value)
    editadmin.value = {...admin}
    emit('edit')
}
</script>

<template>

    <el-table :data="alist" style="width: 100%" border>
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="nickName" label="昵称"></el-table-column>
        <el-table-column prop="name" label="真实姓名"></el-table-column>
        <el-table-column prop="isSuper" label="是否为超管"
        :formatter="(row)=>['普通管理员','超级管理员'].at(row.isSuper)"></el-table-column>
        <el-table-column prop="isBanned" label="是否被封禁"
        :formatter="(row)=>['正常','封禁'].at(row.isBanned)"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作">
            <template #default="scope">
                <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next,sizes" v-model:current-page="pageInfo.page"
            v-model:page-size="pageInfo.size" 
            :total="pageInfo.total" 
            :pager-count="11" 
            :page-sizes="[1, 2, 10, 20]"
            @change="handlePageChange" class="pageInfo"/>

</template>

<style scoped>

</style>