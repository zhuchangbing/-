<script setup>
import { ref } from 'vue';
import categoryApi from '@/axios/categoryApi';
import { spuApi } from '@/axios/spuApi';
import { skuSpecApi } from '@/axios/skuSpecApi';
import { Plus, Pear } from '@element-plus/icons-vue'
import api from '@/axios';
import { skuApi } from '@/axios/skuApi';
import { requiredNumber } from 'element-plus/es/components/table-v2/src/common';
import { ElMessage } from 'element-plus';
const categories = ref([])
const categories2 = ref([])
const categories3 = ref([])

const selectFirstCategory = ref({})
const selectSecondCategory = ref({})
const selectThreeCategory = ref({})


function loadCategory() {
    categoryApi.search({parentId:0}).then((result) => {       // 异步 
        categories.value = result.data//加载一级分类
        console.log(categories.value)
        skus()
        getThreeCategory(); //加载三级分类
        getTwoCategory();//加载二级分类
    }) 
}
loadCategory();  //加载一级分类

/**
 * 加载三级分类
 */
function getThreeCategory(category2Id){
    categoryApi.getThreeCategory(category2Id).then((result) => {
        categories3.value  = result.data;
        console.log(categories3.value)
    })
}


/**
 * 获取二级分类商品
 */
function getTwoCategory(category1Id){
    categoryApi.getTwoCategory(category1Id).then((result) => {
        categories2.value = result.data;
        console.log(categories2.value);
    })
}


/**
 * 选择一级分类
 */

 function selectFirst(category) {
    selectFirstCategory.value = category;
    /*console.log(selectFirstCategory.value)*/
    // 清除选择的二级三级分类

    selectSecondCategory.value = {};
    selectThreeCategory.value = {};
    console.log(category)
    if(Object.keys(category).length == 0) {
        skus()
        return 
    }
    get1(category.category1Id);
    attrs.value = []             // 置空
    selectBrandId.value = 0

    // 置空specList
    addSku.value.specList = []
}

function selectSecond(category) {
    selectSecondCategory.value = category;
    
    // 根据选择的二级分类更新一级分类
    if (category.category1Name) {
        for (const c1 of categories.value) {
            if (c1.category1Name === category.category1Name) {
                selectFirstCategory.value = c1;
                break;
            }
        }
    }
    selectThreeCategory.value = {};
    if(Object.keys(category).length == 0){
        get1(selectFirstCategory.value.category1Id)
        return 
    }
    get2(category.category2Id);
    attrs.value = []
    selectBrandId.value = 0
    // 置空specList
    addSku.value.specList = []
}

function selectThree(category) {
    selectThreeCategory.value = category;
    console.log(categories.value)
    console.log(categories2.value)
    console.log(categories3.value)

    /* console.log(category) */
    // 根据选择的三级分类更新一级分类和二级分类
    if (category.category1Name) {
        for (const c1 of categories.value) {
            if (c1.category1Name === category.category1Name) {
                selectFirstCategory.value = c1;
                break;
            }
        }
    }
    if (category.category2Name) {
        for (const c2 of categories2.value) {
            if (c2.category2Name === category.category2Name) {
                selectSecondCategory.value = c2;
                break;
            }
        }
    }
    if(Object.keys(category).length == 0){
        get2(selectSecondCategory.value.category2Id)
        return 
    }
    console.log(category)
    get3(category.category3Id);
    attrs.value = []
    selectBrandId.value = 0
    // 置空specList
    addSku.value.specList = []
}



/****    获取spu 品牌 */
const spus = ref([])
const selectBrandId = ref(0)

function handleSelectBrand(spu) {

    selectBrandId.value = spu.id
    addSku.value.goodsId = spu.id
    addSku.value.categoryId = spu.category3Id

    console.log(spu)
    console.log(addSku.value)
    getAttrs(spu.id)
    // 置空specList
    addSku.value.specList = []
}

function get1(category1Id) {
    return spuApi.get1(category1Id).then(result => {
        spus.value = result.data
    })
}
function get2(category2Id) {
    return spuApi.get2(category2Id).then(result => {
        spus.value = result.data
    })
}
function get3(category3Id) {
    return spuApi.get3(category3Id).then(result => {
        spus.value = result.data
    })
}


function skus() {
    return spuApi.getspus().then(result => {
        spus.value = result.data
    })
}


