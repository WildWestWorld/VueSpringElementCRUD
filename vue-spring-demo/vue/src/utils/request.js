import axios from 'axios'
import router from "@/router";

const request = axios.create({

    timeout: 5000
})


const whiteUrls = ["/api/user/login", '/api/user/register']
// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    let str = sessionStorage.getItem('token');
    if (!whiteUrls.includes(config.url)) {  // 校验请求白名单   config.url = 你向后端哪个路径请求了
        if (!str) {
            router.push('/login')
        }else {
            let token = JSON.parse(str);
            config.headers['token'] = token;  // 设置请求头

            // console.log(config.url) //用户访问的本地页面页面

        }
    }
    // config.headers['token'] = user.token;  // 设置请求头
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res =     res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

