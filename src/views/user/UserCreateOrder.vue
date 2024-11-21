<script setup>
import { useRoute } from 'vue-router';
import { addrApi } from '@/axios/Addr';
import UserEditAddr from '@/components/user/UserEditAddr.vue';
import { ref } from 'vue';
import { Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import categoryApi from '@/axios/categoryApi';
import api from '@/axios';
import { orderApi } from '@/axios/orderApi';
import router from '@/router';

const addrs = ref([])
const selAddr = ref({})
const selAddrId = ref(0)


function getUserAddress() {
    addrApi.getUserAddress().then(result => {
        addrs.value = result.data     //选中的地址信息
        selAddr.value = addrs?.value[0];  // 起始选中的是默认地址
        selAddrId.value = addrs?.value[0]?.id; // 选中的地址Id
        console.log(addrs.value)
    })

}
getUserAddress()


function changeAddr(a) {
    selAddrId.value = a.id;
    selAddr.value = a;
}


/***     编辑功能    ***/

const addrEditVisible = ref(false)
const editAddr = ref({})


function handleLoad() {
    getUserAddress()
}


function handleEditAddr(addr) {
    addrEditVisible.value = true;
    editAddr.value = { ...addr }
}



/***    添加逻辑 ****/
const addNewAddrVisible = ref(false)

function handleAdd() {
    addNewAddrVisible.value = true;
}

/*** 删除逻辑  ***/
function handleDelete(id) {
    addrApi.delete(id).then(result => {
        if (result.code == 200) {
            ElMessage.success('删除成功')
            getUserAddress()
        }
        else {
            ElMessage.error(result.message)
        }
    })
}

/**       修改默认地址      **/
function handleDefault(a) {
    for (const s of addrs.value) {
        if (s.isSelected == 1) {
            if (s.id == a.id) {
                ElMessage('此地址已是默认地址')
                return
            }
            s.isSelected = 0;
            a.isSelected = 1;
            /** 数据库上修改默认地址 */
            addrApi.updateDefaultAddr(a.id).then(result => {
                if (result.code == 200) {
                    ElMessage.success('修改默认地址成功')
                }
                else {
                    ElMessage.error(result.message)
                }
            })
            return
        }
    }
}

/***    订单商品显示   */
const skus = ref([])
const sku = ref({})
const skuId = useRoute().query.skuId;
/* const ids = useRoute().query.ids */
const total = ref(0)

function getOneSku() {
    /*     if (skuId) { */
    categoryApi.getdetail(skuId).then(result => {
        sku.value = result.data
        console.log(sku.value)
        total.value = sku.value.price
    })

    // 原来的逻辑
    /* categoryApi.getdetail(skuId).then(result => {
        skus.value[0] = result.data
        console.log(skus.value)
        for (const s of skus.value) {
            total.value += s.price
        }
    }) */
    /*     } */
    /*     if (ids) {
            categoryApi.getBatch(ids).then(result => {
                skus.value = result.data
                console.log(skus.value)
                for (const s of skus.value) {
                    total.value += s.price
                }
            })
        } */

}
getOneSku()

/**
 * 处理订单提交逻辑
 */
const order = ref({})
function handleCreate() {
    if(!selAddr.value) {
        ElMessage.warning('请先选择可用地址')
        return
    }
    order.value.address = selAddr.value;
    order.value.puserId = sku.value.puserId
    order.value.skuId = skuId
    console.log(order.value)
    orderApi.addorder(order.value).then(result => {
        if (result.code == 200) {
            ElMessage.success('订单提交成功')
            router.push({ path: '/orderpay', query: { orderId: result.data } })
        }
        else {
            ElMessage.error('你已经将此商品下单了！')
        }
    })




    /* order.value.address = selAddr.value;
    order.value.skus = skus.value;
    order.value.puserId = skus.value[0].puserId
    console.log(order.value)
    orderApi.addorder(order.value).then(result => {
        if(result.code == 200) {
            ElMessage.success('订单提交成功')
            router.push({path:'/orderpay', query:{orderId:result.data}})
        }
        else {
            ElMessage.error('订单提交失败')
        }
    }) */

}


/* <ul>
            <li v-for="sku in skus">
                <el-row style="height: 100px;">
                    <el-col :span="3">
                        <el-image :src="`${api.skuURL}upload/${sku.picList[0].url}`"
                        class="sku-img"></el-image>
                    </el-col>
                    <el-col :span="3" class="sku-name">{{ sku.name }}</el-col>
                    <el-col :span="1" class="sku-status">{{ ['未售出','已售出'].at(sku.isSold) }}</el-col>
                    <el-col :span="7" class="sku-dscp">{{ sku.description }}</el-col>
                    <el-col :span="7" class="sku-attr">
                        <span v-for="a in sku.specList">{{ a.specName }}：{{ a.optionValue }}&nbsp;</span>
                    </el-col>
                    <el-col :span="3" class="sku-price">￥{{ sku.price }}元</el-col>
                </el-row>
            </li>
        </ul> */


</script>


<template>
    <div style="font-size: 18px;margin-bottom: 10px;color: #b0b0b0;margin-top: 10px;">填写并核对收货信息</div>
    <div class="addr-container">
        <div class="title">收货地址🍡</div>
        <UserEditAddr v-model:addrEditVisible="addrEditVisible" v-model:editAddr="editAddr" v-on:load="handleLoad">
        </UserEditAddr>
        <div class="addr-item" v-if="selAddr">
            <ul>
                <li v-for="a in addrs">
                    <span :class="{ active: a.id == selAddrId, contact: true }" @click="changeAddr(a)">{{ a.contact
                        }}</span>
                    <span class="addr">
                        {{ a.province }}{{ a.city }}{{ a.area }}{{ a.detailAddr }}
                        <span class="default" v-if="a.isSelected == 1">默认地址</span>
                    </span>
                    <span class="lian">
                        <div class="contactPerson">{{ a.contact }}先生/女士</div>
                        <div class="phone">{{ a.phone }}</div>
                    </span>
                    <span class="operate">
                        <div><el-button text style="height: 0px;" @click="handleEditAddr(a)">编辑</el-button>
                            <el-button text circle @click="handleDelete(a.id)"><el-icon>
                                    <Delete />
                                </el-icon></el-button>
                        </div>
                        <div><el-button text style="height: 0px;" @click="handleDefault(a)">设为默认地址</el-button></div>
                    </span>
                </li>
            </ul>
        </div>

        <div v-else class="noAddress">
            暂无可用地址选择，请先前往个人主页添加地址在支付哦！
        </div>
    </div>


    <div class="skuInfo-title">商品信息</div>
    <div class="skus-container">
        <ul>
            <li>
                <el-row style="height: 100px;">
                    <el-col :span="3">
                        <el-image v-if="sku.picList && sku.picList.length > 0" :src="`${api.skuURL}upload/${sku.picList[0].url}`" class="sku-img"></el-image>

                    </el-col>
                    <el-col :span="3" class="sku-name">{{ sku.name }}</el-col>
                    <el-col :span="1" class="sku-status">{{ ['未售出', '已售出'].at(sku.isSold) }}</el-col>
                    <el-col :span="7" class="sku-dscp">{{ sku.description }}</el-col>
                    <el-col :span="7" class="sku-attr">
                        <span v-for="a in sku.specList">{{ a.specName }}：{{ a.optionValue }}&nbsp;</span>
                    </el-col>
                    <el-col :span="3" class="sku-price">￥{{ sku.price }}元</el-col>
                </el-row>
            </li>
        </ul>

    </div>

    <!-- 结算的底部bar -->
    <div class="pay-bar">
        <el-row>
            <el-col :span="14" class="address-bottom">
                配送至：{{ selAddr?.province }}
                {{ selAddr?.city }}
                {{ selAddr?.area }}
                {{ selAddr?.detailAddr }}
            </el-col>
            <el-col :span="6" class="pay-money">应付金额：<span class="money">￥{{ total }}</span></el-col>
            <el-col :span="4"><el-button class="pay-button" @click="handleCreate">结算</el-button></el-col>
        </el-row>
    </div>
</template>

<style scoped>
.noAddress{
    font-size: 24px;
    font-weight: bolder;
    color: rgb(226, 95, 50);
    margin: 50px;
}
.money {
    color: rgb(226, 95, 50);
    font-size: 20px;
    font-weight: bold;
    padding-right: 20px;
}

.pay-money {
    margin: 40px 0;
    text-align: right;
    color: #a1a5a8;
    font-size: 16px;
}

.pay-button {
    width: 100%;
    height: 100px;
    border-radius: 0;
    background-color: rgb(245, 108, 108);
    border: 1px solid rgb(245, 108, 108);
    color: #fff;
    font-size: 24px;
    font-weight: bolder;
}

.pay-button:hover {
    box-shadow: 0 0 5px #000;
}

.address-bottom {
    margin: 40px 0;
}

.pay-bar {
    width: 100%;
    background-color: #fff;
    height: 100px;
    margin-top: 20px;
    color: #b0b0b0;
    text-align: right;
}

.sku-attr {
    margin: 20px 0 40px 0;
    line-height: 40px;
    overflow: hidden;
    -webkit-line-clamp: 1;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
}

.sku-price {
    margin-top: 20px;
    padding: 0 20px;
    color: rgb(191, 76, 0);
    font-size: 16px;
}

.sku-dscp {
    margin: 20px 0 40px 0;
    padding: 0 20px;
    height: 40px;
    line-height: 20px;
    overflow: hidden;
    -webkit-line-clamp: 2;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
}

.sku-name {
    margin-top: 20px;
}

.sku-status {
    margin-top: 20px;

}

.sku-img {
    width: 100px;
    height: 100px;
    padding-left: 10px;
}

.skuInfo-title {
    width: 100%;
    margin: 20px 0 10px 0;
    font-size: 18px;
    color: #b0b0b0;
    padding-bottom: 10px;
    border-bottom: 1px solid #b0b0b0;
}

ul {
    padding: 0px 10px;
}

.default {
    background-color: #b0b0b0;
    color: #fff;
    padding: 5px 10px;
    position: absolute;
    right: 320px;
    top: 5px;
}

.lian {
    display: inline-block;
    position: absolute;
    top: 10px;
    right: 150px;
    text-align: right;
}

.operate {
    display: inline-block;
    position: absolute;
    top: 10px;
    right: 10px;
}

.addr {
    margin-left: 50px;
    width: 400px;
    overflow: hidden;
    -webkit-line-clamp: 1;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
}

.addr-container {
    width: 1050px;
    height: auto;
    border: 1px solid #ddd;
    padding: 10px;
    margin-left: 30px;
    background-color: #fff;
}

.skus-container {
    width: 100%;
    background-color: #fff;
    height: auto;
    padding: 5px 5px;
}

.skus-container li {
    height: 100px;
    padding: 15px 0;
    border: 1px dashed #aaa;
}

.title {
    font-size: 16px;
    font-weight: bold;
}

.addr-item {
    height: auto;
}

.addr-item>ul>li {
    width: 1000px;
    height: 70px;
    padding: 15px 10px;
    position: relative;
    border: 1px solid #aaa;
    border-bottom: none;
}

.addr-item>ul>li:last-child {
    border-bottom: 1px solid #aaa;
}

.addr-item>ul>li>span {
    display: inline-block;
}

.contact {
    width: 120px;
    height: 50px;
    line-height: 50px;
    text-align: center;
    border: 2px solid #b0b0b0;
}

.active {
    border: 3px solid rgb(216, 12, 24);
}

.contact:hover {
    border: 3px solid rgb(216, 12, 24);
}
</style>