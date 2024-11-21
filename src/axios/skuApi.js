import api from "."
const baseURL = api.skuURL

export const skuApi = {
    addSku(sku) {
        return api.post(
            baseURL + 'sku/addsku',
            sku
        )
    },
    getPuserSkus() {
        return api.get(
            baseURL + 'sku/getpuserskus',
        )
    },
    // 删除商品
    deleteSku(id) {
        return api.delete(
            baseURL + 'sku',
            {
                params:{id}
            }
        )
    },
    updatesku(sku) {
        return api.put(
            baseURL + 'sku/updatesku',
            sku,
        )
    },
    search(pageInfo) {
        return api.get(
            baseURL + 'sku/es',
            {
                params:{
                    ...pageInfo
                }
            }
        )
    },
}