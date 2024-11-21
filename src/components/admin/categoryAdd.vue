<script setup>
import categoryApi from '@/axios/categoryApi';
import { ElMessage } from 'element-plus';
import { computed, ref } from 'vue';

const emit = defineEmits(['addLoad'])

const categories = defineModel('categories')
const addVisible = defineModel('addVisible')
const addcategory = ref({})

function handleAdd(){
    categoryApi.addCategory(addcategory.value).then((result)=>{
        if(result.code == 200) {
            ElMessage('添加成功')
            addVisible.value = false;
            emit('addLoad') 
        }
    })
}

function handleadd() {
    addVisible.value = true;
}
const addForm = ref(null)
const rules = {
    fathId:[
        { required:true, message:'父分类不可为空',trigger:'blur'},
    ],
    name:[
        { required:true, message:'分类名不可为空',trigger:'blur'},
    ],
    dscp:[
        { required:true, message:'分类描述不可为空',trigger:'blur'},
    ],
    recom:[
        { required:true, message:'是否推荐不可为空',trigger:'blur'},
    ],
    status:[
        { required:true, message:'是否上架不可为空',trigger:'blur'},
    ]
}

const filterNode = (value, data) => {
    console.log('**************************')
  if(data.children.length > 0) return true
  return false
}

</script>

<template>
    <el-button type="primary" @click="handleadd" class="addButton">添加</el-button>
    <el-dialog v-model="addVisible" title="添加新分类" width="800">
    <el-form :model="addcategory" label-width="80px" ref="addForm" :rules="rules">
        <el-form-item label="父分类" class="inputstyle" prop="fathId"> 
            <el-tree-select placeholder="请选择分类" 
            v-model="addcategory.fathId" 
            :data="categories" node-key="id"
            :filter-node-method="filterNode"
            :props="{label:'name'}" check-strictly 
            :render-after-expand="false" 
            style="width: 240px" clearable />
        </el-form-item>
        <el-form-item label="分类名" class="inputstyle" prop="name"> 
          <el-input v-model="addcategory.name" placeholder="请输入分类名"></el-input>
        </el-form-item>
        <el-form-item label="描述" class="inputstyle" prop="dscp">
          <el-input v-model="addcategory.dscp" placeholder="请输入描述"></el-input>
        </el-form-item>
        <el-form-item label="是否推荐" prop="recom">
            <el-select v-model="addcategory.recom" placeholder="请选择是否推荐" size="large" style="width: 240px" clearable>
                <el-option label="推荐" :value = 1 />
                <el-option label="不推荐" :value = 0 />
            </el-select>
        </el-form-item>
        <el-form-item label="是否上架" prop="status">
            <el-select v-model="addcategory.status" placeholder="请选择是否上架" size="large" style="width: 240px" clearable>
                <el-option label="上架" :value = 0 />
                <el-option label="不上架" :value = 1 />
            </el-select>
        </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.addButton{
    margin-bottom: 10px;
}
</style>