import api from "."
const baseURL = api.orderURL

export const orderApi = {
    addorder(orderVo) {
        return api.post(
            baseURL + '/order',
            orderVo
        )
    },
    getOrderDetailById(id) {
        return api.get(
            baseURL + '/order',
            {
                params:{id}
            }
        )
    },
    getUserAllOrders() {
        return api.get(
            baseURL + '/order/getorders',
        )
    },
    pay(order) {
        return api.put(
            baseURL + '/order/pay',
            order
        )
    },
    getporders() {
        return api.get(
            baseURL + '/order/getporders' 
        )
    },
    updateNumber(order) {
        return api.put(
            baseURL + '/order/updateOrderTrackingNumber',
            order,
        )
    }
}