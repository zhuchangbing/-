<script setup>

const category = defineModel('category')
const categories = defineModel('categories')


function handleSearch() {
    category.value = {...category.value}
}


</script>

<template>
    <el-form :model="category" :inline="true">
        <el-form-item label="分类名" class="inputstyle">
            <el-input v-model="category.name" placeholder="请输入分类名" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类" class="inputstyle">
            <el-tree-select placeholder="请选择分类" v-model="category.id" :data="categories" node-key="id"
                :props="{label:'name'}" check-strictly :render-after-expand="false" style="width: 240px" clearable />
        </el-form-item>
        <el-form-item label="是否推荐">
            <el-select v-model="category.recom" placeholder="请选择是否推荐" size="large" style="width: 240px" clearable>
                <el-option label="推荐" :value = 1 />
                <el-option label="不推荐" :value = 0 />
            </el-select>
        </el-form-item>
        <el-form-item label="是否上架">
            <el-select v-model="category.status" placeholder="请选择是否上架" size="large" style="width: 240px" clearable>
                <el-option label="上架" :value = 0 />
                <el-option label="不上架" :value = 1 />
            </el-select>
        </el-form-item>
        <el-button type="success" @click="handleSearch">搜索</el-button>
    </el-form>
</template>

<style scoped>
.inputstyle{
    min-width: 80px;
}
</style>