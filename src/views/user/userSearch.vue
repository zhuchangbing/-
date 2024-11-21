<script setup>
import { watch } from 'vue';
import { ref } from 'vue';
import categoryApi from '@/axios/categoryApi';
import api from '@/axios';
import router from '@/router';
import { onBeforeRouteUpdate, useRoute } from 'vue-router';
import { skuApi } from '@/axios/skuApi';

const props  = defineProps({
    categoryId : String,
});

const key = useRoute().query.key
console.log(key)


const skuList = ref([])
const categories = ref([])
const categories2 = ref([])
const categories3 = ref([])

const selectFirstCategory = ref({})
const selectSecondCategory = ref({})
const selectThreeCategory = ref({})


watch(props, (n, o) => {
    console.log(props)
    loadCategory()
    /* categories.value.forEach(first => {
        if (props.categoryId == first.category1Id) {
            selectFirstCategory.value = first;
            getfirst(props.categoryId)
            return false;
        }
        else {
            categories2.value.forEach(second => {
                if (props.categoryId == second.category1Id) {
                    selectFirstCategory.value = first;
                    selectSecondCategory.value = second;
                    getsecond(props.categoryId)
                    return false;
                }
                else {
                    categories3.value.forEach(third => {
                        if (props.categoryId == third.category1Id) {
                            selectFirstCategory.value = first;
                            selectSecondCategory.value = second;
                            selectThreeCategory.value = third;
                            getthird(props.categoryId)
                            return false;
                        }
                    })
                }
            })
        }
    }) */
})

function loadCategory() {
    categoryApi.search({parentId:0}).then((result) => {       // 异步 
        categories.value = result.data//加载一级分类
        console.log(categories.value)
        getThreeCategory(); //加载三级分类
        getTwoCategory();//加载二级分类
        if(Object.keys(props.categoryId).length === 0) {
            getall()
            return false;    
        }
        //loadsku(categories.value)
        categories.value.forEach(first => {
        if (props.categoryId == first.category1Id) {
            selectFirstCategory.value = first;
            getfirst(props.categoryId)
            return false;
        }
        else {
            categories2.value.forEach(second => {
                if (props.categoryId == second.category1Id) {
                    selectFirstCategory.value = first;
                    selectSecondCategory.value = second;
                    getsecond(props.categoryId)
                    return false;
                }
                else {
                    categories3.value.forEach(third => {
                        if (props.categoryId == third.category1Id) {
                            selectFirstCategory.value = first;
                            selectSecondCategory.value = second;
                            selectThreeCategory.value = third;
                            getthird(props.categoryId)
                            return false;
                        }
                    })
                }
            })
        }
    })
    }) 
}
loadCategory();  //加载一级分类

/**
 * 加载三级分类
 */
function getThreeCategory(category2Id){
    categoryApi.getThreeCategory(category2Id).then((result) => {
        categories3.value  = result.data;
        console.log(categories3.value)
    })
}


/**
 * 获取二级分类商品
 */
function getTwoCategory(category1Id){
    categoryApi.getTwoCategory(category1Id).then((result) => {
        categories2.value = result.data;
        console.log(categories2.value);
    })
}


/**
 * 选择一级分类
 */

 function selectFirst(category) {
    selectFirstCategory.value = category;
    /*console.log(selectFirstCategory.value)*/
    // 清除选择的二级三级分类
    selectSecondCategory.value = {};
    selectThreeCategory.value = {};
    if(Object.keys(category).length === 0){
        /*loadsku()*/
        getall();
    }
    else{
        console.log(category)
        /*loadsku1(category)*/
        getfirst(category.category1Id)
    }
        
}



