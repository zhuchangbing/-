<script setup>
import { ref } from 'vue';
import UserList from '../../components/admin/userList.vue';
import UserSearch from '../../components/admin/userSearch.vue';
import userApi from '@/axios/userApi';


const searchUserVo = ref({})
const pageInfo = ref({page:1,size:10})
const ulist = ref([])


/***  刷新一下用户列表 */
function handleLoad() {
    getUserList();
}


function getUserList() {
    userApi.searchUser(pageInfo.value,searchUserVo.value).then(result=>{
        ulist.value = result.data.list
        pageInfo.value.total = result.data.total
        console.log(ulist.value)
    })
}

getUserList();
</script>

<template>

    <UserSearch
    v-model:searchUserVo="searchUserVo"
    v-on:searchLoad="handleLoad"></UserSearch>
    <UserList
    v-model:ulist="ulist"
    v-model:pageInfo="pageInfo"
    v-on:load="handleLoad"></UserList>
</template>

<style scoped> 

</style>