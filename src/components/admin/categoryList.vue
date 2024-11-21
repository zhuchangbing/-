<script setup>
import { ElMessageBox } from 'element-plus';
import { ref, watch } from 'vue';



const emit = defineEmits(['EditReload','reload'])

const clist = defineModel('clist')
const pageInfo = defineModel('pageInfo')

const editcategory = defineModel('editcategory')
const category = defineModel('category')
const categories = defineModel('categories')


/* 获取自身以及子类的信息 */




function handlePageChange() {
    emit('reload')
}



/*************************     修改商品     *************/
function handleUpdate(id,recom,status) {
    let m = recom==null?['上架','下架'].at(status):['不推荐','推荐'].at(recom)
    ElMessageBox.confirm(
        '你确定要【'+m+'】这一条分类吗？',
        '提醒',
        {
            confirmButtonText:'确定',
            cancelButtonText:'取消',
            type:'warning',
        }
    ).then(() => {
        editcategory.value = {id,recom,status}
        emit('EditReload')
    })
}



/*****              编辑操作           ******/
const editVisible = defineModel('editVisible')
function edit(category) {
    editVisible.value = true;
    editcategory.value = {...category}
    console.log(editVisible.value)
}
</script>


<template>
    <!----------------------------     这是分类商品列表      ------------------------------>
    
    <el-table :data="clist" border>
        <el-table-column prop="id" label="ID" style="width: 100px;"></el-table-column>
        <el-table-column prop="name" label="分类名"></el-table-column>
        <el-table-column prop="dscp" label="描述"></el-table-column>
        <el-table-column prop="recom" label="是否推荐" :formatter="(row)=>['不推荐','推荐'].at(row.recom)"></el-table-column>
        <el-table-column prop="status" label="是否上架" :formatter="(row)=>['上架','下架'].at(row.status)"></el-table-column>
        <el-table-column prop="fathName" label="父属性名"></el-table-column>
        <el-table-column label="操作">
            <template #default="scoped">
                <el-button type="primary" @click="edit(scoped.row)">编辑</el-button>
                <el-button v-if="scoped.row.status == 1" type="success" 
                @click="handleUpdate(scoped.row.id,null,0)">上架</el-button>
                <el-button v-else type="danger" 
                @click="handleUpdate(scoped.row.id,null,1)">下架</el-button>

                <el-button v-if="scoped.row.recom == 0" type="success" 
                @click="handleUpdate(scoped.row.id,1,null)">推荐</el-button>
                <el-button v-else type="danger" 
                @click="handleUpdate(scoped.row.id,0,null)">不推荐</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next,sizes" v-model:current-page="pageInfo.page"
            v-model:page-size="pageInfo.size" 
            :total="pageInfo.total" 
            :pager-count="11" 
            :page-sizes="[1, 2, 10, 20]"
            @change="handlePageChange" class="pageInfo" />
</template>

<style scoped>
.el-table cell {
    width: 50px;
}
</style>