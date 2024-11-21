import api from "."
const baseURL = api.userURL

export const addrApi = {
    getUserAddress() {
        return api.get(
            baseURL + 'addr',
        )
    },
    update(address) {
        return api.put(
            baseURL + 'addr',
            address
        )
    },
    add(address) {
        return api.post(
            baseURL + 'addr',
            address
        )
    },
    delete(id) {
        return api.delete(
            baseURL + 'addr',
            {
                params:{id}
            }
        )
    },
    updateDefaultAddr(id) {
        return api.put(
            `${baseURL}addr/${id}`,
        )
    },
}