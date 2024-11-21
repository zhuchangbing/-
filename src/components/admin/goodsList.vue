<script setup>
import api from '@/axios';
import { ref } from 'vue';

const emit = defineEmits(['load'])

const allGoods = defineModel('allGoods')
const pageInfo = defineModel('pageInfo')
const categories = defineModel('categories')

function handlePageChange() {
    emit('load')
}


</script>

<template>
    <el-table :data="allGoods" border>
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="name" label="商品名"></el-table-column>
        <el-table-column label="图片">
            <template #default="scope" >
                <span v-for="p in scope.row.picList">
                    <el-image :src="`${api.skuURL}upload/${p.url}`" :fit="fitd"></el-image>
                </span>
            </template>
        </el-table-column>
        <el-table-column prop="price" label="价格"></el-table-column>
        <el-table-column prop="isFree" label="是否包邮" 
        :formatter="(row)=>['不包邮','包邮'].at(row.isFree)"></el-table-column>
        <el-table-column prop="freshLevel" label="商品崭新程度" 
        :formatter="(row)=>['全新','几乎全新','轻微使用痕迹','有明显使用痕迹'].at(row.freshLevel)"></el-table-column>
        <el-table-column prop="isSold" label="是否卖出" :formatter="(row)=>['未卖出','已卖出'].at(row.idSold)"></el-table-column>
        <el-table-column prop="categoryName" label="所属分类名"></el-table-column>
        <el-table-column prop="score" label="评分"></el-table-column>
        <el-table-column label="操作">
            <template #default="scoped">
                <el-button type="primary" @click="edit(scoped.row)" v-if="scoped.row.isApproved == 0">审核</el-button>
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