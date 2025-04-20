<template>
  <div class="header">
    <div class="d-flex align-items-center gap-2">
      <button class="btn p-0 fs-5" @click="toggleSidebar">
        <i class="bi bi-list"></i>
      </button>
      <h5 class="mb-0">{{ title }}</h5>
    </div>
    <div>
      <button class="btn p-0 fs-5 me-2" @click="toggleSearchPage">
        <i class="bi bi-search"></i>
      </button>
      <button class="btn p-0 fs-5">
        <i class="bi bi-basket"></i>
      </button>
    </div>
    <SidebarMenu
      :isOpen="isSidebarOpen"
      @update:isOpen="$emit('update:isSidebarOpen', $event)"
      @call-staff="$emit('call-staff')"
      @request-checkout="$emit('request-checkout')"
    />
    <SearchPage
      :isOpen="isSearchPageOpen"
      @update:isOpen="$emit('update:isSearchPageOpen', $event)"
      @add-to-cart="$emit('add-to-cart', $event)"
    />
  </div>
</template>
  
<script>
import SidebarMenu from './SidebarMenu.vue'
import SearchPage from './SearchPage.vue'
export default {
  name: 'HeaderComponent',
  components: {
    SidebarMenu,
    SearchPage,
  },
  props: {
    title: {
      type: String,
      required: true,
    },
    isSidebarOpen: {
      type: Boolean,
      required: true,
    },
    isSearchPageOpen: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    toggleSidebar() {
      this.$emit('update:isSidebarOpen', !this.isSidebarOpen)
    },
    toggleSearchPage() {
      this.$emit('update:isSearchPageOpen', !this.isSearchPageOpen)
    },
  },
  emits: [
    'call-staff',
    'request-checkout',
    'update:isSidebarOpen',
    'update:isSearchPageOpen',
    'add-to-cart',
  ],
}
</script>
  
<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid #eee;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background-color: white;
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
.cart-button {
  position: relative;
}

.cart-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #1877f2;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}
</style>