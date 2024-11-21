<script setup>
import { ref } from 'vue';
import adminAdd from '@/components/admin/adminAdd.vue';
import adminEdit from '@/components/admin/adminEdit.vue';
import adminList from '@/components/admin/adminList.vue';
import AdminSearch from '@/components/admin/adminSearch.vue';
import adminApi from '@/axios/adminApi';

const editadmin = ref({})
const alist = ref([])
const searchA = ref({})
const pageInfo = ref({page:1,size:10})
const editVisible = ref(false)

function searchAdmin() {
    adminApi.searchadmin(pageInfo.value,searchA.value).then(result => {
        alist.value = result.data.list
        pageInfo.value.total = result.data.total
        console.log(alist.value)
    })
}
searchAdmin();

function handleLoad() {
    searchAdmin();
}

function handleEdit() {
    editVisible.value = true;
}


const addVisible = ref(false)

function handleAddAdmin () {
    addVisible.value = true;
}

</script>

<template>
    <AdminSearch
    v-model:searchA="searchA"
    v-on:load="handleLoad"></AdminSearch>
    <el-button type="primary" @click="handleAddAdmin">添加</el-button>
    <adminAdd
    v-model:addVisible="addVisible"
    v-on:addLoad="handleLoad"></adminAdd>
    <adminEdit
    v-model:editVisible="editVisible"
    v-model:editadmin="editadmin"
    v-on:editLoad="handleLoad"></adminEdit>
    <adminList
    v-model:alist="alist"
    v-model:pageInfo="pageInfo"
    v-model:editVisible="editVisible"
    v-model:editadmin="editadmin"
    v-on:pageLoad="handleLoad"
    v-on:edit="handleEdit"></adminList>
</template>

<style scoped>

</style>