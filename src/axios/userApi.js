import api from ".";
const baseURL = api.userURL;

const userApi = {
    searchUser(pageInfo,userVo) {
        return api.get(
            baseURL + 'user/getuser',
            {
                params:{
                    ...pageInfo,
                    ...userVo,
                }
            }
        )
    },

    update(user) {
        return api.put(
            baseURL + 'user/edituser',
            user
        )
    },
    login(user) {
        return api.post(
            baseURL + 'user/login',
            user,
        )
    },
    reg(user) {
        return api.post(
            baseURL + 'user/reg',
            user
        )
    },
    getuserbyid(id) {
        return api.get(
            baseURL + 'user/getuserbyid',
            {
                params:{id}
            }
        )
    },
    send(email) {
        return api.get(
            baseURL + 'vercode/send',
            {
                params:{email}
            }
        )
    },
}

export default userApi;