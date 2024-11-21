<script setup>
import { ref } from 'vue';
const categories = defineModel('categories')
const categories2 = ref([])
const categories3 = ref([])
const searchSkuVo = defineModel('searchSkuVo')
const sfirstc = ref({})
const ssecondc = ref({})
const sthirdc = ref({})

function handleFirstChange(){
    for(const c of categories.value) {
        if(c.id === searchSkuVo.value.category1Id) {
            categories2.value = c.children;
            console.log(categories2.value)
        }
    }
}

function handleSecondChange() {
    console.log(categories2.value)
    console.log(searchSkuVo.value.category2Id)
    for(const c of categories2.value) {
        if(c.id === searchSkuVo.value.category2Id) {
            categories3.value = c.children
            console.log(categories3.value)
        }
    }
}

/**   处理查询事件  */
function handleSearch() {
    searchSkuVo.value = {...searchSkuVo.value}
}
</script>

<template>
    <el-form :model="searchSkuVo" label-width="80px" :inline="true">

        <el-form-item label="商品名">
            <el-input v-model="searchSkuVo.name" placeholder="请输入商品名" clearable></el-input>
        </el-form-item>

        <el-form-item label="商品分类">
            <el-select v-model="searchSkuVo.category1Id" placeholder="请选择一级分类" @change="handleFirstChange"
                class="inputstyle" clearable>
                <el-option v-for="item in categories" :key="item.value" :label="item.name" :value="item.id">
                </el-option>
            </el-select>

            <el-select v-model="searchSkuVo.category2Id" placeholder="请选择二级分类" @change="handleSecondChange" 
            class="inputstyle" clearable>
                <el-option v-for="item in categories2" :key="item.value" :label="item.name" :value="item.id">
                </el-option>
            </el-select>

            <el-select v-model="searchSkuVo.category3Id" placeholder="请选择三级分类" class="inputstyle" clearable>
                <el-option v-for="item in categories3" :key="item.value" :label="item.name" :value="item.id">
                </el-option>
            </el-select>

            <el-button type="success" class="search-button" @click="handleSearch">搜索</el-button>
        </el-form-item>



    </el-form>
</template>

<style scoped>
.inputstyle {
    width: 150px;
}

.search-button {
    margin-left: 20px;
}
</style>