import api from ".";
const baseURL = api.adminURL; 

const adminApi = {
    logout:function() {
        return adminApi.post(
            baseURL + 'admin/logout',
        )
    },
    autologin:function(){
        return adminApi.post(
            baseURL+'admin/autologin',
            {
                header:{
                    'token':localStorage.getItem('token')
                }
            }
        )
    },
    getlogin:function(){
        return adminApi.post(
            baseURL+'admin/getlogin',
        )
    },
    searchadmin(pageInfo,admin) {
        return api.get(
            baseURL + 'admin/searchpage',
            {
                params:{
                    ...pageInfo,
                    ...admin,
                }
            }
        )
    },
    editadmin(admin) {
        return api.put(
            baseURL + 'admin/editadmin',
            admin,
        )
    },
    addadmin(admin) {
        return api.post(
            baseURL + 'admin/addadmin',
            admin,
        )
    },
    login(admin) {
        return api.post(
            baseURL + 'admin/login',
            admin,
        )
    },
}

export default adminApi;