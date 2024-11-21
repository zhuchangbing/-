<script setup>
import api from '@/axios';
import { orderApi } from '@/axios/orderApi';
import { ref } from 'vue';


const porders = ref([])
function getporders(){
    orderApi.getporders().then(result => {
        porders.value = result.data
        console.log(porders.value)
    })
}
getporders()




/***     订单信息弹窗表单    ***/
const selectOrder = ref()
const orderinfoVisible = ref(false)

function updateOrderInfo(order) {
    selectOrder.value = order
    orderinfoVisible.value = true;
}

function HandleUpdateOrderInfo(formEl) {
    formEl.validate((valid => {
        if(valid) {
            orderApi.updateNumber(selectOrder.value).then(result => {
                if(result.code == 200) {
                    ElMessage.success("订单状态更新成功")
                }
            })
        }
    }))
    orderinfoVisible.value = false
}


/**       表单逻辑       **/
const form = ref(null)
const rules = {
    trackingNumber:[
        { required: true, message:'请输入快递单号',trigger:'blur'}
    ]

}

</script>

<template>
    处理订单
    <el-dialog title="更新订单状态" v-model="orderinfoVisible" width="800px">
        <div style="margin-bottom: 20px;">

            <el-descriptions class="margin-top" title="订单详情" v-model="selectOrder" :column="1" :size="size" border>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon :style="iconStyle">
                                <user />
                            </el-icon>
                            订单号
                        </div>
                    </template>
                    {{ selectOrder.id }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template #label>
                        <div class="cell-item">
                            <el-icon :style="iconStyle">
                                <user />
                            </el-icon>
                            用户详细信息
                        </div>
                    </template>
                    {{ selectOrder.userDetail }}
                </el-descriptions-item>

                
            </el-descriptions>
            <!-- 发货信息 快递单号 -->
             <el-form  :model="selectOrder" ref="form" label-width="80px":rules="rules"> 
                <el-form-item  prop="trackingNumber" label="快递单号">
                    <el-input v-model="selectOrder.trackingNumber" placeholder="请输入快递单号"></el-input>
                </el-form-item>
             </el-form>
        </div>

        <div slot="footer">
            <el-button @click="orderinfoVisible = false">取 消</el-button>
            <el-button type="primary" @click="HandleUpdateOrderInfo(form)">更 新</el-button>
        </div>
    </el-dialog>
    <div class="user-orders" v-for="o in porders" :class="['b0','b1','b2'][o.status]">
        <div class="order-item">
            <ul class="top-style" :class="{t0:o.status==0,t1:o.status==1,t2:o.status==2}">
                <li :class="{payw0: o.status==0,payw1:o.status==1,payw2:o.status==2}"
                    style="height: 50px;line-height: 50px;">
                    {{ ['用户待支付', '用户已支付请及时发货', '已发货', '已送达'].at(o.status) }}</li>
                <li class="address">{{ o.userDetail }}</li>
                <li class="order-number">{{ o.createTime }} | 订单号：{{ o.id }}</li>
                <li class="order-price">订单金额：<span :class="{payw0: o.status==0,payw1:o.status==1,payw2:o.status==2}">{{
                        o.sku.price }}元</span></li>

            </ul>
        </div>
        <div class="detail">
            <div class="sku-list">
                <el-row>
                    <el-col :span="4">
                        <el-image :src="`${api.skuURL}upload/${o.sku.picList[0].url}`"
                            style="width: 100px;height: 100px;padding: 10px;"></el-image>
                    </el-col>
                    <el-col :span="5" class="name">{{ o.sku.name }}</el-col>
                    <el-col :span="10" class="dscp">{{ o.sku.description }}</el-col>
                    <el-col :span="5" class="price">单价：<span class="price-style">￥{{ o.sku.price }}元</span></el-col>
                </el-row>
                <ul class="rbutton">
                    <li v-if="o.status == 0" style="font-size: 16px; font-weight: 700;">待用户支付</li>
                    <li v-else-if="o.status == 1"><el-button type="primary"
                            @click="updateOrderInfo(o)">更新订单状态</el-button></li>
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