<script setup>
import { ref, watch } from 'vue';
import categoryList from '@/components/admin/categoryList.vue';
import categorySearch from '@/components/admin/categorySearch.vue';
import categoryApi from '@/axios/categoryApi';
import { ElMessage } from 'element-plus';
import categoryEdit from '@/components/admin/categoryEdit.vue';

const editcategory = ref({})
const pageInfo = ref({page:1 , size:10})
const category = ref({})
const categories = ref([])   /* 全部的商品分类父子关系表  用于select-tree*/
const clist = ref([])
function getAllCategory() {
    categoryApi.getallc(pageInfo.value).then(result => {
        categories.value = result.data.list;
        console.log(categories.value)
    })
}

watch(category,async()=>{
    getclist();
})

getAllCategory();

async function getclist() {
    /* categoryApi.getallcc(pageInfo.value).then(result => {
        clist.value = result.data;
        console.log(clist.value)
    }) */
    const s = await categoryApi.getallcc(pageInfo.value,category.value);
    pageInfo.value.total = s.data.total
    console.log(pageInfo.value)
    clist.value = s.data.list
    console.log(clist.value)
}
getclist();


function handleEditReload(){
    categoryApi.updatecategory(editcategory.value).then(result => {
        if(result.code == 200) {
            getclist()
            ElMessage('修改成功')
        }
        else{
            ElMessage(result.message)
        }
    })
}


/******  编辑操作   *****/
const editVisible = ref(false);
/******  添加操作   *****/
const addVisible = ref(false)


function handleReload() {
    getclist();
}
</script>



<template>
    <categorySearch v-model:category = "category" v-model:categories="categories"
    ></categorySearch>
    <categoryAdd v-model:categories="categories"
    v-model:addVisible="addVisible"
    v-on:addLoad="handleEditReload"
    ></categoryAdd>
    <categoryEdit v-model:categories="categories"
    v-model:editcategory="editcategory"
    v-model:editVisible="editVisible"
    v-on:Edit="handleEditReload"
    ></categoryEdit>
    <categoryList v-model:category = "category" v-model:categories="categories" 
    v-model:clist="clist" v-on:EditReload="handleEditReload" 
    v-model:editcategory="editcategory"
    v-model:editVisible="editVisible"
    v-model:pageInfo="pageInfo"
    v-on:reload="handleReload"></categoryList>
</template>

<style scoped>

</style>