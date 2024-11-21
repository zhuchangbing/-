import { defineStore } from "pinia"

/**
 * 使用pinia时，不能刷新页面，刷新页面就没了
 */

export const useAdminStore = defineStore('admin',{
    state:()=>({
        admin : JSON.parse(sessionStorage.getItem('admin')),
    }),

    actions: {
        changeAdmin(admin){
            this.admin = admin
            //长久保存token  保存在sessionStorage
            sessionStorage.setItem('admin',JSON.stringify(admin))
        },
        clear() {
            this.admin = null
            sessionStorage.removeItem('admin')
        },
    }
})