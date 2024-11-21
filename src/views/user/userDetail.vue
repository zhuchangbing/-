<script setup>
import api from '@/axios';
import categoryApi from '@/axios/categoryApi';
import { collectApi } from '@/axios/collectApi';
import router from '@/router';
import { ElMessage } from 'element-plus';
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { Shop, Plus } from '@element-plus/icons-vue'

const id = useRoute().query.id;
const ssku = ref({})
const sku = ref({puser:{id:0,header:'',old:0,createTime:''}}) 
const cimg = ref('/src/assets/img/weisoucang1.png')

function getsku(){
    console.log(id)
    categoryApi.getdetail(id).then((result) => {
        sku.value = result.data;
        getYear();
        collectApi.getssku({skuId:sku.value.id}).then(result => {
            ssku.value = result.data
            if(ssku.value) {
                cimg.value = '/src/assets/img/yisoucang.png';
            }
            else
                cimg.value = '/src/assets/img/weisoucang1.png';
            console.log(ssku.value)
        })
        console.log(sku.value)
    })
}
getsku();

function del() {
    collectApi.delete(sku.value.id).then(result => {
        if(result.code == 200 ) {
            ElMessage.success('删除成功')
        }
        else {
            ElMessage.warning(result.message)
        }
    })
}

const freshImg = ref([
    'fresh1.webp',
    'fresh2.webp',
    'fresh3.webp',
    'fresh4.webp',
])



function addCollect() {
    collectApi.addCollect({skuId:sku.value.id}).then(result => {
        if(result.code == 200 ) {
            ElMessage.success('添加成功')
        }
        else {
            ElMessage.warning(result.message)
        }
    })
}

function handleChangeImg(){
    if(cimg.value =='/src/assets/img/weisoucang1.png') {
        cimg.value = '/src/assets/img/yisoucang.png'
        addCollect();
    }
    else {
        cimg.value = '/src/assets/img/weisoucang1.png'
        del();
    }
}

function handlePurchase() {
    if(sku.value.isSold == 1) {
        ElMessage.warning('该商品已经售出了，您来晚辣');
        return;
    }
    router.push({path:'/corder',query:{skuId:sku.value.id}})
}


/***   获取商家来平台多少年了 */
function getYear() {
    let currentDate = new Date();
    
    // 检查 sku.value.puser 和 createTime 是否存在
    if (sku.value.puser && sku.value.puser.createTime) {
        let regDate = new Date(sku.value.puser.createTime.replace(' ', 'T'));
        
        // 检查是否成功解析日期
        if (isNaN(regDate.getTime())) {
            console.error('Invalid registration date:', sku.value.puser.createTime);
            return;
        }
        
        sku.value.puser.old = currentDate.getFullYear() - regDate.getFullYear();
        console.log(regDate);
        sku.value.puser.old = sku.value.puser.old < 1 ? 1 : sku.value.puser.old;
    } else {
        console.error('puser or createTime is undefined:', sku.value.puser);
    }
}




</script>

<template>
    <div class="puser">  
            <span class="">
                <el-image :src="`${api.skuURL}upload/${sku.puser.header}`" :fit="contain"
                style="width: 60px;height: 60px;"></el-image>
            </span>
            <span class="puser-nickName">{{ sku.puser.nickName }}</span>
            <span class="puser-province">{{ sku.puser.province }}</span>
            <span class="puser-province">来易购{{ sku.puser.old }}年</span>
            <span class="button-mainpager" ><el-button class="button-1"><el-icon><Shop /></el-icon>商家主页</el-button></span>
            <span class="button-g" ><el-button class="button-2"><el-icon><Plus /></el-icon>关注</el-button></span>
    </div>
    <div class="bg">
        
        <div class="picAnddesp">
            <el-row>
                <el-col :span="8"> <!-- 商品的图片 -->
                    <el-carousel height="500px" class="zm">
                        <el-carousel-item v-for="pic in sku.picList" :key="pic" class="img">
                            <el-image :src="`${api.skuURL}upload/${pic.url}`" :fit="contain" class="image"></el-image>
                        </el-carousel-item>
                    </el-carousel>
                </el-col>
                <el-col :span="16" class="skuDesp"> <!-- 商品的描述 价格等信息 -->
                    <!-- 商品名字 -->
                    <div class="f">
                        <div class="freshLevel">
                            <el-image :src="`${api.skuURL}upload/${freshImg.at(sku.freshLevel)}`"
                                :fit="contain"></el-image>
                        </div>
                        <div class="want">{{ sku.wants }}人想要</div>
                    </div>
                    <div class="name">
                        {{ sku.name }}
                        <el-image :src="cimg" :fit="contain" class="cimg" @click="handleChangeImg"></el-image>
                    </div>
                    <!-- 商品描述 -->
                    <div class="description">{{ sku.description }}</div>
                    <!-- 商品属性显示 -->
                    <div class="attribute">
                        <span class="fle">成色</span><a style="color: rgb(153, 153, 153);font-size: 22px;">：</a>
                        <span class="fle-content">{{ ['全新', '几乎全新', '有轻微使用痕迹', '有明显使用痕迹'].at(sku.freshLevel) }}</span>
                        <span v-for="attr in sku.specList">
                            <span class="fle">{{ attr.specName }}</span><a
                                style="color: rgb(153, 153, 153);font-size: 22px;">：</a><span class="fle-content">{{
                                attr.optionValue }}</span>
                        </span>
                    </div>
                    <!-- 商品价格 -->
                    <div class="price">￥{{ sku.price }}元<span class="free" v-if="sku.isFree == 1">包邮</span></div>
                    <div class="sold" v-if="sku.isSold == 1">已卖出</div>
                    <!-- 商品评分 -->
                    <div class="score">
                        <a>评分：</a><el-rate v-model="sku.score" disabled show-score text-color="#ff9900" size="large"
                            score-template="{value} 分" />
                    </div>
                    <div class="button">
                        <el-button type="primary" class="button1">
                            <el-image src="/src/assets/img/chat.webp" class="chat"></el-image>我想要</el-button>
                        <el-button type="primary" class="button2" @click="handlePurchase">立即购买</el-button>
                    </div>
                </el-col>
            </el-row>
        </div>


    </div>
    <div class="bottom-desp">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane label="商品详情" name="first">商品详情</el-tab-pane>
            <el-tab-pane label="评论" name="second">评论</el-tab-pane>
            <el-tab-pane label="参数配置" name="third">参数配置</el-tab-pane>
            <el-tab-pane label="售后服务" name="fourth">售后服务</el-tab-pane>
        </el-tabs>
    </div>

