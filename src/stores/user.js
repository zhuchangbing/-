import { defineStore } from "pinia"

/**
 * 使用pinia时，不能刷新页面，刷新页面就没了
 */

export const useUserStore = defineStore('user',{
    state:()=>({
        user : JSON.parse(sessionStorage.getItem('user')),
    }),

    actions: {
        changeUser(user){
            this.user = user
            //长久保存token  保存在sessionStorage
            sessionStorage.setItem('user',JSON.stringify(user))
        },
        clear() {
            this.user = null
            sessionStorage.removeItem('user')
        },
    }
})