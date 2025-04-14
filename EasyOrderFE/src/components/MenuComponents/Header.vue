<template>
  <div class="header">
    <div class="d-flex align-items-center gap-2">
      <button class="btn p-0 fs-5" @click="toggleSidebar">
        <i class="bi bi-list"></i>
      </button>
      <h5 class="mb-0">{{ title }}</h5>
    </div>
    <div>
      <button class="btn p-0 fs-5 me-2">
        <i class="bi bi-search"></i>
      </button>
      <button class="btn p-0 fs-5">
        <i class="bi bi-basket"></i>
      </button>
    </div>
    <SidebarMenu
      v-model:isOpen="isSidebarOpen"
      @call-staff="$emit('call-staff')"
      @request-checkout="$emit('request-checkout')"
    />
  </div>
</template>
  
<script>
import SidebarMenu from './SidebarMenu.vue'

export default {
  name: 'HeaderComponent',
  components: {
    SidebarMenu,
  },
  props: {
    title: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      isSidebarOpen: false,
    }
  },
  methods: {
    toggleSidebar() {
      this.isSidebarOpen = !this.isSidebarOpen
    },
  },
  emits: ['call-staff', 'request-checkout'],
}
</script>
  
<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #eee;
}
.header button {
  border: 1px #ddd;
  padding: 4px 8px;
  transition: background-color 0.2s ease;
}
.header button:active {
  background-color: #f0f0f0 !important;
  border-radius: 100px;
}
.btn:hover {
  background-color: transparent !important;
  box-shadow: none !important;
  outline: none !important;
}
.btn:focus {
  outline: none !important;
  box-shadow: none !important;
}
.bi {
  font-size: 24px;
  width: 24px;
  height: 24px;
}
</style>