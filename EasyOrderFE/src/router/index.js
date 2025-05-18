import { createRouter, createWebHistory } from 'vue-router'
import MenuView from '../views/MenuView.vue'
import AdminView from '../views/AdminView.vue'
import MenuManagement from '../components/admin/MenuManagement.vue'
import TableManagement from '../components/admin/TableManagement.vue'
import StaffManagement from '../components/admin/StaffManagement.vue'
import ServiceStaffView from '../views/ServiceStaff.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import { jwtDecode } from 'jwt-decode'
import Dashboard from '../components/serviceStaff/Dashboard.vue'
import Orders from '@/components/serviceStaff/Orders.vue'
import KitchenManager from '@/components/serviceStaff/KitchenManager.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/menu',
            name: 'Menu',
            component: MenuView
        },
        {
            path: '/service-staff',
            name: 'ServiceStaff',
            component: ServiceStaffView,
            children: [
                {
                    path: '',
                    name: 'Dashboard',
                    component: Dashboard
                },
                {
                    path: 'dashboard',
                    name: 'ServiceStaffDashboard',
                    component: Dashboard
                },
                {
                    path: 'orders',
                    name: 'Orders',
                    component: Orders
                },
                {
                    path: 'kitchen-management',
                    name: 'KitchenManager',
                    component: KitchenManager
                }
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: LoginView
        },
        {
            path: "/register",
            name: "Register",
            component: RegisterView
        },

        {
            path: '/admin',
            component: AdminView,
            meta: { requiresAdmin: true },
            children: [
                {
                    path: '',
                    name: 'Admin',
                    redirect: '/admin/tables'
                },
                {
                    path: 'menu',
                    name: 'MenuManagement',
                    component: MenuManagement
                },
                {
                    path: 'tables',
                    name: 'TableManagement',
                    component: TableManagement
                },
                {
                    path: 'staff',
                    name: 'StaffManagement',
                    component: StaffManagement
                }
            ]
        }
    ],
});

// Navigation guard
router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAdmin)) {
        const token = localStorage.getItem('token')
        if (!token) {
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            })
        } else {
            try {
                const decodedToken = jwtDecode(token)
                if (decodedToken.scope && decodedToken.scope.includes('ADMIN')) {
                    next()
                } else {
                    next({
                        path: '/login',
                        query: { redirect: to.fullPath }
                    })
                }
            } catch (error) {
                console.error('Error decoding token:', error)
                next({
                    path: '/login',
                    query: { redirect: to.fullPath }
                })
            }
        }
    } else {
        next()
    }
})

export default router
