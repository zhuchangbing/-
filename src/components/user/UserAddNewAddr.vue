<script setup>
import { ref } from 'vue';
import { User,Iphone, MapLocation } from '@element-plus/icons-vue'
import { areas } from '@/stores/area';
import { addrApi } from '@/axios/Addr';
import { ElMessage } from 'element-plus';
const addNewAddrVisible = defineModel('addNewAddrVisible');
const emits = defineModel('addLoad')
const addAddr = ref({})

function handleAdd() {
    addrApi.add(addAddr.value).then(result => {
        if(result.code == 200) {
            ElMessage.success('添加成功')
            emits('addLoad')
        }
        else {
            ElMessage.warning(result.message)
        }
    })    
    addNewAddrVisible.value = false;
}


/***   选地点的逻辑 */

const selectedProvince = ref({});
const selectedProvinceCity = ref({});

function handlerSelectProvince(value){
    for (var p in areas) {
        if(areas[p].name == value){
            selectedProvince.value = areas[p];
        }
    }
    selectedProvinceCity.value = [];
    addAddr.value.city = null;
    addAddr.value.area = null;

}

function handlerSelectCity(value){
    for(var i in selectedProvince.value.city){
        if(selectedProvince.value.city[i].name == value){
            selectedProvinceCity.value = selectedProvince.value.city[i];
        }
    }
    addAddr.value.area = null;
}

</script>

<template>
    <el-dialog title="添加收货地址🎈" v-model="addNewAddrVisible" width="800px">
        <el-form :model="addAddr" label-width="80px" :inline="true">

            <el-form-item label="联系人">
                <div class="icon-wrapper"><el-icon><User /></el-icon></div>
                <el-input v-model="addAddr.contact" placeholder="请输入联系人姓名"></el-input>
            </el-form-item>

            <el-form-item label="电话">
                <div class="icon-wrapper"><el-icon>
                        <Iphone />
                    </el-icon></div>
                <el-input v-model="addAddr.phone" placeholder="请输入联系人电话"></el-input>
            </el-form-item>

            <el-form-item label="地址">
                <el-select v-model="addAddr.province" placeholder="请选择省份" @change="handlerSelectProvince"
                    style="width: 120px;">
                    <el-option v-for="province in areas" :key="province.name" :label="province.name"
                        :value="province.name"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="">
                <el-select v-model="addAddr.city" placeholder="请选择市" @change="handlerSelectCity" style="width: 120px;">
                    <el-option v-for="city in selectedProvince.city" :key="city.name" :label="city.name"
                        :value="city.name"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="">
                <el-select v-model="addAddr.area" placeholder="请选择区" style="width: 120px;">
                    <el-option v-for="area in selectedProvinceCity.area" :key="area.name" :label="area.name"
                        :value="area.name"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="详细地址">
                <div class="icon-wrapper"><el-icon>
                        <MapLocation />
                    </el-icon></div>
                <el-input v-model="addAddr.detailAddr" placeholder="请输入详细地址" style="width: 500px;"></el-input>
            </el-form-item>


        </el-form>
        <div slot="footer">
            <el-button @click="addNewAddrVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleAdd">确 定</el-button>
        </div>
    </el-dialog>

</template>

<style scoped>
.icon-wrapper{
    position: absolute;
    left: 10px; /* 图标离输入框的左侧距离 */
    top: 50%; /* 图标在输入框中垂直居中 */
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10;
}
</style>