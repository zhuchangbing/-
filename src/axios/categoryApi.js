import api from "."
import qs from "qs"
const baseURL = api.skuURL;


const categoryApi = {
    search(pageInfo){
        return api.get(
            baseURL+ 'sku/getcategorysku',
            {
                params:{
                    ...pageInfo
                }
            }
        )
    },

    getCategory(){
        return api.get(
            baseURL+ 'sku/getcategory',
            {
                params:{}
            }
        )
    },

    getTwoCategory(category1Id) {
        return api.get(
            baseURL + 'sku/gettwoc',
            {
                params:{category1Id}
            }
        )
    },
    getThreeCategory(category2Id) {
        return api.get(
            baseURL + 'sku/getthreec',
            {
                params:{category2Id}
            }
        )
    },
    getfirst(pageInfo,category1Id) {
        return api.get(
            baseURL + 'sku/getfirst',
            {
                params:{...pageInfo,category1Id}
            }
        )
    },
    getsecond(pageInfo,category2Id) {
        return api.get(
            baseURL + 'sku/getsecond',
            {
                params:{...pageInfo,category2Id}
            }
        )
    },
    getthird(pageInfo,category3Id) {
        return api.get(
            baseURL + 'sku/getthird',
            {
                params:{...pageInfo,category3Id}
            }
        )
    },
    getall(pageInfo) {
        return api.get(
            baseURL + 'sku/getall',
            {
                params:{
                    ...pageInfo,
                }
            }
        )
    },
    getdetail(skuId) {
        return api.get(
            baseURL + 'sku/getsku',
            {
                params:{skuId}
            }
        )
    },
    getallc(pageInfo) {
        return api.get(
            baseURL + 'sku/getallc',
            {
                params:{...pageInfo}
            }
        )
    },
    getallcc(pageInfo,category) {
        return api.get(
            baseURL + 'sku/getcategorylist',
            {
                params:{
                    ...pageInfo,
                    ...category,
                }
            }
        )
    },
    updatecategory(category) {
        return api.put(
            baseURL + 'sku/updatecategory',
            category,
        )
        
    },
    addCategory(category) {
        return api.post(
            baseURL + 'sku',
            category,
        )
    },
    searchgoods(pageInfo,skuVo) {
        return api.get(
            baseURL + 'sku/searchgoods',
            {
                params:{
                    ...pageInfo,
                    ...skuVo,
                }
            }
        )
    },
    getBatch(ids) {
        return  api.get(
            baseURL + 'sku/getBatch',
            {
                params:{ids},
                paramsSerializer:(params) => qs.stringify(params,{arrayFormat:'repeat'})
            }
        )
    },
}


export default categoryApi;