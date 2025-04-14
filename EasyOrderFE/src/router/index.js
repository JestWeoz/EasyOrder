import { createRouter, createWebHistory } from 'vue-router'
import MenuView from '../views/MenuView.vue'
import CallStaffMessagesView from '../views/CallStaffMessagesView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Menu',
            component: MenuView
        },
        {
            path: '/callstaff-messages',
            name: 'CallStaffMessages',
            component: CallStaffMessagesView
        }
    ],
});

export default router
