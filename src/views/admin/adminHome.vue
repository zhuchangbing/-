<script setup>
import adminapi from '@/axios/adminApi';
import router from '@/router';
import { useAdminStore } from '@/stores/admin';
import { useTokenStore } from '@/stores/token';
import { ElMessage } from 'element-plus';

/* 这是退出登录的按钮触发事件 */
function handleLogout(){
    adminapi.logout().then((result)=>{
        if(result.code == 200) {
            ElMessage('退出成功')
            localStorage.removeItem('token')  //退出用户， 删除session中的内容，删除token
        }
    })
}

function handleSelectView(key){
    router.push(key)
}

const adminStore = useAdminStore()
// 退出登录 销毁token 和 useStore中的内容
function logout() {
    const tokenStore = useTokenStore()
    const adminStore = useAdminStore()
    tokenStore.clear()
    adminStore.clear()
}
console.log

</script>



<template>
    <div class="common-layout">
        <el-container>
            <!---------   Header    --------->
            <el-header>
                <el-menu :default-active="activeIndex2" class="el-menu-demo" mode="horizontal" :ellipsis="false"
                    background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" @select="handleSelect">
                    <el-menu-item index="1">易购商城后台管理</el-menu-item>
                    <el-sub-menu index="2">
                        <template #title v-if=" ! adminStore.admin"><a href="/admin/login">登录</a></template>
                        <template #title v-else>{{ adminStore.admin.nickName }}</template>
                        <el-menu-item index="2-1">修改信息</el-menu-item>
                        <el-menu-item index="2-2">重置密码</el-menu-item>
                        <el-menu-item index="2-3" @click="logout">退出登录</el-menu-item>
                    </el-sub-menu>
                </el-menu>
            </el-header>

            <!---------   Aside And  Main    --------->
            <el-container>
                <el-aside width="200px">
                    <el-menu active-text-color="#ffd04b" background-color="#545c64" class="el-menu-vertical-demo"
                        default-active="2" text-color="#fff" @open="handleOpen" @close="handleClose"
                        @select="handleSelectView">   
                        <!---------------      管理员管理        -------------->

                        <el-sub-menu index="admin">
                            <template #title><span>管理员管理</span></template>
                            <el-menu-item index="/admin/home/admin">
                                <template #title>管理员列表</template>
                            </el-menu-item>
                        </el-sub-menu>


                        <!---------------      用户管理        -------------->
                        <el-sub-menu index="user">
                            <template #title>
                                <span>用户管理</span>
                            </template>
                            <el-menu-item index="/admin/home/user">
                                <template #title>用户列表</template>
                            </el-menu-item>
                        </el-sub-menu>

                        <!---------------      分类管理        -------------->
                        <el-sub-menu index="category">
                            <template #title>
                                <span>分类管理</span>
                            </template>
                            <el-menu-item index="/admin/home/category">
                                <template #title>分类列表</template>
                            </el-menu-item>
                        </el-sub-menu>

                        <!---------------      商品管理        -------------->
                        <el-sub-menu index="good">
                            <template #title>
                                <span>商品管理</span>
                            </template>
                            <el-menu-item index="/admin/home/good">
                                <template #title>商品列表</template>
                            </el-menu-item>
                        </el-sub-menu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <RouterView></RouterView>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>
<style>
.common-layout {
    width: 100%;
    height: auto;
}
.el-header{
    padding: 0;
}
.el-menu-demo{
    width: 100%;
    display: flex;
    flex: 1;
}


.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}
</style>