<script setup>
import { ref } from 'vue';
import categoryApi from '@/axios/categoryApi';
import { Search } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user';
import { useTokenStore } from '@/stores/token';
import { Plus } from '@element-plus/icons-vue'
import router from '@/router';
import { onBeforeRouteUpdate } from 'vue-router';
import { skuApi } from '@/axios/skuApi';

const categories = ref([])

function loadCategory() {
    categoryApi.getCategory().then((result) => {
        categories.value = result.data;
        console.log(categories.value)
    })
}

loadCategory();

const userStore = useUserStore()

// 退出登录 销毁token 和 useStore中的内容
function logout() {
    const tokenStore = useTokenStore()
    const userStore = useUserStore()
    tokenStore.clear()
    userStore.clear()
}

function gotoRel() {
    router.push('/rel')
}

/* 处理搜索逻辑 */
const key = ref('')
function handleSearch() {
    router.replace({ path: '/search', query: { key: key.value } });
        
}
</script>

<template>
    <div class="nav-bg">
        <div class="center">
            <el-row>
                <el-col :span="16">
                    <ul>
                        <li>
                            <RouterLink to="/">商城首页</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="#">平台官网</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="#">软件支持</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="#">售后服务</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="#">企业用户</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="#">招贤纳士</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="#">联系我们</RouterLink>
                        </li>
                    </ul>
                </el-col>
                <el-col :span="8">
                    <ul class="user" v-if=" ! userStore.user">
                        <li>
                            <RouterLink to="/login">登录</RouterLink>
                        </li>
                        <li>
                            <RouterLink to="/reg">注册</RouterLink>
                        </li>
                    </ul>
                    <ul class="user" v-else>
                        <li>
                            <a href='/rel'>发布商品</a>
                        </li>
                        <li>
                            <a href='/collect'>我的收藏</a>
                        </li>
                        <li>
                            <RouterLink to="/center">{{userStore.user.nickName }}</RouterLink>
                        </li>
                        <li>
                            <a href="javascript:;" @click="logout">退出</a>
                        </li>
                    </ul>
                </el-col>
            </el-row>
        </div>
    </div>

    <!--        搜索框          -->
    <div class="search-bg">
        <div class="center">
            <el-row>
                <el-col :span="8">
                    <el-image src="/src/assets/logo.svg" style="height: 50px;margin-top: 10px;"></el-image>
                </el-col>
                <el-col :span="16" style="text-align: right;">
                    <el-input class="search-input" placeholder="请输入检索关键词" v-model="key"></el-input>
                    <el-button class="search-button" type="primary" @click="handleSearch"><el-icon>
                            <Search />
                        </el-icon></el-button>
                </el-col>
               
            </el-row>
        </div>
    </div>


    <div class="category">
        <div class="center">
            <ul>
                <li v-for="category in categories"><RouterLink :to="`/search/${category.id}`">{{ category.name }}</RouterLink></li>
            </ul>
        </div>
    </div>


</template>

<style scoped>
.button-release{
    margin-top: 22px;
    margin-left: 20px;
    width: 150px;
    height: 40px;
    border: 1px solid #333;
    font-size: 16px;
}
.center{
    width: 1250px;
    margin: 0px auto;
}
li {
    float: left;
    margin: 0px 20px;
}
.user {
    float: right;
}
.nav-bg {
    background-color: #333;
    width: 100%;
    height: 30px;
}
.nav-bg .center ul {
    margin: 0;
    line-height: 30px;
}
.nav-bg .center a {
    color: #b0b0b0;
    font-size: 12px;
}
.search-bg{
    width: 100%;
}
.nav-bg .center a:hover {
    color: aliceblue;
}

.search-input {
    margin-top: 22px;
    width: 350px;
}

.search-button {
    margin-top: 22px;
    border-radius: 0px;
    background-color: rgb(78, 110, 242);
}
.category .center{
    height: 50px;
    background-color: rgb(65, 184, 131);
}
.category .center a {
    font-size: 20px;
    font-weight: bold;
    line-height: 50px;
    color: #eee;
}
.category .center a:hover{
    color: #fff;
}
</style>