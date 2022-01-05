import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/User.vue'
import Layout from "@/layout/Layout";
import Login from "@/views/Login";
import Person from "@/views/Person";
import User from "@/views/User";

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/user',

        children: [{
            path: '/user',
            name: 'User',
            component: ()=>import('@/views/User'),
        }, {
            path: '/person',
            name: 'Person',
            component: Person,
            },{
            path: '/book',
            name: 'Book',
            component: ()=>import('@/views/Book'),
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

export default router
