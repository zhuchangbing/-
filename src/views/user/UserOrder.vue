<script setup>
import api from '@/axios';
import { orderApi } from '@/axios/orderApi';
import router from '@/router';
import { styleType } from 'element-plus/es/components/table-v2/src/common';
import { ref } from 'vue';


const orders = ref([])

function getUserOrders() {
    orderApi.getUserAllOrders().then(result => {
        orders.value = result.data;
        console.log(orders.value)
        /* for(const o of orders.value) {
            let total = 0;
            for(const s of o.orderDetails) {
                total += s.sku.price
            }
            o.total = total;
        } */
        
    })
}
getUserOrders()

function handleToOrderDetail(id){
    router.push({path:'/orderpay',query:{orderId:id}})
}

</script>

<template>

    

    我的订单
    <div class="user-orders" v-for="o in orders" :class="['b0','b1','b2'][o.status]">
        <div class="order-item">
            <ul class="top-style" :class="{t0:o.status==0,t1:o.status==1,t2:o.status==2}">
                <li :class="{payw0: o.status==0,payw1:o.status==1,payw2:o.status==2}" style="height: 50px;line-height: 50px;">
                   {{ ['待支付', '待发货', '已发货', '已送达','已签收','申请退货/换货中','已退款'
                    ,'订单自动取消'
                   ].at(o.status) }}</li>
                <li class="address">{{ o.userDetail }}</li>
                    <li class="order-number">{{ o.createTime }} | 订单号：{{ o.id }}</li>
                <li class="order-price">订单金额：<span :class="{payw0: o.status==0,payw1:o.status==1,payw2:o.status==2}"
                    >{{ o.sku.price }}元</span></li>
                
            </ul>
        </div>
        <div class="detail">
            <div class="sku-list">
                <el-row>
                    <el-col :span="4">
                        <el-image :src="`${api.skuURL}upload/${o.sku.picList[0].url}`" 
                        style="width: 100px;height: 100px;padding: 10px;"
                        ></el-image>
                    </el-col>
                    <el-col :span="5" class="name">{{ o.sku.name }}</el-col>
                    <el-col :span="10" class="dscp">{{ o.sku.description }}</el-col>
                    <el-col :span="5" class="price">单价：<span class="price-style">￥{{ o.sku.price }}元</span></el-col>
                </el-row>
                <ul class="rbutton">
                    <li v-if="o.status == 0"><el-button type="primary" style="width: 100px;" 
                        @click="handleToOrderDetail(o.id)">立即支付</el-button></li>
                    <li v-else-if="o.status == 1"><el-button type="warning" style="width: 100px;" >申请退款</el-button></li>
                    <li v-else-if="o.status == 2"><el-button type="warning" style="width: 100px;">确认收货</el-button></li>
                    <li><el-button style="width: 100px;" @click="handleToOrderDetail(o.id)">订单详情</el-button></li>
                    <li><el-button style="width: 100px;">联系客服</el-button></li>
                </ul>
            </div>
        </div>

    </div>

</template>

<style scoped>
.price-style{
    color: rgb(245, 108, 108);
    font-size: 20px;
    font-weight: bolder;
}
.price {
    padding-right: 30px;
    text-align: right;
    height: 100px;
    line-height: 150px;
}
.dscp{
    margin: auto 0;
    height: 80px;
    line-height: 20px;
    overflow: hidden;
    -webkit-line-clamp: 4;
	text-overflow: ellipsis;
	display: -webkit-box;
	-webkit-box-orient: vertical;
}
.name {
    line-height: 100px;
}
.detail{
    position: relative;
}
.rbutton{
    position: absolute;
    top: -10px;
    right: 30px;
}
.rbutton  li{
    margin-bottom: 9px;
}
.sku-list {
    width: 900px;
    margin: 20px 30px;
    background-color: #f5f5f5;
}
.order-price {
    width: 500px;
    text-align: right;
    position: absolute;
    top: 60px;
    right: 30px;
}

.address{
    font-size: 16px;
}
.b0 {
    border: 1px solid rgb(245, 108, 108);
}
.b1 {
    border: 1px solid #83c44e;
}
.b2 {
    border: 1px solid #39bbef;
}

.top-style li {
    height: 35px;
    line-height: 35px;
}
.payw0{
    font-size: 26px;
    line-height: 26px;
    color: rgb(245, 108, 108);
}
.payw1{
    font-size: 26px;
    line-height: 26px;
    color: #83c44e;
}
.payw2{
    font-size: 26px;
    line-height: 26px;
    color: #39bbef;
}
.order-total {
    color: rgb(245, 108, 108);
    font-size: 26px;
    font-weight: 600;
}

.order-number{
    font-size: 16px;
}
.top-style {
    margin: 0;
    padding: 5px 20px;
    position: relative;
    background-color: #fefbf6;
}
.t0 {
    border-bottom: 1px dashed rgb(245, 108, 108);
}

.t1 {
    border-bottom: 1px dashed #83c44e;
}
.t2 {
    border-bottom: 1px dashed #39bbef;
}
.user-orders {
    height: auto;
    
    margin: 10px 0 20px;
}
</style>