import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const request = axios.create({
    baseURL: '/api', // 注意这里配合 vite.config.js 的代理
    timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    
    // 取出 Token，如果有则带上 (后续功能会用到)
    const user = localStorage.getItem("user");
    if (user) {
        const token = JSON.parse(user).token;
        if (token) {
             // 按照 JWT 标准，通常放在 Authorization header
            config.headers['token'] = token; 
        }
    }
    return config
}, error => {
    return Promise.reject(error)
});

// 响应拦截器
request.interceptors.response.use(response => {
    let res = response.data;
    // 兼容服务端返回字符串的情况
    if (typeof res === 'string') {
        res = res ? JSON.parse(res) : res
    }
    return res;
}, error => {
    console.log('err' + error) // for debug
    ElMessage.error(error.message || '网络连接异常')
    return Promise.reject(error)
})

export default request