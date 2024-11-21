import { defineStore } from "pinia"

/**
 * 使用pinia时，不能刷新页面，刷新页面就没了
 */

export const useTokenStore = defineStore('token',{
    state:()=>({
        token : sessionStorage.getItem('token'),
    }),

    actions: {
        changeToken(token){
            this.token = token
            //长久保存token  保存在sessionStorage
            sessionStorage.setItem('token',token)
        },
        clear() {
            this.token = null
            sessionStorage.removeItem('token')
        }
    }
})