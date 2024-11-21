<script setup>
import categoryApi from '@/axios/categoryApi';
import { ElMessage } from 'element-plus';
import { watch } from 'vue';

const categories = defineModel('categories')
const editcategory = defineModel('editcategory')
const editVisible = defineModel('editVisible')

const emit = defineEmits(['Edit'])

function handleEdit() {
  categoryApi.updatecategory(editcategory.value).then(result => {
    if(result.code == 200) {
      ElMessage('修改成功')
      emit('Edit')
      editVisible.value = false
    }
    else {
      ElMessage(result.message)
      editVisible.value = false
    }
  })
}

</script>

<template>
  <el-dialog v-model="editVisible" title="修改分类信息" width="800">
    <el-form :model="editcategory" label-width="80px">
        <el-form-item label="分类" class="inputstyle">
            <el-tree-select placeholder="请选择分类" v-model="editcategory.id" :data="categories" node-key="id"
                :props="{label:'name'}" check-strictly :render-after-expand="false" style="width: 240px" clearable />
        </el-form-item>
        <el-form-item label="分类名" class="inputstyle"> 
          <el-input v-model="editcategory.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" class="inputstyle">
          <el-input v-model="editcategory.dscp"></el-input>
        </el-form-item>
        <el-form-item label="是否推荐">
            <el-select v-model="editcategory.recom" placeholder="请选择是否推荐" size="large" style="width: 240px" clearable>
                <el-option label="推荐" :value = 1 />
                <el-option label="不推荐" :value = 0 />
            </el-select>
        </el-form-item>
        <el-form-item label="是否上架">
            <el-select v-model="editcategory.status" placeholder="请选择是否上架" size="large" style="width: 240px" clearable>
                <el-option label="上架" :value = 0 />
                <el-option label="不上架" :value = 1 />
            </el-select>
        </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEdit">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>
.inputstyle{
    min-width: 80px;
}
</style>