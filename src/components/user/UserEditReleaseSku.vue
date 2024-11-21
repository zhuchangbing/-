<script setup>
import { skuApi } from '@/axios/skuApi';
import { skuSpecApi } from '@/axios/skuSpecApi';
import { ElMessage, ElMessageBox, tabsEmits } from 'element-plus';
import { ref, watch } from 'vue';
const editVisible = defineModel('editVisible')
const editSku = defineModel('editSku')

const emits = defineEmits(['load'])


const attrs = ref([])
watch(editSku,()=>{
    getAttrs(editSku.value.goodsId)
})
function getAttrs(goodsId) {
    skuSpecApi.attrs(goodsId).then(result => {
        attrs.value = result.data

        for(const s of editSku.value.specList) {
            for(const a of attrs.value) {
                if(s.specId == a.id) {
                    for(const o of a.optionsList) {
                        if(o.id == s.optionId){
                            a.selectId = o.id
                        }
                    }
                }
            }
        }
    })
}

function handleSelectAttr(v) {
    for (const a of attrs.value) {
        if (a.id == v.specId) {
            a.selectId = v.id
        }
    }

    for (const s of editSku.value.specList) {
        if (s.specId == v.specId) {
            s.optionId = v.id
        }
    }
    console.log(editSku.value);
}

function Edit() {
    console.log(attrs.value)
    console.log(editSku.value)
    ElMessageBox.confirm(
        '你确定要这样修改这件商品吗？',
        '提示',
        {
            type: 'warning',
            confirmButtonText : '确定',
            cancelButtonText: '取消',

        }
    ).then(()=>{
        skuApi.updatesku(editSku.value).then(result => {
            if(result.code == 200) {
                ElMessage.success('修改成功')
                emits('load')
                editVisible.value = false
            }
            else {
                ElMessage.error(result.message)
            }
        })
    })

}

</script>

<template>

    <el-dialog title="修改商品信息" v-model="editVisible" width="1200px">
        <el-form :model="editSku" label-width="80px">
            <el-form-item label="品牌">
                <el-input v-model="editSku.name" placeholder="请输入物品品牌名" style="width: 200px;" clearable></el-input>
            </el-form-item>
            <el-form-item label="描述">
                <el-input type="textarea" v-model="editSku.description" placeholder="请输入商品的描述"
                    style="width: 500px;" clearable></el-input>
            </el-form-item>
            <el-form-item label="价格">
                <el-input-number v-model="editSku.price" @change="inputChange" :min="1" :max="10000"
                    label="请输入价格"></el-input-number>
            </el-form-item>
            <el-form-item label="商品状况">
                <el-select v-model="editSku.freshLevel" placeholder="请选择商品状况" style="width: 300px;" clearable>
                    <el-option label="全新" :value=0></el-option>
                    <el-option label="几乎全新" :value=1></el-option>
                    <el-option label="轻微磨损" :value=2></el-option>
                    <el-option label="有明显使用痕迹" :value=3></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="是否包邮">
                <el-radio-group v-model="editSku.isFree" clearable>
                    <el-radio label="包邮" :value=0></el-radio>
                    <el-radio label="不包邮" :value=1></el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
        <div class="attr-container">
            <ul v-for="attr in attrs" class="attr-ul">
                <li>{{ attr.name }}</li>
                <li v-for="v in attr.optionsList" class="attr-li" @click="handleSelectAttr(v)"
                    :class="{ activeAttr: v.id == attr?.selectId }">
                    {{ v.value }}
                </li>
            </ul>
        </div>
        <div slot="footer">
            <el-button @click="editVisible = false">取 消</el-button>
            <el-button type="primary" @click="Edit">确 定</el-button>
        </div>
    </el-dialog>

</template>

<style scoped>
.activeAttr{
    color: #000;
    background-color: rgb(255, 230, 15);
    font-size: 20px;
    font-weight: bold;
}
.attr-container{
    height: auto;
}
.attr-ul li{
    float: left;
    width: 100px;
    height: 50px;
    line-height: 50px;
}

.attr-ul::after {
    content: '';
    display: block;
    clear: both;
}
.attr-ul {
    margin-left: 30px;
    margin-top: 20px;
}

.attr-li{
    float: left;
    text-align: center;
    padding: 2px 10px;
    width: 120px;
    height: 50px;
    line-height: 50px;
    border: 1px solid #aaa;
    margin-bottom: 20px;
    border-radius: 15px;
    margin-right: 20px;
}
</style>