/****   加载商品属性   ****/
const attrs = ref([])

function getAttrs(goodsId) {
    skuSpecApi.attrs(goodsId).then(result => {
        attrs.value = result.data
    })
}


function handleSelectAttr(v) {
    const isContain = addSku.value.specList.some(value => value.specId == v.specId)
    if(isContain) {
        for(const s of addSku.value.specList) {
            if(s.specId == v.specId) {
                s.optionId = v.id;
            }
        }
    }
    else{
        addSku.value.specList.push({specId:v.specId, optionId:v.id})
    }

    console.log(v)
    console.log(addSku.value)
    for(const a of attrs.value) {
        if(a.id == v.specId) {
            a.selectId = v.id;
        }
    }
}

/***********           */
const addSku = ref({specList:[], pics:[]})

const dialogImageUrl = ref('')
const dialogVisible = ref(false)

function handlePictureCardPreview(uploadFile) {
    dialogVisible.value = true;
    dialogImageUrl.value = uploadFile.url
}
function handleSuccessUploadPic(uploadFile) {
    console.log(uploadFile)
    addSku.value.pics.push({url:uploadFile.data})
    console.log(addSku.value)
}

function handleRemove(uploadFile) {
    console.log(uploadFile)
    console.log(uploadFile.response.data)
    for(let i = 0; i < addSku.value.pics.length;i++) {
        if(addSku.value.pics[i].url == uploadFile.response.data) {
            addSku.value.pics.splice(i, 1);
        }
    }
    console.log(addSku.value)
}
/**
 * 处理商品发布的函数
 */
function handlePublish(formEl) {
    console.log(addSku.value)
    formEl.validate((valid) => {
        if(valid) {
            skuApi.addSku(addSku.value).then(result => {
                if(result.code == 200 ) {
                    ElMessage.success('发布商品成功')
                }
                else {
                    ElMessage.error(result.message)
                }
            })
        }
        else {
            ElMessage.warning('请完善商品信息')
        }
    })
    
}



/**
 * 表单验证
 */
 const skuTable = ref(null)
 const rule = {
    name:[
        { required: true, message: '请输入物品型号', trigger: 'blur' }
    ],
    price:[
        { required: true, message: '请输入物品价格', trigger: 'blur' }
    ],
    description:[
        { required: true, message: '请输入物品描述', trigger: 'blur' }
    ],
    freshLevel:[
        { required: true, message: '请选择物品状况', trigger: 'blur' }
    ],
    isFree:[
        { required: true, message: '请选择是否包邮', trigger: 'blur' }
    ],
 }


</script>

