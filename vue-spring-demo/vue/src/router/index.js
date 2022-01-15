import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/User.vue'
import Layout from "@/layout/Layout";
import Login from "@/views/Login";

import User from "@/views/User";

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/echart',

        children: [{
            path: '/echart',
            name: 'Echart',
            component: ()=>import('@/views/Echart'),
        },

        ]
    },
    {
        path: '/login',
        name: 'Login',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: ()=>import('@/views/Login'),
    },
    {
        path: '/register',
        name: 'Register',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import("@/views/Register"),
    },

]
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

// 在刷新页面的时候重置当前路由
activeRouter()

function activeRouter() {
    const permissions = sessionStorage.getItem("permissions")
    if (permissions) {
        const permission = JSON.parse(permissions)
        let root = {
            path: '/',
            name: 'Layout',
            component: Layout,
            redirect: "/echart",
            children: []
        }
        permission.forEach(p => {
            let obj = {
                path: p.path,
                name: p.name,
                component: () => import("@/views/" + p.name)
            };
            root.children.push(obj)
        })
        if (router) {
            router.addRoute(root)
        }
    }
}


router.beforeEach((to, from, next) => {
    if (to.path === '/login' || to.path === '/register') {
        next()
        return
    }
    let permissions = sessionStorage.getItem("permissions") ? JSON.parse(sessionStorage.getItem("permissions")) : {}
    if (!permissions || !permissions.length) {
        next('/login')
    } else if (!permissions.find(p => p.path === to.path)) {
        next('/login')
    } else {
        next()
    }
})



export default router
