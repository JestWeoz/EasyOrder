<template>
  <div class="container-fluid admin-container p-0">
    <AdminSidebar @toggle-sidebar="toggleSidebar" :active-component="currentRouteName" />
    <div id="main" class="layout-navbar">
      <AdminHeader @toggle-sidebar="toggleSidebar" />
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import AdminSidebar from '@/components/admin/AdminSidebar.vue'
import AdminHeader from '@/components/admin/AdminHeader.vue'
import { useRoute } from 'vue-router'

export default {
  name: 'AdminView',
  components: {
    AdminSidebar,
    AdminHeader,
  },
  setup() {
    const route = useRoute()
    return { route }
  },
  computed: {
    currentRouteName() {
      return this.route.name
    },
  },
  mounted() {
    this.initializeScripts()
  },
  methods: {
    initializeScripts() {
      // Khởi tạo Perfect Scrollbar
      if (typeof PerfectScrollbar !== 'undefined') {
        new PerfectScrollbar('.sidebar-wrapper')
      }
    },
    toggleSidebar() {
      const sidebar = document.getElementById('sidebar')
      sidebar.classList.toggle('active')
    },
  },
}
</script>

<style>
/* Import các styles cần thiết */

.admin-container {
  min-height: 100vh;
  padding: 0;
  margin: 0;
  display: flex;
}

#main {
  flex: 1;
  min-height: 100vh;
  background-color: #f2f7ff;
  position: relative;
  padding: 0;
}

/* Các styles tùy chỉnh nếu cần */
.sidebar-item.has-sub .submenu {
  display: none;
}

.sidebar-item.has-sub.active .submenu {
  display: block;
}
</style>