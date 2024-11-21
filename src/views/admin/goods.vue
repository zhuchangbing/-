<script setup>
import { ref, watch } from 'vue';
import goodsList from '@/components/admin/goodsList.vue';
import goodsEdit from '@/components/admin/goodsEdit.vue';
import goodsSearch from '@/components/admin/goodsSearch.vue';
import categoryApi from '@/axios/categoryApi';
const searchSkuVo = ref({})
const allGoods = ref([])
const pageInfo = ref({page:1,size:10})
const categories = ref([]) 




function getAllCategory() {
    categoryApi.getallc(pageInfo.value).then(result => {
        categories.value = result.data.list;
        console.log(categories.value)
    })
}                
getAllCategory();

/**  条件查询商品的方法  */
function searchGoods() {
    categoryApi.searchgoods(pageInfo.value,searchSkuVo.value).then(result=>{
        allGoods.value = result.data.list;
        pageInfo.value.total = result.data.total;
        console.log(allGoods.value)
    })
}

searchGoods();    // 加载一下商品
watch(searchSkuVo,async()=>{
    console.log('******************************')
    console.log(searchSkuVo.value)
    searchGoods();
})

function handleLoad() {
    searchGoods();
}

</script>

<template>
    <goodsSearch
    v-model:categories="categories"
    v-model:searchSkuVo="searchSkuVo"></goodsSearch>
    <goodsEdit></goodsEdit>
    <goodsList 
    v-model:allGoods="allGoods"
    v-model:pageInfo="pageInfo"
    v-model:categories="categories"
    v-on:load="handleLoad"></goodsList>
</template>

<style scoped>

</style>