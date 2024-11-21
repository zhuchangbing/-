<script setup>
import { ref } from 'vue';
import userApi from '@/axios/userApi';
import { ElMessage, ElMessageBox } from 'element-plus';


const edituser = ref({})
const ulist = defineModel('ulist')
const pageInfo = defineModel('pageInfo')
const emit = defineEmits(['load'])

function handlePageChange() {
    emit('load')
}

function handleUpdate(id,isBanned) {
    let m = isBanned==0?'解禁':'封禁';
    ElMessageBox.confirm(
        '你确定要【'+m+'】这个用户么？',
        '提醒',
        {
            confirmButtonText:'确定',
            cancelButtonText:'取消',
            type:'warning'
        }
    ).then(()=>{
        edituser.value.id = id
        edituser.value.isBanned = isBanned
        userApi.update(edituser.value).then(result=>{
            if(result.code == 200) {
                ElMessage.success('修改成功')
                emit('load')
            }
            else {
                ElMessage.error('修改失败 请重试')
            }
        })
    })
}

</script>

<template>
    <el-table :data="ulist" style="width: 100%" border>
        <el-table-column prop="id" label="ID" class="style"></el-table-column>
        <el-table-column prop="id" label="头像"></el-table-column>
        <el-table-column prop="nickName" label="昵称"></el-table-column>
        <el-table-column prop="phone" label="电话"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="province" label="省"></el-table-column>
        <el-table-column prop="city" label="市"></el-table-column>
        <el-table-column prop="area" label="区"></el-table-column>
        <el-table-column prop="detailAddr" label="详细住址"></el-table-column>
        <el-table-column prop="isBanned" label="状态"
         :formatter="(row)=>['正常','封禁'].at(row.isBanned)"></el-table-column>
        <el-table-column label="操作">
            <template  #default="scope">
                <el-button type="warning" v-if="scope.row.isBanned==0" @click="handleUpdate(scope.row.id,1)">封禁</el-button>
                <el-button type="success" v-else @click="handleUpdate(scope.row.id,0)">解禁</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next,sizes" 
    v-model:current-page ="pageInfo.page"
    :total="pageInfo.total"
    :pager-count="11"
    :page-sizes="[1,2,10,20]"
    @change="handlePageChange"/>
</template>

<style scoped>

</style>