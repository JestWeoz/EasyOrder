<template>
  <div class="container-fluid admin-container p-0">
    <ServiceStaffSideBar @toggle-sidebar="toggleSidebar" :active-component="currentRouteName" />
    <div id="main" class="layout-navbar">
      <StaffServiceHeader @toggle-sidebar="toggleSidebar" @logout="logout" />
      <DashBoard />
    </div>
  </div>
</template>

<script>
import ServiceStaffSideBar from '@/components/serviceStaff/SideBar.vue'
import StaffServiceHeader from '@/components/serviceStaff/Header.vue'
import DashBoard from '@/components/serviceStaff/DashBoard.vue'

export default {
  name: 'ServiceStaffView',
  components: {
    ServiceStaffSideBar,
    StaffServiceHeader,
    DashBoard,
  },
  data() {
    return {
      currentRouteName: 'MenuManagement',
    }
  },
  methods: {
    toggleSidebar() {
      // Xử lý toggle sidebar nếu cần
    },
  },
}
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background-color: #f4f6f9;
}
</style> 