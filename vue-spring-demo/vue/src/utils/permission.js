import router from "@/router";
import Layout from "@/layout/Layout";

// 注意：这个文件是设置动态路由的
// permissions是一个资源的数组
export function activeRouter() {
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


