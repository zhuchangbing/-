<script setup>
import { ref } from 'vue';
import { Lock, User,Iphone,MapLocation} from '@element-plus/icons-vue'
import { areas } from '@/stores/area';
import { addrApi } from '@/axios/Addr';
import { ElMessage } from 'element-plus';

const addrEditVisible = defineModel('addrEditVisible');
const editAddr = defineModel("editAddr")
const emits = defineEmits(['load'])

function handleUpdate() {
    addrApi.update(editAddr.value).then(result => {
        if(result.code == 200) {
            ElMessage.success('修改成功😅')
            emits('load')
        }
        else {
            ElMessage.warning(result.message)
        }
    })
    addrEditVisible.value = false;
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
    editAddr.value.city = null;
    editAddr.value.area = null;

}

function handlerSelectCity(value){
    for(var i in selectedProvince.value.city){
        if(selectedProvince.value.city[i].name == value){
            selectedProvinceCity.value = selectedProvince.value.city[i];
        }
    }
    editAddr.value.area = null;
}


</script>

<template>
    <el-dialog title="编辑收货地址😁" v-model="addrEditVisible" width="800px">
        <el-form :model="editAddr" label-width="80px" :inline="true">

            <el-form-item label="联系人">
                <div class="icon-wrapper"><el-icon><User /></el-icon></div>
                <el-input v-model="editAddr.contact" placeholder="请输入联系人姓名"></el-input>
            </el-form-item>

            <el-form-item label="电话">
                <div class="icon-wrapper"><el-icon><Iphone /></el-icon></div>
                <el-input v-model="editAddr.phone" placeholder="请输入联系人电话"></el-input>
            </el-form-item>

            <el-form-item label="地址">
                <el-select v-model="editAddr.province" placeholder="请选择省份" @change="handlerSelectProvince"
                    style="width: 120px;">
                    <el-option v-for="province in areas" :key="province.name" :label="province.name"
                        :value="province.name"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="">
                <el-select v-model="editAddr.city" placeholder="请选择市" @change="handlerSelectCity" style="width: 120px;">
                    <el-option v-for="city in selectedProvince.city" :key="city.name" :label="city.name"
                        :value="city.name"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="">
                <el-select v-model="editAddr.area" placeholder="请选择区" style="width: 120px;">
                    <el-option v-for="area in selectedProvinceCity.area" :key="area.name" :label="area.name"
                        :value="area.name"></el-option>
                </el-select>
            </el-form-item>
            
            <el-form-item label="详细地址">
                <div class="icon-wrapper"><el-icon><MapLocation /></el-icon></div>
                <el-input v-model="editAddr.detailAddr" placeholder="请输入详细地址" style="width: 500px;"></el-input>
            </el-form-item>


        </el-form>
        <div slot="footer">
            <el-button @click="addrEditVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleUpdate">确 定</el-button>
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