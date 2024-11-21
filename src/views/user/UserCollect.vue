<script setup>
import api from '@/axios';
import { collectApi } from '@/axios/collectApi';
import { ref } from 'vue';
import { Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import router from '@/router';

const collects = ref([])
const checkAll = ref(false)
const oldCollects = ref([])
const loading = ref(true)


function getUS() {
    loading.value = true;
    collectApi.getUserCollects().then(result => {
        collects.value = result.data
        for(const s of collects.value) {
            for(const o of oldCollects.value) {
                if(s.id == o.id && o.checked) {
                    s.checked = o.checked
                    break
                }
            }
        }
        oldCollects.value = collects.value

        num.value = 0
        total.value = 0
        ids.value = []
        for(const s of collects.value) {
            if(s.checked && s.checked == true) {
                num.value ++
                total.value += s.sku.price
                ids.value.push(s.id)
            }
        }
        loading.value = false
    })
}
getUS()

/*****    删除收藏的商品 *****/
function del(skuId) {
    collectApi.delete(skuId).then(result => {
        if(result.code == 200 ) {
            ElMessage.success('删除成功')
            getUS()
        }
        else {
            ElMessage.warning(result.message)
        }
    })
}


/***   处理收藏商品选中的逻辑  ***/
const num = ref(0)
const total = ref(0)
const ids = ref([])

function HandlecheckAll() {
    num.value = 0
    total.value = 0
    ids.value = []
    for(const s of collects.value) {
        s.checked = checkAll.value;
        if(s.checked && s.checked == true) {
            num.value++
            total.value += s.sku.price
            ids.value.push(s.id)
        }
    }
}

function HandleChangeOne(){
    num.value = 0
    total.value = 0
    ids.value = []
    checkAll.value = true;
    for(const s of collects.value) {
        checkAll.value = checkAll.value && s.checked;
        if(s.checked && s.checked == true) {
            num.value++
            total.value += s.sku.price
            ids.value.push(s.id)
        }
    }
}

/****   批量删除收藏商品  ****/
function deleteBatch() {
    if(ids.value.length === 0) {
        ElMessage.warning('当前收藏商品数为零，快去收藏商品吧')
        return
    }
    collectApi.deleteBatch(ids.value).then(result => {
        if(result.code == 200) {
            ElMessage.success('删除成功')
            getUS()     // 重新加载一下
        }
        else {
            ElMessage.error(result.message)
        }
    })
    
}

function deleteAll() {
    ids.value = []
    for(const s of collects.value) {
        ids.value.push(s.id);   //全部加入数组
    }
    if(ids.value.length === 0) {
        ElMessage.warning('当前收藏商品数为零，快去收藏商品吧')
        return
    }
    collectApi.deleteBatch(ids.value).then(result => {
        if(result.code == 200) {
            ElMessage.success('删除成功')
            getUS()
        }
        else {
            ElMessage.error(result.message)
        }
    })
}

const skuIds = ref([])

function handlePuchase() {
    if(ids.value.length == 0) {
        ElMessage.warning('当前未选择任何商品')
        return
    }
    for(const s of collects.value) {
        if(s.checked && s.checked == true) {
            skuIds.value.push(s.skuId)
        }
    }
    console.log(skuIds.value)
    router.push({path:'/corder',query:{ids:skuIds.value}})
}

function ToSkuDetail(id) {
    router.push({path:'/detail',query:{id}})
}

</script>

<template>
    <div v-loading="loading">
    <!--    处理收藏商品为空的逻辑     -->
    <div v-if="loading == false && collects.length == 0" class="empty-condition">
        <div class="empty-log">
            <el-icon class="icon-contain"><el-image src="/src/assets/img/heart-breaked.png" class="heart-icon"></el-image></el-icon>
            <div class="empty-tips">收藏商品为空，快去挑选商品吧！</div>
        </div>
    </div>
    <div class="head" v-else>
        <div style="font-size: 16px; font-weight: 900;margin-top: 10px;">我的收藏</div>
        <el-row class="head-row">
            <el-col :span="4">
                <el-checkbox label="全选" v-model="checkAll" @change="HandlecheckAll"></el-checkbox>
            </el-col>
            <el-col :span="3">商品名</el-col>
            <el-col :span="2">状态</el-col>
            <el-col :span="2">品牌</el-col>
            <el-col :span="7">商品描述</el-col>
            <el-col :span="4">价格</el-col>
            <el-col :span="2">操作</el-col>
        </el-row>

        <el-row class="sku-row" v-for="s in collects" >
            <el-col :span="1">
                <el-checkbox v-model="s.checked" @change="HandleChangeOne"></el-checkbox>
            </el-col>
            <el-col :span="3" @click="ToSkuDetail(s.sku.id)">
                <el-image :src="`${api.skuURL}upload/${s.sku.picList[0].url}`" :fit="contain"
                class="sku-img"></el-image>
            </el-col>
            <el-col :span="3" class="sku-name">{{ s.sku.name }}</el-col>
            <el-col :span="2">{{ ['未售出','已售出'].at(s.sku.isSold) }}</el-col>
            <el-col :span="2">{{ s.sku.categoryName }}</el-col>
            <el-col :span="7" class="sku-dscp">{{ s.sku.description }}</el-col>
            <el-col :span="4" class="sku-price">￥{{ s.sku.price }}元</el-col>
            <el-col :span="2"><el-button circle @click="del(s.skuId)"><el-icon><Delete /></el-icon></el-button></el-col>
        </el-row>

        <el-row class="bottom-row">
            <el-col :span="1">
                <el-checkbox label="全选" v-model="checkAll" @change="HandlecheckAll"></el-checkbox>
            </el-col>
            <el-col :span="3" style="height: 50px;"><el-button text style="height: 50px;padding-bottom: 11px;margin-left: 50px;border: 1px solid rgb(0,0,0,0);" @click="deleteBatch">删除选中</el-button></el-col>
            <el-col :span="3" style="height: 50px;"><el-button text style="height: 50px;padding-bottom: 11px;" @click="deleteAll">清理全部收藏</el-button></el-col>
            <!-- <el-col :span="9" style="text-align: right;">总计<span class="total-number">{{ num }}</span>件商品</el-col>
            <el-col :span="3" style="text-align: right;">共<span class="total-number">￥{{ total }}</span>元</el-col>
            <el-col :span="3" style="height: 50px;"><el-button  class="button-pay" @click="handlePuchase">去支付</el-button></el-col> -->
        </el-row>
    </div>
</div>
</template>

<style scoped>
.sku-row {
    cursor: pointer;
}


.icon-contain {
    width: 100px;
    height: 100px;
}
.empty-tips {
    height: 100px;
    line-height: 100px;
    font-size: 24px;
    position: absolute;
    top: 200px;
    left: 120px;
}
.heart-icon {
    width: 100px;
}
.empty-log{
    width: 600px;
    margin: 0px auto;
    padding-top: 200px;
    height: 400px;
    position: relative;
}
.empty-condition{
    height: 800px;
    width: 100%;
    background-color: rgb(194, 195, 196);
}
.total-number{
    padding: 0 10px;
    color: rgb(245, 108, 108);
    font-size: 18px;
    font-weight: 800;
}
.button-pay{
    height:50px;
    background-color: rgb(245, 108, 108);
    padding: 0 50px;
    position: absolute;
    right: 0;
    border: 1px solid rgb(245, 108, 108);
    border-radius: 0;
    color: #fff;
    font-size: 20px;
    font-weight: bolder;
}
.button-pay:hover {
    box-shadow: 0 0 5px #000;
}
.bottom-row{
    background-color: #fff;
    padding: 0 10px;
    margin-top: 20px;
    height: 50px;
    line-height: 50px;
    position: relative;
    right: 0;
}
.sku-img {
    height: 100px;
    padding: 10px 0;
    width: 100px;
}
.sku-price {
    height: 120px;
    font-size: 18px;
    color: rgb(191, 76, 0);
    padding-left: 30px;
}

.sku-dscp {
    line-height: 30px;
    height: 90px;
    margin: 15px 0;

    overflow: hidden;
    -webkit-line-clamp: 3;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;

}
.sku-name {
    padding: 0 5px;
    overflow: hidden;
    -webkit-line-clamp: 2;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
}
.sku-row{
    height: 120px;
    line-height: 120px;
    background-color: #fff;
    border-bottom: 1px dashed #b0b0b0;
    padding: 0 10px;
}
.head {
    position: relative;
    height: auto;
    margin-bottom: 100px;
}
.head-row{
    height: 50px;
    line-height: 50px;
    background-color: #fff;
    margin-top: 20px;
    margin-bottom: 20px;
    padding: 0 10px;
}

</style>