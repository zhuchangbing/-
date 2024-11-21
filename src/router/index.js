import { createRouter, createWebHistory } from 'vue-router'
import adminLogin from '@/views/admin/adminLogin.vue'
import adminHome from '@/views/admin/adminHome.vue'
import userHome from '@/views/user/userHome.vue'
import userLogin from '@/views/user/userLogin.vue'
import index from '@/views/user/index.vue'
import userSearch from '@/views/user/userSearch.vue'
import userDetail from '@/views/user/userDetail.vue'
import userList from '@/components/admin/userList.vue'
import category from '@/views/admin/category.vue'
import goods from '@/views/admin/goods.vue'
import user from '@/views/admin/user.vue'
import admin from '@/views/admin/admin.vue'
import userReg from '@/views/user/userReg.vue'
import UserCenter from '@/views/user/userCenter.vue'
import UserCollect from '@/views/user/UserCollect.vue'
import UserAddr from '@/views/user/UserAddr.vue'
import UserOrder from '@/views/user/UserOrder.vue'
import UserCreateOrder from '@/views/user/UserCreateOrder.vue'
import UserRel from '@/views/user/UserRel.vue'
import OrderPayView from '@/views/user/OrderPayView.vue'
import UserRelease from '@/views/user/UserRelease.vue'
import UserInfo from '@/views/user/UserInfo.vue'
import UserHanleOrder from '@/views/user/UserHanleOrder.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path:'/admin/login', component:adminLogin},
    { path:'/admin/home', component:adminHome,
      children:[
        { path:'/admin/home/userList',component:userList},
        { path:'/admin/home/admin',component:admin},
        { path:'/admin/home/category',component:category},
        { path:'/admin/home/good',component:goods},
        { path:'/admin/home/user',component:user}
      ]
    },
    { path:'/',component:userHome,
      redirect:'/index',
      children:[
        { path:'/login',component:userLogin},
        { path:'/index',component:index},
        { path:'/reg',component:userReg},
        { path:'/search/:categoryId?',props:true,component:userSearch},
        { path:'/detail',component:userDetail},
        { path:'/collect',component:UserCollect},
        { path:'/corder',component:UserCreateOrder},
        { path:'/rel',component:UserRel},
        { path:'/orderpay',component:OrderPayView},
        { path:'/center', component:UserCenter,
          redirect:'/center/orders',
          children:[
            { path:'/center/collect',component:UserCollect},
            { path:'/center/addr',component:UserAddr},
            { path:'/center/orders',component:UserOrder},
            { path:'/center/release',component:UserRelease},
            { path:'/center/info',component:UserInfo},
            { path:'/center/horder',component:UserHanleOrder},
          ]
        }
      ]
    },
  ]
})

export default router
