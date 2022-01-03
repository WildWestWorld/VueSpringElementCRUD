import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Layout from "@/layout/Layout";
import Login from "@/views/Login";

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect:'/home',

    children:[{
      path:'home',
      name:'Home',
      component:Home,
    }]
  },
  {
    path: '/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: Login
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
