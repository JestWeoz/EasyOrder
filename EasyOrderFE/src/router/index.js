import { createRouter, createWebHistory } from 'vue-router'
import MenuView from '../views/MenuView.vue'
import AdminView from '../views/AdminView.vue'
import MenuManagement from '../components/admin/MenuManagement.vue'
import TableManagement from '../components/admin/TableManagement.vue'
import StaffManagement from '../components/admin/StaffManagement.vue'
import ServiceStaffView from '../views/SeviceStaff.vue'
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Menu',
            component: MenuView
        },
        {
            path: '/service-staff',
            name: 'ServiceStaff',
            component: ServiceStaffView
        },
        {
            path: '/admin',
            component: AdminView,
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

export default router
