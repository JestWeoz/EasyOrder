<template>
  <div class="container-fluid admin-container p-0">
    <ServiceStaffSideBar
      @toggle-sidebar="toggleSidebar"
      :active-component="currentRouteName"
      @change-component="setCurrentComponent"
    />
    <div id="main" class="layout-navbar">
      <StaffServiceHeader @toggle-sidebar="toggleSidebar" @logout="logout" />
      <router-view />
    </div>
  </div>
</template>

<script>
import ServiceStaffSideBar from '@/components/serviceStaff/SideBar.vue'
import StaffServiceHeader from '@/components/serviceStaff/Header.vue'

export default {
  name: 'ServiceStaffView',
  components: {
    ServiceStaffSideBar,
    StaffServiceHeader,
  },
  data() {
    return {
      currentRouteName: '',
    }
  },
  created() {
    // Cập nhật component hiện tại dựa trên route
    this.updateCurrentComponent()
  },
  watch: {
    // Theo dõi route thay đổi để cập nhật component đang active
    $route() {
      this.updateCurrentComponent()
    },
  },
  methods: {
    toggleSidebar() {
      // Xử lý toggle sidebar
      const sidebar = document.querySelector('#sidebar')
      if (sidebar) {
        sidebar.classList.toggle('active')
      }
      const mainContent = document.querySelector('#main')
      if (mainContent) {
        mainContent.classList.toggle('expanded')
      }
    },
    updateCurrentComponent() {
      const routePath = this.$route.path
      if (routePath.includes('dashboard')) {
        this.currentRouteName = 'Dashboard'
      } else if (routePath.includes('orders')) {
        this.currentRouteName = 'Orders'
      } else if (routePath.includes('kitchen-management')) {
        this.currentRouteName = 'KitchenManager'
      }
    },
    setCurrentComponent(componentName) {
      this.currentRouteName = componentName
    },
    logout() {
      // Xử lý đăng xuất
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
  },
}
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background-color: #f4f6f9;
}

#main {
  margin-left: 300px;
  padding: 1.5rem;
  transition: margin-left 0.5s;
}

#main.expanded {
  margin-left: 0;
}

@media (max-width: 1199px) {
  #main {
    margin-left: 0;
  }
}
</style> 