function selectSecond(category) {
    selectSecondCategory.value = category;
    
    // 根据选择的二级分类更新一级分类
    if (category.category1Name) {
        for (const c1 of categories.value) {
            if (c1.category1Name === category.category1Name) {
                selectFirstCategory.value = c1;
                break;
            }
        }
    }
    // 清除选择的三级分类
    if(Object.keys(category).length === 0){
        /*loadsku1(selectFirstCategory.value)*/
        if(Object.keys(selectFirstCategory.value).length === 0){
            getall();
        }
        else
            getfirst(selectFirstCategory.value.category1Id)
    }
    else{
        console.log(category)
        /*loadsku1(category)*/
        getsecond(category.category2Id)
    }
        
    selectThreeCategory.value = {};
}

function selectThree(category) {
    selectThreeCategory.value = category;
    
    /* console.log(category) */
    // 根据选择的三级分类更新一级分类和二级分类
    if (category.category1Name) {
        for (const c1 of categories.value) {
            if (c1.category1Name === category.category1Name) {
                selectFirstCategory.value = c1;
                break;
            }
        }
    }
    if (category.category2Name) {
        for (const c2 of categories2.value) {
            if (c2.category2Name === category.category2Name) {
                selectSecondCategory.value = c2;
                break;
            }
        }
    }
    if(Object.keys(category).length === 0){
        if(Object.keys(selectSecondCategory.value).length === 0){
            if(Object.keys(selectFirstCategory.value).length === 0){
                getall();
            }
            else{
                getfirst(selectFirstCategory.value.category1Id)
            }
        }
        else{
            getsecond(selectSecondCategory.value.category2Id)
        }
    }
        /*loadsku1(selectSecondCategory.value)*/
        
    else
        getthird(category.category3Id)
}



/**
 * 商品显示的模块
 */

/* function loadsku(){
    for(const c of categories.value) {
        skuList.value = [...skuList.value,...c.skuList]
    }
}
function loadsku1(category) {
    skuList.value = category.skuList;
} */





const pageInfo = ref({page:1,size:10,total:0,name:key})


/* 搜索 */
function search() {

}

const route = useRoute();
watch(
    () => route.query.key, // 监听搜索关键字
    (newKey) => {
        if (newKey) {
            getall1(newKey); // 根据新关键字加载商品
        }
    }
);

function getall1(key){
    pageInfo.value.name = key
    return skuApi.search(pageInfo.value).then(result => {
        skuList.value = result.data.list;
        pageInfo.value.total = result.data.total;
    })
}

/**
 * 获得各类商品的函数
 */
/**一级分类 */
function getfirst(category1Id){
    categoryApi.getfirst(pageInfo.value,category1Id).then(result => {
        skuList.value = result.data.list;
        pageInfo.value.total = result.data.total;
    })
}
function getsecond(category2Id){
    categoryApi.getsecond(pageInfo.value,category2Id).then(result => {
        skuList.value = result.data.list;
        pageInfo.value.total = result.data.total;
    })
}
function getthird(category3Id){
    categoryApi.getthird(pageInfo.value,category3Id).then(result => {
        skuList.value = result.data.list;
        pageInfo.value.total = result.data.total;
    })
}
function getall(){
    return categoryApi.getall(pageInfo.value).then(result => {
        skuList.value = result.data.list;
        pageInfo.value.total = result.data.total;
    })
}


function handlePageChange(){    /* 如何更新分页查询结果？ */
    if(Object.keys(selectFirstCategory.value).length === 0) {
        getall()
        return false
    }else{
        if(Object.keys(selectSecondCategory.value).length === 0){
            getfirst(selectFirstCategory.value.category1Id);
            return false
        }
        else {
            if(Object.keys(selectThreeCategory.value).length === 0){
                getsecond(selectSecondCategory.value.category2Id);
                return false
            }
            else{
                getthird(selectThreeCategory.value.category3Id);
                return false
            }
        }

    }
    
}

function gotoDetail(id){
    router.push({path:'/detail', query:{id:id}})
}

</script>

