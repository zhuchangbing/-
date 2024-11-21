//利用Axios 访问SpringBoot项目的Web api接口                     /********     axios配置    ********/
import { useTokenStore } from "@/stores/token";
import axios from "axios"                        /********   拦截api请求     ****** */
//创建一个Axios的示例
const api = axios.create({
    
})

api.adminURL   = 'http://localhost:5173/adminApi/';
api.skuURL     = 'http://localhost:5173/skuApi/';
api.userURL    = 'http://localhost:5173/userApi/';
api.orderURL   = 'http://localhost:5173/orderApi';
api.skuSpecURL = 'http://localhost:5173/skuSpecApi/';
api.emailURL   = 'http://localhost:5173/emailApi/';

//请求拦截器
api.interceptors.request.use(function (config) {
    //anxis发送请求前先走这里在往服务器发送

    const tokenStore = useTokenStore()
    if(tokenStore.token) {
      config.headers.token = tokenStore.token;
    }

    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
})

// 添加响应拦截器
api.interceptors.response.use(function (response) {
    //console.log(response);
    return response.data;
  }, function (error) {
    return Promise.reject(error);
  });

//向外暴漏api
export default api;

