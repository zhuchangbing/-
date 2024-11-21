import api from "."
const baseURL = api.emailURL;

export const emailApi = {
    send(email) {
        return api.post(
            baseURL + 'vercode/send',
            email
        )
    },
}