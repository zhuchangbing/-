<script setup>




</script>

<template>
    <el-container>
        <el-aside>
            <div class="title">个人中心</div>
            <ul class="menu">
                <li><RouterLink to="/center/orders">我的订单</RouterLink></li>
                <li><RouterLink to="/center/release">我的发布</RouterLink></li>
                <li><RouterLink to="/collect">我的收藏</RouterLink></li>
                <li><RouterLink to="/center/info">个人信息</RouterLink></li>
                <li><RouterLink to="/center/horder">处理订单</RouterLink></li>
                <li><RouterLink to="/center/addr">地址管理</RouterLink></li>
            </ul>

            <ul class="menu">
                <li><RouterLink to="/center/orders">我的订单</RouterLink></li>
                <li><RouterLink to="/center/release">我的发布</RouterLink></li>
                <li><RouterLink to="/collect">我的收藏</RouterLink></li>
                <li><RouterLink to="/center/info">个人信息</RouterLink></li>
                <li><RouterLink to="/center/horder">处理订单</RouterLink></li>
                <li><RouterLink to="/center/addr">地址管理</RouterLink></li>
            </ul>
        </el-aside>
        <el-main>
            <RouterView></RouterView>
        </el-main>
    </el-container>

</template>


<style scoped>
.title{
    font-size: 18px;
    color: #000;
    font-weight: bold;
    margin: 20px 0 40px;
}
.menu {
    padding: 20px 0;
}
.menu li:hover {
    box-shadow: 0 0 5px #a0a0a0;
}
.el-main {
    background-color: #fff;
}
.el-aside{
    width: 100px;
    padding: 20px 0;
}

.menu li {
    width: 80px;
    height: 30px;
    padding-left: 10px;
}
.menu>li a {
    color: #a0a0a0;
}

</style>