<template>

    <div class="title">发布新商品</div>
    <div class="categories">
        <ul>
            <li @click="selectFirst({})" :class="{ active: !selectFirstCategory.category1Id }"><span
                    class="quanbu">全部</span></li>
            <li v-for="category in categories" :key="category.category1Id" @click="selectFirst(category)"
                :class="{ active: category.category1Id === selectFirstCategory.category1Id }">
                <span>{{ category.category1Name }}</span>
            </li>
        </ul>
        <ul>
            <li @click="selectSecond({})" :class="{ active: !selectSecondCategory.category2Id }"><span
                    class="quanbu">全部</span></li>
            <li v-for="c2 in categories2" :key="c2.category2Id" @click="selectSecond(c2)"
                :class="{ active: c2.category2Id === selectSecondCategory.category2Id }">
                <span
                    v-if="Object.keys(selectFirstCategory).length === 0 || selectFirstCategory.category1Name === c2?.category1Name">{{
                    c2.category2Name }}</span>
            </li>
        </ul>
        <ul>
            <li @click="selectThree({})" :class="{ active: !selectThreeCategory.category3Id }"><span
                    class="quanbu">全部</span></li>
            <li v-for="c3 in categories3" :key="c3.category3Id" @click="selectThree(c3)"
                :class="{ active: c3.category3Id === selectThreeCategory.category3Id }">
                <span
                    v-if="(Object.keys(selectFirstCategory).length === 0 || selectFirstCategory.category1Name === c3?.category1Name)
                        && (Object.keys(selectSecondCategory).length === 0 || selectSecondCategory.category2Name === c3?.category2Name)">{{
                    c3.category3Name }}</span>
            </li>
        </ul>
        <ul class="brand">
            <li style="margin-top: 15px;">品牌</li>
            <li v-for="spu in spus" @click="handleSelectBrand(spu)">
                <span :class="{ brandspan: true,brandActice:spu.id == selectBrandId }">{{ spu.name }}</span>
            </li>
        </ul>
    </div>

    <div class="attr-container">
        <!-- <ul class="attrs">
            <li style="margin-left: 30px;">商品属性</li>
        </ul> -->
        <ul v-for="attr in attrs" class="attr-ul">
            <li>{{ attr.name }}</li>
            <li v-for="v in attr.optionsList" class="attr-li" @click="handleSelectAttr(v)"
                :class="{activeAttr: v.id == attr?.selectId}">
                {{ v.value }}
            </li>
        </ul>
    </div>
    <!--  商品的详细信息  属性  图片  描述 价格   -->


    <el-form :model="addSku" label-width="100px" style="margin-top: 20px;" :rules="rule" ref="skuTable">

        <el-form-item label="物品实物图">
            <el-upload v-model:file-list="fileList" 
            :action="`${api.skuURL}upload`" 
            :on-success="handleSuccessUploadPic"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview" 
            :on-remove="handleRemove"
            :multiple="true"
                >
                <el-icon>
                    <Plus />
                </el-icon>
            </el-upload>

            <el-dialog v-model="dialogVisible">
                <img w-full :src="dialogImageUrl" alt="Preview Image" />
            </el-dialog>
        </el-form-item>

        <el-form-item label="版本/型号" prop="name" style="width: 300px;" >
            <div class="icon-wrapper"><el-icon><Pear /></el-icon></div>
            <el-input v-model="addSku.name" placeholder="请输入商品版本/型号" clearable></el-input>
        </el-form-item>

        <el-form-item label="商品定价" prop="price">
            <el-input-number v-model="addSku.price" @change="inputChange" :min="1" :max="10000"
                label="label" ></el-input-number>
        </el-form-item>

        <el-form-item label="商品描述" prop="description" style="width: 500px;">
            <el-input v-model="addSku.description" placeholder="请输入商品描述" clearable></el-input>
        </el-form-item>

        <el-form-item label="商品状况" prop="freshLevel">
            <el-select v-model="addSku.freshLevel" placeholder="请选择商品状况" style="width: 300px;" clearable>
                <el-option label="全新" :value=0></el-option>
                <el-option label="几乎全新" :value=1></el-option>
                <el-option label="轻微磨损" :value=2></el-option>
                <el-option label="有明显使用痕迹" :value=3></el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="是否包邮" prop="isFree">
            <el-radio-group v-model="addSku.isFree" clearable>
                <el-radio label="包邮" :value=0></el-radio>
                <el-radio label="不包邮" :value=1></el-radio>
            </el-radio-group>
        </el-form-item>


        <el-form-item label="">
            <el-button type="success" class="submit-button" @click="handlePublish(skuTable)">发布商品</el-button>
        </el-form-item>

    </el-form>

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


.submit-button {
    width: 200px;
    height: 50px;
}
.sku-pic {
    margin-left: 20px;
}

.activeAttr{
    color: #000;
    background-color: rgb(255, 230, 15);
    font-size: 20px;
    font-weight: bold;
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
.attr-ul li{
    float: left;
    width: 120px;
    height: 50px;
    line-height: 50px;
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


.attr-container{
    height: auto;
}
.brandspan{
    width: 200px !important;
    margin-top: 20px;
}

.title{
    margin: 10px 30px;
    font-size: 20px;
    font-weight: 700;
}
.page{
    height: 50px;
}
.categories {
    height: auto; /* 3级分类 * 40px */
}
.categories ul{
    height: 40px;
    margin-top: 30px;

}

.categories li {
    float: left;
    margin: 0px 30px;
    line-height: 40px;
    cursor: pointer;
}
.categories span {
    text-align: center;
    border-radius: 10px;
    font-size: 18px;
    display: inline-block;
    width: 100px;
    border: 1px solid #aaa;
}
.categories::after {
    content: '';
    display: block;
    clear: both;
}
.active {
    color: rgb(233, 66, 66)
}
.brandActice {
    color: rgb(233, 66, 66)
}
.categories span{
    position: relative;
    bottom: 5px;
}
.quanbu {
    position: relative;
    bottom: 5px;
}
</style>