<template>
  <div id="main-content">
    <component :is="currentComponent" />
  </div>
</template>

<script>
import MenuManagement from './MenuManagement.vue'
import TableManagement from './TableManagement.vue'
import StaffManagement from './StaffManagement.vue'

export default {
  name: 'AdminMainContent',
  components: {
    MenuManagement,
    TableManagement,
    StaffManagement,
  },
  props: {
    currentComponent: {
      type: String,
      default: 'TableManagement',
    },
  },
}
</script>

<style scoped>
#main-content {
  position: relative;
  z-index: 1;
  margin-top: 60px; /* Thêm margin-top để tránh bị che bởi header */
}
</style> 