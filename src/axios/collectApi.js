import api from "."
const baseURL = api.orderURL

export const collectApi = {
    addCollect(saveSku){
        return api.post(
            baseURL + '/collect/addCollect',
            saveSku
        )
    },
    getssku(saveSku) {
        return api.get(
            baseURL + '/collect/getssku',
            {
                params:{
                    ...saveSku
                }
            }
        )
    },
    delete(skuId) {
        return api.delete(`${baseURL}collect/${skuId}`)
    },
    getUserCollects() {
        return api.get(
            baseURL + '/collect/getUserCollect',
        )
    },
    deleteBatch(ids) {
        return api.delete(
            baseURL + '/collect',
            {
                data:{ids}  // 以json对象形式传递数据
            }
        )
    },
}



