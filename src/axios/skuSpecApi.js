import api from "."
const baseURL = api.skuSpecURL;

export const skuSpecApi = {
    attrs(goodsId) {
        return api.get(
            baseURL + 'skuspec/attrs',
            {
                params:{goodsId}
            }
        )
    },
}