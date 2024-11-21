<script setup>
import { ref } from 'vue';
import { Select } from '@element-plus/icons-vue';
import { useRoute } from 'vue-router';
import VueCountdown from '@chenfengyuan/vue-countdown';
import { orderApi } from '@/axios/orderApi';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';
import userApi from '@/axios/userApi';
import api from '@/axios';

const orderId = useRoute().query.orderId;
const order = ref({ sku: { name: '' } })
const time = ref(0)
const user = useUserStore().user


function getOrderDetail() {
    console.log(orderId)
    orderApi.getOrderDetailById(orderId).then(result => {
        order.value = result.data
        console.log(order.value)
        let createTime = order.value.createTime
        CountTime(createTime)
    })
}
getOrderDetail()


function CountTime(createTime) {
    let nowDate = new Date()
    let createDate = new Date(createTime)
    time.value = 15 * 60 * 1000 -
        (nowDate.getTime() - createDate.getTime())
}

function handlePay() {
    console.log(user)
    disabled.value = true
    let uorder = { ...order.value }
    uorder.status = 1;
    orderApi.pay(uorder).then(result => {
        if (result.code == 200) {
            ElMessage.success('支付成功')
            // 刷新页面
            getuserbyid()
            getOrderDetail()
        }
        else {
            order.value.status = 0;
            ElMessage.error(result.message)
        }
        disabled.value = false
    })
}
/* <ul v-for="d in order.orderDetails">
                            <li>{{ d.sku.name }}</li>
                            <li>{{ d.sku.description }}</li>
                            <li><span class="price">￥{{ d.sku.price }}元</span> | {{ ['不包邮','包邮'].at(d.sku.isFree) }}</li>
                        </ul> */
function getuserbyid() {
    userApi.getuserbyid(user.id).then(result => {
        user = result.data
        useUserStore().changeUser(user)
    })
}


/****  支付按钮   ****/
const disabled = ref(false)


</script>

<template>
    <div class="payInfo" v-if="order.status == 0">
        <el-row>
            <el-col :span="3">
                <el-icon class="dui">
                    <Select />
                </el-icon>
            </el-col>
            <el-col :span="17">
                <div style="font-size: 24px;color: #333;line-height: 40px;">
                    订单提交成功！去付款咯~
                </div>
                <div>订单将在
                    <vue-countdown :time="time">
                        <template #default="{ days, hours, minutes, seconds }">
                            {{ minutes }} 分钟 {{ seconds }}秒
                        </template>
                    </vue-countdown> 后自动取消，请及时支付
                </div>
            </el-col>
        </el-row>
    </div>


    <div class="payInfo" v-else-if="order.status == 1">
        <el-row>
            <el-col :span="3">
                <el-icon class="dui">
                    <Select />
                </el-icon>
            </el-col>
            <el-col :span="17">
                <div style="font-size: 24px;color: #333;line-height: 40px;">
                    订单已经付款成功了！等待发货咯~
                </div>
            </el-col>
        </el-row>
    </div>

    <div class="order-detail">
        <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item title="订单详情" name="1">
                <div class="order-detail-info">
                    <div class="order-id">订单号：{{ order.id }}</div>
                    <div class="addr">收货信息：{{ order.userDetail }}</div>
                    <div class="skus">
                        <ul>
                            <li>{{ order.sku.name }}</li>
                            <li>{{ order.sku.description }}</li>
                            <li><span class="price">￥{{ order.sku.price }}元</span> | {{
                                ['不包邮','包邮'].at(order.sku.isFree) }}
                            </li>
                        </ul>
                    </div>
                </div>
            </el-collapse-item>
        </el-collapse>
    </div>

    <div class="pay-way" v-if="order.status == 0">
        <div style="margin: 5px;font-size: 16px;color: #b0b0b0;">请选择付款方式</div>
        <div class="ali">
            <ul>
                <!-- 通过支付宝的方式支付 -->
                <a :href="`${api.orderURL}alipay/pay?orderId=${orderId}`">
                    <li><el-image src="/src/assets/img/alipay1.png" style="height: 70px;margin: 5px 0;"
                            :fit="contain"></el-image>
                    </li>
                </a>
                <a>
                    <li>
                        <el-image src="/src/assets/img/wechatpay.png" style="height: 70px;margin: 5px 0;"
                            :fit="contain"></el-image>
                    </li>
                </a>
            </ul>
        </div>
        <div style="margin: 50px 5px 5px 5px;font-size: 16px;color: #b0b0b0;">yigou账户付款</div>
        <div>余额：<span class="account">￥{{ user.account }}元</span></div>
        <el-input v-model="order.payPassword" placeholder="请输入支付密码" style="width: 200px;"></el-input>
        <el-button type="primary" style="margin-left: 20px;" @click="handlePay" 
        :disabled="disabled">支付</el-button>
    </div>




</template>

<style scoped>
.account {
    font-size: 20px;
    font-weight: bold;
    color: rgb(233, 66, 66);
}


.ali li {
    width: 200px;
    height: 75px;
    float: left;
    border: 3px solid #333;
    margin-right: 20px;
    line-height: 120px;
    padding: 5px 10px;
}

.ali li:hover {
    border: 3px solid #83c44e;
}

.ali::after {
    content: '';
    display: block;
    clear: both;
}

.price {
    color: rgb(245, 108, 108);
    font-size: 16px;
}

.skus ul {
    width: 1100px;
    float: left;
    border: 1px solid #666;
    margin: 10px auto;
}

.order-detail {
    padding-left: 20px;
    width: 100%;
    background-color: #fff;
}

.payInfo {
    margin: 20px;
}

.dui {
    border: 4px solid #83c44e;
    width: 80px;
    height: 80px;
    line-height: 80px;
    border-radius: 50px;
    font-size: 48px;
    color: #83c44e;
}
</style>
