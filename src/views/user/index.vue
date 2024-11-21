<script setup>
import api from '@/axios';
import categoryApi from '@/axios/categoryApi';
import router from '@/router';
import { ref } from 'vue';


const banners = ref([
    'lb1.webp',
    'lb2.webp',
    'lb3.jpg',
    'lb4.webp',
])
const categories = ref([])

function loadCategory() {
    categoryApi.search({parentId:0}).then((result) => {
        categories.value = result.data;
        /* console.log(categories.value) */
    })
}
loadCategory();


function handleClick(id) {
    router.push({path:'/detail',query:{id}})
}
</script>

<template>
    <!--    轮播图   -->
    <el-carousel height="469px" motion-blur>
      <el-carousel-item v-for="item in banners" :key="item">
        <el-image  :src="`${api.skuURL}upload/${item}`" :fit="fit"></el-image>
      </el-carousel-item>
    </el-carousel>
    <!-- 广告位 -->
    <div class="ad">
        <ul>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
        </ul>
    </div>

    <!-- 首页的推荐商品  分类-商品 -->
    <div class="recomend" v-for="category in categories">
        <div class="title">{{ category.category1Name }}</div>
        <ul class="goods-list">
            <li v-for="s in category.skuList" @click="handleClick(s.id)">
                <div class="img"><el-image :src="`${api.skuURL}upload/${s.picList[0].url}`"></el-image></div>
                <div class="name">{{ s.name }}</div>
                <div class="decp">{{ s.description }}</div>
                <div class="price">￥{{ s.price }}起</div>
            </li>
        </ul>
    </div>
</template>

<style scoped>
/* Clear floats */
.recomend::after {
    content: '';
    display: block;
    clear: both;
}

.ad li {
    width: 291px;
    float: left;
    margin-right: 12px;
}
.ad li:nth-child(4n) {
    margin-right: 0px;
}

.goods-list{
    height: 350px;
    padding: 0px;
    margin-top: 10px;
}
.goods-list li {
    width: 224px;
    height: 350px;
    float: left;
    margin-right: 20px;
    margin-top: 10px;
    background-color: #fff;
}
.goods-list li:hover{
    box-shadow: 0 0 8px #000;
}
.goods-list li:nth-child(5n) {
    margin-right: 0px;
}
.goods-list .img {
    margin-top: 10px;
}
.goods-list .img .el-image {
    width: 200px;
    height: 200px;
}
.recomend {
    margin-bottom: 30px;
    background-color: rgb(241, 243, 245);
}
.title{
    font-size: 20px;
    padding-left: 20px;
    padding-top: 10px;
    font-weight: 800;
}
.img{
    display: flex;
    justify-content: center;
    align-items: center;
}
.name{
    font-size: 20px;
    overflow: hidden;
    -webkit-line-clamp: 1;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
    padding: 10px 10px 0px 10px;
    text-align: center;
}
.decp{
    overflow: hidden;
    -webkit-line-clamp: 2;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
    padding: 10px 10px 0px 10px;
    text-align: center;
}
.price{
    padding: 10px 10px 0px 10px;
    text-align: center;
}
</style>