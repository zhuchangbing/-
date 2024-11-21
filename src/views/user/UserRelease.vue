<script setup>
import api from '@/axios';
import { skuApi } from '@/axios/skuApi';
import { ref } from 'vue';
import { Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import UserEditReleaseSku from '@/components/user/UserEditReleaseSku.vue';
import router from '@/router';

function handleLoad() {
    getPUserSkus()
}

const pskus = ref([])
function getPUserSkus() {
    skuApi.getPuserSkus().then(result => {
        pskus.value = result.data
        console.log(pskus.value)
    })
}
getPUserSkus()

// 删除商品
function handleDelete(id) {
    ElMessageBox.confirm(
        '你确定要下架这一个物品吗？',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        skuApi.deleteSku(id).then(result => {
            if(result.code == 200) {
                ElMessage.success('商品下架成功')
            }
            else {
                ElMessage.error(result.message)
            }
        })
    })
}


/***    用户修改商品信息 */
const editVisible = ref(false)
const editSku = ref({})

function handleEdit(sku) {
    editVisible.value = true
    editSku.value = {...sku}
}

function ToMyOrderPage() {
    router.push('horder')
}

</script>

<template>
    

    <UserEditReleaseSku
    v-model:editVisible="editVisible"
    v-model:editSku="editSku"
    v-on:load="handleLoad"></UserEditReleaseSku>
    <div class="container">
        <div class="title">我发布的商品😀</div>
        <div class="skuHeader">
            <el-row class="head-row">
                <el-col :span="3">商品</el-col>
                <el-col :span="3">状态</el-col>
                <el-col :span="4">品牌</el-col>
                <el-col :span="7">商品描述</el-col>
                <el-col :span="4">价格</el-col>
                <el-col :span="2">操作</el-col>
            </el-row>
        </div>

        <div v-for="s in pskus" class="sku" :class="{b0:s.isSold==0,b1:s.isSold==1}">
            <el-row>
                <el-col :span="3">
                    <el-image style="width: 100px; height: 100px;margin: 5px;" :src="`${api.skuURL}upload/${s.picList[0].url}`"
                        :fit="contain"></el-image>
                </el-col>
                <el-col :span="3" class="name">{{ ['未售出','已支付'].at(s.isSold) }}</el-col>
                <el-col :span="4" class="name">{{ s.name }}</el-col>
                <el-col :span="5" class="dscp">{{ s.description }}</el-col>
                <el-col :span="4" class="price">￥{{ s.price }}元</el-col>
                <el-col :span="4" class="rbutton">
                    <el-button type="primary" @click="handleEdit(s)"  v-if="s.isSold==0">编辑</el-button>
                    <el-button type="success" v-else @click="ToMyOrderPage">去更新订单信息</el-button>
                </el-col>
                
                <el-col :span="1" class="rbutton">
                    <el-button circle @click="handleDelete(s.id)"><el-icon><Delete /></el-icon></el-button>
                </el-col>
            </el-row>
        </div>
    </div>

</template>

<style scoped>
.rbutton{
    height: 40px;
    line-height: 40px;
    margin: auto 0;
}
.title {
    margin: 20px;
    font-size: 20px;
    font-weight: bolder;
}
.b0 {
    border: 1px solid rgb(245, 108, 108);
}

.b1 {
    border: 1px solid #83c44e;
}

.price {
    font-size: 20px;
    color: rgb(245, 108, 108);
    height: 20px;
    margin: auto 0;
}
.dscp {
    height: 60px;
    margin: auto 0;
    line-height: 20px;
    overflow: hidden;
    -webkit-line-clamp: 3;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
}
.sku {
    background-color: #fefbf6;
    margin: 20px;
}
.skuHeader{
    height: 30px;
    background-color: #fff;
    margin: 0 20px;
}
.name {
    height: 16px;
    margin: auto 0;
    line-height: 16px;
    font-size: 16px;
    overflow: hidden;
    -webkit-line-clamp: 1;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
}
</style>