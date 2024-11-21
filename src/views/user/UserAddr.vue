<script setup>

import { addrApi } from '@/axios/Addr';
import UserEditAddr from '@/components/user/UserEditAddr.vue';
import { ref } from 'vue';
import { Plus, Delete } from '@element-plus/icons-vue'
import UserAddNewAddr from '@/components/user/UserAddNewAddr.vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const addrs = ref([])
const selAddr = ref({})
const selAddrId = ref(0)




function getUserAddress() {
    addrApi.getUserAddress().then(result => {
        addrs.value = result.data
        selAddrId.value = addrs.value[0].id;
        console.log(addrs.value)

        // 遍历一遍找到默认地址
        /* for(const a of addrs.value) {
            if(a.isSelected == 1) {
                selAddr.value = a;
                addrs.value.pop(a)
            }
        } */
    })

}
getUserAddress()


function changeAddr (id) {
    selAddrId.value = id;
}


/***     编辑功能    ***/

const addrEditVisible = ref(false)
const editAddr = ref({})


function handleLoad() {
    getUserAddress()
}


function handleEditAddr(addr) {
    addrEditVisible.value = true;
    editAddr.value = {...addr}
}



/***    添加逻辑 ****/
const addNewAddrVisible = ref(false)

function handleAdd() {
    addNewAddrVisible.value = true;
}

/*** 删除逻辑  ***/
function handleDelete(id) {
    ElMessageBox.confirm(
        '你确定要删除这个地址吗？删除后无法恢复',
        '提示',
        {
            type: 'warning',
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }
    ).then(() => {
        addrApi.delete(id).then(result => {
        if(result.code == 200) {
            ElMessage.success('删除成功')
            getUserAddress()
        }
        else {
            ElMessage.error(result.message)
        }
    })
    })
}

/**       修改默认地址      **/
function handleDefault(a) {
    for(const s of addrs.value) {
        if(s.isSelected == 1){
            if(s.id == a.id) {
                ElMessage('此地址已是默认地址')
                return
            }
            s.isSelected = 0;
            a.isSelected = 1;
            /** 数据库上修改默认地址 */
            addrApi.updateDefaultAddr(a.id).then(result => {
                if(result.code == 200) {
                    ElMessage.success('修改默认地址成功')
                }
                else {
                    ElMessage.error(result.message)
                }
            })
             return
        }
    }
    a.isSelected = 1;
    addrApi.updateDefaultAddr(a.id).then(result => {
        if (result.code == 200) {
            ElMessage.success('修改默认地址成功')
        }
        else {
            ElMessage.error(result.message)
        }
    })
}

</script>

<template>
    <UserEditAddr 
    v-model:addrEditVisible="addrEditVisible"
    v-model:editAddr = "editAddr"
    v-on:load = "handleLoad"></UserEditAddr>
    <UserAddNewAddr 
    v-on:addLoad="handleLoad"
    v-model:addNewAddrVisible="addNewAddrVisible"></UserAddNewAddr>
    <div class="addr-container">
        <div class="title">我的收货地址😡</div>
        <el-button type="success" style="margin: 5px 10px;" @click="handleAdd"><Plus />添加地址</el-button>
        <div class="addr-item">
            <ul>
                <li v-for="a in addrs">
                    <span :class="{active:a.id == selAddrId,contact:true}" @click="changeAddr(a.id)">{{ a.contact }}</span>
                    <span class="addr">
                        {{ a.province }}{{ a.city }}{{ a.area }}{{ a.detailAddr }}
                        <span class="default" v-if="a.isSelected==1">默认地址</span>
                    </span>
                    <span class="lian">
                        <div class="contactPerson">{{ a.contact }}先生/女士</div>
                        <div class="phone">{{ a.phone }}</div>
                    </span>
                    <span class="operate">
                        <div><el-button text style="height: 0px;" @click="handleEditAddr(a)">编辑</el-button>
                        <el-button text circle @click="handleDelete(a.id)"><el-icon><Delete /></el-icon></el-button></div>
                        <div><el-button text style="height: 0px;" @click="handleDefault(a)">设为默认地址</el-button></div>
                    </span>
                </li>
            </ul>
        </div>
    </div>
</template>

<style scoped>
ul {
    padding-left: 10px;
}
.default{
    background-color: #b0b0b0;
    color: #fff;
    padding: 5px 10px;
    position: absolute;
    right:320px;
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
    margin-left: 20px;
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
.addr-item>ul>li:last-child{
    border-bottom: 1px solid #aaa;
}
.addr-item>ul>li>span {
    display: inline-block;
}
.contact{
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