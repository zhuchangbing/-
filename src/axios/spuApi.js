import api from "."
const baseURL = api.skuURL;


export const spuApi = {
    get1(category1Id) {
        return api.get(
            baseURL + 'spu/get1',
            {
                params:{category1Id}
            }
        )
    },
    get2(category2Id) {
        return api.get(
            baseURL + 'spu/get2',
            {
                params:{category2Id}
            }
        )
    },
    get3(category3Id) {
        return api.get(
            baseURL + 'spu/get3',
            {
                params:{category3Id}
            }
        )
    },
    getspus() {
        return api.get(
            baseURL + 'spu/spus',
        )
    },

} 