</template>

<style scoped>
.button-1{
    width: 150px;
    height: 40px;
    border: 1px solid #b0b0b0; 
    border-radius: 20px;
    font-size: 16px;
    margin-left: 600px;

}
.button-2 {
    width: 120px;
    height: 40px;
    border: 1px solid #b0b0b0; 
    border-radius: 20px;
    font-size: 16px;
    background-color: rgb(247, 247, 247);
}
.button-g{
    display: inline-block;
    margin-left: 10px;
    margin-bottom: 20px;
    position: relative;
    bottom: 25px;
    font-size: 16px;
}
.button-mainpager {
    display: inline-block;
    margin-left: 10px;
    margin-bottom: 20px;
    position: relative;
    bottom: 25px;
    font-size: 16px;
}
.puser-nickName{
    display: inline-block;
    margin-left: 10px;
    margin-bottom: 20px;
    position: relative;
    bottom: 25px;
    font-size: 16px;
}
.puser-province {
    display: inline-block;
    margin-left: 10px;
    margin-bottom: 20px;
    position: relative;
    bottom: 25px;
    font-size: 16px;
    background-color: rgb(243, 246, 248);
    padding: 7px 15px;
    border-radius: 20px;
}
.cimg {
    height: 25px;
    padding-top: 5px;
    margin-left: 10px;
}
.chat {
    height: 25px;
    margin-right: 20px;
}
.button1 {
    border: none;
    background-color: rgb(255, 230, 15);
    width: 200px;
    height: 40px;
    border-radius: 20px 0 0 20px;
    color: rgb(31, 31, 31);
}
.button2{
    background-color: rgb(31, 31, 31);
    border: none;
    width: 200px;
    height: 40px;
    margin: 0;
    border-radius: 0 20px 20px 0;
}

.bg {
    background-color: #fff;
    margin-top: 20px;                /* 以后放商家信息 */
    border-radius: 20px;
    padding-bottom: 20px;
    padding-left: 20px;
}
.puser{
    background-color: #fff;
    border-radius: 20px;
    margin-top: 20px;
    padding: 0 20px;
    height: 64px;
}

.header-img{
    width: 60px;
    height: 60px;
    margin: 2px 5px;
}

.score{
    
}
.score a {
    color: rgb(153,153,153);
}
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
.demo-tabs{
    margin-left: 50px;
    padding-top: 20px;
    padding-bottom: 50px;
}
.bottom-desp{
    height: auto;
    margin-top: 20px;
    background-color: #fff;
    margin-top: 20px;
    border-radius: 20px;
}
.fle {
    display: inline-block;
    width: 150px; /* 设置固定宽度 */
    text-align: justify;
    position: relative;
    line-height: 0px;
    color: rgb(153,153,153);
    font-size: 22px;
    margin-top: 50px;
    margin-left: 20px;
}

.fle::after {
    content: '';
    display: inline-block;
    width: 100%; /* 占据剩余宽度 */
    height: 0; /* 保持在一行 */
}
.fle-content {
    margin-top: 50px;
    display: inline-block;
    width: 160px;
    color: rgb(31, 31, 31);
    font-size: 22px;
    padding-left: 20px;
}
.picAnddesp{
    height: auto;
}
.img{
    display: flex;
    justify-content: center;
    align-items: center;
}
.zm {
    background-color: rgb(243, 246, 248);
    border: 1px dashed #b0b0b0;
    border-radius: 20px;
    margin-top: 20px;
}
.image{
    width: 500px;
}
.skuDesp {
    margin-top: 20px;
    padding-left: 20px;
}
.name{
    font-size: 28px;
    height: auto;
    line-height: 100px;
}
.description{
    margin-top: 10px;
    font-size: 20px;
}
.price{
    color: rgb(191, 76, 0);
    font-size: 24px;
}
.free{
    padding-left: 5px;
    margin-left: 5px;
    color: rgb(31, 31, 31);
    font-size: 16px;
    border-left: 1px solid #b0b0b0;
}
.want{
    text-align: right;
    color: rgb(153, 153, 153);
}

.attrSpan {
    display: inline-block;
    width: 300px;
    font-size: 20px;
    color: rgb(153, 153, 153);
}
.attrSpan a {
    color: rgb(31, 31, 31);
    font-size: 20px;
}
.f{
    float: right;
    padding-right: 50px;
}
.attribute {
    height: auto;
    margin-bottom: 30px;
}

</style>