<template>
    <div class="categories">
        <ul>
            <li>一级分类</li>
            <li @click="selectFirst({})" :class="{active: !selectFirstCategory.category1Id}">全部</li>
            <li v-for="category in categories" :key="category.category1Id" @click="selectFirst(category)"
                :class="{active: category.category1Id === selectFirstCategory.category1Id}">
                {{ category.category1Name }}
            </li>
        </ul>
        <ul>
            <li>二级分类</li>
            <li @click="selectSecond({})" :class="{active: !selectSecondCategory.category2Id}">全部</li>
            <li v-for="c2 in categories2" :key="c2.category2Id" @click="selectSecond(c2)"
                :class="{active: c2.category2Id === selectSecondCategory.category2Id}">
                <span
                    v-if="Object.keys(selectFirstCategory).length === 0 || selectFirstCategory.category1Name===c2?.category1Name">{{
                    c2.category2Name }}</span>
            </li>
        </ul>
        <ul>
            <li>三级分类</li>
            <li @click="selectThree({})" :class="{active: !selectThreeCategory.category3Id}">全部</li>
            <li v-for="c3 in categories3" :key="c3.category3Id" @click="selectThree(c3)"
                :class="{active: c3.category3Id === selectThreeCategory.category3Id}">
                <span
                    v-if="(Object.keys(selectFirstCategory).length === 0 || selectFirstCategory.category1Name===c3?.category1Name)
                && (Object.keys(selectSecondCategory).length === 0 || selectSecondCategory.category2Name===c3?.category2Name)">
                {{c3.category3Name }}</span>
            </li>
        </ul>
    </div>

    <div class="container-bg">
        <div class="center">
            <ul class="goods-list">
                    <li v-for="s in skuList" @click="gotoDetail(s.id)">
                        <div class="img"><el-image :src="`${api.skuURL}upload/${s.picList[0].url}`"
                                :fit="contain"></el-image></div>
                        <div class="name">{{ s.name }}</div>
                        <div class="decp">{{ s.description }}</div>
                        <div class="price">￥{{ s.price }}起</div>
                    </li>
            </ul>
        </div>
    </div>
    <div class="page">
        <el-pagination layout="prev, pager, next,sizes" v-model:current-page="pageInfo.page"
            v-model:page-size="pageInfo.size" :total="pageInfo.total" :pager-count="11" :page-sizes="[1, 2, 10, 20]"
            @change="handlePageChange" class="pageInfo" />
    </div>


</template>




<style scoped>
.page{
    height: 50px;
}
.categories {
    height: 120px; /* 3级分类 * 40px */
}
.categories ul{
    height: 40px;
    margin: 0;
}

.categories li {
    float: left;
    margin: 0px 30px;
    line-height: 40px;
    cursor: pointer;
}
.active {
    color: rgb(65, 184, 131);
}

.container-bg{
    width: 100%;
    height: auto;
    background-color: rgb(241, 243, 245);
}
.center{
    width: 1200px;
    margin: 0 auto;
    height: auto;
}

.goods-list{
    height: 400px;
    padding: 0;
    
}
.goods-list li {
    width: 291px;
    height: 400px;
    float: left;
    margin-right: 12px;
    background-color: #fff;
    margin-bottom: 10px;
}
.container-bg::after{
    content: '';
    display: block;
    clear: both;
}

.goods-list li:hover{
    box-shadow: 0 0 8px #000;
}
.goods-list li:nth-child(4n) {
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
    line-height: 40px;
    margin-top: 20px;
    font-size: 20px;
    height: 40px;
    overflow: hidden;
    -webkit-line-clamp: 1;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
    padding: 0 10px 0px 10px;
    text-align: center;
}
.decp{
    line-height: 20px;
    height: 40px;
    overflow: hidden;
    -webkit-line-clamp: 2;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
   padding: 0px 10px 0px 10px;
    text-align: center;
    font-size: 15px;
}
.price{
    line-height: 40px;
    font-size: 18px;
    height: 40px;
    padding: 0px 10px 0px 10px;
    text-align: center;
}
</style>